package ch.ice.controller.file;

import ch.ice.controller.interf.Writer;
import ch.ice.exceptions.FileParserNotAvailableException;

/**
 * Generate a FileWriter instance
 * 
 * @author mneuhaus
 *
 */
public class FileWriterFactory {

	public final static String CSV = "csv";
	public final static String EXCEL = "xlsx";
	
	/**
	 * If no params are defined, return the default Excel FileWriter
	 * 
	 * @throws FileParserNotAvailableException
	 */
	public FileWriterFactory() throws FileParserNotAvailableException {
		requestFileWriter(FileWriterFactory.EXCEL);
	}
	
	/**
	 * Request a new FileWriter. Available FileWriters are CSV or EXCEL
	 * 
	 * @param identifier
	 * @return Writer 	The requested Writer
	 * @throws FileParserNotAvailableException
	 */
	public static Writer requestFileWriter(String identifier) throws FileParserNotAvailableException{
		if(identifier.equals(FileWriterFactory.CSV) && identifier == FileWriterFactory.CSV) {
			return new CSVWriter();
		} else if (identifier.equals(FileWriterFactory.EXCEL) && identifier == FileWriterFactory.EXCEL){
			return new ExcelWriter();
		} else {
			throw new FileParserNotAvailableException("Requested FileParser instance is not available. Please use src.ch.ice.controller.file.ExcelParser or src.ch.ice.controller.file.CSVParser");
		}
	}
}