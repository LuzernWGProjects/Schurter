package ch.ice.controller.interf;

import java.io.IOException;
import java.util.List;

import ch.ice.model.Customer;

public interface Writer {
	
	/**
	 * Write a list of CustomerObjects into a newly generated File
	 * 
	 * @param customerList			The list that will be written to an new File
	 * @param fileParserInstance	The instance of the fileParser.<br />
	 * 								This can be used to get the Workbook instance of a Parser so no further styling has to be done.
	 * @throws IOException
	 */
	public  void writeFile(List<Customer> customerList, Parser fileParserInstance) throws IOException;

}
