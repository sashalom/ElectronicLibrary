package net.sashalom.books.web;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import net.sashalom.central.web.UserData;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class BookFilter implements Filter {
	
	private static final Logger LOGGER = Logger.getLogger(BookFilter.class);
	
	private static final Pattern p = Pattern.compile(".*\\/(book)\\/(\\d+).*$");

	public void destroy() {

	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws ServletException, IOException {
		LOGGER.debug("BookFilter.doFilter CALLED");
		HttpServletRequest req = (HttpServletRequest) request;
		String url = req.getRequestURL().toString();
		ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(req.getServletContext());
		UserData userData = ctx.getBean(UserData.class);
		userData.setUrl(url);
		
		Matcher m = p.matcher(url);
		
		if (m.matches())
		{
			BookView bookView = (BookView) ctx.getBean("bookView");
			bookView.setBookID(Integer.parseInt(m.group(2)));
			
			String urlToFw =  "/books/book.html";
			RequestDispatcher dispatcher = req.getRequestDispatcher(urlToFw);
			dispatcher.forward(request,response);
			return;
		}
		chain.doFilter(request, response);
	}

	public void init(FilterConfig arg0) throws ServletException {

	}

}
