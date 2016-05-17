package eu.alfred.personalization_manager.controller.helper;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import eu.alfred.api.personalization.responses.PersonalizationResponse;

/**
 * Created by robert.lill on 19.04.16.
 */
public abstract class PersonalizationArrayResponse implements PersonalizationResponse {

	private final static String TAG = "PMOResponse";

	@Override
	public void OnSuccess(JSONObject jsonObject) {
		Log.e(TAG, "Unexpected JSONObject response");
	}

	@Override
	public void OnSuccess(String s) {
		Log.e(TAG, "Unexpected String response");
	}

	@Override
	public void OnSuccess(Object object) {
		Log.e(TAG, "Unexpected Object response");
	}

	@Override
	public void OnError(Exception e) {
		Log.e(TAG, e.getClass().getSimpleName() + ": " + e.getMessage());
	}
}
