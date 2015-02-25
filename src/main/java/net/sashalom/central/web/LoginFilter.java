package net.sashalom.central.web;

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

import org.apache.log4j.Logger;

public class LoginFilter implements Filter {
	
	private static final Logger LOGGER = Logger.getLogger(LoginFilter.class);
	
	private static final Pattern p = Pattern.compile(".*\\/(login)\\/.*$");

	public void destroy() {

	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws ServletException, IOException {
		LOGGER.debug("LoginFilter.doFilter CALLED");
		HttpServletRequest req = (HttpServletRequest) request;
		String url = req.getRequestURL().toString();
		if (!url.endsWith("/"))
		{
			url +="/";
		}
		
		Matcher m = p.matcher(url);
		
		if (m.matches())
		{
			String urlToFw =  "/login.html";
			RequestDispatcher dispatcher = req.getRequestDispatcher(urlToFw);
			dispatcher.forward(request,response);
			return;
		}
		chain.doFilter(request, response);
	}

	public void init(FilterConfig arg0) throws ServletException {

	}

}
