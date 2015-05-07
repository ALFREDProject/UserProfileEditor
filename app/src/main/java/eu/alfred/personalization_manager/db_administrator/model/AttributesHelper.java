package eu.alfred.personalization_manager.db_administrator.model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class AttributesHelper {
	
	public List<String> getUserProfileFields() {
		
		UserProfile userProfile = new UserProfile();
		Class<?> c = userProfile.getClass();
		Field[] fields = c.getDeclaredFields();
		ArrayList<String> userProfileFields = new ArrayList<String>();
		
		for(Field field : fields) 			
			userProfileFields.add(field.getName());
		
		return userProfileFields;
	}
	
		
	
}
