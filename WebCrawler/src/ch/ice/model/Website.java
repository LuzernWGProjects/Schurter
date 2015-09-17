package ch.ice.model;

import java.net.URL;
import java.util.Map;

public class Website {
	
	private URL websiteUrl;
	private Map metaTags;
	
	public URL getWebsiteUrl() {
		return websiteUrl;
	}
	
	public void setWebsiteUrl(URL websiteUrl) {
		this.websiteUrl = websiteUrl;
	}
	
	public Map getMetaTags() {
		return metaTags;
	}
	
	public void setMetaTags(Map metaTags) {
		this.metaTags = metaTags;
	}
	
	
}
