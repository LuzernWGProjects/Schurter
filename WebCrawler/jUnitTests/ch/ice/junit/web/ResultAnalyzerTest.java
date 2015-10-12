package ch.ice.junit.web;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import ch.ice.controller.interf.SearchEngine;
import ch.ice.controller.web.BingSearchEngine;
import ch.ice.controller.web.ResultAnalyzer;
import ch.ice.controller.web.SearchEngineFactory;
import ch.ice.exceptions.NoUrlFoundException;
import ch.ice.exceptions.SearchEngineNotAvailableException;
import ch.ice.model.Customer;

public class ResultAnalyzerTest {
	
	Customer c1;
	SearchEngine searechengine;
	ArrayList<String> params;
	JSONArray results = null;
	

	/**
	 * Default parameter which uses snowflake production gmhb as customer 
	 */
	public ResultAnalyzerTest()
	{
		//Initialize customer
		c1 = new Customer();
		c1.setId("c1");
		c1.setCountryCode("ch");
		c1.setCountryName("schweiz");
		c1.setFullName("snowflake productions gmbh");
		c1.setShortName("");
		c1.setZipCode("");
		
		params = new ArrayList<String>();
		params.add(c1.getFullName().toLowerCase());
		this.searechengine = SearchEngineFactory.requestSearchEngine(SearchEngineFactory.BING);
	}
	
	/**
	 * Use this for custom test
	 * @param c1	Customer you'd like to test
	 * @param params	params you'd like to use	

	public ResultAnalyzerTest(Customer c1,  List<String> params)
	{
		this.c1 = c1;
		this.params = (ArrayList<String>) params;
	}	 */

	@Test
	public JSONObject testAnalyze(SearchEngine searechengine, int numOfResults ) {
		
		
		//prepare for search
		String query = searechengine.buildQuery(params);
		
		
		
		
		//search bing
		try {
			 results = searechengine.search(query,numOfResults);
		} catch (IOException | NoUrlFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	//
		//Test google
		
		JSONObject aResult = ResultAnalyzer.analyse(results, params);
		
	
		return aResult;
	
		
	}

	public SearchEngine getSearechengine() {
		return searechengine;
	}

	public void setSearechengine(SearchEngine searechengine) {
		this.searechengine = searechengine;
	}

	public JSONArray getResults() {
		return results;
	}

	public void setResults(JSONArray results) {
		this.results = results;
	}

}
