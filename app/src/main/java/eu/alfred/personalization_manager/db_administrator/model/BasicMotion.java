package eu.alfred.personalization_manager.db_administrator.model;

public class BasicMotion {

	private String flexion;
	private String extension;
	
	public BasicMotion(String flexion, String extension) {
		this.flexion = flexion;
		this.extension = extension;
	}
	
	public String getFlexion() {
		return flexion;
	}
	public void setFlexion(String flexion) {
		this.flexion = flexion;
	}
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
	
	
}
