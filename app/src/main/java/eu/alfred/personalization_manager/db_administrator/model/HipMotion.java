package eu.alfred.personalization_manager.db_administrator.model;

public class HipMotion extends BasicMotion {

	private String hyperextension;
	private String abduction;
	private String lateralRotation;
	private String medialRotation;
	
	public HipMotion(String flexion, String extension, String hyperextension, String abduction, 
													String lateralRotation, String medialRotation ) {
		super(flexion, extension);
		this.hyperextension = hyperextension;
		this.abduction = abduction;
		this.lateralRotation = lateralRotation;
		this.medialRotation = medialRotation;		
	}
	
	public String getHyperextension() {
		return hyperextension;
	}
	public void setHyperextension(String hyperextension) {
		this.hyperextension = hyperextension;
	}
	public String getAbduction() {
		return abduction;
	}
	public void setAbduction(String abduction) {
		this.abduction = abduction;
	}
	public String getLateralRotation() {
		return lateralRotation;
	}
	public void setLateralRotation(String lateralRotation) {
		this.lateralRotation = lateralRotation;
	}
	public String getMedialRotation() {
		return medialRotation;
	}
	public void setMedialRotation(String medialRotation) {
		this.medialRotation = medialRotation;
	}
	
	
}
