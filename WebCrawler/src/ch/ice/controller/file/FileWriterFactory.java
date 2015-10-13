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
	
	public FileWriterFactory() throws FileParserNotAvailableException {
		requestFileWriter(FileWriterFactory.EXCEL);
	}
	
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
