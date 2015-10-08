package ch.ice.controller.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import ch.ice.controller.interf.SearchEngine;
import ch.ice.exceptions.NoUrlFoundException;
import ch.ice.utils.JSONUtil;

public class GoogleSearchEngine implements SearchEngine {
	
	private static final Logger logger = LogManager.getLogger(GoogleSearchEngine.class.getName());

	public JSONArray search(String requestedQuery, int limitSearchResult) throws NoUrlFoundException {
		try {

			String charset = Charset.defaultCharset().name();

			final String apiKey = URLEncoder.encode("AIzaSyCA4F-ffoVrV-DP4bGK7hHwjnPdrAk-6Jg", charset);
			final String cx =  URLEncoder.encode("012938936336043454826:yfrotl5pxqu", charset);

			/*
			 * for field options check:
			 * https://developers.google.com/apis-explorer/?hl=de#p/customsearch/v1/search.cse.list
			 */
			final String fields =  URLEncoder.encode("items(link,title),searchInformation/searchTime", charset);
			final String googleHost =  URLEncoder.encode("google.com", charset);
			int searchResultsLimit = limitSearchResult;

			requestedQuery = URLEncoder.encode(requestedQuery, charset);

			// google only ever returns max 10 results
			if(searchResultsLimit < 1){
				searchResultsLimit = 1;
			} else if(searchResultsLimit > 10){
				searchResultsLimit = 10;
			}

			String googleSearchUrl = "https://www.googleapis.com/customsearch/v1?q="+ requestedQuery +"&key="+ apiKey +"&cx="+ cx +"&googlehost="+ googleHost +"&fields="+ fields+"&num="+searchResultsLimit;
			logger.info("Lookup Google with request URL: "+googleSearchUrl);

			URL url = new URL(googleSearchUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			String inputLine;
			final StringBuilder response = new StringBuilder();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}

			JSONObject json = new JSONObject(response.toString());
			JSONArray googleSarchResults = json.getJSONArray("items");
			logger.info("Returned results from Google: "+googleSarchResults.length());

			final int resultsLength = googleSarchResults.length();

			if(resultsLength < 1) 
				throw new NoUrlFoundException("The Search engine delivered " +resultsLength+ " results for ["+requestedQuery+"]. Please change your query");


			Map<String, String> keyNodeMap = new HashMap<String,String>();
			keyNodeMap.put("link", "url");

			JSONArray stdJson = this.standardizer(googleSarchResults, keyNodeMap);

			// Remove unused labels
			JSONUtil.keyNodes = new ArrayList<String>(
					// remove none - can be done in the google api doc
					);

			return JSONUtil.cleanUp(stdJson);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Build a query for the search engine use.
	 * 
	 * @param params
	 * @return String query
	 */
	public String buildQuery(List<String> params){
		String query = "";

		for (String string : params) {
			query += string+" ";
		}

		return query;
	}

	/**
	 * This method standardizes the json array so that the analyzer can make assumptions
	 * 
	 * @return standardized JSONArray. The keys of all elements are now the same and independend from Googles returns.
	 */
	@Override
	public JSONArray standardizer(JSONArray results, Map<String, String> keyNodeMap) {
		return JSONUtil.keyNodeMapper(results, keyNodeMap);
	}
}
