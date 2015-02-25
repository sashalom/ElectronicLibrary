package net.sashalom.books.blogic.services;

import java.io.Serializable;
import java.util.List;

import net.sashalom.books.blogic.interfaces.IBookService;
import net.sashalom.books.model.Book;
import net.sashalom.books.model.Comment;
import net.sashalom.books.model.Rating;
import net.sashalom.central.blogic.exceptions.ElectronicLibraryException;
import net.sashalom.utils.components.PaginatedData;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class BookService implements IBookService, Serializable {

	private static final long serialVersionUID = 8569046128834371022L;
	
	@Autowired
	@Qualifier("mySessionFactory")
	private SessionFactory sessionFactory;
	
	private Session getSession(){
		return sessionFactory.getCurrentSession();
	}
	
	public Book save(Book book) throws ElectronicLibraryException {
		if(book == null){
			throw new ElectronicLibraryException("Book can't be null");
		}
		getSession().saveOrUpdate(book);
		return book;
	}
	
	public Book findById(int id)  throws ElectronicLibraryException{
		return (Book) getSession().get(Book.class, id);
	}
	
	public Book findByIdWithComments(int id)  throws ElectronicLibraryException{
		Book book = findById(id);
		if(book != null){
			book.getComments().size();
		}
		return book;
	}
	
	@SuppressWarnings("unchecked")
	public PaginatedData<Book> getPaginatedDataByCriteria(DetachedCriteria criteria, int pageSize, int pageNumber) throws ElectronicLibraryException {
		PaginatedData<Book> pd = new PaginatedData<Book>(pageSize, pageNumber);
		
		Criteria c = criteria.getExecutableCriteria(getSession());
		c.setProjection(Projections.rowCount());
		pd.setTotalRecords(((Number) c.uniqueResult()).intValue());
		
		c.setProjection(null);
		c.setResultTransformer(Criteria.ROOT_ENTITY);
		c.setFirstResult(pd.getOffset());
		c.setMaxResults(pd.getPageSize());
		pd.setCollection((List<Book>) c.list());
		
		return pd;
	}
	
	public Rating save(Rating rating) throws ElectronicLibraryException {
		if(rating == null){
			throw new ElectronicLibraryException("Rating can't be null");
		}
		getSession().saveOrUpdate(rating);
		return rating;
	}
	
	public int updateRating(int bookId) throws ElectronicLibraryException{
		Query q = getSession().createSQLQuery("SELECT ROUND(AVG(rating)) FROM `ratings` WHERE bookID = :bookID");
		q.setInteger("bookID", bookId);
		Number tmp = (Number) q.uniqueResult();
		int rating = 0;
		if(tmp != null){
			rating = tmp.intValue();
		}
		q = getSession().createQuery("UPDATE Book SET rate = :rating WHERE id = :bookID");
		q.setInteger("bookID", bookId);
		q.setInteger("rating", rating);
		q.executeUpdate();
		return rating;
	}
	
	public boolean ratingAssigned(int bookId, int userId) throws ElectronicLibraryException{
		Query q = getSession().createSQLQuery("SELECT COUNT(id) FROM `ratings` WHERE bookID = :bookID AND userID = :userID");
		q.setInteger("bookID", bookId);
		q.setInteger("userID", userId);
		int count = ((Number) q.uniqueResult()).intValue();
		return count > 0;
	}
	
	public Comment save(Comment comment) throws ElectronicLibraryException {
		if(comment == null){
			throw new ElectronicLibraryException("Comment can't be null");
		}
		getSession().saveOrUpdate(comment);
		return comment;
	}
	
}


