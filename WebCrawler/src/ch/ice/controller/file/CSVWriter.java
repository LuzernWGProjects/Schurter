package ch.ice.controller.file;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import ch.ice.controller.interf.Parser;
import ch.ice.controller.interf.Writer;
import ch.ice.model.Customer;
import ch.ice.utils.FileOutputNameGenerator;

import java.util.Map.Entry;

public class CSVWriter implements Writer {
	private static final Logger logger = LogManager.getLogger(CSVWriter.class.getName());

	//Delimiter used in CSV file
	private static final String NEW_LINE_SEPARATOR = "\n";
	private List<Serializable> customerObjectArray;
	private static final  char semi = ';';
	private List<Customer> customerList;
	private ArrayList<String> cellHeaders;
	private int headCounter = 0;


	@Override
	public void writeFile(List<Customer> customerList, Parser fileParserInstance) throws IOException {
		// convert Parser to actual excelParser. We need getWorkbook() here.
		//Prepare Data
		this.customerList = customerList;

		Parser CSVParser = fileParserInstance;
		Workbook wb = CSVParser.getWorkbook();
		Sheet sheet = wb.getSheetAt(0);
		cellHeaders = (ArrayList<String>) fileParserInstance.getCellHeaders();
		//add Metadata Tags to cellHeaders
		cellHeaders.add(2, "Country");
		//	cellHeaders.add(5,"");
		cellHeaders.add(7, "Customer Ident. Lvl");
		cellHeaders.add(8, "URL");
		cellHeaders.add(9, "Industry");

		createHeader();

		//Defining Writers
		CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR).withDelimiter(semi);

		//  CSVFormat csvFileFormat = CSVFormat.withDelimiter(semi);
		FileWriter fileWriter = new FileWriter(FileOutputNameGenerator.createName());
		CSVPrinter printer = new CSVPrinter(fileWriter, csvFileFormat);

		//Print Prefix and Headers
		printer.printRecord(sheet.getRow(0).getCell(0));
		printer.printRecord(sheet.getRow(1).getCell(0));
		printer.printRecord(cellHeaders);

		for (Customer c : customerList) {

			customerObjectArray = new ArrayList<Serializable>(); 
			customerObjectArray.add(c.getId());
			customerObjectArray.add(c.getCountryCode());
			customerObjectArray.add(c.getCountryName());
			customerObjectArray.add(c.getZipCode());
			customerObjectArray.add(c.getShortName());
			customerObjectArray.add("");
			customerObjectArray.add(c.getFullName());
			customerObjectArray.add("Country+PC+Name");
			customerObjectArray.add(c.getWebsite().getUrl());
			customerObjectArray.add(c.getIndustry());

			try {
				Set<Entry<String, String>> entries =  c.getWebsite().getMetaTags().entrySet();

				for(Entry<String,String> ent: entries) {
					customerObjectArray.add(ent.getValue());
				}
				
			} catch (Exception e) {
				logger.error(e.getMessage());
			}

			printer.printRecord(customerObjectArray);
		}
		
		fileWriter.flush();
		fileWriter.close();
		printer.close();

	}

	private void createHeader() {
		try {

			// go thru the first customer to get the cell headers for the metatags
			Set<Entry<String, String>> headentries = customerList.get(headCounter).getWebsite().getMetaTags().entrySet();
			for(Entry<String,String> ent: headentries){
				cellHeaders.add(ent.getKey());
			}
			
		} catch (Exception e) {
			//if by any chance there is an error at the first customer and the metatags cannot be read, it will make an recursive call and try to get the metatags key from the next one.
			headCounter++;
			createHeader();
		}
	}
}
