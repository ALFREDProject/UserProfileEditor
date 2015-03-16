package eu.alfred.personalization_manager.db_administrator.model;


public class MetacarpophalangealJointOfThumb extends BasicMotion {

	private String abduction;
	
	public MetacarpophalangealJointOfThumb(String flexion, String extension, String abduction) {
		super(flexion,extension);
		this.abduction = abduction;
	}

	public String getAbduction() {
		return abduction;
	}
	public void setAbduction(String abduction) {
		this.abduction = abduction;
	}
	
	
}

