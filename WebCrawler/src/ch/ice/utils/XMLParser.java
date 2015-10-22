package ch.ice.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	
	public static Map<String, Set<String>> getWordsPerIndustry(){
		String keywordfile = "conf/industryLists.xml";
		File xmlFile = new File(keywordfile);
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		
		Map<String, Set<String>> keywordsPerIndustry = new HashMap<String, Set<String>>();
		
		try {
			dBuilder = dbFactory.newDocumentBuilder();

			Document doc = dBuilder.parse(xmlFile);
			doc.getDocumentElement().normalize();
			Element docEle = doc.getDocumentElement();
			
			NodeList industryNode = docEle.getElementsByTagName("industry");
			
			for (int temp = 0; temp < industryNode.getLength(); temp++) {
				String industryName = industryNode.item(temp).getAttributes().item(0).getNodeValue();
				
				Element keywordsElement = (Element) industryNode.item(temp);
				NodeList keyWordList = keywordsElement.getElementsByTagName("keyword");
				
				
				Set<String> keywordsList = new HashSet<String>();
				for (int i = 0; i < keyWordList.getLength(); i++) {

					String keyword = keyWordList.item(i).getTextContent().toLowerCase();
					
					if(!keyword.isEmpty()){
						keywordsList.add(keyword);
					}
				}

				keywordsPerIndustry.put(industryName, keywordsList);
			}
			
			
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		
		return keywordsPerIndustry;
	}
}
