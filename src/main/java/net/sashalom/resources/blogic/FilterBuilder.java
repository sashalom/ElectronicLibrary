package net.sashalom.resources.blogic;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sashalom.books.blogic.services.BookFilesFilter;
import net.sashalom.books.blogic.services.BookImagesFilter;
import net.sashalom.resources.blogic.interfaces.IFilter;

import org.springframework.context.ApplicationContext;

public class FilterBuilder {
	
	private static final Map<String, Class<? extends IFilter>> FILTERS = new HashMap<String, Class<? extends IFilter>>()  {
		private static final long serialVersionUID = 1L;
		{
			put(BookImagesFilter.DESCRIPTOR, BookImagesFilter.class);
			put(BookFilesFilter.DESCRIPTOR, BookFilesFilter.class);
		}
	};
	
	private String descriptor;
	
	private ApplicationContext ctx;
	
	private HttpServletRequest request;
	
	public void setDescriptor(String descriptor) {
		this.descriptor = descriptor;
	}
	
	public void setApplicationContext(ApplicationContext ctx) {
		this.ctx = ctx;
	}
	
	public void setRequest(HttpServletRequest requested) {
		this.request = requested;
	}

	public IFilter getFilter() {
		IFilter filter = null;
		
		Class<? extends IFilter> clazz = FILTERS.get(descriptor);
		
		if (clazz != null) {
			try {
				filter = clazz.newInstance();
				filter.setRequest(request);
				filter.setApplicationContext(ctx);
			} catch (Exception e) {
				throw new RuntimeException(String.format("Failed to instantiated filter class '%'. %s",
						clazz.getName(), e.getMessage()));
			}
		}
		
		return filter;
	}
}
