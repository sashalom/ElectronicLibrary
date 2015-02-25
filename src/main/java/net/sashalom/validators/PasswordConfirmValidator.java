package net.sashalom.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("passwordConfirmValidator")
public class PasswordConfirmValidator implements Validator {
	
	
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		String pswd1 = (String) value;
		String passwordID = (String) component.getAttributes().get("passwordID");
		UIInput pass = (UIInput) component.findComponent(passwordID);
		String pswd = (String) pass.getLocalValue();
		if(!pswd.equals(pswd1)){
			FacesMessage message = new FacesMessage("Passwords should be the same");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(message);
		}
		
	}
}