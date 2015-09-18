package ch.ice.exceptions;

public class IllegalFileExtensionException extends Exception {
	
	public IllegalFileExtensionException() {}
	
	public IllegalFileExtensionException(String message){
		super(message);
	}
}
