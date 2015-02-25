package net.sashalom.central.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import net.sashalom.central.blogic.exceptions.ElectronicLibraryException;
import net.sashalom.central.blogic.interfaces.IUsersService;
import net.sashalom.central.model.Group;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("groupsDictionary")
@Scope("session")
public class GroupsDictionary implements Serializable {

	private static final long serialVersionUID = -5367903905896495560L;

	@Autowired
	private IUsersService usersService;

	private List<Group> groups = new ArrayList<Group>();
	
	@PostConstruct
	private void init(){
		try {
			groups = usersService.findAllGroups();
		} catch (ElectronicLibraryException e) {
			throw new RuntimeException(e);
		}
	}
	
	public Group findGroupByID(int id){
		for(Group g : groups){
			if(g.getId() == id){
				return g;
			}
		}
		return null;
	}
	
	public List<SelectItem> getGroupsSI(){
		List<SelectItem> items = new ArrayList<SelectItem>(groups.size());
		for(Group g : groups){
			items.add(new SelectItem(g, g.getGroup()));
		}
		return items;
	}
}
