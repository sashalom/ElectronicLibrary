package net.sashalom.utils.components;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import net.sashalom.central.model.Group;
import net.sashalom.central.web.GroupsDictionary;

import org.springframework.context.ApplicationContext;
import org.springframework.web.jsf.FacesContextUtils;

@FacesConverter("groupsConverter")
public class GroupsConverter implements Converter, Serializable {

	private static final long serialVersionUID = 5743178155478869243L;

	public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
		if (value == null || value.isEmpty()) {
			return null;
		}
		ApplicationContext ctx = FacesContextUtils.getWebApplicationContext(facesContext);
		GroupsDictionary groupsDictionary = ctx.getBean(GroupsDictionary.class);
		return groupsDictionary.findGroupByID(Integer.valueOf(value));
	}

	public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
		if (value == null) {
			return null;
		}
		return String.valueOf(((Group) value).getId());
	}
}
