package eu.alfred.personalization_manager.controller.helper;

import android.content.Context;
import android.util.Log;

import eu.alfred.api.PersonalAssistant;
import eu.alfred.api.PersonalAssistantConnection;

/**
 * Created by robert.lill on 22.04.16.
 */
public class PersonalAssistantProvider {

	private final static String TAG = "PA-Provider";

	private static PersonalAssistant personalAssistant = null;

	public static PersonalAssistant getPersonalAssistant(Context ctx) {

		if (personalAssistant == null) {

			PersonalAssistant PA = new PersonalAssistant(ctx);

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

			PA.Init();

			personalAssistant = PA;
		}

		return personalAssistant;

	}

}
