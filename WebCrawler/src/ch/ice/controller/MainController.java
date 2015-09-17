/**
 * 
 */
package ch.ice.controller;

/**
 * @author Oliver
 *
 */
public class MainController {

	
	public static void startMainController()
	{
		
		startSearch();
	}
	
	public static void startSearch(){
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
