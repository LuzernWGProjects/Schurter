package ch.ice.controller.interf;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import ch.ice.exceptions.NoUrlFoundException;

public interface SearchEngine {
	public JSONArray search(String requestedQuery, int limitSearchResult) throws IOException, NoUrlFoundException;
	public JSONArray search(String requestedQuery, int limitSearchResult, String countryCode) throws IOException, NoUrlFoundException;

	public String buildQuery(List<String> params);
	
	public JSONArray standardizer(JSONArray results, Map<String,String> keyNodeMap);
}
