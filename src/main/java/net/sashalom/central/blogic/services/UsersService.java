package net.sashalom.central.blogic.services;

import java.io.Serializable;
import java.util.List;

import net.sashalom.central.blogic.exceptions.ElectronicLibraryException;
import net.sashalom.central.blogic.interfaces.IUsersService;
import net.sashalom.central.model.Group;
import net.sashalom.central.model.User;
import net.sashalom.utils.components.PaginatedData;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class UsersService implements IUsersService, Serializable {

	private static final long serialVersionUID = 8569046128834371022L;
	
	@Autowired
	@Qualifier("mySessionFactory")
	private SessionFactory sessionFactory;
	
	private Session getSession(){
		return sessionFactory.getCurrentSession();
	}
	
	public User save(User user) throws ElectronicLibraryException {
		if(user == null){
			throw new ElectronicLibraryException("User can't be null");
		}
		getSession().saveOrUpdate(user);
		return user;
	}
	
	public User findById(int id)  throws ElectronicLibraryException{
		return (User) getSession().get(User.class, id);
	}
	
	public User findByUsername(String username)  throws ElectronicLibraryException{
		if(StringUtils.isBlank(username)){
			throw new ElectronicLibraryException("Username can't be null");
		}
		Query q = getSession().createQuery("from User where username = :username");
		q.setMaxResults(1);
		q.setString("username", username);
		
		return (User) q.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<Group> findAllGroups() throws ElectronicLibraryException {
		return (List<Group>) getSession().createQuery("from Group").list();
	}
	
	@SuppressWarnings("unchecked")
	public PaginatedData<User> getPaginatedDataByCriteria(DetachedCriteria criteria, int pageSize, int pageNumber) throws ElectronicLibraryException {
		PaginatedData<User> pd = new PaginatedData<User>(pageSize, pageNumber);
		
		Criteria c = criteria.getExecutableCriteria(getSession());
		c.setProjection(Projections.rowCount());
		pd.setTotalRecords(((Number) c.uniqueResult()).intValue());
		
		c.setProjection(null);
		c.setResultTransformer(Criteria.ROOT_ENTITY);
		c.setFirstResult(pd.getOffset());
		c.setMaxResults(pd.getPageSize());
		pd.setCollection((List<User>) c.list());
		
		return pd;
	}
	
	public static String md5(String input) throws ElectronicLibraryException {
		String md5 = null;
		try {
			StringBuffer code = new StringBuffer();
			java.security.MessageDigest messageDigest = java.security.MessageDigest
					.getInstance("MD5");
			byte bytes[] = input.getBytes();
			byte digest[] = messageDigest.digest(bytes);
			
			for(char c: Hex.encode(digest)){
				code.append(c);
			}
			md5 = code.toString();
		} catch (Exception e) {
			throw new ElectronicLibraryException(e);
		}

		return md5;
	}
	
}


