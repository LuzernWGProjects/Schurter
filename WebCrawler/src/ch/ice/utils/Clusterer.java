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

public class Clusterer {
	public static final Logger logger = LogManager.getLogger(Clusterer.class.getName());

	private static Map<String, Set<String>> keywordList2Industry = XMLParser.getWordsPerIndustry();

	public static List<Customer> cluster(List<Customer> customerList) {
		int i = 0;
		Map<String, Integer> candidate = new HashMap<String, Integer>();

		// iterate over every customer object
		for (Customer customer : customerList) {
			logger.debug("Cluster Customer[Nr. "+i+"]: "+customer.getFullName());

			Map<String, String> gatheredMetaTags = customer.getWebsite().getMetaTags();

			if(gatheredMetaTags == null)
				continue;

			logger.debug("Gathered MetaTags: "+gatheredMetaTags);
			// run trhough every selected meta tag
			for(Entry<String, String> metaTagObject: gatheredMetaTags.entrySet()){
				logger.debug("---- Check for metatagobject: "+ metaTagObject.getValue());

				String metaTag = metaTagObject.getValue();

				if(metaTag.equals("n/a"))	
					continue;

				int frequency = 0;
				logger.debug("---- frequency = "+frequency);

				// run trough every keywordlist defined in the xml
				for(Entry<String, Set<String>> keywordList: keywordList2Industry.entrySet()){
					logger.debug("-------- Render keywordLIst: "+keywordList.getKey());
					String industryName = keywordList.getKey();

					for (String keyWord : keywordList.getValue()) {
						logger.debug("------------ render keyword: "+ keyWord);

						Pattern p = Pattern.compile(keyWord, Pattern.UNICODE_CASE);
						Matcher m = p.matcher(metaTag);

						while(m.find()){
							frequency++;
							logger.debug("------------ frequency = "+frequency);
						}
					}

					logger.debug("-------- put "+industryName+" with frequency = "+frequency+" into candidate array");
					candidate.put(industryName, frequency);
					frequency = 0;
				}
			}

			logger.debug("Available candidates for this customer: "+candidate);

			List<String> highestIndustry = getIndustryWithHighestScore(candidate);
			candidate.clear();
			logger.debug("Selected Industry = "+highestIndustry.toString());

			customer.setIndustry(highestIndustry.toString());
			
			i++;
		}

		return customerList;
	}


	private static List<String> getIndustryWithHighestScore(Map<String, Integer> candidate) {
		int score = 0;
		String name ="Unable to categorize";
		List<String> industryNames = new ArrayList<String>();

		if(candidate.size() <= 0) {
			industryNames.add(name);

			return industryNames;
		}

		for(Entry<String, Integer> entry: candidate.entrySet()){

			int currentScore = entry.getValue();

			if(currentScore > score) {
				score = entry.getValue();
				name = entry.getKey();

				// delete everything because new top candidate
				industryNames.clear();
				industryNames.add(name);
			}

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