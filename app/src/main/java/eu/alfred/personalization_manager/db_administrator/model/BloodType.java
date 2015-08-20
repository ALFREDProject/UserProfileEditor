package eu.alfred.personalization_manager.db_administrator.model;

public enum BloodType {

	A_POSITIVE("A+"),
	A_NEGATIVE("A-"),
	B_POSITIVE("B+"),
	B_NEGATIVE("B-"),
	AB_POSITIVE("AB+"),
	AB_NEGATIVE("AB-"),
	O_POSITIVE("O+"),
	O_NEGATIVE("O-");
    private String friendlyName;

    private BloodType(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    @Override
    public String toString() {
        return friendlyName;
    }


	
}
