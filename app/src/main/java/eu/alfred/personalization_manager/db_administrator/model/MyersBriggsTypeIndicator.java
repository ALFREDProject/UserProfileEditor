package eu.alfred.personalization_manager.db_administrator.model;

public enum MyersBriggsTypeIndicator {

	ISTJ("Introversion-Sensing-Thinking-Judgment"),
	ISFJ("Introversion-Sensing-Feeling-Judgment"),
	INFJ("Introversion-Intuition-Feeling-Judgment"),
	INTJ("Introversion-Intuition-Thinking-Judgment"),
	ISTP("Introversion-Sensing-Thinking-Perception"),
	ISFP("Introversion-Sensing-Feeling-Perception"),
	INFP("Introversion-Intuition-Feeling-Perception"),
	INTP("Introversion-Intuition-Thinking-Perception"),
	ESTP("Extraversion-Sensing-Thinking-Perception"),
	ESFP("Extraversion-Sensing-Feeling-Perception"),
	ENFP("Extraversion-Intuition-Feeling-Perception"),
	ENTP("Extraversion-Intuition-Thinking-Perception"),
	ESTJ("Extraversion-Sensing-Thinking-Judgment"),
	ESFJ("Extraversion-Sensing-Feeling-Judgment"),
	ENFJ("Extraversion-Intuition-Feeling-Judgment"),
	ENTJ("Extraversion-Intuition-Thinking-Judgment");

    private String friendlyName;

    private MyersBriggsTypeIndicator(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    @Override
    public String toString() {
        return friendlyName;
    }
	
}
