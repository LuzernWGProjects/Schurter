package ch.ice.controller.file;

import ch.ice.controller.interf.Parser;
import ch.ice.exceptions.FileParserNotAvailableException;

/**
 * Generate a FileParser instance
 * 
 * @author mneuhaus
 *
 */
public class FileParserFactory {

	public final static String CSV = "csv";
	public final static String EXCEL = "xlsx";
	
	/**
	 * If no params are defined return the default Excel FileWriter
	 * @throws FileParserNotAvailableException
	 */
	public FileParserFactory() throws FileParserNotAvailableException {
		requestParser(FileParserFactory.EXCEL);
	}
	
	/**
	 * Request a new FileWriter instance.<br />
	 * Based on the String that is passed to the function.
	 * 
	 * @param identifier	Requested FileParser. Available are CSV or EXCEL
	 * @return Parser		The actual requested Parser
	 * @throws FileParserNotAvailableException
	 */
	public static Parser requestParser(String identifier) throws FileParserNotAvailableException {
		if(identifier.equals(FileParserFactory.CSV) && identifier == FileParserFactory.CSV) {
			return new CSVParser();
		} else if (identifier.equals(FileParserFactory.EXCEL) && identifier == FileParserFactory.EXCEL){
			return new ExcelParser();
		} else {
			throw new FileParserNotAvailableException("Requested FileParser instance is not available. Please use src.ch.ice.controller.file.ExcelParser or src.ch.ice.controller.file.CSVParser");
		}
	}
	
}