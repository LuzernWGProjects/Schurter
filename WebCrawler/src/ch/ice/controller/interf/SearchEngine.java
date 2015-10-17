package ch.ice.controller.interf;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import ch.ice.exceptions.NoUrlFoundException;
import ch.ice.exceptions.SearchEngineRequestLimitReachedException;

public interface SearchEngine {
	
	/**
	 * Searches the Internet for a requested Query. <br />
	 * NOTE: requestedQuery has to be the a fully qualified URL specification. <br />
	 * Each Search Engine (Google, Bing) uses different search patterns
	 * 
	 * @param requestedQuery 	The query that will be sent to the Searchengine.
	 * @param limitSearchResult	Limit retrieved searchresults.
	 * @return JSONArray		Results that are returned from the Searchengine.
	 * @throws IOException
	 * @throws NoUrlFoundException
	 * @throws SearchEngineRequestLimitReachedException
	 */
	public JSONArray search(String requestedQuery, int limitSearchResult) throws IOException, NoUrlFoundException, SearchEngineRequestLimitReachedException;
	
	/**
	 * Same as {@link #search(String, int)} except the available country code can be defined.<br />
	 * NOTICE: IF there is no country code use {@link #search(String, int)} and the country code will be set to "us"
	 * 
	 * @param requestedQuery
	 * @param limitSearchResult
	 * @param countryCode
	 * @return
	 * @throws IOException
	 * @throws NoUrlFoundException
	 * @throws SearchEngineRequestLimitReachedException
	 */
	public JSONArray search(String requestedQuery, int limitSearchResult, String countryCode) throws IOException, NoUrlFoundException, SearchEngineRequestLimitReachedException;

	/**
	 * Will return a correct encoded URL.<br />
	 * Each parameter can be added to the List<String>
	 * 
	 * @param params	Params that should be decoded.
	 * @return String	Correct encoded Query
	 */
	public String buildQuery(List<String> params);
	
	/**
	 * Each Searchengine returns a different JSONArray Object. <br />
	 * Therefor each implementation of SearchEnigne should implement a function that will standardize the JSONArray <br />
	 * This will help to better interact with the JSONArray.
	 * 
	 * @param results		Results returned from the Searchengine.
	 * @param keyNodeMap	This List defines the returnLabel and its Standard counterpart<br />
	 * 						e.g. Map<"Link","url"> => "Link" (the old value) will be overwritten with "url"
	 * 						
	 * @return JSONArray	The new standardized JSONArray
	 */
	public JSONArray standardizer(JSONArray results, Map<String,String> keyNodeMap);
}