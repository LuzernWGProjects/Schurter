/**
 * 
 */
package ch.ice.controller.web;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.json.JSONArray;
import org.json.JSONObject;

import ch.ice.utils.JSONUtil;


/**
 * @author Oliver
 *
 */
public class BingSearchEngine  {

	public static  JSONArray Search(String requestedQuery, int limitSearchResults) throws Exception {
	
    	String accountKey = "";
    	String bingUrlPattern = "";
    	PropertiesConfiguration config;
    	
    	/*
		 * Load Configuration File
		 */
		try {
			config = new PropertiesConfiguration("conf/app.properties");
			
			accountKey = config.getString("searchEngine.bing.accountKey");
			bingUrlPattern = config.getString("searchEngine.bing.pattern");
			
		} catch (ConfigurationException e) {
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
    	
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
            
            JSONArray results = d.getJSONArray("results");
            
            final int resultsLength = results.length();
            
            return JSONUtil.cleanUp(results);
        }
    }
	
	/**
	 * Build a query for the search engine use.
	 * 
	 * @param params
	 * @return String query
	 */
	public static String buildQuery(ArrayList<String> params){
		String query = "";
		
		for (String string : params) {
			query += string+" ";
		}
		
		return query;
	}
}