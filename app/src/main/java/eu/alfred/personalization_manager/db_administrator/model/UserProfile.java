package eu.alfred.personalization_manager.db_administrator.model;

import java.util.Date;

public class UserProfile {

    //Main section
	private String id;
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
	private String socialSecurityNumber;
	private MaritalStatus maritalStatus;
	private EducationLevel educationLevel;
	private EmploymentStatus employmentStatus; 
	private String healthInsurance;
	private String profession;
	private Date anniversaryDate;
	private Contact nextOfKin;
	private MyersBriggsTypeIndicator myersBriggsIndicator;
	private String[] selfDescrPersonalityChar;
	private String[] interests;
	private String[] culturalOrFamilyNeeds;
	private String[] socialMediaProfiles;
	private Date alfedAppInstalationDate;   
	
	public String getId() {
		return id;
	}
	public void setId(String id) { // Change it to create id's with UUID that start with alfred-user-...
		this.id = id;
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
	public String getSocialSecurityNumber() {
		return socialSecurityNumber;
	}
	public void setSocialSecurityNumber(String socialSecurityNumber) {
		this.socialSecurityNumber = socialSecurityNumber;
	}
	public MaritalStatus getMaritalStatus() {
		return maritalStatus;
	}
	public void setMaritalStatus(MaritalStatus maritalStatus) {
		this.maritalStatus = maritalStatus;
	}
	public EducationLevel getEducationLevel() {
		return educationLevel;
	}
	public void setEducationLevel(EducationLevel educationLevel) {
		this.educationLevel = educationLevel;
	}
	public EmploymentStatus getEmploymentStatus() {
		return employmentStatus;
	}
	public void setEmploymentStatus(EmploymentStatus employmentStatus) {
		this.employmentStatus = employmentStatus;
	}
	public String getHealthInsurance() {
		return healthInsurance;
	}
	public void setHealthInsurance(String healthInsurance) {
		this.healthInsurance = healthInsurance;
	}
	public String getProfession() {
		return profession;
	}
	public void setProfession(String profession) {
		this.profession = profession;
	}
    //TODO Fix (same problem we had with dateOfBirth)
	public Date getAnniversaryDate() {
		return anniversaryDate;
	}
	public void setAnniversaryDate(Date anniversaryDate) {
		this.anniversaryDate = anniversaryDate;
	}
	//TODO [UserProfile] Select Contact as Next Of Kin
    public Contact getNextOfKin() {
		return nextOfKin;
	}
	public void setNextOfKin(Contact nextOfKin) {
		this.nextOfKin = nextOfKin;
	}
	public MyersBriggsTypeIndicator getMyersBriggsIndicator() {
		return myersBriggsIndicator;
	}
	public void setMyersBriggsIndicator(
            MyersBriggsTypeIndicator myersBriggsIndicator) {
		this.myersBriggsIndicator = myersBriggsIndicator;
	}
    //TODO [UserProfile] SelfDescrPersonalityChar
	public String[] getSelfDescrPersonalityChar() {
		return selfDescrPersonalityChar;
	}
	public void setSelfDescrPersonalityChar(String[] selfDescrPersonalityChar) {
		this.selfDescrPersonalityChar = selfDescrPersonalityChar;
	}
    //TODO [UserProfile] Interests
	public String[] getInterests() {
		return interests;
	}
	public void setInterests(String[] interests) {
		this.interests = interests;
	}
    //TODO [UserProfile] CulturalOrFamilyNeeds
	public String[] getCulturalOrFamilyNeeds() {
		return culturalOrFamilyNeeds;
	}
	public void setCulturalOrFamilyNeeds(String[] culturalOrFamilyNeeds) {
		this.culturalOrFamilyNeeds = culturalOrFamilyNeeds;
	}
    //TODO [UserProfile] SocialMediaProfiles
	public String[] getSocialMediaProfiles() {
		return socialMediaProfiles;
	}
	public void setSocialMediaProfiles(String[] socialMediaProfiles) {
		this.socialMediaProfiles = socialMediaProfiles;
	}
    //TODO [UserProfile] AlfedAppInstalationDate
	public Date getAlfedAppInstalationDate() {
		return alfedAppInstalationDate;
	}
	public void setAlfedAppInstalationDate(Date alfedAppInstalationDate) {
		this.alfedAppInstalationDate = alfedAppInstalationDate;
	}

	@Override
	public String toString() {
        return "id: " + getId() + ", first-name: " + getFirstName() + ", last-name: " + getLastName() + ", date of birth: " + getDateOfBirth() + "...";
	}

}
