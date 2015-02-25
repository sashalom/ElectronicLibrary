package net.sashalom.books.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.FileAlreadyExistsException;
import java.util.Date;

import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import net.sashalom.books.blogic.interfaces.IBookService;
import net.sashalom.books.model.Book;
import net.sashalom.central.blogic.exceptions.ElectronicLibraryException;
import net.sashalom.central.blogic.interfaces.IConfigurationService;
import net.sashalom.central.blogic.services.UsersService;
import net.sashalom.central.web.UserData;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("bookMng")
@Scope("session")
public class BookMng implements Serializable {

	private static final long serialVersionUID = -8736315807334591673L;
	
	private static final Logger LOGGER = Logger.getLogger(BookMng.class);
	
	@Resource
	private ApplicationContext ctx;
	
	@Autowired
	private IBookService bookService;
	
	@Autowired
	private IConfigurationService configurationService;
	
	private Book book = new Book();
	
	private UploadedFile bookFile;
	private UploadedFile imageFile;
	
	public void submitForm(){
		book.setPath(storeFile(bookFile));
		book.setFileName(bookFile.getFileName());
		book.setImage(storeFile(imageFile));
		
		try {
			UserData userData = ctx.getBean(UserData.class);
			book.setUploadDate(new Date());
			book.setUpdateDate(new Date());
			book.setUploadUser(userData.getCurrentLoggedUser());
			book.setUpdateUserId(userData.getCurrentLoggedUser().getId());
			bookService.save(book);
			book = new Book();
		} catch (ElectronicLibraryException e) {
			throw new RuntimeException(e);
		}
	}
	
	private String storeFile(UploadedFile uploadedFile){
		String filename = null;
		String newFilename = null;
		int i = 0;
		do {
			try {
				if(uploadedFile != null) {
					LOGGER.info(uploadedFile.getFileName() + " is uploaded.");
					filename = uploadedFile.getFileName();
					newFilename = prepareFilePath(UsersService.md5(filename + System.currentTimeMillis()));
					prepareFile(uploadedFile.getInputstream(), newFilename);
				}
				i = 5;
				
			} catch (FileAlreadyExistsException e) {
				i++;
				if(i == 5){
					showStoreFileError(filename);
					throw new RuntimeException(e);
				}
			} catch (Exception e) {
				showStoreFileError(filename);
				throw new RuntimeException(e);
			}
		} while (i < 5);
		return newFilename;
	}
	
	private void showStoreFileError(String filename){
		FacesMessage mess = new FacesMessage("Problem with storing file: " + filename);
		mess.setSeverity(FacesMessage.SEVERITY_ERROR);
		FacesContext.getCurrentInstance().addMessage(null, mess);
	}
	
	private String prepareFilePath(String hash){
		if(hash == null){
			throw new NullPointerException("hash can't be null");
		}
		if(hash.length() != 32){
			throw new IllegalArgumentException("Wrong hash length");
		}
		StringBuilder path = new StringBuilder(32);
		for(int i = 0; i < 8; i++){
			path.append(hash.substring(i * 2, (i * 2) + 2)).append("/");
		}
		path.append(hash.substring(16));
		return path.toString();
	}
	
	public File prepareFile(InputStream f, String relativeFilePath)
			throws IllegalStateException, FileAlreadyExistsException, ElectronicLibraryException {
		if (f == null) {
			throw new IllegalStateException(
					"File can't be null and must exists in temporary folder");
		}

		String booksFolder = configurationService.getString("booksFolder");
		if (booksFolder == null) {
			throw new IllegalStateException(
					"Books folder is not specified in configuration");
		}
		if(!booksFolder.endsWith("/")){
			booksFolder += "/"; 
		}
		new File(booksFolder + relativeFilePath.substring(0, relativeFilePath.lastIndexOf("/"))).mkdirs();
		File destFile = new File(booksFolder + relativeFilePath);
		if (destFile.exists()) {
			throw new FileAlreadyExistsException(relativeFilePath + " already exists");
		}

		OutputStream dest = null;
		try {
			dest = new FileOutputStream(destFile); 
			IOUtils.copy(f, dest);
		} catch (IOException e) {
			throw new ElectronicLibraryException(
					"Unable to copy file from tmp folder to destination", e);
		} catch (Exception e) {
			FileUtils.deleteQuietly(destFile);
		}finally{
			if(dest != null){
				try {
					dest.close();
				} catch (IOException e) {
					throw new ElectronicLibraryException(e);
				}
			}
		}
		return destFile;
	}
	
	public void uploadBook(FileUploadEvent event){
		bookFile = event.getFile();
	}
	
	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public UploadedFile getBookFile() {
		return bookFile;
	}

	public void setBookFile(UploadedFile bookFile) {
		this.bookFile = bookFile;
	}

	public UploadedFile getImageFile() {
		return imageFile;
	}

	public void setImageFile(UploadedFile imageFile) {
		this.imageFile = imageFile;
	}

}
