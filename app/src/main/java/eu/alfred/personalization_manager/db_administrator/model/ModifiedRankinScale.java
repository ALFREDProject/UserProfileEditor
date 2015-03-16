package eu.alfred.personalization_manager.db_administrator.model;

public enum ModifiedRankinScale {

	NO_SYMPTOMS("No symptoms"),
	NO_SIGNIFICANT_DISABILITY("No significant disability"),
	SLIGHT_DISABILITY("Slight disability"),
	MODERATE_DISABILITY("Moderate disability"),
	MODERATELY_SEVERE_DISABILITY("Moderately severe disability"),
	SEVERE_DISABILITY("Severe disability"),
	DEAD("Dead");
    private String friendlyName;

    private ModifiedRankinScale(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    @Override
    public String toString() {
        return friendlyName;
    }
}
