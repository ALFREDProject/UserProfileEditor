package eu.alfred.personalization_manager.db_administrator.model;

public class ShoulderMotion {

	private String flexionTo90Degress;
	private String extension;
	private String abductionTo90Degrees;
	private String abduction;
	private String lateralRotation;
	private String medialRotation;
	
	public ShoulderMotion(String flexionTo90Degress, String extension, String abductionTo90Degrees,
									String abduction,String lateralRotation,String medialRotation) {
		this.flexionTo90Degress = flexionTo90Degress;
		this.extension = extension;
		this.abductionTo90Degrees=abductionTo90Degrees;
		this.abduction= abduction;
		this.lateralRotation = lateralRotation;
		this.medialRotation = medialRotation;
	}
	
	public String getFlexionTo90Degress() {
		return flexionTo90Degress;
	}
	public void setFlexionTo90Degress(String flexionTo90Degress) {
		this.flexionTo90Degress = flexionTo90Degress;
	}
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
	public String getAbductionTo90Degrees() {
		return abductionTo90Degrees;
	}
	public void setAbductionTo90Degrees(String abductionTo90Degrees) {
		this.abductionTo90Degrees = abductionTo90Degrees;
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
