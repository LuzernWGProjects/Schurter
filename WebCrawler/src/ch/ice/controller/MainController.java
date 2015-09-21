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
	
	ExcelParser excelParserInstance;
	
	public void startMainController() {

		Configuration config;
		List<String> metaTagElements = new ArrayList<String>();
		
		// retrieve all customers from file
		LinkedList<Customer> customerList = retrieveCustomerFromFile(new File("posTest.xlsx"));
		
		// Core settings
		boolean isSearchAvail = false;
		URL defaultUrl = null;
		
		/*
		 * Load Configuration File
		 */
		try {
			config = new PropertiesConfiguration("app.properties");
			
			isSearchAvail = config.getBoolean("core.search.isEnabled");
			defaultUrl = new URL(config.getString("core.search.defaultUrl"));
			
			metaTagElements = Arrays.asList(config.getStringArray("crawler.searchForMetaTags"));
		} catch (ConfigurationException | MalformedURLException e) {
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
		
		
		WebCrawler wc = new WebCrawler();
		
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
				e.printStackTrace();
			}

			System.out.println(customer.getWebsite().toString());
		}
		
		/*
		 * Write every enhanced customer object into a new file
		 */
		this.startWriter(customerList);
	}

	public URL searchForUrl(Customer c) {
		System.out.println("start test bing");
		
		// Define Query
		String query = c.getFullName() + " " + c.getCountryName() + " "	+ c.getZipCode();

		try {

			// Start Search
			JSONArray results = BingSearchEngine.Search(query);

			//TODO write logichandler class
			// logic to pick the first record ; here should be the search logic!
			JSONObject aResult = results.getJSONObject(0);

			// return only the URL form first object
			return new URL((String) aResult.get("Url"));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Each Row returns a customer object. These customers are saved in an List-Object.
	 * 
	 * @param file
	 * @return LinkedList<Customer>
	 */
	public LinkedList<Customer> retrieveCustomerFromFile(File file) {
		this.excelParserInstance = new ExcelParser();

		try {
			// retrieve all Customers from list
			return this.excelParserInstance.readFile(file);

		} catch (IOException | IllegalFileExtensionException | EncryptedDocumentException | InvalidFormatException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		return new LinkedList<Customer>();
	}

	public void startWriter(List<Customer> customerList) {
		
		//TODO Check if user demands CSV or EXCEL -> if(excel)->getWorkbook, Else ->write normal
		//ExcelWriter ew = new ExcelWriter(this.excelParserInstance.getWorkbook());
		
		System.out.println("Start writing...");
		
		ExcelWriter ew = new ExcelWriter();
		
		ew.writeFile(customerList, this.excelParserInstance.getWorkbook());
	}
}
