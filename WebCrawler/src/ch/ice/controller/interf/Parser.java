
package ch.ice.controller.interf;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import ch.ice.exceptions.IllegalFileExtensionException;
import ch.ice.exceptions.InternalFormatException;
import ch.ice.exceptions.MissingCustomerRowsException;
import ch.ice.model.Customer;

/**
 * 
 * @author mneuhaus
 *
 */
public interface Parser {
	
	// input can either be a Excel File or a CSV file
	public LinkedList<Customer> readFile(File file) throws IOException, IllegalFileExtensionException, EncryptedDocumentException, InvalidFormatException, InternalFormatException, MissingCustomerRowsException;
	
	//progress bar and statistic stuff
	public void setTotalDataSets(int totalRows); // Updated once
	public int getTotalDataSets();
	
	public void setCurrentRow(int currentRowNumber); // Updated within for-loop
	public int getCurrentRow();
	
		
}
