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

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.JSONArray;
import org.json.JSONObject;

import ch.ice.controller.file.ExcelParser;
import ch.ice.controller.file.ExcelWriter;
import ch.ice.controller.web.BingSearchEngine;
import ch.ice.controller.web.WebCrawler;
import ch.ice.exceptions.IllegalFileExtensionException;
import ch.ice.model.Customer;
import ch.ice.utils.JSONUtil;

/**
 * @author Oliver
 *
 */
public class MainController {
	private static final Logger logger = LogManager
			.getLogger(MainController.class.getName());
	ExcelParser excelParserInstance;

	public static File file;
	public static LinkedList<Customer> customerList;
	public static int i;
	public static String progressText;

	private Integer limitSearchResults = 4;

	public void startMainController() {

		PropertiesConfiguration config;
		List<String> metaTagElements = new ArrayList<String>();

		// For testing if used without GUI
		if (file == null) {
			customerList = retrieveCustomerFromFile(new File("posTest.xlsx"));
		} else {
			customerList = retrieveCustomerFromFile(file);

			// retrieve all customers from file
			logger.info("Retrieve Customers from File "
					+ file.getAbsolutePath());
		}

		// Core settings
		boolean isSearchAvail = false;
		URL defaultUrl = null;

		/*
		 * Load Configuration File
		 */
		try {
			config = new PropertiesConfiguration("conf/app.properties");

			isSearchAvail = config.getBoolean("core.search.isEnabled");
			defaultUrl = new URL(config.getString("core.search.defaultUrl"));
			// this.limitSearchResults =
			// config.getInteger("searchEngine.bing.limitSearchResults", 15);

			metaTagElements = Arrays.asList(config
					.getStringArray("crawler.searchForMetaTags"));
		} catch (ConfigurationException | MalformedURLException e) {
			logger.error("Faild to load config file");
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		}

		WebCrawler wc = new WebCrawler();

		for (Customer customer : customerList) {
			i++;

			// only search via SearchEngine if search is enabled. Disable search
			// for testing purpose
			if (isSearchAvail) {
				// Add url for customer
				URL retrivedUrl = searchForUrl(customer);
				customer.getWebsite().setUrl(retrivedUrl);
				progressText = "Gathering data at: " + retrivedUrl.toString();

			} else {
				customer.getWebsite().setUrl(defaultUrl);
			}

			// add metadata
			try {
				wc.connnect(customer.getWebsite().getUrl().toString());
				customer.getWebsite().setMetaTags(
						wc.getMetaTags(metaTagElements));
			} catch (IOException e) {
				e.printStackTrace();
			}

			logger.info(customer.getWebsite().toString());
		}

		/*
		 * Write every enhanced customer object into a new file
		 */
		this.startWriter(customerList);

		logger.info("end");
	}

	public URL searchForUrl(Customer c) {

		ArrayList<String> params = new ArrayList<String>();
		params.add(c.getFullName().toLowerCase());
		// params.add(c.getCountryName().toLowerCase());
		// params.add("loc:"+c.getCountryCode().toLowerCase()); -> delivers 0
		// results sometimes. we have to TEST this!!!!

		String query = BingSearchEngine.buildQuery(params);
		progressText = "Loockup on: " + query;

		logger.info("start searchEngine for URL with query: " + query);

		try {

			// Start Search
			JSONArray results = BingSearchEngine.Search(query,
					this.limitSearchResults);

			// logger.debug(results.toString());

			// logic to pick the first record ; here should be the search logic!
			results = JSONUtil.cleanUp(results);
			System.out.println(results);
			JSONObject aResult = results.getJSONObject(0);

			// return only the URL form first object
			return new URL((String) aResult.get("Url"));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Each Row returns a customer object. These customers are saved in an
	 * List-Object.
	 * 
	 * @param file
	 * @return LinkedList<Customer>
	 */
	public LinkedList<Customer> retrieveCustomerFromFile(File file) {
		this.excelParserInstance = new ExcelParser();

		try {

			return this.excelParserInstance.readFile(file);

		} catch (IOException | IllegalFileExtensionException
				| EncryptedDocumentException | InvalidFormatException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		return new LinkedList<Customer>();
	}

	public void startWriter(List<Customer> customerList) {

		// TODO Check if user demands CSV or EXCEL -> if(excel)->getWorkbook,
		// Else ->write normal
		// ExcelWriter ew = new
		// ExcelWriter(this.excelParserInstance.getWorkbook());

		logger.info("Start writing customers to File");

		ExcelWriter ew = new ExcelWriter();

		ew.writeFile(customerList, this.excelParserInstance.getWorkbook());
	}
}
