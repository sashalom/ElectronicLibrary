package net.sashalom.central.web;

import java.io.Serializable;

import net.sashalom.central.model.User;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("userData")
@Scope("session")
public class UserData implements Serializable {

	private static final long serialVersionUID = 7696914492299188915L;

	private User currentLoggedUser;
	
	private String url;

	public User getCurrentLoggedUser() {
		return currentLoggedUser;
	}

	public void setCurrentLoggedUser(User currentLoggedUser) {
		this.currentLoggedUser = currentLoggedUser;
	}
	
	public boolean isAdminUser(){
		if(currentLoggedUser!=null){
			if(getCurrentLoggedUser().getGroup() != null && "ADMIN".equals(getCurrentLoggedUser().getGroup().getGroup())){
				return true;
			}
		}
		return false;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}
