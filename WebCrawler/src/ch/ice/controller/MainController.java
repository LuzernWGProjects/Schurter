/**
 * 
 */
package ch.ice.controller;

import java.io.File;
import java.io.IOException;
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
		
		//startSearch();
		startExcelParser();
	}
	
	
	public static void startParser()
	{
		
	}
	
	
	public static Customer startSearch(Customer  customer){
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
	public static void startExcelParser(){
		Parser excelParser = new ExcelParser();
		
		try {
			// retrive all Customers from list
			LinkedList<Customer> customerList = excelParser.readFile(new File("posTest.xlsx"));
			
			for (Customer customer : customerList) {
				System.out.println(customer.toString());
			}
			
		} catch (IOException e) {
			System.out.println("File not found");
			e.printStackTrace();
		}
	}
	
	public static void startWriter()
	{
		
	}
	
	public static void startCrawler()
	{
		
	}
}
