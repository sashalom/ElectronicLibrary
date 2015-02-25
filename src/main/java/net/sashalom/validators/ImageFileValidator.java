package net.sashalom.validators;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.imageio.ImageIO;

import org.primefaces.model.DefaultUploadedFile;

@FacesValidator("imageFileValidator")
public class ImageFileValidator implements Validator {
	
	
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		DefaultUploadedFile fileUpload = (DefaultUploadedFile) value;
		
		String allowTypes = (String) component.getAttributes().get("allowTypes");
		if(allowTypes == null){
			allowTypes = "[\\s\\w\\W\\.-]+\\.(gif|jpe?g|png)$";
		}
		String tmp = (String) component.getAttributes().get("maxWidth");
		int maxWidth = 800;
		if(tmp != null){
			maxWidth = Integer.parseInt(tmp);
			tmp=null;
		}
		
		tmp = (String) component.getAttributes().get("maxHeight");
		int maxHeight = 600;
		if(tmp != null){
			maxHeight = Integer.parseInt(tmp);
			tmp=null;
		}
		
		Pattern pattern = Pattern.compile(allowTypes,Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(fileUpload.getFileName());
		
		if (!matcher.matches()) {
			FacesMessage message = new FacesMessage("File type is not valid");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(message);
		}
		
		try {
			BufferedImage bimg = ImageIO.read(fileUpload.getInputstream());
			if(bimg.getWidth() > maxWidth || bimg.getHeight() > maxHeight){
				FacesMessage message = new FacesMessage(String.format("Image size should be not more then %dx%d", maxWidth, maxHeight));
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ValidatorException(message);
			}
			
			
		} catch (IOException e) {
			FacesMessage message = new FacesMessage("Problem with reading image");
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(message);
		}
		
	}
}