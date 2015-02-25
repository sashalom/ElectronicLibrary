package net.sashalom.central.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.servlet.http.HttpServletRequest;

import net.sashalom.central.blogic.exceptions.ElectronicLibraryException;
import net.sashalom.central.blogic.interfaces.IUsersService;
import net.sashalom.central.blogic.services.UsersService;
import net.sashalom.central.model.Group;
import net.sashalom.central.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("userMng")
@Scope("session")
public class UserMng implements Serializable {

	private static final long serialVersionUID = 2101143803115186846L;
	
	@Autowired
	private IUsersService usersService;

	private User user;
	
	private List<Group> groups = new ArrayList<Group>();
	
	@PostConstruct
	private void init(){
		try {
			groups = usersService.findAllGroups();
		} catch (ElectronicLibraryException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void preparePageLoadHandler(ComponentSystemEvent event) {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext ec = context.getExternalContext();
		if (((HttpServletRequest) ec.getRequest()).getMethod().equals("GET")){
			onPageCall();
		}
	}
	
	private void onPageCall(){
		initNewUser();
	}
	
	private void initNewUser(){
		user = new User();
		user.setActive(false);
		user.setConfirmed(false);
		user.setGroup(getDefaultGroup());
	}
	
	private Group getDefaultGroup(){
		for(Group g : groups){
			if("USER".equals(g.getGroup())){
				return g;
			}
		}
		return null;
	}
	
	public String saveUser(){
		try {
			if(usersService.findByUsername(user.getUsername()) != null){
				FacesMessage mess = new FacesMessage("Username: " + user.getUsername() + " alreadyExist");
				mess.setSeverity(FacesMessage.SEVERITY_ERROR);
				FacesContext.getCurrentInstance().addMessage(null, mess);
				return null;
			}
			user.setPassword(UsersService.md5(user.getPassword()));
			usersService.save(user);
		} catch (ElectronicLibraryException e) {
			throw new RuntimeException(e);
		}
		return "registrationComplete";
	}

	public User getUser() {
		return user;
	}
}
