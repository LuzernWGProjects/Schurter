/**
 * 
 */
package ch.ice.controller;

import java.io.File;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

import ch.ice.controller.parser.ExcelParser;
import ch.ice.controller.interf.Parser;

/**
 * @author Oliver
 *
 */
public class MainController {

	
	public static void startMainController()
	{
		
		//startSearch();
		startExcelParser();
	}
	
	
	public static void startParser()
	{
		
	}
	
	
	public static String startSearch(String query){
		System.out.println("start test bing");
		String url = null;
		
		try {
			
		 JSONArray results = BingSearchEngine.Search(query);
	/*	 
	     final int resultsLength = results.length();

		   for (int i = 0; i < resultsLength; i++) {
	    	   JSONObject   aResult = results.getJSONObject(i);
	        System.out.println(aResult.get("Url"));
	    }
	      */ 
	        JSONObject   JsonObj = results.getJSONObject(0); 
	        
	         url = (String) JsonObj.get("Url");
	      	
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  return url;
		}
	
	
	// Test purpose
	public static void startExcelParser(){
		Parser excelParser = new ExcelParser();
		
		try {
			excelParser.readFile(new File("posTest.xlsx"));
		} catch (IOException e) {
			System.out.println("File not found");
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
