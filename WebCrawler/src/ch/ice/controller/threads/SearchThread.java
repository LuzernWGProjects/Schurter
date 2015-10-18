package ch.ice.controller.threads;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import ch.ice.controller.MainController;
import ch.ice.controller.web.WebCrawler;
import ch.ice.exceptions.HttpStatusException;
import ch.ice.model.Customer;
import ch.ice.view.SaveWindowController;

public class SearchThread extends Thread {

	// public SearchThread(List<Customer> firstArray) {
	// // TODO Auto-generated constructor stub
	// this.searchList = (CopyOnWriteArrayList<Customer>) firstArray;
	// System.out.println("Object Created");
	//
	// }

	private List<Customer> searchList;
	private int checkNumber;

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
		MainController.customersEnhanced = 0;
		SaveWindowController.d = 0.0;
		while (!Thread.currentThread().isInterrupted()) {

			System.out.println("Were in!");

			WebCrawler wc = new WebCrawler();
			MainController mc = new MainController();
			if (checkNumber == 1) {
				System.out.println("Thread 1 running");

			}
			if (checkNumber == 2) {
				System.out.println("Thread 2 running");
			}
			if (checkNumber == 3) {
				System.out.println("Thread 3 running");
			}
			if (checkNumber == 4) {
				System.out.println("Thread 4 running");
			}

			for (Customer customer : searchList) {
				MainController.customersEnhanced++;
				System.out.println("In Customer Loop");

				// only search via SearchEngine if search is enabled. Disable
				// search
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
					MainController.logger
							.info(customer.getWebsite().toString());
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
			return;
		}
		return;
	}
}
