package ch.ice.controller.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import ch.ice.controller.interf.Parser;

/*
 *  NOTE: Write config files with allowed file extensions.
 *  	-> XLS OR XLSX
 * 
 */

public class ExcelParser implements Parser {
	private File file;
	private InputStream ExcelFileToRead;

	@Override
	public void readFile(File file) throws IOException {
		
		// set file to private access
		this.file = file;
		
		
		// check if it is an XLS file or a XLSX file
		String fileExtension = FilenameUtils.getExtension(file.getName()).toLowerCase();
		
		switch(fileExtension) {
			case "xlsx":
				readXLSXFile();
				break;
				
			case "xls":
				readXLSFile();
				break;
			
			default:
					System.out.println("Diese Dateeinung ist nicht erlaubt");
					break;
		}
	}

	
	private void readXLSXFile() throws IOException {
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
				String customerIDHeader = 			row.getCell(0).toString();
				String countryNameHeader =			row.getCell(1).toString();
				String zipCodeHeader = 				row.getCell(3).toString();
				String customerNameShortHeader = 	row.getCell(4).toString();
				String customerNameHeader = 		row.getCell(6).toString();
				
				System.out.println(customerIDHeader);
				System.out.println(countryNameHeader);
				System.out.println(zipCodeHeader);
				System.out.println(customerNameShortHeader);
				System.out.println(customerNameHeader);
				System.out.println();
				continue;
				
			}
			
			Iterator<?> cells = row.cellIterator();
			
			
			while (cells.hasNext()) {
				cell=(XSSFCell) cells.next();
				
				// skip unused cells
				if(cell.getColumnIndex() >= 7) continue;
				
				switch(cell.getColumnIndex()){
					case 0: //customer id
						String customerID = this.checkForCellType(cell);
						System.out.println(customerID);
						break;
					case 1: continue; //skip this (laendercode)
					case 2: //land
						String country = this.checkForCellType(cell);
						System.out.println(country);
						break;
					case 3: //zip
						String zip = this.checkForCellType(cell);
						System.out.println(zip);
						break;
					case 4: continue; //customer name short
					case 5: continue; //skip
					case 6: //customer name
						String customerFullName = this.checkForCellType(cell);
						System.out.println(customerFullName);
						break;
				}
			}
			System.out.println();
		}
		
	}
	
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
