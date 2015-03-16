package eu.alfred.personalization_manager.db_administrator.api;
 

import java.util.HashMap;
import java.util.List;

import eu.alfred.personalization_manager.db_administrator.model.HistoricalEntry;


public class UserHistoricalEntryClient {
	private final String baseUrl = "http://80.86.83.34:8080/personalization-manager/services/databaseServices/users/";
	private DataTransformationsHandler dtHandler;
	private IWebServiceClient wsClient;
	
	public UserHistoricalEntryClient() { 
		dtHandler = new DataTransformationsHandler();
		wsClient = new JerseyWebServiceClient();
	}
 
	public String createUserHistoricalEntry(String userID, HistoricalEntry newHistoricalEntry) {
		String result = null;		
		String input = dtHandler.mapObjectToJson(newHistoricalEntry); 	
		result = wsClient.doPostRequestToCreate(baseUrl + userID + "/historicalentries/new", input);
		
		return result;
	}
	 
	public List<Object> retrieveAllUserHistoricalEntries(String userID) {
		List<Object> result = null;		
		result = wsClient.doGetRequest(baseUrl + userID + "/historicalentries/all",HistoricalEntry.class); 
				
		return result;
	}
	 
	public List<Object> retrieveUserHistoricalEntries(String userID, HashMap<String, String> values) {
		List<Object> result = null;		
		String input = dtHandler.KeyValuesToJson(values);
		result = wsClient.doPostRequestToRetrieve(baseUrl + userID + "/historicalentries", input,HistoricalEntry.class);
		
		return result;		
	}
	 
	public String updateUserHistoricalEntries(String historicalEntryID, HashMap<String, String> values) {
		String result = null;		
		String input = dtHandler.KeyValuesToJson(values); 
		result = wsClient.doPutRequest(baseUrl + "historicalentries/" + historicalEntryID, input);
		
		return result; 
	}
	 
	public String deleteUserHistoricalEntries(String historicalEntryID) {
		String result = null;		
		result = wsClient.doDeleteRequest(baseUrl + "historicalentries/" + historicalEntryID); 
				
		return result; 
	}
}
