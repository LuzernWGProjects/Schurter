/**
 * 
 */
package ch.ice.controller.web;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import ch.ice.utils.JSONUtil;

/**
 * @author Oliver
 *
 */
public class ResultAnalyzer {
	
	/**
	 * Method to analyze which received result suits the best
	 * 
	 * @param results	List of received results
	 * @param parameters Search parameters to compare with the results
	 * @return	Result which suits the best (as JSONObject)
	 *
	 */
	public static JSONObject analyze(JSONArray results, ArrayList parameters)
	{
		
		
		
	
		//Go thur each recieved result of the search request
		for (int i = 0; i < results.length(); i++) {
			
			//create a new temporary JSONObject for each result
			JSONObject singleResult = results.getJSONObject(i);
			
			//extract the url, title and the desc
			String url = (String) singleResult.get("Url");
			String title = (String) singleResult.get("Title");
			String desc = (String) singleResult.get("Description");
			
			//store name of comapny in a variable to work with later on
			String companyName = (String) parameters.get(0);
			//create acronym of comapny name
			String acroOfCompanyName = createAcronym(companyName);
		
			// if the url contains the companys name return this as the best result
			if(url.contains((CharSequence) parameters.get(0)))
					{
				System.out.println("----------- NAMEN in URL!" +url +"diese url enthält den namen: "+ parameters.get(0));
				
				return singleResult;
					}
			// else if the url contains a acronym of the name return this as the best result
			else if(url.contains((CharSequence) acroOfCompanyName)){
			
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
		//TODO Silbentrennung einbinden (hyphenation)
		String acro = "";
		String[] words = name.split(" ");
		
		for(String word : words)
		{
			acro+=word.charAt(0);
		}
		System.out.println(acro);
		
		
		return acro;
	}

}
