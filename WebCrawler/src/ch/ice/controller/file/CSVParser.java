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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setTotalDataSets(int totalRows) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getTotalDataSets() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setCurrentRow(int currentRowNumber) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getCurrentRow() {
		// TODO Auto-generated method stub
		return 0;
	}
}