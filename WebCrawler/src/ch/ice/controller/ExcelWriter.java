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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

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
	private Row firstRow;

	@Override
	public void writeFile(List<Customer> customerList, Workbook wb) {
		this.workbook = wb;
		this.sheet = wb.getSheetAt(0);
		firstRow = sheet.createRow(0);

		int rownum = 1;

		for (Customer c : customerList) {
			row = sheet.createRow(rownum++);

			Object[] cObject = new Object[] {c.getFullName(),c.getCountryName(), c.getZipCode(), c.getWebsite().getUrl(), c.getWebsite().getMetaTags()};

			cellnum = 0;
			for (Object obj :cObject) {

				cell = row.createCell(cellnum++);


				if(obj instanceof String){
					cell.setCellValue((String)obj);
				}
				else if (obj instanceof URL)
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
			FileOutputStream out = new FileOutputStream(new File("enrichedPOSFile.xlsx"));
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

		Cell firstCell = firstRow.createCell(cellnum+mapCellNum);
		firstCell.setCellValue((String)k);
		cell = row.createCell(cellnum+mapCellNum);
		cell.setCellValue((String)v);
		mapCellNum++;

	}

	public Workbook getWorkbook() {
		return workbook;
	}


	public Sheet getSheet() {
		return sheet;
	}
}
