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
import java.util.Base64;

import org.json.JSONArray;
import org.json.JSONObject;

import ch.ice.model.Customer;


/**
 * @author Oliver
 *
 */
public class BingSearchEngine {


    public static  Customer Search(Customer c) throws Exception {
      
    	
    	String companyQuery = c.getCountryName()+" "+c.getZipCode();
    	
    	// Bing Constants
    	final String accountKey = "Ji1A66TE2PeWimPqfLKVsKq4Q91Xb6cNBEEBmPjRWyQ";
        final String bingUrlPattern = "https://api.datamarket.azure.com/Bing/SearchWeb/Web?Query=%%27%s%%27&$format=JSON";

        final String query = URLEncoder.encode(companyQuery, Charset.defaultCharset().name());
        final String bingUrl = String.format(bingUrlPattern, query);

        final String accountKeyEnc = Base64.getEncoder().encodeToString((accountKey + ":" + accountKey).getBytes());

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
          
           /* 
            for (int i = 0; i < resultsLength; i++) {
            	   JSONObject   aResult = results.getJSONObject(i);
                System.out.println(aResult.get("Url"));
            
            }
            */
            
            
            return results;
          
        }
    }

}