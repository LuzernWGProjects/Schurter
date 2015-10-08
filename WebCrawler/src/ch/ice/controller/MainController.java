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
import ch.ice.controller.web.ResultAnalyzer;
import ch.ice.controller.web.SearchEngineFactory;
import ch.ice.controller.web.WebCrawler;
import ch.ice.exceptions.FileParserNotAvailableException;
import ch.ice.exceptions.HttpStatusException;
import ch.ice.exceptions.IllegalFileExtensionException;
import ch.ice.exceptions.InternalFormatException;
import ch.ice.exceptions.MissingCustomerRowsException;
import ch.ice.exceptions.SearchEngineNotAvailableException;
import ch.ice.model.Customer;
import ch.ice.utils.JSONStandardizedKeys;

public class MainController {
	private static final Logger logger = LogManager.getLogger(MainController.class.getName());

	public static File uploadedFileContainingCustomers;
	public static List<Customer> customerList;
	public static int customersEnhanced;
	public static String progressText;
	private static StopWatch stopwatch;

	// search engine
	private static String searchEngineIdentifier = SearchEngineFactory.BING;
	private static SearchEngine searchEngine;
	private Integer limitSearchResults = 10;

	// file Parser
	private static Parser fileParser;

	/**
	 * This is the main controller. From here the whole program gets controlled.
	 * I/O and lookups are also triggered from here.
	 * 
	 * @throws InternalFormatException
	 * @throws MissingCustomerRowsException
	 */
	public void startMainController() throws InternalFormatException, MissingCustomerRowsException {
		// Core settings
		boolean isSearchAvail = false;
		URL defaultUrl = null;
		
		PropertiesConfiguration config;
		List<String> metaTagElements = new ArrayList<String>();

		/*
		 * Load Configuration File
		 */
		try {
			config = new PropertiesConfiguration("conf/app.properties");

			isSearchAvail = config.getBoolean("core.search.isEnabled");
			defaultUrl = new URL(config.getString("core.search.defaultUrl"));
			// this.limitSearchResults = config.getInteger("searchEngine.bing.limitSearchResults", 15);

			metaTagElements = Arrays.asList(config.getStringArray("crawler.searchForMetaTags"));
		} catch (ConfigurationException | MalformedURLException e) {
			logger.error("Faild to load config file");
		}


		// request new SearchEngine
		try {
			MainController.searchEngine = SearchEngineFactory.requestSearchEngine(MainController.searchEngineIdentifier);
			logger.info("Starting " + searchEngine.getClass().getName());
		} catch (SearchEngineNotAvailableException e1) {
			logger.error(e1.getMessage());
		}

		stopwatch = new StopWatch();
		stopwatch.start();

		// For testing if used without GUI
		if (uploadedFileContainingCustomers == null) {
			MainController.customerList = retrieveCustomerFromFile(new File("posTest.xlsx"));
		} else {
			MainController.customerList = retrieveCustomerFromFile(uploadedFileContainingCustomers);

			// retrieve all customers from file
			logger.info("Retrieve Customers from File "	+ uploadedFileContainingCustomers.getAbsolutePath());
		}

		stopwatch.split();
		logger.info("Spilt: " + stopwatch.toSplitString() + " total: "+ stopwatch.toString());
		
		
		/*
		 *	Start the webcrawler service to gather all meta tags and 
		 *	additional information from a customers website. 
		 */
		WebCrawler wc = new WebCrawler();

		for (Customer customer : MainController.customerList) {
			customersEnhanced++;

			// only search via SearchEngine if search is enabled. Disable search for testing purpose
			if (isSearchAvail) {
				// Add url for customer
				try {
					URL retrivedUrl = searchForUrl(customer);
					customer.getWebsite().setUrl(retrivedUrl);
					progressText = "Gathering data at: "+ retrivedUrl.toString();
				} catch (Exception e) {
					e.printStackTrace();
					logger.error(e.getMessage());
				}

			} else {
				customer.getWebsite().setUrl(defaultUrl);
			}

			// add metadata
			try {
				wc.connnect(customer.getWebsite().getUrl().toString());
				customer.getWebsite().setMetaTags(wc.getMetaTags(metaTagElements));
				logger.info(customer.getWebsite().toString());
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.getMessage());

			} catch (HttpStatusException e) {
				e.printStackTrace();
				logger.error(e.getMessage());

			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());

			}
		}

		stopwatch.split();
		logger.info("Spilt: " + stopwatch.toSplitString() + " total: "+ stopwatch.toString());

		/*
		 * Write every enhanced customer object into a new file
		 */
		this.startWriter(MainController.customerList);

		stopwatch.stop();
		logger.info("Spilt: " + stopwatch.toSplitString() + " total: "+ stopwatch.toString());

		logger.info("end");
	}

	/**
	 * Each Row returns a customer object. These customers are saved in a
	 * List-Object.
	 * 
	 * @param file
	 * @return List of Customers from file. Each row in a file represents a
	 *         customer
	 * @throws InternalFormatException, MissingCustomerRowsException
	 */
	public List<Customer> retrieveCustomerFromFile(File file) throws InternalFormatException, MissingCustomerRowsException {
		String uploadedFileExtension = FilenameUtils.getExtension(file.getName());
		String fileParserIdentifier = "";
		
		switch(uploadedFileExtension) {
		case "xlsx":
		case "xls":
			fileParserIdentifier = FileParserFactory.EXCEL;
			break;
		case "csv":
			fileParserIdentifier = FileParserFactory.CSV;
			break;
		}

		try {
			MainController.fileParser = FileParserFactory.requestParser(fileParserIdentifier);
			return MainController.fileParser.readFile(file);

		} catch (FileParserNotAvailableException | EncryptedDocumentException | InvalidFormatException | IOException | IllegalFileExtensionException e) {
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

		// more parameters can be added. These parameters are similar to Googles site: input. E.g. Automation site:schurter.com
		List<String> params = new ArrayList<String>();
		params.add(c.getFullName().toLowerCase());

		String lookupQuery = MainController.searchEngine.buildQuery(params);
		progressText = "Lookup on: " + lookupQuery;

		logger.info("Lookup "+MainController.searchEngine.getClass().getName()+"  with Query \""+ lookupQuery +"\"");

		try {
			// Start Search
			JSONArray results = MainController.searchEngine.search(lookupQuery, this.limitSearchResults);

			// logic to pick the first record ; here should be the search logic!
			JSONObject aResult = ResultAnalyzer.analyse(results, params);

			// return only the URL form first object
			return new URL((String) aResult.get(JSONStandardizedKeys.URL));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Write enhanced customer array into a file and save it to the selected directory
	 * 
	 * @param customerList
	 */
	public void startWriter(List<Customer> enhancedCustomerList) {
		logger.info("Start writing customers to File");
		
		// TODO: implement CVS Writer if user chooses to do so. Basically just if excel->excelWriter; csv->csvWriter
		Writer fileWriter = FileWriterFactory.requestFileWriter(FileWriterFactory.EXCEL);
		
		fileWriter.writeFile(enhancedCustomerList, MainController.fileParser);
	}
}
