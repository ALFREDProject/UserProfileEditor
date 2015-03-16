package eu.alfred.personalization_manager.db_administrator.model;

public enum Language {

	// The possible enumerations for language codes are based on the ISO 639-1 standard
	DE("Deutsch"),
	EN("English"),
	FR("Français"),
	NL("Nederlands"),
	SV("Svenska");
    private String friendlyName;

    private Language(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    @Override
    public String toString() {
        return friendlyName;
    }

}
