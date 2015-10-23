package ch.ice.model;

/**
 * @author Marco
 *
 */
public class Customer {
	
	private String id;
	private String fullName;
	private String shortName;
	private String countryCode;
	private String countryName;
	private String zipCode;
	private Website website;
	private String industryCode;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getFullName() {
		return fullName;
	}
	
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	public String getShortName() {
		return shortName;
	}
	
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	
	public String getCountryCode() {
		return countryCode;
	}
	
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	
	public String getCountryName() {
		return countryName;
	}
	
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	
	public String getZipCode() {
		return zipCode;
	}
	
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	
	public Website getWebsite() {
		return website;
	}
	
	public void setWebsite(Website website) {
		this.website = website;
	}
	
	public String getIndustry(){
		return industryCode;
	}
	
	public void setIndustry(String industryCode){
		this.industryCode = industryCode;
	}
}
