package ch.ice.exceptions;

public class FileParserNotAvailableException extends Exception {

	private static final long serialVersionUID = 3039694748225707627L;

	public FileParserNotAvailableException() {}

	public FileParserNotAvailableException(String message){
		super(message);
	}

}
