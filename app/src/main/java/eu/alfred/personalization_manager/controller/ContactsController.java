package eu.alfred.personalization_manager.controller;

import android.content.Context;
import android.util.Log;


import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.HashMap;

import eu.alfred.api.personalization.model.Contact;
import eu.alfred.api.personalization.model.Relation;
import eu.alfred.api.personalization.model.Requester;
import eu.alfred.personalization_manager.db_administrator.api.volley.VolleyWebServiceContactClient;
import eu.alfred.personalization_manager.gui.tabs.ContactsSectionFragment;
import eu.alfred.personalization_manager.gui.tabs.contacts.ContactActivity;

/**
 * Created by Arturo.Brotons on 02/03/2015.
 */
public class ContactsController {
    private static final String TAG = "ContactsController";


    private VolleyWebServiceContactClient client;
    private ContactActivity mActivity;
    private ContactsSectionFragment mFragment;
    private ArrayList<Contact> mContacts;
    private static HashMap<String, Requester> mRequesters = new HashMap<String, Requester>();

    static private ContactsController mInstance = null;
    private String mUserId;
    private Context context;
    private String alfredUserId;

    /*static public ContactsController getInstance() {
        if (mInstance == null) {
            mInstance = new ContactsController();
        }
        return mInstance;
    }*/

    public void setContactActivity(ContactActivity activity) {
        mActivity = activity;
    }

    public void setFragment(ContactsSectionFragment fragment) {
        this.mFragment = fragment;
    }

    public ContactsController(Context context) {
        this.context = context;
        this.client = new VolleyWebServiceContactClient(this);
        mContacts = new ArrayList<Contact>();
//        mRequesters = new HashMap<String, Requesters>();
    }

    public void getAllContacts() {
        if (mUserId == null) {
            throw new NullPointerException("In Contacts Controller, User ID cannot be null.");
        }
        client.doGetAllContacts(mUserId);
    }

//    public void onSuccessGettingAllContacts(ArrayList<Contact> contacts) {
//        mContacts = contacts;
//        if (mFragment != null) {
//            mFragment.updateContactList(mContacts);
//        }
//    }

    public void getContacts(String id) {
        client.doGetAllContacts(id);
    }

    public void getContact(String upId, String contactId) {
        client.doGetContact(upId, contactId);
    }

    public void newContact(Contact contact, String alfredUserId) {
        if (contact.getAccessRightsToAttributes() == null || contact.getAccessRightsToAttributes().isEmpty()) {
            defaultAccessRights(contact);
        }
        client.doPostNewContact(contact);
        saveRequester(contact);
    }

    private void saveRequester(Contact contact) {
        Requester requesters = null;

        if (contact.getAlfredUserName() != null) {
            if (mRequesters.containsKey(contact.getAlfredUserName())) {
                requesters = mRequesters.get(contact.getAlfredUserName());
                requesters.setAccessRightsToAttributes(contact.getAccessRightsToAttributes());
                client.doPutRequester(requesters);
            } else {
                requesters = new Requester();
                requesters.setTargetAlfredId(alfredUserId);
                requesters.setRequesterAlfredId(contact.getAlfredUserName());
                requesters.setAccessRightsToAttributes(contact.getAccessRightsToAttributes());
                client.doPostNewRequester(requesters);
            }
        }
    }

    public HashMap<String, Boolean> defaultAccessRights(Contact contact) {
        HashMap<String, Boolean> rights = new HashMap<String, Boolean>();

        Relation relation = Relation.OTHER;

        if (contact != null && contact.getRelationToUser() != null && contact.getRelationToUser().length > 0) {
            relation = contact.getRelationToUser()[0];
        }

        boolean closeRel =
                relation != Relation.OTHER &&
                relation != Relation.FRIEND;

        boolean medicalRel =
                relation == Relation.DOCTOR ||
                relation == Relation.NURSE ||
                relation == Relation.CARER;



        rights.put("firstName", true);
        rights.put("middleName", true);
        rights.put("lastName", true);
        rights.put("prefferedName", true);
        rights.put("gender", true);
        rights.put("dateOfBirth", closeRel);

        rights.put("phone", closeRel);
        rights.put("mobilePhone", closeRel);
        rights.put("email", closeRel);

        rights.put("residentialAddress", closeRel);
        rights.put("postalAddress", closeRel);

        rights.put("citizenship", closeRel);
        rights.put("nationality", closeRel);
        rights.put("language", closeRel);

        rights.put("socialSecurityNumber", medicalRel);
        rights.put("anniversaryDate", closeRel);
        rights.put("maritalStatus", closeRel);
        rights.put("educationLevel", closeRel);
        rights.put("employmentStatus", closeRel);
        rights.put("profession", closeRel);
        rights.put("healthInsurance", medicalRel);
        rights.put("myersBriggsIndicator", false);

        rights.put("id", false);
        rights.put("alfredUserName", true);
        rights.put("selfDescrPersonalityChar", false);
        rights.put("socialMediaProfiles", false);
        rights.put("interests", false);
        rights.put("culturalOrFamilyNeeds", false);
        rights.put("nextOfKin", false);
        rights.put("alfedAppInstalationDate", false);

        if (contact != null) {
            contact.setAccessRightsToAttributes(rights);
        }
        return rights;
    }

    public void deleteContact(Contact contact) {
        client.doDeleteRequest(contact);
//        deleteRequester(contact);
    }

    public void updateContact(Contact contact) {
        client.doPutRequest(contact);
        saveRequester(contact);
    }


    public void onSuccessGetAllContacts(ArrayList<Contact> contacts) {
        Log.d(TAG, "onSuccessGetAllContacts: " + (contacts!=null?contacts.size():"NULL"));
        mContacts = contacts;
        mFragment.updateContactList(mContacts);
        for (Contact contact : mContacts) {
            if (contact.getAlfredUserName() != null) {
                client.doGetRequestByAlfredUsername(alfredUserId, contact.getAlfredUserName());
            }
        }
    }

    public void onErrorGetAllContacts(Exception ex) {
        Log.d(TAG, "onErrorGetAllContacts " + ex.getMessage());
    }

    public void onSuccessCreatingNewContact(Contact contact) {
        Log.d(TAG, "onSuccessCreatingNewContact: contactId=" + contact.getId());
        mActivity.onSuccessCreatingContact(contact);
    }

    public void onErrorCreatingNewContact(Exception ex) {
        Log.d(TAG, "onErrorCreatingNewContact: " + ex.getMessage());
        //TODO show error message
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }


    public void onSuccessUpdatingContact(String contactId) {
        Log.d(TAG, "onSuccessUpdatingContact: contactId=" + contactId);
        mActivity.onSuccessUpdatingContact();
    }

    public void onErrorUpdatingContact(Exception ex) {
        Log.d(TAG, "onErrorUpdatingContact: " + ex.getMessage());
    }

    public void onSuccessDeletingContact(Contact contact) {
        Log.d(TAG, "onSuccessDeletingContact: contactId=" + contact.getId());
        mActivity.onSuccessDeletingContact(contact);
    }

    public void onErrorDeletingContact(Exception ex) {
        Log.d(TAG, "onErrorDeletingContact: " + ex.getMessage());
        mActivity.onErrorDeletingContact(ex.getMessage());
    }

    public void onSuccessGettingContact(Contact contact) {
        mActivity.onSuccessGettingContact(contact);
        getContacts(contact.getId());
    }

    public void onErrorGettingContact(Exception ex) {

    }

    public void saveRequester(Requester req) {
        if (req.getId() == null) { //Create a new Requester
            client.doPostNewRequester(req);
        } else {
            client.doPutRequester(req);
        }

    }


    public void onSuccessCreatingNewRequesters(Requester req) {
        Log.d(TAG, "New Requester id: " + req.getId());
        mRequesters.put(req.getRequesterAlfredId(), req);
        mActivity.onSuccessCreatingNewRequesters(req);
    }

    public void onErrorCreatingNewRequesters(Exception ex, Requester req) {
        Log.d(TAG, "[ERROR] Creating New Requester: " + ex.getMessage());
        mActivity.onErrorCreatingNewRequesters(ex, req);
    }

    public void setUserId(String userId) {
        this.mUserId = userId;
    }

    public void onErrorGettingRequester(Exception e) {
        if (mActivity != null) {
            mActivity.onErrorGettingRequester(e.getMessage());
        }
    }

    public void onSuccessGettingRequester(Requester req) {
        mRequesters.put(req.getRequesterAlfredId(), req);
        if (mActivity != null) {
            mActivity.onSuccessCreatingNewRequesters(req);
        }
    }

    public void setAlfredUserId(String alfredUserId) {
        this.alfredUserId = alfredUserId;
    }

    public void onSuccessUpdatingRequesters(String response, Requester req) {
        Log.d(TAG, "Updated Requester: " + response);
        mRequesters.put(req.getRequesterAlfredId(), req);
        mActivity.onSuccessUpdatingRequesters(req);
    }

    public void onErrorUpdatingRequester(VolleyError ex) {
        Log.e(TAG, "[ERROR] Updating requester: " + ex.getMessage());
    }
}
