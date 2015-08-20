package eu.alfred.personalization_manager.controller;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import eu.alfred.personalization_manager.controller.auth.User;
import eu.alfred.personalization_manager.controller.health.HealthController;
import eu.alfred.personalization_manager.db_administrator.api.volley.VolleyWebServiceClient;
import eu.alfred.personalization_manager.db_administrator.model.UserProfile;
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
    final private VolleyWebServiceClient client;
    private String upId;
    private final SharedPreferences shPref;
    private ContactsController mContactsController;
    private HealthSectionFragment mHealthFragment;
    private HealthController mHealthController;
    private User user;
    private int emailAttempts = 0;

    public UserProfileController(UserProfileActivity context) {
        mActivity = context;
        this.client = new VolleyWebServiceClient(this);
        shPref = mActivity.getSharedPreferences(mActivity.getString(R.string.up_current_id), Context.MODE_PRIVATE);
        mContactsController = new ContactsController(context.getApplicationContext());
        mContactsController.setContext(mActivity.getApplicationContext());

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

    public void newProfile(UserProfile up) {
        client.doPostRequestToCreate(up);
    }

    public void deleteProfile(UserProfile up) {
        client.doDeleteRequest(up.getId());
    }

    public void updateProfile(UserProfile up) {
        if (up != null) {
            up.setId(upId);
            Log.d(TAG, "Updating profile: " + up.toString());
            client.doPutRequest(up);
        } else {
            Log.w(TAG, "Profile was null when trying to update.");
        }
    }

    public void onSuccessCreatingNewUserProfile(UserProfile newUp) {
        upId = newUp.getId();
        setStoredUserProfileId(upId);
        mActivity.fillForm(newUp);
        mActivity.setMenuItemsVisibleForEditing(true);
        mActivity.notification(true, "New User Profile created.");
        Log.d(TAG, "New User Profile created with id = " + upId);

    }

    public void onSuccessRetrievingUser(UserProfile up) {
        upId = up.getId();
        mActivity.fillForm(up);
        mActivity.setMenuItemsVisibleForEditing(true);
        mContactsController.setUserId(upId);
        mContactsController.getAllContacts();
        mHealthController.getInfo(user);
    }

/*    public void init() {
        upId = getStoredUserProfileId();
        if (upId != null) {
            mContactsController.setUserId(upId);
            client.doGetRequest(upId);
            mContactsController.getAllContacts();
        }
    }*/

    public void initRetrieving(User user) {
        upId = getStoredUserProfileId();
        this.user = user;
        if (upId != null) {
            mContactsController.setUserId(upId);
            client.doGetRequest(upId);
        } else {
            Log.d(TAG, "doGetRequestByUsername attempt #" + emailAttempts);
            client.doGetRequestByUsername(user.getEmail());
        }
    }


    public void initCreating(User user) {
        this.user = user;
        UserProfile up = new UserProfile();
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

    public void onSuccessUpdatingUserProfile(String response) {
        /*Stop animation/spinner*/
        mActivity.notification(true, "User Profile updated");
    }

    public Context getContext() {
        return mActivity.getApplicationContext();
    }

    public void onErrorRetrievingUser(Exception ex) {
        String msg = ex.getMessage();
        mActivity.notification(false, msg);
    }

    public void onErrorDeletingUserProfile(Exception ex) {
        String msg = ex.getMessage();
        mActivity.notification(false, msg);
    }

    public void onErrorCreatingNewUserProfile(Exception ex) {
        String msg = ex.getMessage();
        mActivity.notification(false, msg);
    }

    public void onErrorUpdatingUserProfile(Exception ex) {
        String msg = ex.getMessage();
        mActivity.notification(false, msg);
    }

    public void onErrorRetrievingUserByEmail(Exception ex) {
        emailAttempts++;
        Log.w(TAG, "doGetRequestByUsername attempt #" + emailAttempts);
        client.doGetRequestByUsername(user.getEmail());
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
}
