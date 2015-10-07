package ch.ice.model;

import java.net.URL;
import java.util.Map;

public class Website {
	
	private URL url;
	private boolean unsure;
	private Map<String,String> metaTags;
	
	public Website() {}
	
	public Website(URL url, Map<String,String> metaTags) {
		this.setMetaTags(metaTags);
		this.setUrl(url);
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}
	
	public void setUnsure(boolean unsure)
	{
		this.unsure = unsure;
	}
	
	public boolean getUnsure()
	{
		return unsure;
	}

	public Map<String,String> getMetaTags() {
		return metaTags;
	}

	public void setMetaTags(Map<String,String> metaTags) {
		this.metaTags = metaTags;
	}
	
	@Override
	public String toString() {
		return "Website URL: "+this.getUrl().toString()+"; MetaTags: "+this.getMetaTags().toString()+"";
	}
}
