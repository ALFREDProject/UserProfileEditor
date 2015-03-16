package eu.alfred.personalization_manager.db_administrator.api;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.type.TypeReference;

public class DataTransformationsHandler {

	protected String KeyValuesToJson(HashMap<String,String> keysValues) {
		String jsonResult="{";
		
		for(Map.Entry<String, String> keyValueEntry : keysValues.entrySet()) {
			jsonResult += "\"" + keyValueEntry.getKey() + "\":\"" + keyValueEntry.getValue() + "\",";
		}
		// An ugly way to remove last ','. Find better solution
		jsonResult=jsonResult.substring(0,jsonResult.length()-1);
		jsonResult +="}"; 
		return jsonResult;
	}
	
	protected HashMap<String,String> breakJsonToHashMap(String rawJson){		
		HashMap<String,String> jsonAsMap = new HashMap<String,String>();
		ObjectMapper mapper = new ObjectMapper();
	 
		try {
			//convert JSON string to Map
			jsonAsMap = mapper.readValue(rawJson, new TypeReference<HashMap<String,String>>(){});
	 	 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonAsMap;
	}
	
	protected String mapObjectToJson(Object objectToMap) {
		String mappedJson=null;		
		
		try{
			ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
			mappedJson = objectWriter.writeValueAsString(objectToMap);
		}catch(Exception ex) {
			ex.printStackTrace();
		} 
		
		return mappedJson;
	}
}
