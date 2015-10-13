package ch.ice.exceptions;

public class MissingCustomerRowsException extends Exception{
	
	private static final long serialVersionUID = -6640585459525231605L;
	
	public MissingCustomerRowsException() {}
	
	public MissingCustomerRowsException(String message){
		super(message);
	}
}
