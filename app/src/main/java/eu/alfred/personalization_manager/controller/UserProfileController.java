package eu.alfred.personalization_manager.controller;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import eu.alfred.api.PersonalAssistant;
import eu.alfred.api.PersonalAssistantConnection;
import eu.alfred.api.personalization.client.UserProfileDto;
import eu.alfred.api.personalization.client.UserProfileMapper;
import eu.alfred.api.personalization.model.UserProfile;
import eu.alfred.api.personalization.webservice.PersonalizationManager;
import eu.alfred.personalization_manager.controller.auth.User;
import eu.alfred.personalization_manager.controller.health.HealthController;
import eu.alfred.personalization_manager.controller.helper.PersonalAssistantProvider;
import eu.alfred.personalization_manager.controller.helper.PersonalizationArrayResponse;
import eu.alfred.personalization_manager.controller.helper.PersonalizationObjectResponse;
import eu.alfred.personalization_manager.controller.helper.PersonalizationStringResponse;
import eu.alfred.personalization_manager.gui.UserProfileActivity;
import eu.alfred.personalization_manager.gui.tabs.ContactsSectionFragment;
import eu.alfred.personalization_manager.gui.tabs.HealthSectionFragment;
import eu.alfred.userprofile.R;

/**
 * Created by Arturo.Brotons on 21/01/2015.
 */
public class UserProfileController {

    private static final String TAG = "UserProfileController";
    private final UserProfileActivity mActivity;
    private String upId;
    private final SharedPreferences shPref;
    private ContactsController mContactsController;
    private HealthSectionFragment mHealthFragment;
    private HealthController mHealthController;
    private User user;
	private PersonalAssistant PA;

    public UserProfileController(UserProfileActivity context) {
        mActivity = context;
        shPref = mActivity.getSharedPreferences(mActivity.getString(R.string.up_current_id), Context.MODE_PRIVATE);
        mContactsController = new ContactsController(context.getApplicationContext());
        mContactsController.setContext(mActivity.getApplicationContext());
		this.PA = PersonalAssistantProvider.getPersonalAssistant(context);
    }

    public String getStoredUserProfileId() {
        String tempUpId = shPref.getString(mActivity.getString(R.string.up_current_id), upId);
        Log.d(TAG, "Stored UP ID is: " + tempUpId);
        return tempUpId;
    }

    public void setStoredUserProfileId(String upId) {
        this.upId = upId;
        Log.d(TAG, "Storing UP ID as: " + upId);
        SharedPreferences.Editor editor = shPref.edit();
        editor.putString(mActivity.getString(R.string.up_current_id), upId);
        boolean commitResult = editor.commit();
        Log.d(TAG, (commitResult ? "Success" : "Fail") + " storing upId.");
    }

    public void logout() {
        Log.d(TAG, "Removing UP ID as: " + upId);
        SharedPreferences.Editor editor = shPref.edit();
        editor.remove(mActivity.getString(R.string.up_current_id));
        boolean commitResult = editor.commit();
        Log.d(TAG, (commitResult ? "Success" : "Fail") + " removing upId.");
    }

    public void newProfile(final UserProfile up) {
	    PersonalizationManager PM = new PersonalizationManager(PA.getMessenger());
	    PM.createUserProfile(up, new PersonalizationStringResponse() {
		    @Override
		    public void OnSuccess(String s) {
			    Log.i(TAG, "createUserProfile succeeded");
			    mActivity.notification(true, "Profile created");
			    onSuccessCreatingNewUserProfile(up);
		    }

		    @Override
		    public void OnError(Exception e) {
			    Log.e(TAG, e.getClass().getSimpleName() + ": " + e.getMessage());
			    mActivity.notification(false, "Creating profile failed");
		    }
	    });
    }

    public void deleteProfile(UserProfile up) {
	    PersonalizationManager PM = new PersonalizationManager(PA.getMessenger());
	    PM.deleteUserProfile(up.getId(), new PersonalizationStringResponse() {
		    @Override
		    public void OnSuccess(String s) {
			    Log.i(TAG, "deleteUserProfile succeeded");
			    mActivity.notification(true, "Profile deleted");
			    onSuccessDeletingUserProfile(s);
		    }

		    @Override
		    public void OnError(Exception e) {
			    Log.e(TAG, e.getClass().getSimpleName() + ": " + e.getMessage());
			    mActivity.notification(false, "Deleting profile failed");
		    }
	    });

    }

    public void updateProfile(UserProfile up) {
        if (up == null) {
	        Log.e(TAG, "Profile was null when trying to update.");
	        return;
        }

        up.setId(upId);
        Log.d(TAG, "Updating profile: " + up.toString());

        PersonalizationManager PM = new PersonalizationManager(PA.getMessenger());
        PM.updateUserProfile(up, new PersonalizationStringResponse() {
	        @Override
	        public void OnSuccess(String s) {
		        Log.i(TAG, "updateUserProfile succeeded");
		        mActivity.notification(true, "Profile updated");
	        }

	        @Override
	        public void OnError(Exception e) {
		        Log.e(TAG, e.getClass().getSimpleName() + ": " + e.getMessage());
		        mActivity.notification(false, "Updating profile failed");
	        }
        });

        mHealthController.update(user);
    }

    public void onSuccessCreatingNewUserProfile(UserProfile newUp) {
        upId = newUp.getId();
        setStoredUserProfileId(upId);
        mActivity.fillForm(newUp);
        mActivity.setMenuItemsVisibleForEditing(true);
        mActivity.notification(true, "New User Profile created.");
        mHealthController.createInfo(user);
        Log.d(TAG, "New User Profile created with id = " + upId);

    }

    public void onSuccessRetrievingUser(UserProfile up) {
        upId = up.getId();
        mActivity.fillForm(up);
        mActivity.setMenuItemsVisibleForEditing(true);
        mContactsController.setUserId(upId);
        mContactsController.setAlfredUserId(user.getEmail());
        mContactsController.getAllContacts();
        mHealthController.getInfo(user);
    }

    public void initRetrieving(User user) {
        upId = getStoredUserProfileId();
        this.user = user;
        if (upId != null) {
	        Log.d(TAG, "retrieve UserProfile by id " + upId);

            mContactsController.setUserId(upId);
            mContactsController.setAlfredUserId(user.getEmail());

	        PersonalizationManager PM = new PersonalizationManager(PA.getMessenger());
	        PM.retrieveUserProfile(upId, new PersonalizationObjectResponse() {
		        @Override
		        public void OnSuccess(JSONObject o) {
			        Log.i(TAG, "retrieveUserProfile succeeded");
			        mActivity.notification(true, "Profile retrieved");

			        Type type = new TypeToken<UserProfileDto>() {}.getType();
			        UserProfileDto dto = new Gson().fromJson(o.toString(), type);

			        UserProfile up = UserProfileMapper.toModel(dto);
			        onSuccessRetrievingUser(up);
		        }

		        @Override
		        public void OnError(Exception e) {
			        Log.e(TAG, e.getClass().getSimpleName() + ": " + e.getMessage());
			        mActivity.notification(false, "Retrieving profile failed");
		        }
	        });


        } else {

	        Log.d(TAG, "retrieve UserProfile by username " + user.getEmail());
	        String searchCriteria = "{\"email\":\"" + user.getEmail() + "\"}";


	        int ct = 0;
	        while (++ct <= 3) {

		        int mc = 0;
		        while (++mc < 10 && PA.getMessenger() == null) {

			        Log.i(TAG, "no messenger available yet, sleeping " + mc);
			        try { Thread.sleep(1000); } catch (InterruptedException e) { }

		        }

		        if (PA.getMessenger() != null) break;

		        Log.i(TAG, "call Init() again");

/*
		        PA = new PersonalAssistant(mActivity);

		        PA.setOnPersonalAssistantConnectionListener(new PersonalAssistantConnection() {
			        @Override
			        public void OnConnected() {
				        Log.i(TAG, "PersonalAssistantConnection connected");
			        }

			        @Override
			        public void OnDisconnected() {
				        Log.i(TAG, "PersonalAssistantConnection disconnected");
			        }
		        });
*/
		        PA.Init();

	        }

	        PersonalizationManager PM = new PersonalizationManager(PA.getMessenger());
	        PM.retrieveUserProfiles(searchCriteria, new PersonalizationArrayResponse() {
		        @Override
		        public void OnSuccess(JSONArray a) {
			        Log.i(TAG, "retrieveUserProfiles succeeded");
			        mActivity.notification(true, "Profile retrieved");

			        Type type = new TypeToken<ArrayList<UserProfileDto>>() {}.getType();
			        List<UserProfileDto> dto = new Gson().fromJson(a.toString(), type);

			        Log.i(TAG, "number of results = " + dto.size());

			        if (dto.size() > 0) {
				        UserProfile up = UserProfileMapper.toModel(dto.get(0));
				        onSuccessRetrievingUser(up);
			        }
		        }

		        @Override
		        public void OnError(Exception e) {
			        Log.e(TAG, "retrieveUserProfiles failed: " + e.getClass().getSimpleName() + ": " + e.getMessage());
			        mActivity.notification(false, "Retrieving profile failed");
		        }
	        });

        }
    }


    public void initCreating(User user) {
        this.user = user;
        UserProfile up = new UserProfile();
	    up.setId(user.getUserId());
        up.setAlfredUserName(user.getEmail());
        up.setAlfredUserName(user.getEmail());
        up.setEmail(user.getEmail());
        up.setFirstName(user.getFirstName());
        up.setMiddleName(user.getMiddleName());
        up.setLastName(user.getLastName());
        newProfile(up);
    }


    public void onSuccessDeletingUserProfile(String res) {
        String tempUpId = upId;
        upId = null;
        setStoredUserProfileId(null);
        mActivity.emptyForm();
        mActivity.setMenuItemsVisibleForEditing(false);
        mActivity.notification(true, "User Profile deleted (" + tempUpId + ")");
    }

    public Context getContext() {
        return mActivity.getApplicationContext();
    }

    public void setContactsFragment(ContactsSectionFragment sfContacts) {
        mContactsController.setFragment(sfContacts);
    }

    public void setHealthFragment(HealthSectionFragment healthFragment) {
        this.mHealthFragment = healthFragment;
        mHealthController = new HealthController(healthFragment);
    }


    public HealthSectionFragment getmHealthFragment() {
        return mHealthFragment;
    }

    public String getAlfredUserId() {
        return user.getEmail();
    }
}
