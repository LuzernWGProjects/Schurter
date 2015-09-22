package ch.ice;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.ice.controller.MainController;

/**
 * @author Oliver
 *
 */
public class Main {
	private static final Logger logger = LogManager.getLogger(Main.class.getName());
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		logger.trace("Start Program");
		
		MainController main = new MainController();
		main.startMainController();
	}

}
