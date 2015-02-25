package net.sashalom.central.blogic.interfaces;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import net.sashalom.central.blogic.exceptions.ElectronicLibraryException;
import net.sashalom.central.model.Group;
import net.sashalom.central.model.User;
import net.sashalom.utils.components.PaginatedData;

public interface IUsersService {

	public User save(User user) throws ElectronicLibraryException;
	
	public User findById(int id)  throws ElectronicLibraryException;
	
	public List<Group> findAllGroups() throws ElectronicLibraryException;
	
	public User findByUsername(String username)  throws ElectronicLibraryException;
	
	public PaginatedData<User> getPaginatedDataByCriteria(DetachedCriteria criteria, int pageSize, int pageNumber) throws ElectronicLibraryException;
}
