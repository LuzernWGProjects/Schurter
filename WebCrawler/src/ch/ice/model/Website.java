package ch.ice.model;

import java.net.URL;
import java.util.Map;

public class Website {
	
	private URL url;
	private Map metaTags;
	
	public Website() {}
	
	public Website(URL url, Map metaTags) {
		this.setMetaTags(metaTags);
		this.setUrl(url);
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public Map getMetaTags() {
		return metaTags;
	}

	public void setMetaTags(Map metaTags) {
		this.metaTags = metaTags;
	}
	
}
