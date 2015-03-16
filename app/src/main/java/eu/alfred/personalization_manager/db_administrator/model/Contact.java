package eu.alfred.personalization_manager.db_administrator.model;

import java.util.Date;
import java.util.UUID;

public class Contact {
	
	private String id;
	private String userID;
	private String firstName;
	private String middleName;
	private String lastName;
	private String prefferedName;
	private Gender gender;
	private Date dateOfBirth;
	private String phone;
	private String mobilePhone;
	private String email;
	private Address residentialAddress;
	private Address postalAddress;
	private String citizenship;
	private String nationality;
	private Language language;
	private Relation[] relationToUser;
	private AccessLevels accessLevels;
	private String[] socialMediaProfiles;
	
	public Contact() {
		setId(); 
	}
			
	public String getId() {
		return id;
	}
	// For cases we create id for the contact
	public String setId() {
		this.id =  "alfred-user-contact-"+UUID.randomUUID().toString(); 
		
		return this.id; // When creating a new contact we provide the id back so they assign them to the users
	}	
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPrefferedName() {
		return prefferedName;
	}
	public void setPrefferedName(String prefferedName) {
		this.prefferedName = prefferedName;
	}
	public Gender getGender() {
		return gender;
	}
	public void setGender(Gender gender) {
		this.gender = gender;
	}
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Address getResidentialAddress() {
		return residentialAddress;
	}
	public void setResidentialAddress(Address residentialAddress) {
		this.residentialAddress = residentialAddress;
	}
	public Address getPostalAddress() {
		return postalAddress;
	}
	public void setPostalAddress(Address postalAddress) {
		this.postalAddress = postalAddress;
	}
	public String getCitizenship() {
		return citizenship;
	}
	public void setCitizenship(String citizenship) {
		this.citizenship = citizenship;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public Language getLanguage() {
		return language;
	}
	public void setLanguage(Language language) {
		this.language = language;
	}
	public Relation[] getRelationToUser() {
		return relationToUser;
	}
	public void setRelationToUser(Relation[] relationToUser) {
		this.relationToUser = relationToUser;
	}
	public String[] getSocialMediaProfiles() {
		return socialMediaProfiles;
	}
	public void setSocialMediaProfiles(String[] socialMediaProfiles) {
		this.socialMediaProfiles = socialMediaProfiles;
	}

	public AccessLevels getAccessLevels() {
		return accessLevels;
	}

	public void setAccessLevels(AccessLevels accessLevels) {
		this.accessLevels = accessLevels;
	}
	
	@Override
	public String toString() {
		return "id: " + this.getId() + ", first-name: " + this.getFirstName() + "...";
	}

	

}
