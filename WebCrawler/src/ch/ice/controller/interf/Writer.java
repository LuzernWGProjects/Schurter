package ch.ice.controller.interf;

import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;

import ch.ice.model.Customer;


public interface Writer {
	
	/**
	 *  Writes List zu File
	 *
	 * @param list
	 */
	public  void writeFile(List<Customer> customerList, Parser fileParserInstance) throws IOException;

}
