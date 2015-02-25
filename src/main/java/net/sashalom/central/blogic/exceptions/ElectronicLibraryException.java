package net.sashalom.central.blogic.exceptions;

public class ElectronicLibraryException extends Exception {
	private static final long serialVersionUID = 1L;

	public ElectronicLibraryException() {
		super();
	}
	
	public ElectronicLibraryException(Throwable t) {
		super(t);
	}
	
	public ElectronicLibraryException(String message) {
		super(message);
	}

	public ElectronicLibraryException(String message, Throwable cause) {
		super(message, cause);
	}

}
