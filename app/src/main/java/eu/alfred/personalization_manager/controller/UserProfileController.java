package eu.alfred.personalization_manager.controller;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;

import eu.alfred.personalization_manager.db_administrator.api.VolleyWebServiceClient;
import eu.alfred.personalization_manager.db_administrator.model.Contact;
import eu.alfred.personalization_manager.db_administrator.model.UserProfile;
import eu.alfred.personalization_manager.gui.UserProfileActivity;
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
    private ArrayList<Contact> mContacts;

    public UserProfileController(UserProfileActivity context) {
        mActivity = context;
        this.client = new VolleyWebServiceClient(this);
        shPref = mActivity.getSharedPreferences(mActivity.getString(R.string.up_current_id), Context.MODE_PRIVATE);
    }

    public String getStoredUserProfileId() {
        String upId = shPref.getString(mActivity.getString(R.string.up_current_id), null);
        Log.d(TAG, "Stored UP ID is: " + upId);
        return upId;
    }

    public void setStoredUserProfileId(String upId) {
        Log.d(TAG, "Storing UP ID as: " + upId);
        SharedPreferences.Editor editor = shPref.edit();
        editor.putString(mActivity.getString(R.string.up_current_id), upId);
        boolean commitResult = editor.commit();
        Log.d(TAG, String.format("Success storing upId: %b", commitResult));

    }



    public void newProfile(UserProfile up) {
        client.doPostRequestToCreate(up);
    }

    public void deleteProfile(UserProfile up) {
        client.doDeleteRequest(up.getId());
    }

    public void updateProfile(UserProfile up) {
        if (up != null && up.getId() != null) {
            client.doPutRequest(up);
        }
    }

    public void onSuccessCreatingNewUserProfile(String newId) {
        upId = newId;
        setStoredUserProfileId(upId);
        mActivity.fillFormId(upId);
        mActivity.setMenuItemsVisibleForEditing(true);
        mActivity.notification(true, "New User Profile created with ID=" + upId);

    }

    public void onSuccessRetrievingUser(UserProfile up) {
        mActivity.fillForm(up);
        mActivity.setMenuItemsVisibleForEditing(true);
    }

    public void init() {
        upId = getStoredUserProfileId();
        if (upId != null) {
            client.doGetRequest(upId);
        }
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

    public void onSuccessGetAllContacts(ArrayList<Contact> contacts) {
        mContacts = contacts;
        mActivity.setContacts(mContacts);
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
}
