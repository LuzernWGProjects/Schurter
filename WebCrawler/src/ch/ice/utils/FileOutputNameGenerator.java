package ch.ice.utils;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import ch.ice.controller.MainController;
import ch.ice.controller.file.CSVWriter;
import ch.ice.controller.file.ExcelWriter;


public  class FileOutputNameGenerator {
	private static Configuration config;
	
	public static String createName()
	{
		try {
			config = new PropertiesConfiguration("conf/app.properties");
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String fileType;
		String fileName;
		
			if(MainController.fileWriter instanceof CSVWriter)
			{
				fileType = ".csv";
			}else if (MainController.fileWriter instanceof ExcelWriter) {
				fileType = ".xlsx";
			}
			else
			{
				fileType = ".xlsx";
			}
		
		
		String timestampPattern = config.getString("writer.timestampPattern");
		DateTime dt = new DateTime();
		DateTimeFormatter fmt = DateTimeFormat.forPattern(timestampPattern);
		String timestampFormat = fmt.print(dt);
		
		
		fileName = config.getString("writer.file.path") + "/"+ config.getString("writer.fileNamePattern") + "_"+ timestampFormat + fileType;
		
		return fileName;
	}
}
	


