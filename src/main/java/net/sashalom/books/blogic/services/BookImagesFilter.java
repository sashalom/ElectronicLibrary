package net.sashalom.books.blogic.services;

import javax.servlet.http.HttpServletRequest;

import net.sashalom.central.blogic.interfaces.IConfigurationService;
import net.sashalom.resources.blogic.interfaces.IFilter;

import org.springframework.context.ApplicationContext;

public class BookImagesFilter implements IFilter {

	public static final String DESCRIPTOR = "book.images";

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
		if (request.getParameter("fileName") != null) {
			String fileName = request.getParameter("fileName");
			IConfigurationService conf = ctx.getBean(IConfigurationService.class);
			String folder = conf.getString("booksFolder");
			if(folder == null){
				return null;
			}
			if(!folder.endsWith("/")){
				folder += "/";
			}
			return folder + fileName;
		}
		return null;
	}

}
