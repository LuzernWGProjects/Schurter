/**
 * 
 */
package ch.ice.controller;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
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

		Configuration config;
		List<String> metaTagElements = new ArrayList<String>();
		
		LinkedList<Customer> customerList = startExcelParser(new File(
				"posTest.xlsx"));
		
		// Core settings
		boolean isSearchAvail = false;
		URL defaultUrl = null;
		
		WebCrawler wc = new WebCrawler();
		
		
		/*
		 * Load Configuration File
		 */
		try {
			config = new PropertiesConfiguration("app.properties");
			
			isSearchAvail = config.getBoolean("core.search.isEnabled");
			defaultUrl = new URL(config.getString("core.search.defaultUrl"));
			
			metaTagElements = Arrays.asList(config.getStringArray("crawler.searchForMetaTags"));
		} catch (ConfigurationException | MalformedURLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
		
		for (Customer customer : customerList) {
			
			// only search via SearchEngine if search is enabled. Disable search for testing purpose
			if(isSearchAvail){
				// Add url for customer
				URL retrivedUrl = searchForUrl(customer);
				customer.getWebsite().setUrl(retrivedUrl);
				
			} else {
				customer.getWebsite().setUrl(defaultUrl);
			}
			
			
			
			// add metadata
			try {
				wc.connnect(customer.getWebsite().getUrl().toString());
				customer.getWebsite().setMetaTags(wc.getMetaTags(metaTagElements));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.println(customer.getWebsite().toString());
			
			
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

		} catch (IOException | IllegalFileExtensionException | EncryptedDocumentException | InvalidFormatException e) {
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
