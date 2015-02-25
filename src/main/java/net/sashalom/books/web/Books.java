package net.sashalom.books.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import net.sashalom.books.blogic.interfaces.IBookService;
import net.sashalom.books.blogic.services.BookFilesFilter;
import net.sashalom.books.blogic.services.BookImagesFilter;
import net.sashalom.books.model.Book;
import net.sashalom.books.model.BooksOrdering;
import net.sashalom.central.blogic.exceptions.ElectronicLibraryException;
import net.sashalom.central.blogic.interfaces.IConfigurationService;
import net.sashalom.central.model.Constants;
import net.sashalom.central.web.UserData;
import net.sashalom.utils.components.PaginatedData;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("books")
@Scope("session")
public class Books implements Serializable {

	private static final long serialVersionUID = 2067147456682161865L;
	
	private static final String BOOK_URL_PATTERN = "/book/%d";

	@Autowired
	private IConfigurationService conf;
	
	@Autowired
	private IBookService bookService;
	
	@Autowired
	private UserData userData;
	
	private int booksTablePageSize = 10;
	
	private String booksFolder;
	
	private String searchField;
	
	private BooksOrdering ordering = BooksOrdering.NAME_ASC;
	
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
			userData.setUrl(((HttpServletRequest) ec.getRequest()).getRequestURL().toString());
		}
	}
	
	private LazyDataModel<Book> books = new LazyDataModel<Book>() {

		private static final long serialVersionUID = -2398479484646207843L;
		
		@Override
		public void setRowIndex(int rowIndex) {
		    if (rowIndex == -1 || getPageSize() == 0) {
		        super.setRowIndex(-1);
		    }
		    else
		        super.setRowIndex(rowIndex % getPageSize());
		}

		@Override
		public List<Book> load(int first, int pageSize,
				String sortField, SortOrder sortOrder,
				Map<String, Object> filters) {
			booksTablePageSize = pageSize;
			PaginatedData<Book> pd = null;
			DetachedCriteria dc = DetachedCriteria.forClass(Book.class);
			if(StringUtils.isNotBlank(searchField)){
				dc.add(Restrictions.or(Restrictions.like("name", searchField, MatchMode.ANYWHERE), Restrictions.like("description", searchField, MatchMode.ANYWHERE)));
			}
			switch (ordering) {
			case NAME_ASC:
				dc.addOrder(Order.asc("name"));
				break;
			case NAME_DESC:
				dc.addOrder(Order.desc("name"));
				break;
			case DATE_ASC:
				dc.addOrder(Order.asc("uploadDate"));
				break;
			case DATE_DESC:
				dc.addOrder(Order.desc("uploadDate"));
				break;
			default:
				dc.addOrder(Order.asc("name"));
				break;
			}
			dc.addOrder(Order.asc("name"));
			try {
				pd = bookService.getPaginatedDataByCriteria(dc, pageSize, first / pageSize);
			} catch (ElectronicLibraryException e) {
				throw new RuntimeException(e);
			}
			setRowCount(pd.getTotalRecords());
			return pd.getCollection();
		}
		
	};
	
	public String getImageLink(Book book){
		if(book == null || StringUtils.isBlank(book.getImage())){
			return null;
		}
		return String.format(Constants.IMAGE_URL_PATTERN, BookImagesFilter.DESCRIPTOR, book.getImage());
	}
	
	public String getBookFileLink(Book book){
		if(book == null){
			return null;
		}
		return FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() 
				+ String.format(Constants.BOOK_URL_PATTERN, BookFilesFilter.DESCRIPTOR, book.getId(), book.getFileName());
	}
	
	public String getBookLink(Book book){
		if(book == null){
			return null;
		}
		return FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() 
				+ String.format(BOOK_URL_PATTERN, book.getId());
	}
	
	public LazyDataModel<Book> getBooks() {
		return books;
	}

	public int getBooksTablePageSize() {
		return booksTablePageSize;
	}

	public String getSearchField() {
		return searchField;
	}

	public void setSearchField(String searchField) {
		this.searchField = searchField;
	}
	
	public List<SelectItem> getOrderItems(){
		List<SelectItem> items = new ArrayList<SelectItem>(4);
		items.add(new SelectItem(BooksOrdering.NAME_ASC, "Name A-Z"));
		items.add(new SelectItem(BooksOrdering.NAME_DESC, "Name Z-A"));
		items.add(new SelectItem(BooksOrdering.DATE_ASC, "Date from old"));
		items.add(new SelectItem(BooksOrdering.DATE_DESC, "Date from new"));
		return items;
	}

	public BooksOrdering getOrdering() {
		return ordering;
	}

	public void setOrdering(BooksOrdering ordering) {
		this.ordering = ordering;
	}
}
