package ch.ice.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Helperclass for JSONArray handling.
 * It provides some functionalities to improve
 * the handling with the reutrned JSONarray
 * results from the SearchEngine.
 * 
 * @author mneuhaus
 *
 */
public final class JSONUtil {
	private static final Logger logger = LogManager.getLogger(JSONUtil.class.getName());

	/**
	 * All defined labels here will be kept in the JSONArray.<br />
	 * The standardized keys will be set as the default but can be
	 * easly overwritten in the actual implementation.
	 */
	public static List<String> keepLablesInJSONArray = new ArrayList<String>(
			// default ones for bing
			Arrays.asList(
				JSONStandardizedKeys.URL,
				JSONStandardizedKeys.DESCRIPTION,
				JSONStandardizedKeys.TITLE
			)
	);

	/**
	 * Set the standard URL label for the trimming function.<br />
	 * {@see JSONUtil#trimUrls(JSONArray, String)}
	 */
	public static String urlLabel = JSONStandardizedKeys.URL;

	/**
	 * Clean up all JSONArray elements. <br />
	 * This function will call {@link JSONUtil#removeUnusedElements(JSONArray, List)} and {@link JSONUtil#trimUrls(JSONArray, String)}
	 * 
	 * @param results
	 * @return JSONArray
	 */
	public static JSONArray cleanUp(JSONArray results) {
		logger.info("Started JSON Clean Up");
		JSONArray stripedResults = removeUnusedElements(results, keepLablesInJSONArray);
		stripedResults = trimUrls(stripedResults, urlLabel);

		return stripedResults;
	}

	/**
	 * Trim Urls so only the base uri is available<br />
	 * E.g. http://www.schurter.net/team/ceo will bechome<br />
	 * http://www.schurter.net/
	 * 
	 * @param results
	 * @return JSONArray JSONArray containing the trimmed URLs.
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
	 * Remove all unwanted tags and JSON Nodes.<br />
	 * Fewer data means faster iteration over an array.
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
	 * This will map the old JSONArray node Name to<br />
	 * a new JSONArray node Name. Can be usefulle <br />
	 * if something needs to be standardized.
	 * <br />
	 * Key: "URL" -> new Key: "url"<br />
	 * <br />
	 * Remove the oldkey name and replace it with the <br />
	 * new key name. All values will be in the same place.
	 * 
	 * @param results
	 * @param renameLabelMap	A Map containing old Keys and the new key value
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