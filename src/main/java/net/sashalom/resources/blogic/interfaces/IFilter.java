package net.sashalom.resources.blogic.interfaces;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;

public interface IFilter {

	public String getDescriptor();
	
	public void setApplicationContext(ApplicationContext ctx);
	
	public void setRequest(HttpServletRequest request);
	
	public boolean allowed();
	
	public String getFileName();
}
