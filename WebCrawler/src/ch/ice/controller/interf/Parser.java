
package ch.ice.controller.interf;

import java.io.File;
import java.io.IOException;

/**
 * 
 * @author mneuhaus
 *
 */
public interface Parser {
	
	// input can either be a Excel File or a CSV file
	public void readFile(File file) throws IOException;
		
}
