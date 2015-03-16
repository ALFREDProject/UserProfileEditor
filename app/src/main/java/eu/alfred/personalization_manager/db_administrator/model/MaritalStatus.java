package eu.alfred.personalization_manager.db_administrator.model;

public enum MaritalStatus {

	MARRIED("Married"),
	SINGLE("Single"),
	WIDOW_WIDOWER("Widow/Widower");
    private String friendlyName;

    private MaritalStatus(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    @Override
    public String toString() {
        return friendlyName;
    }


}
