package ch.ice.controller.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.ice.controller.interf.Parser;
import ch.ice.model.Customer;
import ch.ice.model.Website;

public class CSVParser implements Parser{
	private static final Logger logger = LogManager.getLogger(CSVParser.class.getName());

	private static org.apache.commons.csv.CSVParser parser;

	private File CSVFileToRead;
	private int physicalRowCount;
	private int currentRowCount;

	// file internals
	private List<String> headerInfos = new ArrayList<String>();

	private List<Customer> customerList = new ArrayList<Customer>();


	@Override
	public List<Customer> readFile(File file) throws IOException {
		// set file access to private
		this.CSVFileToRead = file;
		return this.readFile();
	}


	/**
	 * Read customer from CSV File
	 * @return List<Customer> customer list
	 * @throws IOException
	 */
	private List<Customer> readFile() throws IOException {
		Reader in = new BufferedReader(new FileReader(this.CSVFileToRead));

		CSVParser.parser = CSVFormat.DEFAULT.parse(in);

		List<CSVRecord> records = CSVParser.parser.getRecords();

		this.setTotalDataSets(records.size() - 3);

		for (int i = 0; i < records.size(); i++) {
			CSVRecord record = records.get(i);

			// skip unused rows
			if(record.getRecordNumber() <= 2) continue;


			// gather csv headers
			if(record.getRecordNumber() == 3){
				String headerRecord = record.get(0);

				String[] headerValues = headerRecord.split(";(?=([^\"]*\"[^\"]*\")*[^\"]*$)", 8);
				//System.out.println(headerRecord);

				for (String cellValue : headerValues) {
					//System.out.println("value: "+cellValue);
					this.headerInfos.add(cellValue);
				}

				continue;
			}

			// retrieve all customer infos
			String customerRecord = record.get(0);
			String[] customerValues = customerRecord.split(";(?=([^\"]*\"[^\"]*\")*[^\"]*$)", 8);

			// create customer
			Customer c = new Customer();
			c.setId(customerValues[0]);
			c.setCountryCode(customerValues[1]);
			c.setCountryName(customerValues[2]);
			c.setZipCode(customerValues[3]);
			c.setShortName(customerValues[4]);
			c.setFullName(customerValues[6]);

			c.setWebsite(new Website());


			this.customerList.add(c);
		}

		logger.info("Rendered Customers from CSV File ("+this.CSVFileToRead.getName()+"): "+this.customerList.size());

		return this.customerList;
	}

	/**
	 * Return all collected Headercells from File. Will be used for creating new
	 * file with the correct Headers
	 * 
	 * @return Value in header cells
	 */
	public List<String> getCellHeaders() {
		return this.headerInfos;
	}


	@Override
	public void setTotalDataSets(int totalRows) {
		this.physicalRowCount = totalRows;
	}

	@Override
	public int getTotalDataSets() {
		return this.physicalRowCount;
	}

	@Override
	public void setCurrentRow(int currentRowNumber) {
		this.currentRowCount = currentRowNumber;
	}

	@Override
	public int getCurrentRow() {
		return this.currentRowCount;
	}
}