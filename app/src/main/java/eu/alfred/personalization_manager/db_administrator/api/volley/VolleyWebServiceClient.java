package eu.alfred.personalization_manager.db_administrator.api.volley;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.Iterator;

import eu.alfred.personalization_manager.controller.UserProfileController;
import eu.alfred.personalization_manager.db_administrator.model.UserProfile;

/**
 * Created by Arturo.Brotons on 26/01/2015.
 */
public class VolleyWebServiceClient {

    private static VolleyWebServiceClient mInstance;
    private final UserProfileController mUpController;
    private final Gson mGson;
    private RequestQueue mRequestQueue;
    final static public String TAG = "Volley";
    private final String URL = "http://80.86.83.34:8080/personalization-manager/services/databaseServices/users/";

//    Date _tempDate = new Date(-880735200000L);

    public static synchronized VolleyWebServiceClient getInstance(UserProfileController upController) {
        if (mInstance == null) {
            mInstance = new VolleyWebServiceClient(upController);
        }
        return mInstance;
    }

    public VolleyWebServiceClient(UserProfileController upController) {
        mUpController = upController;
        mRequestQueue = getRequestQueue();
        GsonBuilder gsonBuilder = new GsonBuilder()
                .registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {

                    @Override
                    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        Date date = null;
                        try {
                            date = new Date(json.getAsJsonPrimitive().getAsLong());
                        } catch (Exception e) {
                            date = new Date();
                        }
                        return date;
                    }
                })
                .registerTypeAdapter(Date.class, new JsonSerializer<Date>() {
                    @Override
                    public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
                        return new JsonPrimitive(src.getTime());
                    }
                });

        mGson = gsonBuilder.create();
//        mGson = gsonBuilder.setDateFormat("dd/MM/yyyy").create();
    }

    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mUpController.getContext());
        }
        return mRequestQueue;
    }

    /**
     * GET User Profile. Fetches user from server.
     * @param upId ID of the User Profile
     */
    public void doGetRequest(final String upId) {
        JsonArrayRequest request = new JsonArrayRequest(URL + upId, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                UserProfile up = null;
                try {
                    JSONObject obj = jsonArray.getJSONObject(0);
                    if (obj != null) {
                        Log.d(TAG, "Retrieved user: " + obj.toString());
                        Object aClass = obj.remove("_class");
                        Log.d(TAG, "\"_class\" was " + aClass);
//                        Object dateOfBirth = obj.remove("dateOfBirth");
//                        Log.d(TAG, "\"dateOfBirth\" was " + dateOfBirth);
                        String jsUpStr = obj.toString();
                        up = mGson.fromJson(jsUpStr, UserProfile.class);
//                        up.setDateOfBirth(_tempDate);
                    } else {
                        Log.e(TAG, "In doGetRequest() JSON obj was null");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mUpController.onSuccessRetrievingUser(up);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG, "doGetRequest() -> onErrorResponse<JSON>() " + volleyError.getMessage());
                volleyError.printStackTrace();
                mUpController.onErrorRetrievingUser(volleyError);
            }
        });
        mRequestQueue.add(request);
    }

    /**
     * DELETE User Profile. Deletes user in server.
     * @param upId ID of the User Profile
     */
    public void doDeleteRequest(final String upId) {
        Log.d(TAG, "Deleting User Profile: " + upId);
        StringRequest deleteRequest = new StringRequest(
                Request.Method.DELETE,
                URL + upId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String res) {
                        Log.d(TAG, String.format("doDeleteRequest(%s) -> onResponse<String>(%s) ", upId, res));
                        mUpController.onSuccessDeletingUserProfile(res);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e(TAG, "doDeleteRequest() -> onErrorResponse<String>() " + volleyError.getMessage());
                        mUpController.onErrorDeletingUserProfile(volleyError);
                    }
                });
        mRequestQueue.add(deleteRequest);
    }

    /**
     * POST User Profile. Creates a user on server.
     * @param up Content of the User Profile.
     */
    public void doPostRequestToCreate(UserProfile up) {
        String jsUpStr = mGson.toJson(up);
        try {
            final JSONObject jsUp = new JSONObject(jsUpStr);
            jsUp.remove("id");
            jsUp.remove("_class");
            // Until Anniversary Date is fixed on server
            jsUp.remove("anniversaryDate");
            Log.d(TAG, "Save User Profile: " + jsUp);

            UPRequest request = new UPRequest(
                    Request.Method.POST,
                    URL + "new",
                    jsUp,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "doPostRequestToCreate() -> onResponse<String>() " + response);
                            mUpController.onSuccessCreatingNewUserProfile(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Log.e(TAG, "doPostRequestToCreate() -> onErrorResponse<String>() " + volleyError.getMessage());
                            mUpController.onErrorCreatingNewUserProfile(volleyError);
                        }
                    }
            );
            mRequestQueue.add(request);
        } catch (JSONException e) {
            Log.d(TAG, "doPostRequestToCreate() -> " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * PUT User Profile. Updates a user.
     * @param up Content of the User Profile.
     */
    public void doPutRequest(UserProfile up) {
        String jsUpStr = mGson.toJson(up);
        try {
            final JSONObject jsUp = new JSONObject(jsUpStr);
            jsUp.remove("id");
            jsUp.remove("_class");
//            jsUp.remove("dateOfBirth");
//            jsUp.put("dateOfBirth", up.getDateOfBirth().getTime());
//            _tempDate = up.getDateOfBirth();
            jsUp.remove("anniversaryDate");
            fixAddress(jsUp);

            Log.d(TAG, "Update User Profile: " + jsUp);

            UPRequest request = new UPRequest(
                    Request.Method.PUT,
                    URL + up.getId(),
                    jsUp,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "doPutRequest() -> onResponse<String>() " + response);
                            mUpController.onSuccessUpdatingUserProfile(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Log.e(TAG, "doPutRequest() -> onErrorResponse<String>() " + volleyError.getMessage());
                            mUpController.onErrorUpdatingUserProfile(volleyError);
                        }
                    }
            );
            mRequestQueue.add(request);
        } catch (JSONException e) {
            Log.d(TAG, "doPutRequest() -> " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Inline addresses fix when updating a User Profile.
     * From: {  "residentialAddress" :
     *              {"street" : "A", "number":"B"},
     *          "postalAddress":
     *              {"street" : "C", "number":"D"}
     *       }
     * To:  {
     *          "residentialAddress.street" : "A",
     *          "residentialAddress.number" : "B",
     *          "postalAddress.street" : "C",
     *          "postalAddress.number" : "D"
     *      }
     * @param jsonUserProfile JSON User Profile to inline addresses.
     * @throws JSONException
     */
    private void fixAddress(JSONObject jsonUserProfile) throws JSONException {
        String[] addrs = {"residentialAddress", "postalAddress"};
        for (String addrsLabels : addrs) {
            if (jsonUserProfile.has(addrsLabels)) {
                JSONObject jsonAddr = jsonUserProfile.getJSONObject(addrsLabels);
                Iterator<String> keys = jsonAddr.keys();
                while (keys.hasNext()) {
                    String field = keys.next();
                    jsonUserProfile.put(addrsLabels + "." + field, jsonAddr.get(field));
                }
                jsonUserProfile.remove(addrsLabels);
            }
        }

    }


}
