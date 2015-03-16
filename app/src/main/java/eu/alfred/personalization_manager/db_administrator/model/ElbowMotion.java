package eu.alfred.personalization_manager.db_administrator.model;


public class ElbowMotion extends BasicMotion {

	private String pronation;
	private String supination;
	
	public ElbowMotion(String flexion, String extension, String pronation, String supination) {
		super(flexion,extension);
		this.pronation = pronation;
		this.supination = supination;
	}
	
	public String getPronation() {
		return pronation;
	}
	public void setPronation(String pronation) {
		this.pronation = pronation;
	}
	public String getSupination() {
		return supination;
	}
	public void setSupination(String supination) {
		this.supination = supination;
	}
	
	
}
