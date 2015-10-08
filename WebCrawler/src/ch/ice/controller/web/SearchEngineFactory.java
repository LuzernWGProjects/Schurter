package ch.ice.controller.web;

import ch.ice.controller.interf.SearchEngine;
import ch.ice.exceptions.SearchEngineNotAvailableException;

public class SearchEngineFactory {

	public final static String GOOGLE = "google";
	public final static String BING = "bing";
	
	/**
	 * Return a search engine. google will be used as a default
	 * 
	 * @return SearchEngine - Google
	 */
	public SearchEngineFactory() throws SearchEngineNotAvailableException{
		requestSearchEngine(GOOGLE);
	}
	
	/**
	 * Request a search engine that can be used. Either its google or bing
	 * 
	 * @param identifier
	 * @return SearchEngine
	 */
	public static SearchEngine requestSearchEngine(String identifier) throws SearchEngineNotAvailableException {
		if(identifier.equals(SearchEngineFactory.GOOGLE) && identifier == SearchEngineFactory.GOOGLE) {
			return new GoogleSearchEngine();
		} else if (identifier.equals(SearchEngineFactory.BING) && identifier == SearchEngineFactory.BING){
			return new BingSearchEngine();
		} else {
			throw new SearchEngineNotAvailableException("Reqeusted SearchEngine is not available. Plase use src.ch.controller.web.GoogleSearchEngine or src.ch.ice.controller.web.BingSearchEngine");
		}
	}
}
