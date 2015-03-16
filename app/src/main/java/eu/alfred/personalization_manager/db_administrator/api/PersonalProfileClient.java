package eu.alfred.personalization_manager.db_administrator.api;

import java.util.HashMap;  
import java.util.List;

import eu.alfred.personalization_manager.db_administrator.model.UserProfile;


public class PersonalProfileClient {	
	private final String baseUrl = "http://80.86.83.34:8080/personalization-manager/services/databaseServices/users/";
	private DataTransformationsHandler dtHandler;
	private IWebServiceClient wsClient;
	
	public PersonalProfileClient() { 
		dtHandler = new DataTransformationsHandler();
		wsClient = new JerseyWebServiceClient();
	}

	public String createUserProfile(UserProfile userProfile) {
		String result = null;		
		String input = dtHandler.mapObjectToJson(userProfile); 	
		result = wsClient.doPostRequestToCreate(baseUrl + "new", input);
		
		return result;  
	}
	 
	public List<Object> retrieveUserProfile(String userID) {
		List<Object> result = null;		
		result = wsClient.doGetRequest(baseUrl+userID, UserProfile.class); 
				
		return result; 
	}
	 
	public  List<Object> retrieveUserProfiles(HashMap<String, String> values) {
		 List<Object> result = null;		
		String input = dtHandler.KeyValuesToJson(values); 
		result = wsClient.doPostRequestToRetrieve(baseUrl + "retrieve", input, UserProfile.class);
		
		return result;	 
	}
	 
	public String updateUserProfile(String userID, HashMap<String, String> values) {
		String result = null;		
		String input = dtHandler.KeyValuesToJson(values);
		result = wsClient.doPutRequest(baseUrl + userID, input);
		
		return result;  
	}
	 
	public String deleteUserProfile(String userID) {
		String result = null;		
		result = wsClient.doDeleteRequest(baseUrl+userID); 
				
		return result;  
	}
	 
}
