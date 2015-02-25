package net.sashalom.utils.components;

import java.io.Serializable;
import java.util.List;

public class PaginatedData<T> implements Serializable {

	private static final long serialVersionUID = -9187296518413649761L;

	public static final int DEFAULT_PAGE_SIZE = 10;
	
	public static final int MAX_PAGE_SIZE = 500;
	
	private List<T> collection;
	
	private int totalRecords;
	
	private int pageSize;
	
	private int pageNumber;
	
	public PaginatedData(){
		pageSize = DEFAULT_PAGE_SIZE;
	}
	
	public PaginatedData(int pageSize, int pageNumber){
		setPageSize(pageSize);
		setPageNumber(pageNumber);
	}
	
	public PaginatedData(int pageSize, int pageNumber, int totalRecords, List<T> collection){
		setPageSize(pageSize);
		setPageNumber(pageNumber);
		setTotalRecords(totalRecords);
		setCollection(collection);
	}

	public List<T> getCollection() {
		return collection;
	}

	public void setCollection(List<T> collection) {
		this.collection = collection;
	}

	public int getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(int totalRecords) {
		if(totalRecords < 0){
			throw new IllegalArgumentException("Total records can't be less then 0");
		}
		this.totalRecords = totalRecords;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		if(pageSize < 0 || pageSize > MAX_PAGE_SIZE){
			throw new IllegalArgumentException("Page size can't be less then 0 or more then " + MAX_PAGE_SIZE);
		}
		this.pageSize = pageSize;
	}

	public int getPageNumber() {
		if(pageNumber < 0){
			throw new IllegalArgumentException("Page number can't be less then 0");
		}
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	
	public int getOffset(){
		return pageNumber * pageSize;
	}
	
}
