package ch.ice.controller.file;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import ch.ice.controller.interf.Parser;
import ch.ice.exceptions.IllegalFileExtensionException;
import ch.ice.model.Customer;

public class CSVParser implements Parser {

	@Override
	public LinkedList<Customer> readFile(File file) throws IOException, IllegalFileExtensionException {
		return null;
	}

	@Override
	public void setTotalDataSets(int totalRows) {
		
	}

	@Override
	public int getTotalDataSets() {
		
		return 0;
	}

	@Override
	public void setCurrentRow(int currentRowNumber) {
		
	}

	@Override
	public int getCurrentRow() {
		
		return 0;
	}
}
