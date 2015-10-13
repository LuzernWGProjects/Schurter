/**
 * 
 */
package ch.ice.junit.web;

import static org.junit.Assert.*;

import org.json.JSONObject;
import org.junit.Test;

import ch.ice.controller.interf.SearchEngine;
import ch.ice.controller.web.BingSearchEngine;
import ch.ice.controller.web.GoogleSearchEngine;
import ch.ice.controller.web.SearchEngineFactory;
import ch.ice.exceptions.SearchEngineNotAvailableException;

/**
 * @author Oliver
 *
 */
public class BingWebTest {

	SearchEngine searechengine = new BingSearchEngine();
	private static String searchEngineIdentifier = SearchEngineFactory.BING;
	SearchEngine searchEngine;
	
	@Test
	public void startWebTestBing() {

		try {
			searchEngine = SearchEngineFactory.requestSearchEngine(searchEngineIdentifier);
		} catch (SearchEngineNotAvailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int numOfResults = 5;
		ResultAnalyzerTest bing = new ResultAnalyzerTest();
		JSONObject aResult = bing.testAnalyze(searechengine, numOfResults);
	
		String url = (String) aResult.get("url");
		assertEquals(url, "http://www.linkedin.com/");
	//	assertEquals(false,aResult.get("Unsure"));
		assertEquals(numOfResults, bing.getResults().length());
	}

}
