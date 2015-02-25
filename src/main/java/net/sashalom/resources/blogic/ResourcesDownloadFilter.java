package net.sashalom.resources.blogic;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sashalom.resources.blogic.interfaces.IFilter;

import org.springframework.web.context.support.WebApplicationContextUtils;

public class ResourcesDownloadFilter implements Filter {

	private ServletContext servletContext;
	
	public void destroy() {

	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain arg2) throws IOException, ServletException {

		String descriptor = request.getParameter("descriptor");
		FilterBuilder filterBuilder = new FilterBuilder();
		filterBuilder.setDescriptor(descriptor);
		filterBuilder.setApplicationContext(WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext));
		filterBuilder.setRequest((HttpServletRequest)request);
		
		IFilter filter = filterBuilder.getFilter();
		
		if (filter == null) {
			throw new ServletException(String.format("Unknown descriptor '%s'", descriptor));
		}
		
		if (!filter.allowed()) {
			responseForbidden((HttpServletResponse)response);
		} else {
			File file = getFile((HttpServletRequest)request, filter);
			
			if (file.isFile() && file.canRead()) {
				responseOk((HttpServletResponse)response, file);
			} else {
				responseNotFound((HttpServletResponse)response);
			}
		}
		
	}

	public void init(FilterConfig config) throws ServletException {
		this.servletContext = config.getServletContext();

	}
	
	private File getFile(HttpServletRequest request, IFilter filter) {
		String fileName = filter.getFileName();
		return new File(fileName);
	}
	
	/**
	 * Send 403 page.
	 * 
	 * @param response
	 * @throws IOException
	 */
	private void responseForbidden(HttpServletResponse response) throws IOException {
		String page = "<html><body><h1>"
				+ HttpServletResponse.SC_FORBIDDEN
				+ " Forbidden"
				+ "</h1></body></html>";
		
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.getWriter().println(page);
	}
	
	/**
	 * Send 404 response page.
	 * 
	 * @param response
	 * @throws IOException
	 */
	private void responseNotFound(HttpServletResponse response) throws IOException {
		String page = "<html><body><h1>"
			+ HttpServletResponse.SC_NOT_FOUND
			+ " Page not found"
			+ "</h1></body></html>";
		
		response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		response.getWriter().println(page);
	}
	
	/**
	 * Send requested file.
	 * 
	 * @param response
	 * @param file
	 * @throws IOException
	 */
	private void responseOk(HttpServletResponse response, File file) throws IOException {
		final int BUFF_SIZE = 1024;
		
		String mimeType = servletContext.getMimeType(file.getAbsolutePath());
		
		response.setContentType(mimeType);
		response.setContentLength((int) file.length());
		response.setHeader("Content-Disposition",
				"attachment; filename=\"" + file.getName() + "\"");
		response.setHeader("Content-Type",mimeType);

		byte[] buff = new byte[BUFF_SIZE];
		DataInputStream in = new DataInputStream(new FileInputStream(file));
		BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());

		int read;
		while ((read = in.read(buff)) != -1) {
			out.write(buff, 0, read);
		}

		in.close();
		out.close();
	}

}
