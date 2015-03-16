package eu.alfred.personalization_manager.db_administrator.api;

import java.util.HashMap;
import java.util.List;

import eu.alfred.personalization_manager.db_administrator.model.HealthProfile;


public class UsersHealthProfileClient {
	private final String baseUrl = "http://80.86.83.34:8080/personalization-manager/services/databaseServices/users/";
	private DataTransformationsHandler dtHandler;
	private IWebServiceClient wsClient;
	
	public UsersHealthProfileClient() { 
		dtHandler = new DataTransformationsHandler();
		wsClient = new JerseyWebServiceClient();
	}
	 
	public String createUserHealthProfile(String userID, HealthProfile newHealthProfile) {
		String result = null;		
		String input = dtHandler.mapObjectToJson(newHealthProfile); 	
		result = wsClient.doPostRequestToCreate(baseUrl + userID + "/healthprofiles/new", input);
		
		return result; 
	}
	 
	public List<Object> retrieveUsersHealthProfile(String userID) {
		List<Object> result = null;		
		result = wsClient.doGetRequest(baseUrl + userID + "/healthprofiles", HealthProfile.class); 
				
		return result; 
	}
	 
	public String updateUsersHealthProfile(String userID, HashMap<String, String> values) {
		String result = null;		
		String input = dtHandler.KeyValuesToJson(values);
		result = wsClient.doPutRequest(baseUrl + userID + "/healthprofiles", input);
		
		return result;  
	}
	 
	public String deleteUsersHealthProfile(String userID) {
		String result = null;		
		result = wsClient.doDeleteRequest(baseUrl + userID + "/healthprofiles"); 
				
		return result; 
	}
}
