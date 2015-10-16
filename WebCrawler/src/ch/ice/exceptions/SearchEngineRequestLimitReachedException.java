package ch.ice.exceptions;

public class SearchEngineRequestLimitReachedException extends Exception {
	
	private static final long serialVersionUID = -1413097402828509384L;

	public SearchEngineRequestLimitReachedException() {}

	public SearchEngineRequestLimitReachedException(String message){
		super(message);
	}

}
