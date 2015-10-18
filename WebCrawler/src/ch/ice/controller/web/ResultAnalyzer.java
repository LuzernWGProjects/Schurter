package ch.ice.controller.web;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import ch.ice.controller.MainController;
import ch.ice.utils.Config;
import ch.ice.utils.JSONStandardizedKeys;

/**
 * @author Oliver
 *
 */
public class ResultAnalyzer {

	static URI uri;
	private static final Logger logger = LogManager.getLogger(MainController.class.getName());

	/**
	 * Method to analyze which received result suits the best
	 * 
	 * @param results
	 *            List of received results
	 * @param parameters
	 *            Search parameters to compare with the results
	 * @return Result which suits the best (as JSONObject)
	 *
	 */
	public static JSONObject analyse(JSONArray results, List<String> parameters) {
		logger.info("Analyze new results: "+results.toString());

		double[] candidatesArray;

		// initlayze candiatesArray for decision making
		candidatesArray = new double[results.length()];

		for (int y = 0; y < candidatesArray.length; y++) {
			candidatesArray[y] = 0;
		}

		candidatesArray[0] = 2;

		// Go thru each received result of the search request
		for (int i = 0; i < results.length(); i++) {

			// create a new temporary JSONObject for each result
			JSONObject singleResult = results.getJSONObject(i);
			results.getJSONObject(i).put("Unsure", false);

			// extract the url, title and the desc
			String url = (String) singleResult.get(JSONStandardizedKeys.URL);

			/* ******************************************
			 * Pre-processing for further analysis
			 * *******************************************
			 */
			try {
				uri = new URI(url);
			} catch (URISyntaxException e) {
				logger.error(e.getMessage());
			}

			String host = uri.getHost().replaceFirst("^(http://|http://www\\.|www\\.)", "");

			// store name of company in a variable to work with later on
			String companyName = (String) parameters.get(0);
			String companyNameNoWhitespace = (String) parameters.get(0).replaceAll("\\s+", "");

			// spilt single words of the name
			String[] singleWords = companyName.split("\\s+");

			for (int j = 0; j < singleWords.length; j++) {
				singleWords[j] = singleWords[j].replaceAll("[^\\w]", "");
			}

			// split url in single words
			String strippedURL = host.replace("-", " ").replaceAll("[^\\w]"," ");
			String strippedURLNoWhitespace = host.replace("-", "").replaceAll("[^\\w]",	"");
			String[] singleURLWords = strippedURL.split("\\s+");

			// create acronym of company name
			String acroOfCompanyName = createAcronym(companyName);

			/* *******************************************
			 * Start the basics("killer criteria") checks
			 * *******************************************
			 */

			// if the url contains the full company name return this as the best
			// result
			if (host.contains(companyName)) {
				logger.info("URL["+url+"] contains Name["+parameters.get(0)+"]");

				return singleResult;
			}

			if(strippedURLNoWhitespace.contains(companyNameNoWhitespace)){
				logger.info("URL["+url+"] contains Name["+parameters.get(0)+"] without Whitespaces");
				return singleResult;
			}

			if(host.contains(companyNameNoWhitespace)) {
				logger.info("URL["+url+"] contains FullName["+parameters.get(0)+"]");
				return singleResult;
			}

			// if the url is not on the blacklist, do further checks; otherwise
			// this result will have a 0.0 score
			if (!isBlacklist(host)) {
				/* *******************************************
				 * Do further checks
				 * *******************************************
				 */

				// is first word in company name equal than first in url?
				if (singleWords[0].equals(singleURLWords[0])) {
					candidatesArray[i] = candidatesArray[i] + (5);
					logger.info("First word of CopanyName is in URL["+url+"]; Add 5 units to total score");
				}

				// does the url contains the first word of the company name?
				if (host.contains(singleWords[0])) {
					candidatesArray[i] = candidatesArray[i] + (2);
					logger.info("URL["+url+"] contains first word of the ComapnyName; Add 2 units to total score");
				}

				// if the company name contains more than 1 word...
				if (singleWords.length > 1) { // does the url any other word of

					for (int j = 0; j < singleWords.length - 1; j++) {
						if (host.contains(singleWords[j])) {
							candidatesArray[i] = candidatesArray[i] + (0.5);
							logger.info("URL["+url+"] contains \""+singleWords[j]+"\"; Add 0.5 units to the total score");
						}
					}
				}

				// does the url contains the acronym of the company?
				if (host.equals((CharSequence) acroOfCompanyName)) {
					candidatesArray[i] = candidatesArray[i] + (5);
					logger.info("Acronym \""+acroOfCompanyName+"\" is equal as URL["+url+"]; Add 5 units to total score");
				}

				// check if unsure (the critical value is 0.5 since if is less
				// than 0.5, the first word is not contained in the url)
				if (candidatesArray[i] <= 4.5) {
					results.getJSONObject(i).put("Unsure", true);
				}

			} else {
				results.getJSONObject(i).put("Unsure", true);
				logger.warn("URL["+url+"] is beeing Blacklisted");
			}
			logger.info("URL["+url+"] has a total core of "+ candidatesArray[i]);
		}

		// determine the best result
		double max = candidatesArray[0];

		for (int counter = 1; counter < candidatesArray.length; counter++) {
			if (candidatesArray[counter] > max) {
				max = candidatesArray[counter];
			}
		}
		// return the object which is the best score
		for (int l = 0; l < candidatesArray.length; l++) {
			if (candidatesArray[l] == max) {
				logger.info("Highest Score is "+candidatesArray[1]);
				return results.getJSONObject(l);
			}
		}

		// if neither the name nor the acronym of the name is contained in the
		// url, return the first result
		return null;
	}

	/**
	 * Creates an Acronym of the given string using the first letter of each
	 * word
	 * 
	 * @param name
	 *            String to generate Acronym
	 * @return Acronym as String
	 */
	private static String createAcronym(String name) {
		String acro = "";
		String[] words = name.split(" ");

		for (String word : words) {
			acro += word.charAt(0);
		}

		return acro;
	}

	/**
	 * Checks if the host in url is one of the blacklist which is defined in the
	 * propertiesfile as searchEngine.bing.blacklist
	 * 
	 * @param host
	 *            to check if blacklisted
	 * @return <true> if is blacklisted <false> if is not blacklisted
	 */
	private static boolean isBlacklist(String host) {
		// Create array with blacklisted hosts
		List<String> blacklist = new ArrayList<String>();
		
		// Get Blacklist form propertiesfile
		PropertiesConfiguration config = Config.PROPERTIES;
		
		blacklist = Arrays.asList(config.getStringArray("searchEngine.bing.blacklist"));


		// Check if one element form the blacklist equals the regarding host..
		for (String blacklistElement : blacklist) {
			if (host.contains(blacklistElement)) {
				return true;
			}

		}

		return false;
	}
}
