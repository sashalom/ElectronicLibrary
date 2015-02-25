package net.sashalom.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.primefaces.model.DefaultUploadedFile;

@FacesValidator("bookFileValidator")
public class BookFileValidator implements Validator {
	
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		DefaultUploadedFile fileUpload = (DefaultUploadedFile) value;
		
		String allowTypes = (String) component.getAttributes().get("allowTypes");
		if(allowTypes == null){
			allowTypes = "[\\s\\w\\W\\.-]+\\.(odt|pdf|doc|rtf|txt)$";
		}
		Long sizeLimit = (Long) component.getAttributes().get("sizeLimit");
		if(sizeLimit == null){
			sizeLimit = 20971520L;
		}
		
		Pattern pattern = Pattern.compile(allowTypes,Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(fileUpload.getFileName());
		
		if (!matcher.matches()) {
			FacesMessage message = new FacesMessage("File type is not valid");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			
			throw new ValidatorException(message);
		}
		
		if(fileUpload.getSize() > sizeLimit){
			FacesMessage message = new FacesMessage("File size is not valid");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			
			throw new ValidatorException(message);
		}
	}
}