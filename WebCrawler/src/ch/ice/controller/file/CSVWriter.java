package ch.ice.controller.file;

import java.util.List;

import ch.ice.controller.interf.Parser;
import ch.ice.controller.interf.Writer;
import ch.ice.model.Customer;

public class CSVWriter implements Writer {

	@Override
	public void writeFile(List<Customer> customerList, Parser fileParserInstance) {
		// convert Parser to actual excelParser. We need getWorkbook() here.
		CSVParser CSVParser = (CSVParser) fileParserInstance;
		

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
