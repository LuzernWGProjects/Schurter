package ch.ice.exceptions;

public class NoUrlFoundException extends Exception{
	
	private static final long serialVersionUID = -6640585459525231605L;
	
	public NoUrlFoundException() {}
	
	public NoUrlFoundException(String message){
		super(message);
	}
}
