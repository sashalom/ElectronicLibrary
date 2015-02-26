package net.sashalom.central;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/jsf2-context.xml"})
public class SpringSignonServiceTest {

	@Autowired
	private UserDetailsService springSignonService;
	
	@Test
	public void adminUserShouldBePresent(){
		springSignonService.loadUserByUsername("admin");
	}
}
