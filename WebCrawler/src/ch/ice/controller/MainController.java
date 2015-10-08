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
import org.apache.commons.lang.time.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.JSONArray;
import org.json.JSONObject;

import ch.ice.controller.file.ExcelParser;
import ch.ice.controller.file.ExcelWriter;
import ch.ice.controller.threads.SearchThread;
import ch.ice.controller.web.BingSearchEngine;
import ch.ice.controller.web.ResultAnalyzer;
import ch.ice.exceptions.IllegalFileExtensionException;
import ch.ice.exceptions.InternalFormatException;
import ch.ice.exceptions.MissingCustomerRowsException;
import ch.ice.model.Customer;
import ch.ice.view.SaveWindowController;

/**
 * @author Oliver
 *
 */
public class MainController {
	public static final Logger logger = LogManager
			.getLogger(MainController.class.getName());
	ExcelParser excelParserInstance;

	public static File file;
	public static List<Customer> customerList;
	public List<Customer> threadedCustomerList;
	public static int i;
	public static String progressText;
	private static org.apache.commons.lang.time.StopWatch stopwatch;
	public static boolean isSearchAvail = false;
	public static URL defaultUrl = null;
	public static List<String> metaTagElements;
	public static List<Customer> firstArray;
	public static List<Customer> secondArray;
	public static List<Customer> thirdArray;
	public static List<Customer> fourthArray;

	private Integer limitSearchResults = 4;

	public void startMainController() throws InternalFormatException,
			MissingCustomerRowsException, InterruptedException {

		// Core settings
		// public static boolean isSearchAvail = false;
		// URL defaultUrl = null;

		PropertiesConfiguration config;
		metaTagElements = new ArrayList<String>();

		stopwatch = new StopWatch();
		stopwatch.start();

		// For testing if used without GUI
		if (file == null) {
			customerList = retrieveCustomerFromFile(new File("posTest.xlsx"));
		} else {
			customerList = retrieveCustomerFromFile(file);

			// retrieve all customers from file
			logger.info("Retrieve Customers from File "
					+ file.getAbsolutePath());
		}

		stopwatch.split();
		logger.info("Spilt: " + stopwatch.toSplitString() + " total: "
				+ stopwatch.toString());

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

		int listSize = customerList.size();
		int quarterSize = listSize / 4;
		int firstEnd = quarterSize - 1;

		int secondEnd = quarterSize * 2 - 1;
		int thirdStart = 2 * quarterSize;
		int thirdEnd = quarterSize * 3 - 1;
		int fourthStart = quarterSize * 3;
		int fourthEnd = listSize - 1;

		// System.out.println("Size: " + firstArray.size());

		if (listSize < 16) {
			System.out.println("Below 16");
			firstArray = customerList;
			SearchThread s1 = new SearchThread();
			s1.setCheckNumber(1);
			Thread t1 = new Thread(s1);
			t1.start();
			customerList.clear();
			customerList.addAll(firstArray);
		} else {
			firstArray = customerList.subList(0, firstEnd);
			secondArray = customerList.subList(quarterSize, secondEnd);
			thirdArray = customerList.subList(thirdStart, thirdEnd);
			fourthArray = customerList.subList(fourthStart, fourthEnd);

			SearchThread s1 = new SearchThread();
			s1.setCheckNumber(1);
			Thread t1 = new Thread(s1);
			t1.setName("FIRST THREAD");
			SearchThread s2 = new SearchThread();
			s2.setCheckNumber(2);
			Thread t2 = new Thread(s2);
			t2.setName("SECOND THREAD");
			SearchThread s3 = new SearchThread();
			s3.setCheckNumber(4);
			Thread t3 = new Thread(s3);
			t3.setName("THIRD THREAD");
			SearchThread s4 = new SearchThread();
			s4.setCheckNumber(4);
			Thread t4 = new Thread(s4);
			t4.setName("FOURTH THREAD");

			t1.start();
			t2.start();
			t3.start();
			t4.start();
			t1.join();
			t2.join();
			t3.join();
			t4.join();
			customerList.clear();
			customerList.addAll(s4.getSearchList());
			customerList.addAll(thirdArray);
			customerList.addAll(secondArray);
			customerList.addAll(firstArray);
		}
		// customerList.addAll(s2.getSearchList());
		// customerList.addAll(s3.getSearchList());
		// customerList.addAll(s4.getSearchList());

		// WebCrawler wc = new WebCrawler();
		//
		// for (Customer customer : customerList) {
		// i++;
		//
		// // only search via SearchEngine if search is enabled. Disable search
		// // for testing purpose
		// if (isSearchAvail) {
		// // Add url for customer
		// try {
		// URL retrivedUrl = searchForUrl(customer);
		// customer.getWebsite().setUrl(retrivedUrl);
		//
		// progressText = "Gathering data at: "
		// + retrivedUrl.toString();
		// } catch (Exception e) {
		// e.printStackTrace();
		// logger.error(e.getMessage());
		// }
		//
		// } else {
		// customer.getWebsite().setUrl(defaultUrl);
		// }
		//
		// // add metadata
		// try {
		// wc.connnect(customer.getWebsite().getUrl().toString());
		// customer.getWebsite().setMetaTags(
		// wc.getMetaTags(metaTagElements));
		// logger.info(customer.getWebsite().toString());
		// } catch (IOException e) {
		// e.printStackTrace();
		// logger.error(e.getMessage());
		//
		// } catch (HttpStatusException e) {
		// e.printStackTrace();
		// logger.error(e.getMessage());
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// logger.error(e.getMessage());
		//
		// }
		//
		// }

		stopwatch.split();
		logger.info("Spilt: " + stopwatch.toSplitString() + " total: "
				+ stopwatch.toString());

		/*
		 * Write every enhanced customer object into a new file
		 */
		this.startWriter(customerList);

		stopwatch.stop();
		logger.info("Spilt: " + stopwatch.toSplitString() + " total: "
				+ stopwatch.toString());

		logger.info("end");
		SaveWindowController.myBoo = true;
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
			JSONArray results = BingSearchEngine.search(query,
					this.limitSearchResults);

			// logger.debug(results.toString());

			// logic to pick the first record ; here should be the search logic!
			JSONObject aResult = ResultAnalyzer.analyze(results, params);

			c.getWebsite().setUnsure((boolean) aResult.get("Unsure"));

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
	 * @return List of Customers from file. Each row in a file represents a
	 *         customer
	 * @throws InternalFormatException
	 * @throws MissingCustomerRowsException
	 */
	public List<Customer> retrieveCustomerFromFile(File file)
			throws InternalFormatException, MissingCustomerRowsException {
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
