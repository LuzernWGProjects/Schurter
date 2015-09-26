package ch.ice.exceptions;

public class HttpStatusException extends Exception{
	
	public HttpStatusException() {}
	
	public HttpStatusException(String message){
		super(message);
	}

}
