package ch.ice.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class JSONUtil {
	private static final Logger logger = LogManager.getLogger(JSONUtil.class.getName());

	// init key search field
	public static List<String> keepLablesInJSONArray = new ArrayList<String>(
			// default ones for bing
			Arrays.asList(
				JSONStandardizedKeys.URL,
				JSONStandardizedKeys.DESCRIPTION,
				JSONStandardizedKeys.TITLE
			)
	);

	// set the standard URL label
	public static String urlLabel = JSONStandardizedKeys.URL;

	/**
	 * Starter Function for our analysis methods.
	 * 
	 * @param results
	 * @return 
	 */
	public static JSONArray cleanUp(JSONArray results) {
		logger.info("Started JSON Clean Up");
		JSONArray stripedResults = removeUnusedElements(results, keepLablesInJSONArray);
		stripedResults = trimUrls(stripedResults, urlLabel);

		return stripedResults;
	}

	/**
	 * Trim Urls so only the base uri is available
	 * 
	 * @param results
	 * @return
	 */
	public static JSONArray trimUrls(JSONArray results, String urlLabel) {
		JSONArray trimedUrls = new JSONArray();
		String cleanUrl ="";

		if (results != null) {
			// iterate over every customer results
			for (int i = 0; i < results.length(); i++) {
				JSONObject customerDetailObject = results.getJSONObject(i);

				// iterate over every customer detail (url, desc, title, usw)
				for (int j = 0; j < customerDetailObject.length(); j++) {

					try {

						//Get every customer URL
						String customerUrl = customerDetailObject.getString(urlLabel);

						if(customerUrl != null) {
							// get specific fields from URL
							URL url = new URL(customerUrl);
							String protocol = url.getProtocol();
							String host = url.getHost();

							cleanUrl = protocol+"://"+host+"/"; 

							// set clean url to specific customer
							customerDetailObject.remove(urlLabel);
							customerDetailObject.put(urlLabel,cleanUrl);
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
	public static JSONArray removeUnusedElements(JSONArray results, List<String> keyNodes) {
		JSONArray stripedResults = new JSONArray();

		if (results != null) {

			// if nothing gets removed
			if(keyNodes.size() < 1)
				return results;

			// strip everything from array
			for (int i = 0; i < results.length(); i++) {	
				JSONObject jObj = new JSONObject();
				for (String key : keyNodes) {
					String value = (String) results.getJSONObject(i).get(key);
					jObj.put(key, value);
				}
				
				stripedResults.put(jObj);
			}
		}

		return stripedResults;
	}

	/**
	 * Can be used for JSON Standardization
	 * 
	 * oldKey -> newKey
	 * e.g. "Url" -> "url"
	 * 
	 * @param results
	 * @param renameLabelMap
	 * @return JSONArray
	 */
	public static JSONArray keyNodeMapper(JSONArray results, Map<String,String> renameLabelMap) {
		JSONArray standardizedResults = new JSONArray();
		if (results != null) {
			
			// strip everything from array
			for (int i = 0; i < results.length(); i++) {	
				
				JSONObject customerDetailObject = results.getJSONObject(i);
				JSONObject jObjContainer = new JSONObject();
				
				for (int j = 0; j < customerDetailObject.length(); j++) {
					
					for(Entry<String, String> entry : renameLabelMap.entrySet()){
						String oldKey = entry.getKey();
						String newKey = entry.getValue();
						String value = customerDetailObject.getString(oldKey);
						
						jObjContainer.put(newKey, value);
					}
				}
				
				standardizedResults.put(jObjContainer);
			}
		}
		return standardizedResults;
	}
}