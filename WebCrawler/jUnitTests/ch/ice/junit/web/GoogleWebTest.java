package ch.ice.junit.web;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import ch.ice.controller.MainController;
import ch.ice.controller.interf.SearchEngine;
import ch.ice.controller.web.GoogleSearchEngine;
import ch.ice.controller.web.SearchEngineFactory;
import ch.ice.exceptions.SearchEngineNotAvailableException;
import ch.ice.model.Customer;

public class GoogleWebTest {

	

	//SearchEngine searechengine = new GoogleSearchEngine();
	private static String searchEngineIdentifier = SearchEngineFactory.GOOGLE;
	SearchEngine searchEngine;
	
	@Test
	public void startWebTestGoogle()
	{
				searchEngine = SearchEngineFactory.requestSearchEngine(searchEngineIdentifier);
				
				int numOfResults = 5;
				ResultAnalyzerTest google = new ResultAnalyzerTest();
				JSONObject aResult = google.testAnalyze(searchEngine, numOfResults);
			
				String url = (String) aResult.get("url");
				assertEquals(url, "https://www.snowflake.ch/");
				assertEquals(false,aResult.get("Unsure"));
			//	assertEquals(numOfResults, google.getResults().length());
		
	}
	
	
}
