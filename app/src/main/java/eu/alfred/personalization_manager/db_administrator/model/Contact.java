package eu.alfred.personalization_manager.db_administrator.model;

import java.util.Date;
import java.util.HashMap;

public class Contact {

	private String id;
	private String alfredUserName; // contacts can be alfred users, we point to this user
    private String contactsAlfredId;
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
	private Relation[] relationToUser;
	private AccessLevels accessLevels;
	private String[] socialMediaProfiles;
    private HashMap<String,Boolean> accessRightsToAttributes;
			
	public String getId() {
		return id;
	}
    public void setId(String id) {
        this.id = id;
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

    public HashMap<String, Boolean> getAccessRightsToAttributes() {
        if (accessRightsToAttributes == null) {
            accessRightsToAttributes = new HashMap<String, Boolean>();
        }
        return accessRightsToAttributes;
    }

    public void setAccessRightsToAttributes(
			HashMap<String, Boolean> accessRightsToAttributes) {
        this.accessRightsToAttributes = accessRightsToAttributes;
    }

    @Override
	public String toString() {
		return "id: " + this.getId() + ", first-name: " + this.getFirstName() + "...";
	}

	public String getAlfredUserName() {
		return alfredUserName;
	}

	public void setAlfredUserName(String alfredUserName) {
		this.alfredUserName = alfredUserName;
	}

    public String getContactsAlfredId() {
        return contactsAlfredId;
    }

    public void setContactsAlfredId(String contactsAlfredId) {
        this.contactsAlfredId = contactsAlfredId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((dateOfBirth == null) ? 0 : dateOfBirth.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Contact other = (Contact) obj;
        if (dateOfBirth == null) {
            if (other.dateOfBirth != null)
                return false;
        } else if (!dateOfBirth.equals(other.dateOfBirth))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
