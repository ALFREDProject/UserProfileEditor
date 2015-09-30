package eu.alfred.personalization_manager.db_administrator.model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AttributesHelper {

    String[] avoid = {

            "id",
            "alfedAppInstalationDate",
            "alfredUserName",
            "anniversaryDate",
            "myersBriggsIndicator",
            "socialSecurityNumber",
            "culturalOrFamilyNeeds",
            "healthInsurance",
            "selfDescrPersonalityChar",
            "interests",
            "nextOfKin",
            "socialMediaProfiles"/*,


            "citizenship",
            "dateOfBirth",
            "educationLevel",
            "email",
            "employmentStatus",
            "firstName",
            "gender",
            "language",
            "lastName",
            "maritalStatus",
            "middleName",
            "mobilePhone",
            "nationality",
            "phone",
            "postalAddress",
            "prefferedName",
            "profession",
            "residentialAddress"*/
    };

    public List<String> getUserProfileFields() {

        UserProfile userProfile = new UserProfile();
        Class<?> c = userProfile.getClass();
        Field[] fields = c.getDeclaredFields();
        ArrayList<String> userProfileFields = new ArrayList<String>();

        List<String> avoidList = Arrays.asList(avoid);

        for (Field field : fields) {
            if (!avoidList.contains(field.getName())) {
                userProfileFields.add(field.getName());
            }
        }

        return userProfileFields;
    }


}
