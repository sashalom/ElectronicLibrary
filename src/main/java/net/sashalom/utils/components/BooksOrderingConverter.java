package net.sashalom.utils.components;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import net.sashalom.books.model.BooksOrdering;

@FacesConverter("booksOrderingConverter")
public class BooksOrderingConverter implements Converter, Serializable {

	private static final long serialVersionUID = 5743178155478869243L;

	public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
		if (value == null || value.isEmpty()) {
			return null;
		}
		return BooksOrdering.valueOf(BooksOrdering.class, value);
	}

	public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
		if (value == null) {
			return null;
		}
		return value.toString();
	}
}
