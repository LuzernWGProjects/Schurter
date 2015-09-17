package ch.ice.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class WebCrawler {

	Document document;

	public WebCrawler() {
	}

	// Get Document object after parsing the html from given url.
	public void connnect(String url) throws IOException {
		document = Jsoup.connect(url).get();
	}

	// Get description from document object.
	public String getDescription() {
		String description = document.select("meta[name=description]").get(0)
				.attr("content");

		// Print description.
		// System.out.println("Meta Description: " + description);

		return description;
	}

	// Get keywords from document object.
	public Map<String, String> getMetaTags(ArrayList<String> metaDef) {

		Map<String, String> map = new HashMap<String, String>();

		for (String metaWord : metaDef) {
			try {
				String metaTags = document
						.select("meta[name=" + metaWord + "]").first()
						.attr("content");
				map.put(metaWord, metaTags);
			} catch (Exception e) {
				System.out.println("fuck this");
				map.put(metaWord, " n/a");

			}

		}

		// Print keywords.
		// System.out.println("Meta Keyword : " + keywords);

		return map;
	}

	public static void main(String[] args) {

		WebCrawler wc = new WebCrawler();
		ArrayList<String> array = new ArrayList<String>();
		array.add("description");
		array.add("keyword");
		array.add("author");
		array.add("viewport");

		try {
			wc.connnect("http://www.snowflake.ch");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Connection failed");

		}
		try {
			Map<String, String> test = wc.getMetaTags(array);
			System.out.println(test.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("something wrong with Desc");
		}

	}
}
