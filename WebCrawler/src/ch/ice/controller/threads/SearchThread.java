package ch.ice.controller.threads;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import ch.ice.controller.MainController;
import ch.ice.controller.web.WebCrawler;
import ch.ice.exceptions.HttpStatusException;
import ch.ice.model.Customer;

public class SearchThread extends Thread {

	// public SearchThread(List<Customer> firstArray) {
	// // TODO Auto-generated constructor stub
	// this.searchList = (CopyOnWriteArrayList<Customer>) firstArray;
	// System.out.println("Object Created");
	//
	// }

	List<Customer> searchList;
	int checkNumber;

	public List<Customer> getSearchList() {
		return searchList;
	}

	public void setSearchList(List<Customer> searchList) {
		this.searchList = searchList;
	}

	public int getCheckNumber() {
		return checkNumber;
	}

	public void setCheckNumber(int checkNumber) {
		this.checkNumber = checkNumber;
	}

	@Override
	public void run() {
		System.out.println("Were in!");

		WebCrawler wc = new WebCrawler();
		MainController mc = new MainController();
		if (checkNumber == 1) {
			searchList = MainController.firstArray;
		}
		if (checkNumber == 2) {
			searchList = MainController.secondArray;
		}
		if (checkNumber == 3) {
			searchList = MainController.thirdArray;
		}
		if (checkNumber == 4) {
			searchList = MainController.fourthArray;
		}

		for (Customer customer : searchList) {
			MainController.i++;

			// only search via SearchEngine if search is enabled. Disable search
			// for testing purpose
			if (MainController.isSearchAvail) {
				// Add url for customer
				try {
					URL retrivedUrl = mc.searchForUrl(customer);
					customer.getWebsite().setUrl(retrivedUrl);

					MainController.progressText = "Gathering data at: "
							+ retrivedUrl.toString();
				} catch (Exception e) {
					e.printStackTrace();
					MainController.logger.error(e.getMessage());
				}

			} else {
				customer.getWebsite().setUrl(MainController.defaultUrl);
			}

			// add metadata
			try {
				wc.connnect(customer.getWebsite().getUrl().toString());
				customer.getWebsite().setMetaTags(
						wc.getMetaTags(MainController.metaTagElements));
				MainController.logger.info(customer.getWebsite().toString());
			} catch (IOException e) {
				e.printStackTrace();
				MainController.logger.error(e.getMessage());

			} catch (HttpStatusException e) {
				e.printStackTrace();
				MainController.logger.error(e.getMessage());

			} catch (Exception e) {
				e.printStackTrace();
				MainController.logger.error(e.getMessage());

			}

		}

		System.out.println("Ended");

	}
}
