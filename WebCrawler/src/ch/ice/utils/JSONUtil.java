package ch.ice.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class JSONUtil {
	private static final Logger logger = LogManager.getLogger(JSONUtil.class.getName());

	// init key search field
	public static List<String> keyNodes = new ArrayList<String>(
			// default ones for bing
			Arrays.asList("Url", "Description", "Title")
	);

	/**
	 * Starter Function for our analysis methods.
	 * 
	 * @param results
	 * @param searchParams
	 * @return 
	 */
	public static JSONArray cleanUp(JSONArray results, List<String> searchParams) {
		JSONArray stripedResults = removeUnusedElements(results, keyNodes);

		stripedResults = trimUrls(stripedResults);
		
		
		return stripedResults;
	}
	
	/**
	 * Trim Urls so only the base uri is available
	 * 
	 * @param results
	 * @return
	 */
	private static JSONArray trimUrls(JSONArray results) {
		JSONArray trimedUrls = new JSONArray();
		String cleanUrl ="";
		
		if (results != null) {
			// iterate over every customer results
			for (int i = 0; i < results.length(); i++) {
				JSONObject customerDetailObject = results.getJSONObject(i);
				
				// iterate over every customer detail (url, desc, title, usw)
				for (int j = 0; j < customerDetailObject.length()-1; j++) {
					
					try {
						
						//Get every customer URL
						String customerUrl = results.getJSONObject(j).getString("Url");
						
						if(customerUrl != null) {
							// get specific fields from URL
							URL url = new URL(customerUrl);
							String protocol = url.getProtocol();
							String host = url.getHost();
							
							cleanUrl = protocol+"://"+host+"/"; 
							
							// set clean url to specific customer
							customerDetailObject.put("Url",cleanUrl);
						}
						
					} catch (JSONException | MalformedURLException e) {
						logger.error("Malformed URL: "+ e.getMessage());
						e.printStackTrace();
					}
				}
				
				trimedUrls.put(customerDetailObject);
			}
		}
		return trimedUrls;
	}

	/**
	 * Remove all unwanted tags and JSON Nodes
	 * 
	 * @param results
	 * @param keyNodes
	 * @return stripedResults
	 */
	private static JSONArray removeUnusedElements(JSONArray results, List<String> keyNodes) {
		JSONArray stripedResults = new JSONArray();

		if (results != null) {
			for (int i = 0; i < results.length(); i++) {	
				JSONObject jObj = new JSONObject();
				for (String key : keyNodes) {
					jObj.put(key, results.getJSONObject(i).get(key));
				}

				stripedResults.put(jObj);
			}
		}
		return stripedResults;
	}
	
}