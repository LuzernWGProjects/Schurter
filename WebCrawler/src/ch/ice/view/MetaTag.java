package ch.ice.view;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class MetaTag {

	public MetaTag() {
		super();
		// TODO Auto-generated constructor stub
	}

	private Map<String, String> mapXML;

	public Map<String, String> getMapXML() {
		return mapXML;
	}

	public void setMapXML(Map<String, String> mapXML) {
		this.mapXML = mapXML;
	}

	public static void main(String[] args) {
		getMetaList();
	}

	public static ArrayList<MetaTag> getMetaList() {

		ArrayList<MetaTag> finalListMeta = new ArrayList<MetaTag>();

		try {

			File fXmlFile = new File("conf/basicHTMLMetaTags.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("metaTag");

			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);
				NodeList childList = nNode.getChildNodes();
				HashMap<String, String> mapXML = new HashMap<String, String>();

				int length = childList.getLength();
				MetaTag mt = new MetaTag();

				for (int i = 0; i < length; i++) {
					if (childList.item(i).getNodeType() == Node.ELEMENT_NODE) {
						String nodeName = childList.item(i).getNodeName();
						String nodeContent = childList.item(i).getTextContent();

						mapXML.put(nodeName, nodeContent);

					}

				}
				mt.setMapXML(mapXML);
				finalListMeta.add(mt);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return finalListMeta;

	}

}
