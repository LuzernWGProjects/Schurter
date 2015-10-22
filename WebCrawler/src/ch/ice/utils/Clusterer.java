package ch.ice.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.ice.model.Customer;

/**
 * Provide a clustering option for the gathered data.<br />
 * It tries to associate an Industry to a customer based <br />
 * on given keywords.
 * 
 * @author mneuhaus
 *
 */
public class Clusterer {
	public static final Logger logger = LogManager.getLogger(Clusterer.class.getName());

	/**
	 * Map containing all Industries with associated Keywords
	 */
	private static Map<String, Set<String>> keywordList2Industry = XMLParser.getWordsPerIndustry("conf/industryLists.xml");

	
	/**
	 * Tries to categorize the given customer with an industry<br />
	 * This function tries to gather all occurences in a given String (MetaTag)<br />
	 * and then associate the industry to the customer.
	 * 
	 * @param customerList
	 * @return
	 */
	public static List<Customer> cluster(List<Customer> customerList) {
		int i = 0;
		Map<String, Integer> candidate = new HashMap<String, Integer>();

		// iterate over every customer object
		for (Customer customer : customerList) {
			logger.debug("Cluster Customer[Nr. "+i+"]: "+customer.getFullName());

			Map<String, String> gatheredMetaTags = customer.getWebsite().getMetaTags();
			
			// if no metatags are available, skip the whole thing to save computing power
			if(gatheredMetaTags == null)
				continue;

			logger.debug("Gathered MetaTags: "+gatheredMetaTags);
			
			// run trhough every selected meta tag
			for(Entry<String, String> metaTagObject: gatheredMetaTags.entrySet()){
				logger.debug("---- Check for metatagobject: "+ metaTagObject.getValue());

				String metaTag = metaTagObject.getValue();
				
				// if no metatags are found, skip the whole thing to save computing power
				if(metaTag.equals("n/a"))	
					continue;

				int frequency = 0;
				logger.debug("---- frequency = "+frequency);

				// run trough every keywordlist defined in the xml
				for(Entry<String, Set<String>> keywordList: keywordList2Industry.entrySet()){
					
					logger.debug("-------- Render keywordLIst: "+keywordList.getKey());
					
					String industryName = keywordList.getKey();
					
					// match every keyword in the given industry list with the given metatag
					for (String keyWord : keywordList.getValue()) {
						logger.debug("------------ render keyword: "+ keyWord);

						Pattern p = Pattern.compile(keyWord, Pattern.UNICODE_CASE);
						Matcher m = p.matcher(metaTag);
						
						// count each occurence of the keyword in the string
						while(m.find()){
							frequency++;
							logger.debug("------------ frequency = "+frequency);
						}
					}

					logger.debug("-------- put "+industryName+" with frequency = "+frequency+" into candidate array");
					
					// set all available candidates that could be a potential match
					candidate.put(industryName, frequency);
					frequency = 0;
				}
			}

			logger.debug("Available candidates for this customer: "+candidate);
			
			// check which industry had the most hits.
			List<String> highestIndustry = getIndustryWithHighestScore(candidate);
			
			candidate.clear();
			logger.debug("Selected Industry = "+highestIndustry.toString());
			
			customer.setIndustry(highestIndustry.toString());
			
			i++;
		}

		return customerList;
	}

	
	/**
	 * check which industry candidate has the most hits.<br />
	 * If two industries are on par, then both of them are <br />
	 * considered a possible industry.
	 * 
	 * @param candidate	the calculated possible industries
	 * @return List<String>	of Industries that are the most likley to match.
	 */
	private static List<String> getIndustryWithHighestScore(Map<String, Integer> candidate) {
		int score = 0;
		String name ="Unable to categorize";
		List<String> industryNames = new ArrayList<String>();

		// if there are no candidates, or null then return default name
		if(candidate.size() <= 0) {
			industryNames.add(name);

			return industryNames;
		}
		
		
		for(Entry<String, Integer> entry: candidate.entrySet()){
			
			int currentScore = entry.getValue();
			
			/*
			 * if there is a higher score than the last one,
			 * clear the list and put the actual one on top
			 */
			if(currentScore > score) {
				score = entry.getValue();
				name = entry.getKey();

				// delete everything because new top candidate
				industryNames.clear();
				industryNames.add(name);
			}
			
			/*
			 * if the current score ist the same as
			 * the actual score, add it to the
			 * possible candidates.
			 * In this case n industries can be possible
			 */
			else if (currentScore == score){
				score = currentScore;

				if(score != 0){
					name = entry.getKey();
					industryNames.add(name);
				} else {
					industryNames.clear();
					industryNames.add(name);
				}
			}
		}

		return industryNames;
	}
}