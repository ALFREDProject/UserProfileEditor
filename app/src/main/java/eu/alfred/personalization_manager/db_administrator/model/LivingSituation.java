package eu.alfred.personalization_manager.db_administrator.model;

public enum LivingSituation {

	PRIVATE_RESIDENCE("Private residence"),
	NON_SUPERVISED_BOARDING_HOUSE("Non supervised boarding house"),
	RESIDENTIAL_REHABILITATION_PROGRAM("Residential rehabilitation program"),
	ASSISTED_LIVING("Assisted living"),
	SKILLED_NURSING_FACILITY("Skilled nursing facility"),
	RESIDENTIAL_TREATMENT_CENTER("Residential treatment center"),
	HOSPITAL("Hospital"),
	CRISIS_RESIDENCE("Crisis residence"),
	OTHER("Other");
    private String friendlyName;

    private LivingSituation(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    @Override
    public String toString() {
        return friendlyName;
    }
}
