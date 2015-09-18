package ch.ice.controller.interf;

import java.util.List;


public interface Writer {
	
	/**
	 *  Writes List zu File
	 * @param <T>
	 * @param list
	 */
	public <T> void writeFile(List<T> list);

}
