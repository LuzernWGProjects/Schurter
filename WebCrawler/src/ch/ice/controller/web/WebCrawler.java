package ch.ice.controller.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import ch.ice.exceptions.HttpStatusException;

public class WebCrawler {
	private static final Logger logger = LogManager.getLogger(WebCrawler.class.getName());

	Document document;
	Connection connection;

	/**
	 *  Get Document object after parsing the html from given url.
	 *  
	 *  @param url	Connect to the given URL  
	 */
	public void connnect(String url) throws IOException, Exception {
		connection = Jsoup.connect(url).userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21");
		document = connection.get();
	}

	/**
	 * Get Metatags from document object and return Map
	 * 
	 * @param metaDef
	 * @return Map	return all found MetaTags on the website
	 * @throws HttpStatusException
	 */
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
					map.put(metaWord, "n/a");
					logger.warn("MetaTags not available for this URL");
				}
			}
		}

		else {
			logger.error("Received HTTP error code : " + statusCode +" "+ connection.response().statusMessage());
			throw new HttpStatusException("Received HTTP error code : " +statusCode +" "+ connection.response().statusMessage());
		}

		return map;
	}
}