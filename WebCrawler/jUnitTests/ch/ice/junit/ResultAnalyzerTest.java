package ch.ice.junit;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import ch.ice.controller.interf.SearchEngine;
import ch.ice.controller.web.BingSearchEngine;
import ch.ice.controller.web.ResultAnalyzer;
import ch.ice.exceptions.NoUrlFoundException;
import ch.ice.model.Customer;

public class ResultAnalyzerTest {
	Customer c1;
	SearchEngine bing;
	ArrayList<String> params;
	
	public ResultAnalyzerTest()
	{
		//Initialize customer
		c1 = new Customer();
		c1.setId("c1");
		c1.setCountryCode("us");
		c1.setCountryName("united states");
		c1.setFullName("snowflake productions gmbh");
		c1.setShortName("");
		c1.setZipCode("");
		
		params = new ArrayList<String>();
		params.add(c1.getFullName().toLowerCase());
	}

	@Test
	public void testAnalyze() {
		
		//prepare for search
		this.bing = new BingSearchEngine();
		String query = BingSearchEngine.buildQuery(params);
		JSONArray results = null;
		//search bing
		try {
			 results = BingSearchEngine.search(query, 5);
		} catch (IOException | NoUrlFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Test google
		
		JSONObject aResult = ResultAnalyzer.analyze(results, params);
		
		//assertif better
		String url = (String) aResult.get("Url");
		assertEquals(url, "http://www.snowflake.ch/");
		//assertEquals(0.3, ResultAnalyzer.candidatesArray[2],0.003);
		
		
		
	}

}
