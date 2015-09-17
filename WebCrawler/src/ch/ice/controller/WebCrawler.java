package ch.ice.controller;

import java.io.IOException;

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
	public void getDescription() {
		String description = document.select("meta[name=description]").get(0)
				.attr("content");
		// Print description.
		System.out.println("Meta Description: " + description);
	}

	// Get keywords from document object.
	public void getKeywords() {
		String keywords = document.select("meta[name=keywords]").first()
				.attr("content");
		// Print keywords.
		System.out.println("Meta Keyword : " + keywords);
	}

	public static void main(String[] args) {

		WebCrawler wc = new WebCrawler();
		try {
			wc.connnect("http://www.snowflake.ch");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Connection failed");

		}
		try {
			wc.getDescription();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("something wrong with Desc");
		}
		try {
			wc.getKeywords();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("something wrong with Keyw");
		}

	}
}
