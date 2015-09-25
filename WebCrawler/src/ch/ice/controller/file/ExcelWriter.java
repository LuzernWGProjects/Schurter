/**
 * 
 */
package ch.ice.controller.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import ch.ice.controller.interf.Writer;
import ch.ice.model.Customer;

/**
 * @author Oliver
 *
 */

public class ExcelWriter implements Writer {

	private static final Logger logger = LogManager.getLogger(ExcelParser.class
			.getName());
	private Workbook workbook;
	private Sheet sheet;
	private Cell cell;
	private Row row;
	private int cellnum;
	private int rownum;
	private int mapCellNum;
	private Row headerRow;
	private Configuration config;
	public static String fileName;

	public ExcelWriter() {
		try {
			config = new PropertiesConfiguration("conf/app.properties");
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void writeFile(List<Customer> customerList, Workbook wb) {
		// get existing workbook and sheet
		this.workbook = wb;
		this.sheet = wb.getSheetAt(0);

		// Start with row Number 3
		rownum = 3;
		// Foreach Customer in CustomerList generate a new row
		for (Customer c : customerList) {

			// get the 3rd row
			row = sheet.getRow(rownum++);

			// create an array of objects including all the properties that are
			// need to be written of a customer object.
			Object[] customerObjectArray = new Object[] {
					c.getWebsite().getUrl(), c.getWebsite().getMetaTags() };

			// Start at cell number 8 -> H
			cellnum = 8;

			// TODO get style of existing cells
			// Cell existingCell =
			// if(sheet.getRow(3).getCell(cellnum) == null){
			// System.out.println("null s");
			// }else{
			// System.out.println("zelleninhalt:"
			// +sheet.getRow(4).getCell(0).getStringCellValue());
			// System.out.println("inhalt der zelle welche style hat: "+cell.getStringCellValue());
			// }

			// iterate thru the customerObjectArray and write them into a new
			// cell
			for (Object obj : customerObjectArray) {

				// Start at cell (3/8 -> 3/H)

				if (obj instanceof URL) {
					// Write header cell
					Cell firstCell = sheet.getRow(2).createCell(cellnum);
					firstCell.setCellValue("URL");
					// create a new Cell at the particular point and write down
					// the value
					cell = sheet.getRow(row.getRowNum()).createCell(cellnum);
					// TODO add cellstyle setting logic here

					cell.setCellValue((String) obj.toString());
				} else if (obj instanceof Map) {
					// Start the writeMap lamda Method to write down the whole
					// Metatags-Map
					((Map) obj).forEach((k, v) -> writeMap(k, v));
				}
				// Iterate the cellnum counting variable to get to the next cell
				cellnum++;
			}
			// reset the counting variable to 0 in order to not shift the
			// alignment
			mapCellNum = 0;
		}

		// Autosize Columns
		for (int i = 0; i < 20; i++)
			sheet.autoSizeColumn(i);

		try {

			// Timestamp Format
			String timestampPattern = config
					.getString("writer.timestampPattern");
			DateTime dt = new DateTime();
			DateTimeFormatter fmt = DateTimeFormat.forPattern(timestampPattern);
			String timestampFormat = fmt.print(dt);

			// Write File
			FileOutputStream out = new FileOutputStream(new File(
					config.getString("writer.file.path") + "/"
							+ config.getString("writer.fileNamePattern") + "_"
							+ timestampFormat + ".xlsx"));

			fileName = config.getString("writer.fileNamePattern") + "_"
					+ timestampFormat + ".xlsx";
			workbook.write(out);
			out.close();
			logger.info("Excel file sucessfully written.");

		} catch (FileNotFoundException e) {
			logger.error(e.getMessage());

		} catch (IOException e) {

			logger.error(e.getMessage());
		}

	}

	/**
	 * This Method is executed for each element in the Metatags Map
	 * 
	 * @param k
	 *            Metatag name
	 * @param v
	 *            Metatag value
	 */
	private void writeMap(Object k, Object v) {

		// write header informations (key)
		Cell firstCell = sheet.getRow(2).createCell(cellnum + mapCellNum);
		firstCell.setCellValue((String) k);
		// write cell values (Value)
		cell = row.createCell(cellnum + mapCellNum);
		cell.setCellValue((String) v);
		mapCellNum++;

	}

	public Workbook getWorkbook() {
		return workbook;
	}
}
