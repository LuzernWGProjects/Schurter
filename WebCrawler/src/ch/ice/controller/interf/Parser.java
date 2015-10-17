package ch.ice.controller.interf;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;

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

	/**
	 * Reads a file and returns a list of Customers retrieved from each row.<br />
	 * Notice: Each file input MUST follow a specified Standard. E.G. Customers Start on Row 4 in Excel.
	 * 
	 * @param	file			The input file containing all Customers to render.
	 * @return	List<Customer>	Rendered Customers are returned in a List containing all Customers
	 * 
	 * @throws IOException
	 * @throws IllegalFileExtensionException
	 * @throws EncryptedDocumentException
	 * @throws InvalidFormatException
	 * @throws InternalFormatException
	 * @throws MissingCustomerRowsException
	 */
	public List<Customer> readFile(File file) throws IOException, IllegalFileExtensionException, EncryptedDocumentException, InvalidFormatException, InternalFormatException, MissingCustomerRowsException;
	
	/**
	 * Returns all collected column headers.<br />
	 * These can be used in a Writer instance to render all specified headers
	 * 
	 * @return	List<String>	All collected Headers
	 */
	public List<String> getCellHeaders();
	
	/**
	 * Returns a Workbook instance from the Apache POI Library.<br />
	 * Can be used in a FileWriter instance to simply work with a Workbook
	 * 
	 * @return	Workbook	Workbook instance from Apache POI
	 */
	public Workbook getWorkbook();
	
	/**
	 * Set the total amount of datasets.<br />
	 * Can be used for a Progressbar or the like.
	 * 
	 * @param totalRows	Total rows to render in a File
	 */
	public void setTotalDataSets(int totalRows); // Updated once
	
	/**
	 * Returns the total Datasets.
	 * 
	 * @return int	number of renderet rows (datasets)
	 */
	public int getTotalDataSets();
	
	/**
	 * Set the current iteration row.<br />
	 * Can be used in a Progressbar type of application to calculate progress.
	 * 
	 * @param currentRowNumber	The actual rownumber that has been rendered.
	 */
	public void setCurrentRow(int currentRowNumber); // Updated within for-loop

	/**
	 * Returns the current row that has been rendered.
	 * 
	 * @return int 	current row
	 */
	public int getCurrentRow();
}