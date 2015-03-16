package eu.alfred.personalization_manager.db_administrator.model;

public class FootMotion {

	private String inversion;
	private String eversion;
	
	public FootMotion(String inversion, String eversion) {
		this.inversion = inversion;
		this.eversion = eversion;
	}
	
	public String getInversion() {
		return inversion;
	}
	public void setInversion(String inversion) {
		this.inversion = inversion;
	}
	public String getEversion() {
		return eversion;
	}
	public void setEversion(String eversion) {
		this.eversion = eversion;
	}
	
	
}
