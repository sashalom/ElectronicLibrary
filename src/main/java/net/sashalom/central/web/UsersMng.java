package net.sashalom.central.web;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import net.sashalom.central.blogic.exceptions.ElectronicLibraryException;
import net.sashalom.central.blogic.interfaces.IUsersService;
import net.sashalom.central.model.User;
import net.sashalom.utils.DialogHandler;
import net.sashalom.utils.components.PaginatedData;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("usersMng")
@Scope("session")
public class UsersMng implements Serializable {

	private static final long serialVersionUID = 2101143803115186846L;
	
	@Autowired
	private IUsersService usersService;

	private User editUser;
	
	private LazyDataModel<User> users = new LazyDataModel<User>() {

		private static final long serialVersionUID = -2398479484646207843L;
		
		@Override
		public void setRowIndex(int rowIndex) {
		    if (rowIndex == -1 || getPageSize() == 0) {
		        super.setRowIndex(-1);
		    }
		    else
		        super.setRowIndex(rowIndex % getPageSize());
		}

		@Override
		public List<User> load(int first, int pageSize,
				String sortField, SortOrder sortOrder,
				Map<String, Object> filters) {
			PaginatedData<User> pd = null;
			DetachedCriteria dc = DetachedCriteria.forClass(User.class);
			dc.addOrder(Order.asc("username"));
			try {
				pd = usersService.getPaginatedDataByCriteria(dc, pageSize, first / pageSize);
			} catch (ElectronicLibraryException e) {
				throw new RuntimeException(e);
			}
			setRowCount(pd.getTotalRecords());
			return pd.getCollection();
		}
	};
	
	public void showEditPopup(User user){
		editUser = user;
		DialogHandler.show("editDialogue");
	}
	
	public void save(){
		try {
			usersService.save(editUser);
		} catch (ElectronicLibraryException e) {
			throw new RuntimeException(e);
		}
		DialogHandler.hide("editDialogue");
	}

	public LazyDataModel<User> getUsers() {
		return users;
	}

	public User getEditUser() {
		return editUser;
	}
	
}
