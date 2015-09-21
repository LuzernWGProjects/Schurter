package ch.ice.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class WebCrawler {

	Document document;

	// Get Document object after parsing the html from given url.
	public void connnect(String url) throws IOException {
		document = Jsoup.connect(url).get();
	}

	// Get Metatags from document object and return Map
	public Map<String, String> getMetaTags(List<String> metaDef) {

		Map<String, String> map = new HashMap<String, String>();

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

		return map;
	}
}