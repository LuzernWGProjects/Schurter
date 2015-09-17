package ch.ice.controller.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import ch.ice.controller.interf.Parser;
import ch.ice.model.Customer;
import ch.ice.model.Website;

/*
 *  NOTE: Write config files with allowed file extensions.
 *  	-> XLS OR XLSX
 * 
 */

public class ExcelParser implements Parser {
	private File file;
	private InputStream ExcelFileToRead;
	
	LinkedList<Customer> customerList = new LinkedList<Customer>();
	
	// Customer Fields
	//headers
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

	@Override
	public LinkedList<Customer> readFile(File file) throws IOException {
		
		// set file to private access
		this.file = file;
		
		
		// check if it is an XLS file or a XLSX file
		String fileExtension = FilenameUtils.getExtension(file.getName()).toLowerCase();
		
		switch(fileExtension) {
			case "xlsx":
				return readXLSXFile();
				
			case "xls":
				readXLSFile();
				break;
			
			default:
					System.out.println("Diese Dateeinung ist nicht erlaubt");
					break;
		}
		return null;
		
	}

	
	private LinkedList<Customer> readXLSXFile() throws IOException {
		ExcelFileToRead = new FileInputStream(this.file);
		
		XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);
		
		//load first sheet in the File
		XSSFSheet sheet = wb.getSheetAt(0);
		
		XSSFRow row; 
		XSSFCell cell;
		

		Iterator<?> rows = sheet.rowIterator();
		
		
		while (rows.hasNext()) {
			
			row=(XSSFRow) rows.next();
			//skip first two rows
			if(row.getRowNum() == 0 || row.getRowNum() == 1) continue;
			
			//get table heads
			if(row.getRowNum() == 2){
				this.customerIDHeader = 			row.getCell(0).toString();
				this.countryNameHeader =			row.getCell(1).toString();
				this.zipCodeHeader = 				row.getCell(3).toString();
				this.customerNameShortHeader = 	row.getCell(4).toString();
				this.customerNameHeader = 		row.getCell(6).toString();
				
				continue;
				
			}
			Iterator<?> cells = row.cellIterator();
			
			
			while (cells.hasNext()) {
				cell=(XSSFCell) cells.next();
				
				// skip unused cells
				if(cell.getColumnIndex() >= 7) continue;
				
				switch(cell.getColumnIndex()){
					//customer id
					case 0: 
						this.customerID = this.checkForCellType(cell);
						break;
					
					//country Code
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
					
					//customer name short
					case 4:
						this.custonerShortName = this.checkForCellType(cell);
						break;
					
					// empty cell
					case 5: continue;
					
					//customer full name
					case 6:
						this.customerFullName = this.checkForCellType(cell);
						break;
				}
			}
			
			/*
			 * Generate Customer Object
			 */
			
			Customer customer = new Customer();
			customer.setId(this.customerID);
			customer.setCountryCode(this.customerCountryCode);
			customer.setCountryName(this.country);
			customer.setFullName(this.customerFullName);
			customer.setShortName(this.custonerShortName);
			customer.setZipCode(this.zipCode);
			
			// Website model content is null
			customer.setWebsite(new Website());
			
			System.out.println(customer.toString());
			
			// add customer to array
			this.customerList.add(customer);
		}
		
		return this.customerList;
	}
	
	/*
	 * Check if the cell is numeric or a string type
	 */
	private String checkForCellType(XSSFCell cell) {
		if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
			return cell.getStringCellValue().toString();
		}
		
		else if(cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC) {
			return Double.toString(cell.getNumericCellValue());
		}
		return null;
	}


	private void readXLSFile() {
		// TODO Auto-generated method stub
	}
	
}
