package ch.ice;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.ice.controller.MainController;
import ch.ice.exceptions.InternalFormatException;
import ch.ice.exceptions.MissingCustomerRowsException;

public class Main {
	private static final Logger logger = LogManager.getLogger(Main.class.getName());
	
	public static void main(String[] args) {
		logger.trace("Start Program");
		
		MainController main = new MainController();
		try {
			main.startMainController();
		} catch (InternalFormatException | MissingCustomerRowsException | InterruptedException e) {
			logger.error(e.getMessage());
		}
	}

}
