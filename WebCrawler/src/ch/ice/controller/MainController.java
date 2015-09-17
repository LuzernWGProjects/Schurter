/**
 * 
 */
package ch.ice.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

import ch.ice.controller.parser.ExcelParser;
import ch.ice.model.Customer;
import ch.ice.controller.interf.Parser;

/**
 * @author Oliver
 *
 */
public class MainController {

	
	public static void startMainController()
	{
		
		
		LinkedList<Customer> customerList = startExcelParser(new File("posTest.xlsx"));
		
		for (Customer customer : customerList) {
			// Add url for customer
			URL retrivedUrl = searchForUrl(customer);
			customer.getCustomersWebsite().setWebsiteUrl(retrivedUrl);
			
			// add metadata
			
		}
	}
	
	public static URL searchForUrl(Customer  customer){
		System.out.println("start test bing");
		
		try {
			return BingSearchEngine.Search(customer);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return customer;
	}
	
	
	// Test purpose
	public static LinkedList<Customer> startExcelParser(File file){
		Parser excelParser = new ExcelParser();
		
		try {
			// retrive all Customers from list
			return excelParser.readFile(file);
			
		} catch (IOException e) {
			System.out.println("File not found");
			e.printStackTrace();
		}
		return null;
	}
	
	public static void startWriter()
	{
		
	}
	
	public static void startCrawler()
	{
		
	}
}
