package ch.ice.controller.web;

import ch.ice.controller.interf.SearchEngine;

public class SearchEngineFactory {

	public final static String GOOGLE = "google";
	public final static String BING = "bing";
	
	/**
	 * Return a search engine. google will be used as a default
	 * 
	 * @return SearchEngine - Google
	 */
	public SearchEngineFactory(){
		requestSearchEngine(GOOGLE);
	}
	
	/**
	 * Request a search engine that can be used. Either its google or bing
	 * 
	 * @param identifier
	 * @return SearchEngine
	 */
	public static SearchEngine requestSearchEngine(String identifier){
		if(identifier.equals(SearchEngineFactory.GOOGLE) && identifier == SearchEngineFactory.GOOGLE) {
			return new GoogleSearchEngine();
		} else if (identifier.equals(SearchEngineFactory.BING) && identifier == SearchEngineFactory.BING){
			return new BingSearchEngine();
		} else {
			//throw new 
		}
		return null;
	}
}
