package eu.alfred.personalization_manager.db_administrator.model;

public enum EmploymentStatus {

	EMPLOYED("Employed"),
	RETIRED("Retired"),
	UNEMPLOYED("Unemployed");
    private String friendlyName;

    private EmploymentStatus(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    @Override
    public String toString() {
        return friendlyName;
    }
}
