package ch.ice.controller.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.json.JSONArray;
import org.json.JSONObject;

import ch.ice.controller.interf.SearchEngine;
import ch.ice.exceptions.NoUrlFoundException;
import ch.ice.utils.Config;
import ch.ice.utils.JSONStandardizedKeys;
import ch.ice.utils.JSONUtil;


public class BingSearchEngine implements SearchEngine {

	@Override
	public JSONArray search(String requestedQuery, int limitSearchResults, String countryCode)  throws IOException, NoUrlFoundException {

		String accountKey = "";
		String bingUrlPattern = "";
		PropertiesConfiguration config = Config.PROPERTIES;

		/*
		 * Load Configuration File
		 */
		accountKey = config.getString("searchEngine.bing.accountKey");
		bingUrlPattern = config.getString("searchEngine.bing.pattern");



		// Bing Constants

		String query = URLEncoder.encode(requestedQuery, Charset.defaultCharset().name());

		// if search results limit is smaller then 1, set to 1
		if(limitSearchResults < 1) limitSearchResults = 1;

		String bingUrl = String.format(bingUrlPattern+"&$top="+limitSearchResults, query);

		String accountKeyEnc = Base64.getEncoder().encodeToString((accountKey + ":" + accountKey).getBytes());

		final URL url = new URL(bingUrl);
		final URLConnection connection = url.openConnection();


		//Search 
		connection.setRequestProperty("Authorization", "Basic " + accountKeyEnc);

		try (final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {

			String inputLine;
			final StringBuilder response = new StringBuilder();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}

			final JSONObject json = new JSONObject(response.toString());
			JSONObject d = json.getJSONObject("d");

			JSONArray bingResults = d.getJSONArray("results");

			final int resultsLength = bingResults.length();

			if(resultsLength < 1) 
				throw new NoUrlFoundException("The Search engine delivered " +resultsLength+ " results for ["+requestedQuery+"]. Please change your query");


			// remove unused elements and trim urls
			JSONUtil.keepLablesInJSONArray = new ArrayList<String>(
					// default ones for bing
					Arrays.asList(
							"Url",
							"Description",
							"Title"
							)
					);
			JSONUtil.urlLabel = "Url";

			bingResults = JSONUtil.cleanUp(bingResults);

			System.out.println("Cleaned up results from Bing: "+bingResults);
			System.out.println("Cleaned up resulsts length: "+bingResults.length());

			// standardize lables
			Map<String, String> keyNodeMap = new HashMap<String,String>();
			keyNodeMap.put("Url", JSONStandardizedKeys.URL);
			keyNodeMap.put("Description", JSONStandardizedKeys.DESCRIPTION);
			keyNodeMap.put("Title", JSONStandardizedKeys.TITLE);

			bingResults = this.standardizer(bingResults, keyNodeMap);

			System.out.println("STD results from Bing: "+bingResults);
			System.out.println("STD resulsts length: "+bingResults.length());

			return bingResults;
		}
	}
	
	@Override
	public JSONArray search(String requestedQuery, int limitSearchResults) throws IOException, NoUrlFoundException {
		return search(requestedQuery, limitSearchResults, "us");
	}

	@Override
	public String buildQuery(List<String> params){
		String query = "";

		for (String string : params) {
			query += string+" ";
		}

		return query;
	}

	@Override
	public JSONArray standardizer(JSONArray results, Map<String, String> keyNodeMap) {
		JSONArray stdJson = JSONUtil.keyNodeMapper(results, keyNodeMap);
		return stdJson;
	}
}