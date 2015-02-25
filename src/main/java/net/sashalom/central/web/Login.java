package net.sashalom.central.web;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import net.sashalom.central.model.Constants;
import net.sashalom.central.model.User;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.jsf.FacesContextUtils;

@Component("login")
@Scope("request")
public class Login {

	private static final Logger LOGGER = Logger.getLogger(Login.class);
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	public Login(){
		
	}
	
	private String username;
	
	private String password;
	
	public void login(ActionEvent e) throws IOException
	{
		try {
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
			Authentication newAuth = authenticationManager.authenticate(authToken);
			SecurityContextHolder.getContext().setAuthentication(newAuth);
			
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			User user = null;
			if (principal instanceof User){
				user = ((User) principal);
				if(!user.isConfirmed() || !user.isActive()){
					throw new BadCredentialsException("User not active or not confirmed");
				}
			}
		} catch (UsernameNotFoundException unfe) {
			FacesMessage mess = new FacesMessage("Check username and password1");
			mess.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage("default", mess);
			return;
		} catch (BadCredentialsException unfe) {
			FacesMessage mess = new FacesMessage("Check username and password2");
			mess.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage("default", mess);
			return;
		} catch (Exception e2) {
			LOGGER.error(e2.getMessage(), e2);
			return;
		}

		try{
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			
			ApplicationContext ctx = (ApplicationContext) FacesContextUtils
					.getWebApplicationContext(FacesContext.getCurrentInstance());

			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			User currentLoggedUser = null;
			if (principal instanceof User){
				currentLoggedUser = ((User) principal);
			}
			
			UserData userData = ctx.getBean(UserData.class);
			userData.setCurrentLoggedUser(currentLoggedUser);
			
			String encodedURL = null;
			if(userData.getUrl() == null){
				encodedURL = ec.encodeResourceURL(ec.getRequestContextPath() + "/index"  + Constants.DEFAULT_FACES_EXTENSION);
			} else {
				encodedURL = userData.getUrl();
			}
			ec.redirect(encodedURL);
			ec.responseFlushBuffer();
			FacesContext.getCurrentInstance().responseComplete();
		}catch (Exception e2) {
			LOGGER.error(e2.getMessage(),e2);
		}
	}

	public void logout(ActionEvent actionEvent) throws IOException
	{
		ApplicationContext ctx = FacesContextUtils.getWebApplicationContext(FacesContext.getCurrentInstance());
		UserData userData = (UserData) ctx.getBean("userData");
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();

		LOGGER.trace("logout CALLED with " + userData.getCurrentLoggedUser().getUsername());
		//TODO will be good to put here some operations to store data about last access time
		String encodedURL = ec.encodeResourceURL(ec.getRequestContextPath() + "/j_spring_security_logout");
		userData.setCurrentLoggedUser(null);
		ec.redirect(encodedURL);
		ec.responseFlushBuffer();
	}
	
	public void goBack(){
		ApplicationContext ctx = (ApplicationContext) FacesContextUtils
				.getWebApplicationContext(FacesContext.getCurrentInstance());
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		UserData userData = ctx.getBean(UserData.class);
		
		String encodedURL = null;
		if(userData.getUrl() == null){
			encodedURL = ec.encodeResourceURL(ec.getRequestContextPath() + "/index"  + Constants.DEFAULT_FACES_EXTENSION);
		} else {
			encodedURL = userData.getUrl();
		}
		try {
			ec.redirect(encodedURL);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
}
