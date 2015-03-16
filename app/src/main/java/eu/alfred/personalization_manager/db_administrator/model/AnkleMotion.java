package eu.alfred.personalization_manager.db_administrator.model;

public class AnkleMotion {

	private String plantarFlexion;
	private String dorsiflexion;
	
	public AnkleMotion(String plantarFlexion, String dorsiflexion) {
		this.plantarFlexion = plantarFlexion;
		this.dorsiflexion = dorsiflexion;
	}
	
	public String getPlantarFlexion() {
		return plantarFlexion;
	}
	public void setPlantarFlexion(String plantarFlexion) {
		this.plantarFlexion = plantarFlexion;
	}
	public String getDorsiflexion() {
		return dorsiflexion;
	}
	public void setDorsiflexion(String dorsiflexion) {
		this.dorsiflexion = dorsiflexion;
	}
	
	
}
