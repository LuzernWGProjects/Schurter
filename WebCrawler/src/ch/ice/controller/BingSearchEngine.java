/**
 * 
 */
package ch.ice.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Base64;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.json.JSONArray;
import org.json.JSONObject;

import ch.ice.model.Customer;


/**
 * @author Oliver
 *
 */
public class BingSearchEngine  {

	public static  JSONArray Search(String requestedQuery) throws Exception {
	
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
        String bingUrl = String.format(bingUrlPattern, query);

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
            final JSONObject d = json.getJSONObject("d");
            final JSONArray results = d.getJSONArray("results");
            final int resultsLength = results.length();

            return results;
        }
    }
}