package eu.alfred.personalization_manager.controller;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import eu.alfred.personalization_manager.db_administrator.api.volley.VolleyWebServiceContactClient;
import eu.alfred.personalization_manager.db_administrator.model.Contact;
import eu.alfred.personalization_manager.db_administrator.model.Requesters;
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

    static private ContactsController mInstance = null;
    private String mUserId;
    private Context context;

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
    }

    public void getAllContacts() {
        client.doGetAllContacts(mUserId);
    }

    public void onSuccessGettingAllContacts(ArrayList<Contact> contacts) {
        mContacts = contacts;
        if (mFragment != null) {
            mFragment.updateContactList(mContacts);
        }
    }

    public void getContacts(String id) {
        client.doGetAllContacts(id);
    }

    public void getContact(String upId, String contactId) {
        client.doGetContact(upId, contactId);
    }

    public void newContact(Contact contact) {
        client.doPostNewContact(contact);
    }

    public void deleteContact(Contact contact) {
        client.doDeleteRequest(contact.getId());
    }

    public void updateContact(Contact contact) {
        client.doPutRequest(contact);
    }


    public void onSuccessGetAllContacts(ArrayList<Contact> contacts) {
        Log.d(TAG, "onSuccessGetAllContacts: " + (contacts!=null?contacts.size():"NULL"));
        mContacts = contacts;
        mFragment.updateContactList(mContacts);
    }

    public void onSuccessCreatingNewContact(String contactId) {
        Log.d(TAG, "onSuccessCreatingNewContact: contactId=" + contactId);
        mActivity.onSuccessCreatingContact(contactId);
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

    public void onSuccessDeletingContact(String res) {
        Log.d(TAG, "onSuccessUpdatingContact: contactId=" + res);
        mActivity.onSuccessDeletingContact();
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

    public void saveRequester(Requesters req) {
        if (req.getId() == null) { //Create a new Requester
            client.doPostNewRequester(req);
        } else {
            client.doPutRequester(req);
        }

    }

    public void onSuccessCreatingNewRequesters(String response) {
        System.out.println("New Requesters id: " + response);
    }


    public void onErrorCreatingNewRequesters(Exception ex) {
        System.out.println("[ERROR] New Requesters id: " + ex.getMessage());
    }

    public void setUserId(String userId) {
        this.mUserId = userId;
    }
}
