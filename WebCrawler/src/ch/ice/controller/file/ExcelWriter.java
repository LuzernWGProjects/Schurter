package ch.ice.controller.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import ch.ice.controller.interf.Parser;
import ch.ice.controller.interf.Writer;
import ch.ice.model.Customer;
import ch.ice.utils.FileOutputNameGenerator;
import ch.ice.view.SaveWindowController;

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
	public static String fileName;
	CellStyle style = null;
	
	@Override
	public void writeFile(List<Customer> customerList, Parser fileParserInstance) throws IOException {
		// convert Parser to actual excelParser. We need getWorkbook() here.
		Parser excelParser = fileParserInstance;

		// get existing workbook and sheet
		this.workbook = excelParser.getWorkbook();
		this.sheet = this.workbook.getSheetAt(0);

		// Start with row Number 3
		rownum = 3;
		// Foreach Customer in CustomerList generate a new row
		for (Customer c : customerList) {
			// initialize cell style (needed for foreground color if unsure)
			style = this.workbook.createCellStyle();
			
			
			// get the 3rd row
			row = sheet.getRow(rownum++);

			// create an array of objects including all the properties that are
			// need to be written of a customer object.
			Object[] customerObjectArray = new Object[] {
					c.getWebsite().getUrl(), c.getWebsite().getMetaTags() };

			// Start at cell number 8 -> H
			cellnum = 8;

			// check if found result is unsure, if yes, set forground colort
			// yellow
			if (c.getWebsite().getUnsure() == true) {
				style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
				style.setFillPattern(CellStyle.SOLID_FOREGROUND);

				row.setRowStyle(style);
			}

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
					cell.setCellStyle(style);
					
					//set style as hyperlink
					 CellStyle hlink_style = workbook.createCellStyle();
				        Font hlink_font = workbook.createFont();
				        hlink_font.setUnderline(Font.U_SINGLE);
				        hlink_font.setColor(IndexedColors.BLUE.getIndex());
				        hlink_style.setFont(hlink_font);
				        hlink_style.setFillForegroundColor(style.getFillForegroundColor());
				        hlink_style.setFillPattern(style.getFillPattern());
				        
					CreationHelper createHelper = this.workbook.getCreationHelper();

				    Hyperlink link = createHelper.createHyperlink(Hyperlink.LINK_URL);
			        link.setAddress(obj.toString());
					cell.setHyperlink(link);
					cell.setCellValue((String) obj.toString());
					cell.setCellStyle(hlink_style);
					
//					Cell UnsureFirstCell = sheet.getRow(2).createCell(cellnum+1);
//					UnsureFirstCell.setCellValue("unsure");
//					cell = sheet.getRow(row.getRowNum()).createCell(cellnum+1);
//					cell.setCellStyle(style);
//					cell.setCellValue(c.getWebsite().getUnsure());
					
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
			// reset style
			style = null;
		}

		// Autosize Columns
		for (int i = 0; i < 20; i++)
			sheet.autoSizeColumn(i);

		try {

			FileOutputStream out = new FileOutputStream(new File(FileOutputNameGenerator.createName()));
			
//			// Timestamp Format
//			String timestampPattern = config
//					.getString("writer.timestampPattern");
//			DateTime dt = new DateTime();
//			DateTimeFormatter fmt = DateTimeFormat.forPattern(timestampPattern);
//			String timestampFormat = fmt.print(dt);
//
//			// Write File
//			FileOutputStream out = new FileOutputStream(new File(
//					config.getString("writer.file.path") + "/"
//							+ config.getString("writer.fileNamePattern") + "_"
//							+ timestampFormat + ".xlsx"));

//			fileName = config.getString("writer.fileNamePattern") + "_"
//					+ timestampFormat + ".xlsx";
			workbook.write(out);
			out.close();
			logger.info("Excel file sucessfully written.");
			SaveWindowController.myBoo = true;

		} catch (FileNotFoundException e) {
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
		cell.setCellStyle(style);
		mapCellNum++;

	}

	public Workbook getWorkbook() {
		return workbook;
	}
}
