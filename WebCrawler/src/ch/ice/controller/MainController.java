/**
 * 
 */
package ch.ice.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import ch.ice.controller.interf.Parser;
import ch.ice.controller.parser.ExcelParser;
import ch.ice.exceptions.IllegalFileExtensionException;
import ch.ice.model.Customer;

/**
 * @author Oliver
 *
 */
public class MainController {

	public static void startMainController() {

		LinkedList<Customer> customerList = startExcelParser(new File(
				"posTest.xlsx"));
		WebCrawler wc = new WebCrawler();
		//Testing purpose
		ArrayList<String> array = new ArrayList<String>();
		array.add("description");
		array.add("keyword");
		array.add("author");
		array.add("viewport");
		
		for (Customer customer : customerList) {
			// Add url for customer
			URL retrivedUrl = searchForUrl(customer);
			customer.getWebsite().setUrl(retrivedUrl);

			// add metadata
			
			try {
				wc.connnect(retrivedUrl.toString());
					
				customer.getWebsite().setMetaTags(wc.getMetaTags(array));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.println("Customer: "+ "url: "+customer.getWebsite().getUrl()+" ------ meta: "+ customer.getWebsite().getMetaTags());
			
			
		}
	}

	public static URL searchForUrl(Customer c) {
		System.out.println("start test bing");

		// decide whether to use bing or google
		// if (bing)
		// { BingSearchEngine(......
		// else{....

		// Define Query
		String query = c.getFullName() + " " + c.getCountryName() + " "
				+ c.getZipCode();

		try {

			// Start Search
			JSONArray results = BingSearchEngine.Search(query);

			// logic to pick the first record ; here should be the search logic!
			JSONObject aResult = results.getJSONObject(0);

			// return only the URL form first object
			return new URL((String) aResult.get("Url"));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// Test purpose
	public static LinkedList<Customer> startExcelParser(File file) {
		Parser excelParser = new ExcelParser();

		try {
			// retrive all Customers from list
			return excelParser.readFile(file);

		} catch (IOException | IllegalFileExtensionException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	public static void startWriter() {

	}

	public static void startCrawler() {

	}
}
