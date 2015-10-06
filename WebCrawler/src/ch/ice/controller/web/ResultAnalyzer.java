/**
 * 
 */
package ch.ice.controller.web;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.json.JSONArray;
import org.json.JSONObject;

import ch.ice.utils.JSONUtil;

/**
 * @author Oliver
 *
 */
public class ResultAnalyzer {
	
	static URI uri;
	
	/**
	 * Method to analyze which received result suits the best
	 * 
	 * @param results	List of received results
	 * @param parameters Search parameters to compare with the results
	 * @return	Result which suits the best (as JSONObject)
	 *
	 */
	public static JSONObject analyze(JSONArray results, List<String> parameters)
	{
		System.out.println("Laenge: "+results.length()+";");
		//Go thru each received result of the search request
		for (int i = 0; i < results.length(); i++) {
			
			//create a new temporary JSONObject for each result
			JSONObject singleResult = results.getJSONObject(i);
			
			//extract the url, title and the desc
			String url = (String) singleResult.get("Url");
			String title = (String) singleResult.get("Title");
			String desc = (String) singleResult.get("Description");
			
			
			try {
				uri = new URI(url);
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			String host = uri.getHost().replaceFirst("^(http://|http://www\\.|www\\.)","");
			
			//store name of company in a variable to work with later on
			String companyName = (String) parameters.get(0);
			//create acronym of company name
			String acroOfCompanyName = createAcronym(companyName);
		
			// if the url is on the blacklist, remove this element form the list
			if(isBlacklist(host))
				{
					System.out.println("remove weil blacklist");
					results.remove(i);
					//recursive call of the same function
					analyze(results, parameters);
				}
		
			// if the url contains the company name return this as the best result
			if(host.contains((CharSequence) parameters.get(0)))
					{
				System.out.println("----------- NAMEN in URL!" +url +"diese url enthält den namen: "+ parameters.get(0));
				
				return singleResult;
					}
			// else if the url contains a acronym of the name return this as the best result
			else if(host.contains((CharSequence) acroOfCompanyName)){
			
				System.out.println("-----------ABK in URL: Diese Url enthält die abk: " + acroOfCompanyName);
			
				return singleResult;
			}
		
		}
		//if neither the name nor the acronym of the name is contained in the url, return the first result
		System.out.println("--------- Nicht gefunden nimm erstes");
		return results.getJSONObject(0);
	}
	
	/**
	 * Creates an Acronym of the given string using the first letter of each word
	 * @param name String to generate Acronym
	 * @return	Acronym as String
	 */
	private static String createAcronym(String name)
	{
		String acro = "";
		String[] words = name.split(" ");
		
		for(String word : words)
		{
			acro+=word.charAt(0);
		}
		System.out.println(acro);
		
		
		return acro;
	}
	
	/**
	 * Checks if the host in url is one of the blacklist which is defined in the propertiesfile as searchEngine.bing.blacklist
	 * @param host to check if blacklisted
	 * @return	<true> if is blacklisted <false> if is not blacklisted
	 */
	private static boolean isBlacklist(String host)
	{
		//Create array with blacklisted hosts
		List<String> blacklist = new ArrayList<String>();
		try {
			//Get Blacklist form propertiesfile
			PropertiesConfiguration config = new PropertiesConfiguration("conf/app.properties");
			blacklist =  Arrays.asList(config.getStringArray("searchEngine.bing.blacklist"));

		} catch (ConfigurationException e) {
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
		
		//Check if one element form the blacklist equals the regarding host..
		for (String  blacklistElement : blacklist) {
		
			//.. if yes return true
			if(host.contains(blacklistElement))
			{
				return true;
			}
			
		}
		//..else return false
		return false;
	}

}
