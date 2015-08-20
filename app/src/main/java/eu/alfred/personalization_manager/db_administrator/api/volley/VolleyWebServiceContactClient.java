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
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import eu.alfred.personalization_manager.controller.ContactsController;
import eu.alfred.personalization_manager.db_administrator.model.Contact;
import eu.alfred.personalization_manager.db_administrator.model.Relation;
import eu.alfred.personalization_manager.db_administrator.model.Requesters;

/**
 * Created by Arturo.Brotons on 16/04/2015.
 */
public class VolleyWebServiceContactClient {
    private final ContactsController controller;
    private final Gson mGson;
    private final String REQUEST_TYPE_APP_JSON = "application/json; charset=utf-8";
    private RequestQueue mRequestQueue;
    final static public String TAG = "VolleyContact";
    private final String URL = "http://80.86.83.34:8080/personalization-manager/services/databaseServices/";

    public VolleyWebServiceContactClient(ContactsController cController) {
        this.controller = cController;
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
    }

    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(controller.getContext());
        }
        return mRequestQueue;
    }

    public void doGetAllContacts(String upId) {
        Log.d(TAG, "");
        String url = URL + "users/" + upId + "/contacts/all";
        Log.d(TAG, "doGetAllContacts " + url);
        JsonArrayRequest request = new JsonArrayRequest(
                url,
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
                        controller.onSuccessGetAllContacts(contacts);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        controller.onErrorGetAllContacts(volleyError);
                    }
                });
        mRequestQueue.add(request);
    }

    public void doPostNewContact(final Contact contact) {
        String jsContactStr = mGson.toJson(contact);
        try {
            final JSONObject jsContact = new JSONObject(jsContactStr);
            jsContact.remove("id");
            jsContact.remove("_class");
            Log.d(TAG, "Save Contact: " + jsContact);

            UPRequest request = new UPRequest(
                    Request.Method.POST,
                    URL + "users/" + contact.getUserID() + "/contacts/new",
                    jsContact,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "doPostNewContact() -> onResponse<String>() " + response);
                            contact.setId(response);
                            controller.onSuccessCreatingNewContact(contact);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Log.e(TAG, "doPostNewContact() -> onErrorResponse<String>() " + volleyError.getMessage());
                            controller.onErrorCreatingNewContact(volleyError);
                        }
                    }
            );
            mRequestQueue.add(request);
        } catch (JSONException e) {
            Log.d(TAG, "doPostRequestToCreate() -> " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void doGetContact(final String upId, final String contactId) {
        JsonArrayRequest request = new JsonArrayRequest(
                URL + "users/" + upId + "/contacts/all",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        Contact contact = null;
                        ArrayList<Contact> contacts = new ArrayList<Contact>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject jsonObj = jsonArray.getJSONObject(i);
                                Contact c = mGson.fromJson(jsonObj.toString(), Contact.class);
                                if (contactId.equals(c.getId())) {
                                    contact = c;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        controller.onSuccessGettingContact(contact);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        controller.onErrorGettingContact(volleyError);
                    }
                });
        mRequestQueue.add(request);
    }

    /**
     * PUT Contact. Updates a user.
     * @param upc Content of the User Profile.
     */
    public void doPutRequest(final Contact upc) {
        String url = URL + "users/contacts/" + upc.getId();
        Log.d(TAG, "doPutRequest " + url);
        String jsUpStr = mGson.toJson(upc);
        try {
            final JSONObject jsUp = new JSONObject(jsUpStr);
            jsUp.remove("id");
            jsUp.remove("_class");


            //FIXME for Tasos in server side, when updating contact
            jsUp.remove("relationToUser");
            Relation[] relToUsr = upc.getRelationToUser();
            if (relToUsr != null && relToUsr.length > 0) {
                jsUp.put("relationToUser", relToUsr[0].name());
            }
//            jsUp.put("dateOfBirth", up.getDateOfBirth().getTime());
//            _tempDate = up.getDateOfBirth();
            fixAddress(jsUp);

            Log.d(TAG, "Update User Profile: " + jsUp);

            UPRequest request = new UPRequest(
                    Request.Method.PUT,
                    url,
                    jsUp,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "doPutRequest() -> onResponse<String>() " + response);
                            controller.onSuccessUpdatingContact(upc.getId());
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Log.e(TAG, "doPutRequest() -> onErrorResponse<String>() " + volleyError.getMessage());
                            controller.onErrorUpdatingContact(volleyError);
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
     * DELETE User Profile. Deletes user in server.
     * @param contact ID of the User Profile
     */
    public void doDeleteRequest(final Contact contact) {
        Log.d(TAG, "Deleting User Profile: " + contact.getId());
        StringRequest deleteRequest = new StringRequest(
                Request.Method.DELETE,
                URL + "users/" + "contacts/" + contact.getId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String res) {
                        Log.d(TAG, String.format("doDeleteRequest(%s) -> onResponse<String>(%s) ", contact.getId(), res));
                        controller.onSuccessDeletingContact(contact);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e(TAG, "doDeleteRequest() -> onErrorResponse<String>() " + volleyError.getMessage());
                        controller.onErrorDeletingContact(volleyError);
                    }
                });
        mRequestQueue.add(deleteRequest);
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


    public void doPostNewRequester(Requesters req) {
        String jsContactStr = mGson.toJson(req);
        try {
            final JSONObject jsContact = new JSONObject(jsContactStr);
            jsContact.remove("id");
            jsContact.remove("_class");
            Log.d(TAG, "Save Requesters: " + jsContact);

            UPRequest request = new UPRequest(
                    Request.Method.POST,
                    URL + "requesters/new",
                    jsContact,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "doPostNewRequester() -> onResponse<String>() " + response);

                            controller.onSuccessCreatingNewRequesters(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Log.e(TAG, "doPostNewRequester() -> onErrorResponse<String>() " + volleyError.getMessage());
                            controller.onErrorCreatingNewRequesters(volleyError);
                        }
                    }
            );
            mRequestQueue.add(request);
        } catch (JSONException e) {
            Log.d(TAG, "doPostRequestToCreate() -> " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void doPutRequester(Requesters req) {

    }
}
