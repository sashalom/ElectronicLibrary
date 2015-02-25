package net.sashalom.central.blogic.services;

import javax.annotation.Resource;

import net.sashalom.central.model.User;

import org.apache.log4j.Logger;
import org.hibernate.NonUniqueResultException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class SpringSignonService implements UserDetailsService {

	private Logger logger = Logger.getLogger(SpringSignonService.class);
	
	@Autowired
	@Qualifier("mySessionFactory")
	private SessionFactory sessionFactory;
	
	private Session getSession(){
		return sessionFactory.getCurrentSession();
	}

	@Resource
	private ApplicationContext ctx;
	
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		logger.debug("loadUserByUsername CALLED with parameter: " + username);
		User user = null;
		try{
			Query query = getSession().createQuery("from User where username = :username").setString("username", username);
			user = (User) query.uniqueResult();
		} catch(NonUniqueResultException e){
			throw new UsernameNotFoundException("Found more then one User with username: " + username); 
		}
		if(user == null){
			throw new UsernameNotFoundException("Username: " + username + " not found");
		}
		return user;
	}

}
