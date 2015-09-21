package ch.ice.controller.interf;

import java.util.List;

import ch.ice.model.Customer;


public interface Writer {
	
	/**
	 *  Writes List zu File
	 *
	 * @param list
	 */
	public  void writeFile(List<Customer> customerList, List<String> cellHeaders);

}
