package ch.ice.controller.file;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import ch.ice.controller.interf.Parser;
import ch.ice.controller.interf.Writer;
import ch.ice.model.Customer;
import ch.ice.utils.FileOutputNameGenerator;

public class CSVWriter implements Writer {

	 //Delimiter used in CSV file
	private static final String NEW_LINE_SEPARATOR = "\n";
	private static final Object [] FILE_HEADER = {"id","firstName","lastName","gender","age"};
	
	@Override
	public void writeFile(List<Customer> customerList, Parser fileParserInstance) throws IOException {
		// convert Parser to actual excelParser. We need getWorkbook() here.
		Parser CSVParser = fileParserInstance;
		
		List<String> cellHeaders = fileParserInstance.getCellHeaders();
		
		for (String head : cellHeaders) {
			System.out.println(head);
		}
		
        //Create the CSVFormat object
      //  CSVFormat csvFileFormat = CSVFormat.RFC4180.withHeader().withDelimiter(',');
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR);
        
        //CSV Write Example using CSVPrinter
        FileWriter fileWriter = new FileWriter(FileOutputNameGenerator.createName());
        CSVPrinter printer = new CSVPrinter(fileWriter, csvFileFormat);
      
        System.out.println("********");
        
//        printer.printRecord("Customer Name","URL","keywords","description");
        printer.printRecord(FILE_HEADER);
        
        for (Customer c : customerList) {
    	List customerObjectArray = new ArrayList();    			customerObjectArray.add(c.getFullName());
    	customerObjectArray.add(c.getWebsite().getUrl());
    	customerObjectArray.add( c.getWebsite().getMetaTags());
//    	customerObjectArray.add(c.getFullName());
//			c.getFullName(),	c.getWebsite().getUrl(), c.getWebsite().getMetaTags() };

    	printer.printRecords(customerObjectArray);
    	
        }
        printer.close();
//		for (Customer customer : customerList) {
//			System.out.println(customer.getFullName());
//			System.out.println(customer.getWebsite().getMetaTags().toString());
//		}
//		
//		for (String headerValue : CSVParser.getCellHeaders()) {
//			System.out.println("header values: "+headerValue);
//		}
		
	}

}
