package net.sashalom.central;

import java.util.List;

import net.sashalom.central.blogic.exceptions.ElectronicLibraryException;
import net.sashalom.central.blogic.interfaces.IUsersService;
import net.sashalom.central.model.Group;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/jsf2-context.xml"})
public class UsersServiceTest {

	@Autowired
	private IUsersService usersService;
	
	@Test
	public void groupsShouldBePresent(){
		try {
			List<Group> groups = usersService.findAllGroups();
			if(groups.isEmpty()){
				throw new RuntimeException("Groups should be present");
			}
		} catch (ElectronicLibraryException e) {
			throw new RuntimeException(e);
		}
	}
}
