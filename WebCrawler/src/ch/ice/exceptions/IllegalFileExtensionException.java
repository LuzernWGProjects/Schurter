package ch.ice.exceptions;

public class IllegalFileExtensionException extends Exception {
	
	private static final long serialVersionUID = -6733913013425684742L;

	public IllegalFileExtensionException() {}
	
	public IllegalFileExtensionException(String message){
		super(message);
	}
}
