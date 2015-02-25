package net.sashalom.books.web;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.servlet.http.HttpServletRequest;

import net.sashalom.books.blogic.interfaces.IBookService;
import net.sashalom.books.blogic.services.BookFilesFilter;
import net.sashalom.books.blogic.services.BookImagesFilter;
import net.sashalom.books.model.Book;
import net.sashalom.books.model.Comment;
import net.sashalom.books.model.Rating;
import net.sashalom.central.blogic.exceptions.ElectronicLibraryException;
import net.sashalom.central.blogic.interfaces.IConfigurationService;
import net.sashalom.central.model.Constants;
import net.sashalom.central.web.UserData;
import net.sashalom.utils.DialogHandler;

import org.apache.commons.lang.StringUtils;
import org.primefaces.event.RateEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("bookView")
@Scope("session")
public class BookView implements Serializable {

	private static final long serialVersionUID = 2067147456682161865L;
	
	@Autowired
	private IConfigurationService conf;
	
	@Autowired
	private IBookService bookService;
	
	@Autowired
	private UserData userdData;
	
	private int bookID; 
	
	private Book book = new Book();
	
	private boolean canAssignRating = false;
	
	private String booksFolder;
	
	private Comment newComment;
	
	private String commentText;
	
	private TreeNode comments;
	
	@PostConstruct
	private void init(){
		booksFolder = conf.getString("booksFolder");
		if(booksFolder == null){
			throw new RuntimeException("Books folder is not specified in configuration");
		}
		if(!booksFolder.endsWith("/")){
			booksFolder += "/";
		}
	}
	
	public void preparePageLoadHandler(ComponentSystemEvent event) {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext ec = context.getExternalContext();
		if (((HttpServletRequest) ec.getRequest()).getMethod().equals("GET")){
			onPageCall();
		}
	}
	
	private void onPageCall(){
		try {
			commentText = null;
			book = bookService.findByIdWithComments(bookID);
			
			if(book == null){
				ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
				try {
					ec.redirect(ec.getRequestContextPath() + "/404.html");
					return;
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			
			comments = new DefaultTreeNode(new Comment(), null);
	        buildTree(book.getComments(), null, comments);
			
			if(userdData.getCurrentLoggedUser() == null){
				canAssignRating = false;
			} else {
				canAssignRating = !bookService.ratingAssigned(book.getId(), userdData.getCurrentLoggedUser().getId());
			}
		} catch (ElectronicLibraryException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void buildTree(List<Comment> comments, Comment parent, TreeNode parentNode){
		for(Comment c : comments){
			if(parent == null){
				if(c.getParentComment() == null){
					TreeNode node = new DefaultTreeNode(c, parentNode);
					parentNode.setExpanded(true);
					buildTree(comments, c, node);
				}
			} else {
				if(parent.equals(c.getParentComment())){
					TreeNode node = new DefaultTreeNode(c, parentNode);
					parentNode.setExpanded(true);
					buildTree(comments, c, node);
				}
			}
		}
	}
	
	private void prepareNewComment(Comment comment){
		newComment = new Comment();
		newComment.setParentComment(comment);
		newComment.setBookID(book.getId());
		newComment.setUser(userdData.getCurrentLoggedUser());
	}
	
	public void ansver(Comment comment){
		prepareNewComment(comment);
		DialogHandler.show("answerDialogue");
	}
	
	public void addAnsverComment(){
		addComment();
		DialogHandler.hide("answerDialogue");
	}
	
	private void addComment(){
		try {
			newComment.setDate(new Date());
			newComment = bookService.save(newComment);
			book.getComments().add(newComment);
			newComment = null;
			comments = new DefaultTreeNode(new Comment(), null);
	        buildTree(book.getComments(), null, comments);
		} catch (ElectronicLibraryException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void addNewComment(){
		prepareNewComment(null);
		newComment.setComment(commentText);
		addComment();
		commentText = null;
	}

	public String getImageLink(){
		if(book == null || StringUtils.isBlank(book.getImage())){
			return null;
		}
		return String.format(Constants.IMAGE_URL_PATTERN, BookImagesFilter.DESCRIPTOR, book.getImage());
	}
	
	public String getBookFileLink(){
		if(book == null){
			return null;
		}
		return FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath()
				+ String.format(Constants.BOOK_URL_PATTERN, BookFilesFilter.DESCRIPTOR, book.getId(), book.getFileName());
	}
	
	 public void onrate(RateEvent rateEvent) {
		 int newRate = ((Integer) rateEvent.getRating()).intValue();
		 Rating r = new Rating();
		 r.setBookID(book.getId());
		 r.setUserID(userdData.getCurrentLoggedUser().getId());
		 r.setRateDate(new Date());
		 r.setRating(newRate);
		 try {
			bookService.save(r);
			book.setRate(bookService.updateRating(book.getId()));
			canAssignRating = false;
		} catch (ElectronicLibraryException e) {
			throw new RuntimeException(e);
		}
	 }

	public int getBookID() {
		return bookID;
	}

	public void setBookID(int bookID) {
		this.bookID = bookID;
	}


	public Book getBook() {
		return book;
	}


	public void setBook(Book book) {
		this.book = book;
	}

	public boolean isCanAssignRating() {
		return canAssignRating;
	}

	public void setCanAssignRating(boolean canAssignRating) {
		this.canAssignRating = canAssignRating;
	}

	public Comment getNewComment() {
		return newComment;
	}

	public String getCommentText() {
		return commentText;
	}

	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}
	
	public TreeNode getComments() {
		return comments;
	}
	
}
