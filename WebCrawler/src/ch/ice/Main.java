/**
 * 
 */
package ch.ice;

import ch.ice.controller.BingSearchEngine;

/**
 * @author Oliver
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("start test bing");
		
		String query = "schurter luzern";
		try {
			BingSearchEngine.Search(query);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
