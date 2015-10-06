package ch.ice.controller.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import ch.ice.controller.interf.Parser;
import ch.ice.exceptions.IllegalFileExtensionException;
import ch.ice.exceptions.InternalFormatException;
import ch.ice.exceptions.MissingCustomerRowsException;
import ch.ice.model.Customer;
import ch.ice.model.Website;

/**
 * 
 * @author mneuhaus
 *
 */

public class ExcelParser implements Parser {
	private static final Logger logger = LogManager.getLogger(ExcelParser.class
			.getName());

	private File file;
	private InputStream ExcelFileToRead;
	private List<String> allowedFileExtensions = new ArrayList<String>();
	private int physicalRowCount;
	private int currentRowCount;

	List<Customer> customerList = new ArrayList<Customer>();

	PropertiesConfiguration config;

	Workbook wb;

	// Customer Fields
	// headers from File
	List<String> headerInfos = new ArrayList<String>();
	String customerIDHeader;
	String countryNameHeader;
	String zipCodeHeader;
	String customerNameShortHeader;
	String customerNameHeader;

	// customer data
	String customerID;
	String customerCountryCode;
	String country;
	String zipCode;
	String customerFullName;
	String custonerShortName;

	public ExcelParser() {

		// load config file
		try {
			this.config = new PropertiesConfiguration("conf/app.properties");

			// get all allowed file extensions (xls,xlsx,csv)
			this.allowedFileExtensions = Arrays.asList(this.config
					.getStringArray("parser.allowedFileExtensions"));
		} catch (ConfigurationException e) {
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	@Override
	public List<Customer> readFile(File file)
			throws IllegalFileExtensionException, EncryptedDocumentException,
			InvalidFormatException, IOException, InternalFormatException,
			MissingCustomerRowsException {

		// set file to private access only
		this.file = file;

		// check if it is an XLS file or a XLSX file
		String fileExtension = FilenameUtils.getExtension(file.getName())
				.toLowerCase();

		// check for all allowed file extensions
		if (!this.allowedFileExtensions.contains(fileExtension)) {
			logger.error("Wrong Fileextension: " + fileExtension + "; Only "
					+ this.allowedFileExtensions.toString() + " allowed.");

			throw new IllegalFileExtensionException(
					"Wrong file Extension. Please only use "
							+ this.allowedFileExtensions.toString());
		}

		switch (fileExtension) {
		case "xlsx":
		case "xls":
			return this.readFile();

		case "cvs":
			// return readCVSFile();
			break;
		}

		return null;
	}

	/**
	 * Read a File and ingnore file format (xls, xlsx). Due to the ss usermodel
	 * and generic handling.
	 * 
	 * @return LinkedList<Customer>
	 * @throws EncryptedDocumentException
	 * @throws InvalidFormatException
	 * @throws IOException
	 * @throws InternalFormatException
	 * @throws MissingCustomerRowsException
	 */
	private List<Customer> readFile() throws EncryptedDocumentException,
			InvalidFormatException, IOException, InternalFormatException,
			MissingCustomerRowsException {
		ExcelFileToRead = new FileInputStream(this.file);

		this.wb = WorkbookFactory.create(ExcelFileToRead);

		// load first sheet in File
		Sheet sheet = this.wb.getSheetAt(0);

		// set total amount of rows (Customers)
		if (sheet.getPhysicalNumberOfRows() == 0) {
			this.setTotalDataSets(0);
		} else {
			this.setTotalDataSets(sheet.getPhysicalNumberOfRows() - 3);
		}

		Row row;
		Cell cell;

		Iterator<?> rows = sheet.rowIterator();

		while (rows.hasNext()) {

			row = (Row) rows.next();

			// skip first two rows
			if (row.getRowNum() == 0 || row.getRowNum() == 1)
				continue;

			// get table heads
			if (row.getRowNum() == 2) {
				if (row.getCell(0) == null && row.getCell(1) == null
						&& row.getCell(3) == null && row.getCell(4) == null
						&& row.getCell(6) == null)
					throw new InternalFormatException(
							"It seems that the selected File has the wrong internal Format. Customers should start on row 3");

				this.customerIDHeader = row.getCell(0).toString();
				this.countryNameHeader = row.getCell(1).toString();
				this.zipCodeHeader = row.getCell(3).toString();
				this.customerNameShortHeader = row.getCell(4).toString();
				this.customerNameHeader = row.getCell(6).toString();

				this.headerInfos.add(this.customerIDHeader);
				this.headerInfos.add(this.countryNameHeader);
				this.headerInfos.add(this.zipCodeHeader);
				this.headerInfos.add(this.customerNameShortHeader);
				this.headerInfos.add(this.customerNameHeader);

				continue;

			}

			// current row number
			this.setCurrentRow(row.getRowNum() + 1);

			Iterator<?> cells = row.cellIterator();

			while (cells.hasNext()) {
				cell = (Cell) cells.next();

				// skip unused cells
				if (cell.getColumnIndex() >= 7)
					continue;

				switch (cell.getColumnIndex()) {
				// customer id
				case 0:
					this.customerID = this.checkForCellType(cell);
					break;

				// country Code
				case 1:
					this.customerCountryCode = this.checkForCellType(cell);

					// country Name
				case 2:
					this.country = this.checkForCellType(cell);
					break;

				// Zip code
				case 3:
					this.zipCode = this.checkForCellType(cell);
					break;

				// customer name short
				case 4:
					this.custonerShortName = this.checkForCellType(cell);
					break;

				// empty cell
				case 5:
					continue;

					// customer full name
				case 6:
					this.customerFullName = this.checkForCellType(cell);
					break;
				}
			}

			/*
			 * Generate Customer Object and add it to the array
			 */
			this.customerList.add(this.createCustomer());
		}

		if (this.customerList.size() < 1)
			throw new MissingCustomerRowsException(
					"There are no rendered Customers. Please make sure customers start on row number 4.");

		logger.info("Rendered Customers from List: " + this.customerList.size());
		return this.customerList;

	}

	/**
	 * Create a new customer for every row in the file
	 * 
	 * @return customer model with empty Website.
	 */
	public Customer createCustomer() {

		Customer customer = new Customer();

		customer.setId(this.customerID);
		customer.setCountryCode(this.customerCountryCode);
		customer.setCountryName(this.country);
		customer.setFullName(this.customerFullName);
		customer.setShortName(this.custonerShortName);
		customer.setZipCode(this.zipCode);

		// Website model content is null
		customer.setWebsite(new Website());

		return customer;
	}

	/**
	 * Return all collected Headercells from File. Will be used for creating new
	 * file with the correct Headers
	 * 
	 * @return Value in header cells
	 */
	public List<String> getCellHeaders() {
		return this.headerInfos;
	}

	/**
	 * Check if the cell is numeric or a string type
	 */
	private String checkForCellType(Cell cell) {
		if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
			return cell.getStringCellValue().toString();
		}

		else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			return Double.toString(cell.getNumericCellValue());
		}
		return null;
	}

	// progress bar and statistic stuff
	public void setTotalDataSets(int totalRows) {
		this.physicalRowCount = totalRows;
	}

	public int getTotalDataSets() {
		return this.physicalRowCount;
	}

	public void setCurrentRow(int currentRowNumber) {
		this.currentRowCount = currentRowNumber;
	}

	public int getCurrentRow() {
		return this.currentRowCount;
	}

	public Workbook getWorkbook() {
		return this.wb;
	}
}