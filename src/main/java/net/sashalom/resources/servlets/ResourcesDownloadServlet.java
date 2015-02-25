package net.sashalom.resources.servlets;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sashalom.central.blogic.interfaces.IConfigurationService;
import net.sashalom.resources.blogic.FilterBuilder;
import net.sashalom.resources.blogic.interfaces.IFilter;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class ResourcesDownloadServlet  extends HttpServlet {
	private static final long serialVersionUID = 2759614495605017237L;
	
	private ApplicationContext ctx;
	
	private IConfigurationService configurationService;
	
	@Override
	public void init() throws ServletException {
		super.init();
		
		ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
		if (ctx == null) {
			throw new ServletException("Failed to retrive ApplicationContext");
		}
		
		configurationService = ctx.getBean(IConfigurationService.class);
		if (configurationService == null) {
			throw new ServletException("Failed to retrive ConfigurationService");
		}
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String descriptor = request.getParameter("descriptor");
		
		FilterBuilder filterBuilder = new FilterBuilder();
		filterBuilder.setDescriptor(descriptor);
		filterBuilder.setApplicationContext(WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext()));
		filterBuilder.setRequest(request);
		
		IFilter filter = filterBuilder.getFilter();
		
		if (filter == null) {
			throw new ServletException(String.format("Unknown descriptor '%s'", descriptor));
		}
		
		if (!filter.allowed()) {
			responseForbidden(response);
		} else {
			File file = getFile(request, filter);
			if(file == null){
				responseNotFound(response);
				return;
			}
			String finalName = request.getParameter("finalName");
			if (file.isFile() && file.canRead()) {
				responseOk(response, file, finalName);
			} else {
				responseNotFound(response);
			}
		}
	}
	
	private File getFile(HttpServletRequest request, IFilter filter) {
		String fileName = filter.getFileName();
		if(fileName == null){
			return null;
		}
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
	 * @param finalName
	 * @throws IOException
	 */
	private void responseOk(HttpServletResponse response, File file, String finalName) throws IOException {
		final int BUFF_SIZE = 1024;
		
		ServletContext servletContext = getServletConfig().getServletContext();
		String mimeType = servletContext.getMimeType(file.getAbsolutePath());
		
		response.setContentType(mimeType);
		response.setContentLength((int) file.length());
		if(finalName!=null){
			response.setHeader("Content-Disposition",
					"attachment; filename=\"" + finalName + "\"");
		} else {
			response.setHeader("Content-Disposition",
				"attachment; filename=\"" + file.getName() + "\"");
		}
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
