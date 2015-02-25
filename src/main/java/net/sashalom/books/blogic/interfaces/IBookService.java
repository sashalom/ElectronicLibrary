package net.sashalom.books.blogic.interfaces;

import net.sashalom.books.model.Book;
import net.sashalom.books.model.Comment;
import net.sashalom.books.model.Rating;
import net.sashalom.central.blogic.exceptions.ElectronicLibraryException;
import net.sashalom.utils.components.PaginatedData;

import org.hibernate.criterion.DetachedCriteria;

public interface IBookService {

	public Book save(Book book) throws ElectronicLibraryException;
	
	public Book findById(int id) throws ElectronicLibraryException;
	
	public Book findByIdWithComments(int id)  throws ElectronicLibraryException;
	
	public PaginatedData<Book> getPaginatedDataByCriteria(DetachedCriteria criteria, int pageSize, int pageNumber) 
			throws ElectronicLibraryException;
	
	public Rating save(Rating rating) throws ElectronicLibraryException;
	
	public int updateRating(int bookId) throws ElectronicLibraryException;
	
	public boolean ratingAssigned(int bookId, int userId) throws ElectronicLibraryException;
	
	public Comment save(Comment comment) throws ElectronicLibraryException;
	
}
