package eu.alfred.personalization_manager.db_administrator.model;

import java.util.Date;
import java.util.UUID;


public class HistoricalEntry {
	
	private String id;
	private String userID;
	private HistoricalEntryCategories histEntryCategories;
	private String histEntrydescription;
	private Date histEntryDate;
			
	public HistoricalEntry() {
		setId();
	}
	
	public String getId() {
		return id;
	}
	public void setId() {
		this.id = "alfred-user-historicalEntry-"+UUID.randomUUID().toString(); 
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public HistoricalEntryCategories getHistEntryCategories() {
		return histEntryCategories;
	}
	public void setHistEntryCategories(HistoricalEntryCategories histEntryCategories) {
		this.histEntryCategories = histEntryCategories;
	}
	public String getHistEntrydescription() {
		return histEntrydescription;
	}
	public void setHistEntrydescription(String histEntrydescription) {
		this.histEntrydescription = histEntrydescription;
	}
	public Date getHistEntryDate() {
		return histEntryDate;
	}
	public void setHistEntryDate(Date histEntryDate) {
		this.histEntryDate = histEntryDate;
	}
	
	@Override
	public String toString() {
		return "id: " + getId() + ", userID: " + getUserID() + ", entry category: "+ getHistEntryCategories() + "...";
	}
	

}
