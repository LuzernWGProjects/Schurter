package ch.ice.controller;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.JSONArray;
import org.json.JSONObject;

import ch.ice.controller.file.FileParserFactory;
import ch.ice.controller.file.FileWriterFactory;
import ch.ice.controller.interf.Parser;
import ch.ice.controller.interf.SearchEngine;
import ch.ice.controller.interf.Writer;
import ch.ice.controller.threads.SearchThread;
import ch.ice.controller.web.ResultAnalyzer;
import ch.ice.controller.web.SearchEngineFactory;
import ch.ice.exceptions.FileParserNotAvailableException;
import ch.ice.exceptions.IllegalFileExtensionException;
import ch.ice.exceptions.InternalFormatException;
import ch.ice.exceptions.MissingCustomerRowsException;
import ch.ice.model.Customer;
import ch.ice.utils.Config;
import ch.ice.utils.JSONStandardizedKeys;
import ch.ice.view.SaveWindowController;

public class MainController {
	public static final Logger logger = LogManager.getLogger(MainController.class.getName());

	public static File uploadedFileContainingCustomers;
	
	/**
	 * List containing all rendered Customers
	 */
	public static List<Customer> customerList;
	public static int customersEnhanced;
	public static String progressText;
	private static StopWatch stopwatch;

	/**
	 * Searchengine to be used.<br >
	 * See: {@see SearchEngineFactory}
	 */
	public static String searchEngineIdentifier;
	/**
	 * Requested Searchengine.
	 */
	private static SearchEngine searchEngine;
	/**
	 * Limit searchresult that will be returned from the SearchEngine
	 */
	private static Integer limitSearchResults;
	/**
	 * Fallback url if search is not available
	 */
	public static URL defaultUrl;
	public static boolean isSearchAvail;
	/**
	 * Searchengine to be used.<br >
	 * See: {@see SearchEngineFactory} - Available SearchEngines: Google or Bing
	 */
	public static boolean fileWriterFactory;
	public static boolean processEnded = false;

	/**
	 * All available Metatags
	 */
	public static List<String> metaTagElements;
	public static List<Customer> firstArray;
	public static List<Customer> secondArray;
	public static List<Customer> thirdArray;
	public static List<Customer> fourthArray;

	// file Parser
	private static Parser fileParser;
	public static Writer fileWriter = null;

	/**
	 * This is the main controller. From here the whole program gets controlled.
	 * I/O and lookups are also triggered from here.
	 * 
	 * @throws InternalFormatException
	 * @throws MissingCustomerRowsException
	 * @throws InterruptedException
	 */
	public void startMainController() throws InternalFormatException,
			MissingCustomerRowsException, InterruptedException {
		// Core settings
		isSearchAvail = false;
		defaultUrl = null;

		PropertiesConfiguration config = Config.PROPERTIES;
		metaTagElements = new ArrayList<String>();

		/*
		 * Load Configuration File
		 */
		try {
			
			isSearchAvail = config.getBoolean("core.search.isEnabled");
			defaultUrl = new URL(config.getString("core.search.defaultUrl"));
			MainController.limitSearchResults = config.getInteger(
					"searchEngine.limitSearchResult", 5);

			metaTagElements = Arrays.asList(config
					.getStringArray("crawler.searchForMetaTags"));
		} catch (MalformedURLException e) {
			logger.error("Faild to load config file");
		}

		// for test without gui
		if (searchEngineIdentifier == null) {
			searchEngineIdentifier = "BING";
		}
		// request new SearchEngine
		MainController.searchEngine = SearchEngineFactory
				.requestSearchEngine(MainController.searchEngineIdentifier);
		logger.info("Starting " + searchEngine.getClass().getName());

		stopwatch = new StopWatch();
		stopwatch.start();

		// For testing if used without GUI
		if (uploadedFileContainingCustomers == null) {
			MainController.customerList = retrieveCustomerFromFile(new File(
					"posTest.xlsx"));
		} else {
			MainController.customerList = retrieveCustomerFromFile(uploadedFileContainingCustomers);

			// retrieve all customers from file
			logger.info("Retrieve Customers from File "
					+ uploadedFileContainingCustomers.getAbsolutePath());
		}

		stopwatch.split();
		logger.info("Spilt: " + stopwatch.toSplitString() + " total: "
				+ stopwatch.toString());

		int listSize = customerList.size();
		int quarterSize = listSize / 4;
		int firstEnd = quarterSize;
		int secondStart = quarterSize;
		int secondEnd = (quarterSize) * 2;
		int thirdStart = 2 * quarterSize;
		int thirdEnd = quarterSize * 3;
		int fourthStart = quarterSize * 3;
		int fourthEnd = listSize;

		System.out.println(0 + ", " + firstEnd + ", " + secondStart + ", "
				+ secondEnd + ", " + thirdStart + ", " + thirdEnd + ", "
				+ fourthStart + ", " + fourthEnd);

		if (listSize < 16) {
			System.out.println("Below 16");
			firstArray = new ArrayList<Customer>(customerList);
			SearchThread s1 = new SearchThread();
			s1.setCheckNumber(1);
			s1.setSearchList(firstArray);
			Thread t1 = new Thread(s1);
			t1.setName("FIRST THREAD");
			t1.start();
			t1.join();
			customerList.clear();
			customerList.addAll(s1.getSearchList());
		} else {
			firstArray = new ArrayList<Customer>(customerList.subList(0,
					firstEnd));
			secondArray = new ArrayList<Customer>(customerList.subList(
					secondStart, secondEnd));
			thirdArray = new ArrayList<Customer>(customerList.subList(
					thirdStart, thirdEnd));
			fourthArray = new ArrayList<Customer>(customerList.subList(
					fourthStart, fourthEnd));

			SearchThread s1 = new SearchThread();
			s1.setCheckNumber(1);
			s1.setSearchList(firstArray);
			Thread t1 = new Thread(s1);
			t1.setName("FIRST THREAD");
			System.out.println("First Thread Size: "
					+ s1.getSearchList().size());

			SearchThread s2 = new SearchThread();
			s2.setCheckNumber(2);
			s2.setSearchList(secondArray);
			Thread t2 = new Thread(s2);
			t2.setName("SECOND THREAD");
			System.out.println("Second Thread Size: "
					+ s2.getSearchList().size());

			SearchThread s3 = new SearchThread();
			s3.setCheckNumber(4);
			s3.setSearchList(thirdArray);
			Thread t3 = new Thread(s3);
			t3.setName("THIRD THREAD");
			System.out.println("Third Thread Size: "
					+ s3.getSearchList().size());

			SearchThread s4 = new SearchThread();
			s4.setCheckNumber(4);
			s4.setSearchList(fourthArray);
			Thread t4 = new Thread(s4);
			t4.setName("FOURTH THREAD");
			System.out.println("Fourth Thread Size: "
					+ s4.getSearchList().size());

			t1.start();
			t2.start();
			t3.start();
			t4.start();
			t1.join();
			t2.join();
			t3.join();
			t4.join();

			System.out.println("First Thread Size: "
					+ s1.getSearchList().size());
			System.out.println("Second Thread Size: "
					+ s2.getSearchList().size());
			System.out.println("Third Thread Size: "
					+ s3.getSearchList().size());
			System.out.println("Fourth Thread Size: "
					+ s4.getSearchList().size());

			customerList.clear();
			customerList.addAll(s1.getSearchList());
			customerList.addAll(s2.getSearchList());
			customerList.addAll(s3.getSearchList());
			customerList.addAll(s4.getSearchList());
		}

		stopwatch.split();
		logger.info("Spilt: " + stopwatch.toSplitString() + " total: "
				+ stopwatch.toString());

		/*
		 * Write every enhanced customer object into a new file
		 */
		SaveWindowController.myBooWriting = true;

		this.startWriter(MainController.customerList);

		stopwatch.stop();
		logger.info("Spilt: " + stopwatch.toSplitString() + " total: "
				+ stopwatch.toString());

		logger.info("end");
		SaveWindowController.myBoo = true;
		processEnded = true;
	}

	/**
	 * Each Row returns a customer object. These customers are saved in a
	 * List-Object.
	 * 
	 * @param file
	 * @return List of Customers from file. Each row in a file represents a
	 *         customer
	 * @throws InternalFormatException
	 *             , MissingCustomerRowsException
	 */
	public static List<Customer> retrieveCustomerFromFile(File file)
			throws InternalFormatException, MissingCustomerRowsException {
		String uploadedFileExtension = FilenameUtils.getExtension(file
				.getName());
		String fileParserIdentifier = "";

		switch (uploadedFileExtension) {
		case "xlsx":
		case "xls":
			fileParserIdentifier = FileParserFactory.EXCEL;
			break;
		case "csv":
			fileParserIdentifier = FileParserFactory.CSV;
			break;
		}

		try {
			MainController.fileParser = FileParserFactory
					.requestParser(fileParserIdentifier);
			return MainController.fileParser.readFile(file);

		} catch (FileParserNotAvailableException | EncryptedDocumentException
				| InvalidFormatException | IOException
				| IllegalFileExtensionException e) {
			logger.error(e.getMessage());
		}

		return new LinkedList<Customer>();

	}

	/**
	 * Search for a Customers URL based on his name and other parameters.
	 * 
	 * @param Customer
	 * @return URL of Customer - Depends on the quality of the search engine
	 */
	public URL searchForUrl(Customer c) {

		// more parameters can be added. These parameters are similar to Googles
		// site: input. E.g. Automation site:schurter.com
		List<String> params = new ArrayList<String>();
		params.add(c.getFullName().toLowerCase());

		String lookupQuery = MainController.searchEngine.buildQuery(params);
		progressText = "Lookup on: " + lookupQuery;

		logger.info("Lookup "
				+ MainController.searchEngine.getClass().getName()
				+ "  with Query \"" + lookupQuery + "\"");

		try {
			// Start Search
			JSONArray results = MainController.searchEngine.search(lookupQuery,
					MainController.limitSearchResults, c.getCountryCode().toLowerCase());

			// logic to pick the first record ; here should be the search logic!
			JSONObject aResult = ResultAnalyzer.analyse(results, params);
			c.getWebsite().setUnsure(aResult.getBoolean("Unsure"));

			// return only the URL form first object
			return new URL((String) aResult.get(JSONStandardizedKeys.URL));

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("FEHLER!!!!!!!");
		}

		return defaultUrl;
	}

	/**
	 * Write enhanced customer array into a file and save it to the selected
	 * directory
	 * 
	 * @param customerList
	 */
	public void startWriter(List<Customer> enhancedCustomerList) {
		logger.info("Start writing customers to File");

		try {
			if (fileWriterFactory == true) {
				fileWriter = FileWriterFactory
						.requestFileWriter(FileWriterFactory.EXCEL);
			}
			if (fileWriterFactory == false) {
				fileWriter = FileWriterFactory
						.requestFileWriter(FileWriterFactory.CSV);
			}

		} catch (FileParserNotAvailableException e) {
			e.printStackTrace();
		}

		try {

			fileWriter.writeFile(enhancedCustomerList,
					MainController.fileParser);
		} catch (IOException e) {
			// TODO Throw this to gui!!!!!!!!!!!!!!!!!!!!!!!!!
			e.printStackTrace();
		}
	}

	public void stopThread(String threadName) throws InterruptedException {
		ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
		Thread[] threads = new Thread[threadGroup.activeCount()];
		threadGroup.enumerate(threads);
		for (int nIndex = 0; nIndex < threads.length; nIndex++) {
			if (threads[nIndex] != null
					&& threads[nIndex].getName().equals(threadName)) {
				threads[nIndex].stop();
			}
		}
	}
}