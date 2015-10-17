package ch.ice.utils;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class Config {
	
	public final static PropertiesConfiguration PROPERTIES = new PropertiesConfiguration();
	private final static String CONFIG_FILE = "conf/app.properties";
	
	static {
		try {
			Config.PROPERTIES.load(Config.CONFIG_FILE);
			Config.PROPERTIES.setFileName("app.properties");
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}	
}
