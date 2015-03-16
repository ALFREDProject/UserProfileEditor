package eu.alfred.personalization_manager.db_administrator.model;

public class MiniMentalStateScore {

	private int orientationToTime;
	private int orientationToPlace;
	private int registration;
	private int attentionAndCalculation;
	private int recall;
	private int language;
	private int repetition;
	private int complexCommands;
	
	public int getOrientationToTime() {
		return orientationToTime;
	}
	public void setOrientationToTime(int orientationToTime) {
		this.orientationToTime = orientationToTime;
	}
	public int getOrientationToPlace() {
		return orientationToPlace;
	}
	public void setOrientationToPlace(int orientationToPlace) {
		this.orientationToPlace = orientationToPlace;
	}
	public int getRegistration() {
		return registration;
	}
	public void setRegistration(int registration) {
		this.registration = registration;
	}
	public int getAttentionAndCalculation() {
		return attentionAndCalculation;
	}
	public void setAttentionAndCalculation(int attentionAndCalculation) {
		this.attentionAndCalculation = attentionAndCalculation;
	}
	public int getRecall() {
		return recall;
	}
	public void setRecall(int recall) {
		this.recall = recall;
	}
	public int getLanguage() {
		return language;
	}
	public void setLanguage(int language) {
		this.language = language;
	}
	public int getRepetition() {
		return repetition;
	}
	public void setRepetition(int repetition) {
		this.repetition = repetition;
	}
	public int getComplexCommands() {
		return complexCommands;
	}
	public void setComplexCommands(int complexCommands) {
		this.complexCommands = complexCommands;
	}
	
	
}
