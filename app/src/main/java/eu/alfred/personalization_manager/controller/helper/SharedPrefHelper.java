package eu.alfred.personalization_manager.controller.helper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPrefHelper {

    private static final String TAG = "SharedPrefHelper";


    public static final String CURRENT_SHARED_PREF = "up_shared_prefs";
    public static final String CURRENT_UP_ID = "up_current_id";
    public static final String CURRENT_UP_EMAIL = "up_current_email";
    public static final String CURRENT_UP_PASSWORD = "up_current_password";
    public static final String CURRENT_UP_USERID = "pref_userId";
/*    public static final String CURRENT_UP_USERID = "up_current_userid";*/


    private SharedPreferences sharedPreferences;

    public SharedPrefHelper(Activity activity) {
        sharedPreferences = activity.getSharedPreferences(CURRENT_SHARED_PREF, Context.MODE_PRIVATE);
    }

    public String get(String key) {

        String value = sharedPreferences.getString(key, null);

        Log.d(TAG, String.format("Stored %s is {%s}", key, value));
        return value;
    }

    public void put(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        boolean commitResult = editor.commit();
        Log.d(TAG, (commitResult ? "Success" : "Fail") + " storing " + key + ".");
    }

    public void delete(String key) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        boolean commitResult = editor.commit();
        Log.d(TAG, (commitResult ? "Success" : "Fail") + " removing " + key + ".");
    }
}
