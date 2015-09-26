package ch.ice.controller.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import ch.ice.exceptions.HttpStatusException;

public class WebCrawler {

	Document document;
	Connection connection;

	// Get Document object after parsing the html from given url.
	public void connnect(String url) throws IOException {
	
		connection = Jsoup.connect(url);
		document = connection.get();
	}

	// Get Metatags from document object and return Map
	public Map<String, String> getMetaTags(List<String> metaDef) throws HttpStatusException{

		Map<String, String> map = new HashMap<String, String>();

		
		int statusCode =   connection.response().statusCode();
		if(statusCode == 200) {
		 
		
		for (String metaWord : metaDef) {
			try {
				String metaTags = document.select("meta[name=" + metaWord + "]").first().attr("content");
				map.put(metaWord, metaTags);
			} catch (Exception e) {
				// MetaTags not available
				//TODO Throw custom Exception -> gui handle
				map.put(metaWord, "n/a");
			}
		}
	    }
		
			else {
				  System.out.println("received error code : " + statusCode +" "+ connection.response().statusMessage());
				throw new HttpStatusException("received error code : " +statusCode +" "+ connection.response().statusMessage());
			  
			}

		return map;
	}
}