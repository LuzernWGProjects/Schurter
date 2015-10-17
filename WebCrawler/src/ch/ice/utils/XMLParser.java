package ch.ice.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLParser {

	private static final String country2xmlFile = "conf/country2market.xml";
	
	public static void main(String[] args) {
		XMLParser.getTLDOfCountry();
	}
	
	public static Map<String, String> getTLDOfCountry(){

		Map<String, String> country2tld = new HashMap<String, String>();

		File xmlFile = new File(country2xmlFile);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;

		try {
			dBuilder = dbFactory.newDocumentBuilder();

			Document doc = dBuilder.parse(xmlFile);
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("country");

			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;
					
					
					String countryCode = eElement.getElementsByTagName("code").item(0).getTextContent().toLowerCase();
					String tld = eElement.getElementsByTagName("tld").item(0).getTextContent();
					
					if(tld.isEmpty())
						continue;
					
					country2tld.put(countryCode, tld);

				}
			}


		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		
		return country2tld;
	}
	
	public static Map<String, String> getMarket(){

		Map<String, String> country2Market = new HashMap<String, String>();

		File xmlFile = new File(country2xmlFile);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;

		try {
			dBuilder = dbFactory.newDocumentBuilder();

			Document doc = dBuilder.parse(xmlFile);
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("country");

			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;
					
					
					String countryCode = eElement.getElementsByTagName("code").item(0).getTextContent().toLowerCase();
					String market = eElement.getElementsByTagName("market").item(0).getTextContent();
					
					if(market.isEmpty())
						continue;
					
					country2Market.put(countryCode, market);

				}
			}


		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		
		return country2Market;
	}

}
