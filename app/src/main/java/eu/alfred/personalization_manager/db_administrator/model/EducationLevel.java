package eu.alfred.personalization_manager.db_administrator.model;

public enum EducationLevel {
    //Based in ISCED 2011, change if needed
    A("Primary education")/*,
    PRIMARY("Primary education"),
    LO_SECONDARY("Lower secondary education"),
    UP_SECONDARY("Upper secondary education"),
    POST_SECONDARY("Post-secondary non-tertiary education"),
    TERTIARY("Short-cycle tertiary education"),
    BACHELOR("Bachelor or equivalent"),
    MASTER("Master or equivalent"),
    DOCTORAL("Doctoral or equivalent")*/;
    private String friendlyName;

    private EducationLevel(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    @Override
    public String toString() {
        return friendlyName;
    }
}
