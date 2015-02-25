package net.sashalom.books.blogic.services;

import javax.servlet.http.HttpServletRequest;

import net.sashalom.books.blogic.interfaces.IBookService;
import net.sashalom.books.model.Book;
import net.sashalom.central.blogic.exceptions.ElectronicLibraryException;
import net.sashalom.central.blogic.interfaces.IConfigurationService;
import net.sashalom.resources.blogic.interfaces.IFilter;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;

public class BookFilesFilter implements IFilter {

	public static final String DESCRIPTOR = "books";

	private HttpServletRequest request;

	private ApplicationContext ctx;

	public String getDescriptor() {
		return DESCRIPTOR;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.ctx = applicationContext;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public boolean allowed() {
		if (request == null) {
			throw new IllegalStateException("Request is not specified");
		}
		return true;
	}

	public String getFileName() {
		if (request.getParameter("bookID") != null) {
			String bookIDString = request.getParameter("bookID");
			if(bookIDString == null){
				return null;
			}
			int id = 0;
			try {
				id = Integer.valueOf(bookIDString);
			} catch (NumberFormatException e){
				throw new RuntimeException("Wrong format of bookID");
			}
			IBookService bookService = ctx.getBean(IBookService.class);
			Book book = null;
			try {
				book = bookService.findById(id);
			} catch (ElectronicLibraryException e) {
				throw new RuntimeException(e);
			}
			if(book == null || StringUtils.isBlank(book.getPath())){
				return null;
			}
			
			IConfigurationService conf = ctx.getBean(IConfigurationService.class);
			String folder = conf.getString("booksFolder");
			if(folder == null){
				return null;
			}
			if(!folder.endsWith("/")){
				folder += "/";
			}
			return folder + book.getPath();
		}
		return null;
	}

}
