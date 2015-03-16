package eu.alfred.personalization_manager.db_administrator.model;

public class Address {
	
	private String street;
	private String number;
	private String postalCode;
	private String city;
	private String state;
	private String country;
		
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	
	public boolean isEmpty() {
        return street == null || "".equals(street) ||
        number == null || "".equals(street) ||
        postalCode == null || "".equals(street) ||
        city == null || "".equals(street) ||
        state == null || "".equals(street) ||
        country == null || "".equals(street);
    }
}
