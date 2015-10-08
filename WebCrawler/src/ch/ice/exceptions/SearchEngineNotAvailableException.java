package ch.ice.exceptions;

public class SearchEngineNotAvailableException extends Exception {
	
	private static final long serialVersionUID = -5571100869780302077L;

	public SearchEngineNotAvailableException() {}

	public SearchEngineNotAvailableException(String message){
		super(message);
	}

}
