package ch.ice.controller.interf;

import java.util.List;

import org.json.JSONArray;

public interface SearchEngine {
	
	public static JSONArray search(String requestedQuery, int limitSearchResult) {
		return null;
	}
	
	public static String buildQuery(List<String> params){
		return "";
	}
}
