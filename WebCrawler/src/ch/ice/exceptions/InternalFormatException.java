package ch.ice.exceptions;

public class InternalFormatException extends Exception{
	
	private static final long serialVersionUID = 7117557165832874867L;
	
	public InternalFormatException() {}
	
	public InternalFormatException(String message){
		super(message);
	}
}
