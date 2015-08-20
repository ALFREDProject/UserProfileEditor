package eu.alfred.personalization_manager.db_administrator.model;

public enum Relation {

    WIFE("Wife"),
    HUSBANT("Husband"),
    DOCTOR("Doctor"),
    CARER("Carer"),
    NURSE("Nurse"),
    MOTHER("Mother"),
    FATHER("Father"),
    DAUGHTER("Daughter"),
    SON("Son"),
    SISTER("Sister"),
    BROTHER("Brother"),
    RELATIVE("Relative"),
    FRIEND("Friend"),
	OTHER("Other");

    private String friendlyName;

    private Relation(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    @Override
    public String toString() {
        return friendlyName;
    }
}
