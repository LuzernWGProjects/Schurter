/**
 * 
 */
package ch.ice.controller;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Oliver
 *
 */
public class MainController {

	
	public static void startMainController()
	{
		
		startSearch();
	}
	
	
	public static void startParser()
	{
		
	}
	
	public static void startSearch(){
	System.out.println("start test bing");
	
	String query = "emmi ";
	try {
		
	 JSONArray results = BingSearchEngine.Search(query);
	 
     final int resultsLength = results.length();

	   for (int i = 0; i < resultsLength; i++) {
    	   JSONObject   aResult = results.getJSONObject(i);
        System.out.println(aResult.get("Url"));
    
    }
	
		
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	
	
	public static void startWriter()
	{
		
	}
	
	public static void startCrawler()
	{
		
	}
}
