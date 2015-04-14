package eu.alfred.personalization_manager.db_administrator.api;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
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

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import eu.alfred.personalization_manager.controller.UserProfileController;
import eu.alfred.personalization_manager.db_administrator.model.Contact;
import eu.alfred.personalization_manager.db_administrator.model.UserProfile;

/**
 * Created by Arturo.Brotons on 26/01/2015.
 */
public class VolleyWebServiceClient {

    private static VolleyWebServiceClient mInstance;
    private final UserProfileController mUpController;
    private final Gson mGson;
    private final String REQUEST_TYPE_APP_JSON = "application/json; charset=utf-8";
    private RequestQueue mRequestQueue;
    final static public String TAG = "Volley";
    private final String URL = "http://80.86.83.34:8080/personalization-manager/services/databaseServices/users/";

//    Date _tempDate = new Date(-880735200000L); //TODO Fix date parsing with server

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
                        return new Date(json.getAsJsonPrimitive().getAsLong());
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

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public void doGetRequest(String urlToCall, final Class entityClass) {
        StringRequest getRequest = new StringRequest(Request.Method.GET, urlToCall,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String resultOfRequest) {
                        try {
                            List<Object> resultList = null;
                            ObjectMapper mapper = new ObjectMapper();
                            resultList = mapper.readValue(resultOfRequest,
                                    TypeFactory.defaultInstance().constructCollectionType(List.class, entityClass));

                        } catch (IOException e) {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                    }
                });
        addToRequestQueue(getRequest);
    }

    public String doPostRequestToCreate(String urlToPost, String jsonToPost) {
        return null;
    }

    public List<Object> doPostRequestToRetrieve(String urlToPost, String jsonToPost, Class entityClass) {
        return null;
    }

    public void doPutRequest(String urlToPut, String jsonToPut) {
        try {
            JSONObject jsonObject = new JSONObject(jsonToPut);
            JsonObjectRequest putRequest = new JsonObjectRequest(
                    Request.Method.PUT,
                    urlToPut,
                    jsonObject,
                    new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });
            addToRequestQueue(putRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

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

    public void doPostRequestToCreate(UserProfile up) {
        String jsUpStr = mGson.toJson(up);
        try {
            final JSONObject jsUp = new JSONObject(jsUpStr);
            jsUp.remove("id");
            jsUp.remove("_class");
//            jsUp.remove("dateOfBirth");
//            _tempDate = up.getDateOfBirth();
            jsUp.remove("livingSituation");
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

    public void doPutRequest(UserProfile up) {
        String jsUpStr = mGson.toJson(up);
        try {
            final JSONObject jsUp = new JSONObject(jsUpStr);
            jsUp.remove("id");
            jsUp.remove("_class");
//            jsUp.remove("dateOfBirth");
//            jsUp.put("dateOfBirth", up.getDateOfBirth().getTime());
//            _tempDate = up.getDateOfBirth();
            /*TODO Remove this hack if possible*/
            if (jsUp.has("residentialAddress")) {
                JSONObject residentialAddress = jsUp.getJSONObject("residentialAddress");
                if (residentialAddress.has("street")) {
                    jsUp.put("residentialAddress.street", residentialAddress.get("street"));
                }
                if (residentialAddress.has("number")) {
                    jsUp.put("residentialAddress.number", residentialAddress.get("number"));
                }
                if (residentialAddress.has("postalCode")) {
                    jsUp.put("residentialAddress.postalCode", residentialAddress.get("postalCode"));
                }
                if (residentialAddress.has("city")) {
                    jsUp.put("residentialAddress.city", residentialAddress.get("city"));
                }
                if (residentialAddress.has("state")) {
                    jsUp.put("residentialAddress.state", residentialAddress.get("state"));
                }
                if (residentialAddress.has("country")) {
                    jsUp.put("residentialAddress.country", residentialAddress.get("country"));
                }
                jsUp.remove("residentialAddress");
            }

            if (jsUp.has("postalAddress")) {
                JSONObject postalAddress = jsUp.getJSONObject("postalAddress");
                if (postalAddress.has("street")) {
                    jsUp.put("postalAddress.street", postalAddress.get("street"));
                }
                if (postalAddress.has("number")) {
                    jsUp.put("postalAddress.number", postalAddress.get("number"));
                }
                if (postalAddress.has("postalCode")) {
                    jsUp.put("postalAddress.postalCode", postalAddress.get("postalCode"));
                }
                if (postalAddress.has("city")) {
                    jsUp.put("postalAddress.city", postalAddress.get("city"));
                }
                if (postalAddress.has("state")) {
                    jsUp.put("postalAddress.state", postalAddress.get("state"));
                }
                if (postalAddress.has("country")) {
                    jsUp.put("postalAddress.country", postalAddress.get("country"));
                }
                jsUp.remove("postalAddress");
            }

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

    public void doGetAllContacts(String upId) {
        JsonArrayRequest request = new JsonArrayRequest(
                URL + upId + "/contacts/all",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        ArrayList<Contact> contacts = new ArrayList<Contact>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject jsonObj = jsonArray.getJSONObject(i);
                                Contact contact = mGson.fromJson(jsonObj.toString(), Contact.class);
                                contacts.add(contact);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        mUpController.onSuccessGetAllContacts(contacts);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });
        mRequestQueue.add(request);
    }

    /**
     * Custom Request for Volley API, as StringRequest acts as if it was function
     * String function(String param)
     * and JSONRequest acts like
     * JSON function(JSON param)
     * and what we need is a mixture of both
     * String function(JSON param)
     */
    private class UPRequest extends Request<String> {

        private JSONObject mBody;
        private final Response.Listener<String> mListener;

        /**
         *
         * @param method Usually Request.Method.POST or Request.Method.PUT
         * @param url complete URL (String) like "new" or "54e5cd60..."
         * @param body JSON Object we want to persist.
         * @param listener If success, what we do after the call.
         * @param errorListener If error, what we do after the call.
         */
        private UPRequest(int method, String url, JSONObject body, Response.Listener<String> listener, Response.ErrorListener errorListener) {
            super(method, url, errorListener);
            mBody = body;
            mListener = listener;
        }

        @Override
        public byte[] getBody() throws AuthFailureError {
            return mBody.toString().getBytes();

        }

        @Override
        public String getBodyContentType() {
            return REQUEST_TYPE_APP_JSON;
        }

        /**
         * This is where we parse the response, in our case a simple String with an ID
         * @param response ID of the object we have persisted.
         * @return parsed response.
         */
        @Override
        protected Response<String> parseNetworkResponse(NetworkResponse response) {
            try {
                String id = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                return Response.success(id, HttpHeaderParser.parseCacheHeaders(response));
            } catch (UnsupportedEncodingException e) {
                return Response.error(new ParseError(e));
            }
        }

        @Override
        protected void deliverResponse(String response) {
            mListener.onResponse(response);
        }
    }
}
