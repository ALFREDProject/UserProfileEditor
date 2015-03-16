package eu.alfred.personalization_manager.db_administrator.api;

import java.util.HashMap; 
import java.util.List;

import eu.alfred.personalization_manager.db_administrator.model.Contact;


public class UserContactsClient {
	private final String baseUrl = "http://80.86.83.34:8080/personalization-manager/services/databaseServices/users/";
	private DataTransformationsHandler dtHandler;
	private IWebServiceClient wsClient;
	
	public UserContactsClient() { 
		dtHandler = new DataTransformationsHandler();
		wsClient = new JerseyWebServiceClient();
	}
 
	public String createUserContact(String userID, Contact newContact) {
		String result = null;		
		String input = dtHandler.mapObjectToJson(newContact); 	
		result = wsClient.doPostRequestToCreate(baseUrl + userID + "/contacts/new", input);
		
		return result; 		
	}
	 
	public List<Object> retrieveAllUserContacts(String userID) {
		List<Object> result = null;		
		result = wsClient.doGetRequest(baseUrl + userID + "/contacts/all", Contact.class); 
				
		return result; 
	}
	 
	public List<Object> retrieveUserContacts(String userID, HashMap<String, String> values) {
		List<Object> result = null;		
		String input = dtHandler.KeyValuesToJson(values); 
		result = wsClient.doPostRequestToRetrieve(baseUrl + userID + "/contacts", input, Contact.class);
		
		return result;  
	}
	 
	public String updateUserContact(String contactID, HashMap<String, String> values) {
		String result = null;		
		String input = dtHandler.KeyValuesToJson(values); 
		result = wsClient.doPutRequest(baseUrl + "contacts/" + contactID, input);
		
		return result;   
	}
	 
	public String deleteUserContact(String contactID) {
		String result = null;		
		result = wsClient.doDeleteRequest(baseUrl + "contacts/" + contactID); 
				
		return result;  
	}
	
}
