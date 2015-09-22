/**
 * 
 */
package ch.ice.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import ch.ice.controller.interf.Writer;
import ch.ice.model.Customer;
import ch.ice.model.Website;

/**
 * @author Oliver
 *
 */

public class ExcelWriter implements Writer {

	private Workbook workbook;
	private Sheet sheet;
	private Cell cell;
	private Row row;
	private int cellnum;
	private int mapCellNum =0;
	private Row headerRow;
	private Configuration config;

	public ExcelWriter()
	{
		
		try {
			config = new PropertiesConfiguration("conf/app.properties");
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void writeFile(List<Customer> customerList, Workbook wb) {
		this.workbook = wb;
		this.sheet = wb.getSheetAt(0);
		//firstRow = sheet.createRow(0);
		headerRow = sheet.getRow(2);

		int rownum = 3;

		for (Customer c : customerList) {
			row = sheet.getRow(rownum++);

			Object[] cObject = new Object[] {c.getFullName(),c.getCountryName(), c.getZipCode(), c.getWebsite().getUrl(), c.getWebsite().getMetaTags()};

			cellnum = 0;
			for (Object obj :cObject) {

				cell = row.createCell(cellnum++);
				//cell.setCellStyle((CellStyle) row.getCell(0));
/*
				if(obj instanceof String){
					cell.setCellValue((String)obj);
				} */
				 if (obj instanceof URL)
				{
					cell.setCellValue((String) obj.toString());
				} 
				else if (obj instanceof Map)
				{
					((Map) obj).forEach((k,v) -> writeMap(k, v));
				} 

			}
			mapCellNum = 0;
		}

		try {
			
			//Timestamp Format
			String timestampPattern = config.getString("writer.timestampPattern");
			DateTime dt = new DateTime();
			DateTimeFormatter fmt = DateTimeFormat.forPattern(timestampPattern);
			String timestampFormat = fmt.print(dt);
			
			//Write File
			FileOutputStream out = new FileOutputStream(new File(config.getString("writer.fileNamePattern")+"_"+timestampFormat+".xlsx" ));
			workbook.write(out);
			out.close();
			System.out.println("Excel written successfully..");

		} catch (FileNotFoundException ea) {
			ea.printStackTrace();
		} catch (IOException er) {
			er.printStackTrace();
		}

	}

	private void writeMap(Object k, Object v)
	{

		Cell firstCell = headerRow.getCell(cellnum+mapCellNum);
		firstCell.setCellValue((String)k);
		cell = row.getCell(cellnum+mapCellNum);
		cell.setCellValue((String)v);
		mapCellNum++;

	}

	public Workbook getWorkbook() {
		return workbook;
	}
}
