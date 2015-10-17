package ch.ice.controller.file;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import au.com.bytecode.opencsv.CSVReader;
import ch.ice.controller.interf.Parser;
import ch.ice.exceptions.InternalFormatException;
import ch.ice.exceptions.MissingCustomerRowsException;
import ch.ice.model.Customer;

public class CSVParser implements Parser{
	private static final Logger logger = LogManager.getLogger(CSVParser.class.getName());

	private File CSVFileToRead;
	private int physicalRowCount;
	private int currentRowCount;
	private Workbook wb;

	// file internals
	private List<String> headerInfos = new ArrayList<String>();

	@Override
	public List<Customer> readFile(File file) throws IOException, MissingCustomerRowsException, EncryptedDocumentException, InvalidFormatException, InternalFormatException {
		// set file access to private
		this.CSVFileToRead = file;
		return this.readFile();
	}


	/**
	 * Read customer from CSV File and covert it to XSSF standard (EXCEL)
	 * 
	 * @return List<Customer> customer list
	 * @throws IOException
	 * @throws MissingCustomerRowsException 
	 * @throws InternalFormatException 
	 * @throws InvalidFormatException 
	 * @throws EncryptedDocumentException 
	 */
	private List<Customer> readFile() throws IOException, EncryptedDocumentException, InvalidFormatException, InternalFormatException, MissingCustomerRowsException {
		String[] nextLine;
		CSVReader reader = new CSVReader(new FileReader(this.CSVFileToRead),';');

		this.wb =  new XSSFWorkbook();
		Sheet sheet = this.wb.createSheet("POS Customer ID");

		int RowNum=0;
		while ((nextLine = reader.readNext()) != null)
		{
			Row currentRow = sheet.createRow(RowNum++);
			for(int i=0; i<nextLine.length; i++){
				currentRow.createCell(i).setCellValue(nextLine[i]);
			}
		}
		reader.close();
		
		logger.info("Convert CSV File to XSSF Standard.");
		
		ExcelParser excelParser = new ExcelParser();
		List<Customer> customerList = excelParser.readFile(wb);

		setCurrentRow(excelParser.getCurrentRow());
		setTotalDataSets(excelParser.getTotalDataSets());		
		this.headerInfos = excelParser.getCellHeaders();
		
		return customerList;
	}
	
	@Override
	public List<String> getCellHeaders() {
		return this.headerInfos;
	}

	@Override
	public Workbook getWorkbook(){
		return this.wb;
	}

	@Override
	public void setTotalDataSets(int totalRows) {
		this.physicalRowCount = totalRows;
	}

	@Override
	public int getTotalDataSets() {
		return this.physicalRowCount;
	}

	@Override
	public void setCurrentRow(int currentRowNumber) {
		this.currentRowCount = currentRowNumber;
	}

	@Override
	public int getCurrentRow() {
		return this.currentRowCount;
	}
}