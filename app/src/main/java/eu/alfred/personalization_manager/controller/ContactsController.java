package eu.alfred.personalization_manager.controller;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import eu.alfred.api.PersonalAssistant;
import eu.alfred.api.personalization.client.ContactDto;
import eu.alfred.api.personalization.client.ContactMapper;
import eu.alfred.api.personalization.client.RequesterDto;
import eu.alfred.api.personalization.client.RequesterMapper;
import eu.alfred.api.personalization.model.AttributesHelper;
import eu.alfred.api.personalization.model.Contact;
import eu.alfred.api.personalization.model.Relation;
import eu.alfred.api.personalization.model.Requester;
import eu.alfred.api.personalization.webservice.PersonalizationManager;
import eu.alfred.personalization_manager.controller.helper.PersonalAssistantProvider;
import eu.alfred.personalization_manager.controller.helper.PersonalizationArrayResponse;
import eu.alfred.personalization_manager.controller.helper.PersonalizationObjectResponse;
import eu.alfred.personalization_manager.controller.helper.PersonalizationStringResponse;
import eu.alfred.personalization_manager.gui.tabs.ContactsSectionFragment;
import eu.alfred.personalization_manager.gui.tabs.contacts.ContactActivity;

/**
 * Created by Arturo.Brotons on 02/03/2015.
 */
public class ContactsController {
    private static final String TAG = "ContactsController";


    private ContactActivity mActivity;
    private ContactsSectionFragment mFragment;
    private List<Contact> mContacts;
    private static HashMap<String, Requester> mRequesters = new HashMap<String, Requester>();

    static private ContactsController mInstance = null;
    private String mUserId;
    private Context context;
    private String alfredUserId;
	private PersonalAssistant PA;

    public void setContactActivity(ContactActivity activity) {
        mActivity = activity;
    }

    public void setFragment(ContactsSectionFragment fragment) {
        this.mFragment = fragment;
    }

    public ContactsController(Context context) {
        this.context = context;
        mContacts = new ArrayList<Contact>();
	    this.PA = PersonalAssistantProvider.getPersonalAssistant(context);
    }

    public void getAllContacts() {
	    if (mUserId == null) {
		    throw new NullPointerException("In Contacts Controller, User ID cannot be null.");
	    }
//        client.doGetAllContacts(mUserId);
	    getContacts(mUserId);
    }

    public void getContacts(String id) {
        // client.doGetAllContacts(id);
	    PersonalizationManager PM = new PersonalizationManager(PA.getMessenger());
	    PM.retrieveAllUserContacts(id, new PersonalizationArrayResponse() {
		    @Override
		    public void OnSuccess(JSONArray a) {
			    Log.i(TAG, "retrieveUserProfiles succeeded");
			    if (mActivity != null) mActivity.notification(true, "Profile retrieved");

			    Type type = new TypeToken<ArrayList<ContactDto>>() {}.getType();
			    List<ContactDto> dto = new Gson().fromJson(a.toString(), type);

			    Log.i(TAG, "number of results = " + dto.size());

			    List<Contact> clist = new ArrayList<Contact>();
			    for (ContactDto cd : dto) {
				    clist.add(ContactMapper.toModel(cd));
			    }

			    onSuccessGetAllContacts(clist);
		    }

		    @Override
		    public void OnError(Exception e) {
//			    Log.e(TAG, "retrieveUserProfiles failed: " + e.getClass().getSimpleName() + ": " + e.getMessage());
			    Log.e(TAG, "retrieveUserProfiles failed", e);
			    if (mActivity != null) mActivity.notification(false, "Retrieving profile failed");
		    }
	    });
    }

    public void newContact(final Contact contact, String alfredUserId) {
        if (contact.getAccessRightsToAttributes() == null || contact.getAccessRightsToAttributes().isEmpty()) {
            defaultAccessRights(contact);
        }
//        client.doPostNewContact(contact);

	    PersonalizationManager PM = new PersonalizationManager(PA.getMessenger());
	    PM.createUserContact(alfredUserId, contact, new PersonalizationStringResponse() {
		    @Override
		    public void OnSuccess(String s) {
			    Log.i(TAG, "createUserContact succeeded");
			    mActivity.notification(true, "Contact created");
			    mActivity.onSuccessCreatingContact(contact);
		    }

		    @Override
		    public void OnError(Exception e) {
			    Log.e(TAG, e.getClass().getSimpleName() + ": " + e.getMessage());
			    mActivity.notification(false, "Creating contact failed");
		    }
	    });


	    saveRequester(contact);
    }

    private void saveRequester(final Contact contact) {
        Requester requester = null;

        if (contact.getAlfredUserName() != null) {
            if (mRequesters.containsKey(contact.getAlfredUserName())) {
                requester = mRequesters.get(contact.getAlfredUserName());
                requester.setAccessRightsToAttributes(contact.getAccessRightsToAttributes());
//                client.doPutRequester(requester);

	            final Requester r2 = requester;

	            PersonalizationManager PM = new PersonalizationManager(PA.getMessenger());
	            PM.updateRequester(requester, new PersonalizationStringResponse() {
		            @Override
		            public void OnSuccess(String s) {
			            Log.i(TAG, "updateRequester succeeded");
			            mActivity.notification(true, "Requester updated");

			            mRequesters.put(contact.getAlfredUserName(), r2);
			            mActivity.onSuccessUpdatingRequesters(r2);
		            }

		            @Override
		            public void OnError(Exception e) {
			            Log.e(TAG, e.getClass().getSimpleName() + ": " + e.getMessage());
			            mActivity.notification(false, "Update requester failed");
		            }
	            });


            } else {
                requester = new Requester();
                requester.setTargetAlfredId(alfredUserId);
                requester.setRequesterAlfredId(contact.getAlfredUserName());
                requester.setAccessRightsToAttributes(contact.getAccessRightsToAttributes());
//                client.doPostNewRequester(requester);

	            final Requester r2 = requester;

	            PersonalizationManager PM = new PersonalizationManager(PA.getMessenger());
	            PM.createRequester(requester, new PersonalizationStringResponse() {
		            @Override
		            public void OnSuccess(String s) {
			            Log.i(TAG, "createRequester succeeded");
			            mActivity.notification(true, "Requester created");

			            mRequesters.put(contact.getAlfredUserName(), r2);
			            mActivity.onSuccessCreatingNewRequesters(r2);
		            }

		            @Override
		            public void OnError(Exception e) {
			            Log.e(TAG, e.getClass().getSimpleName() + ": " + e.getMessage());
			            mActivity.notification(false, "Creating requester failed");
			            mActivity.onErrorCreatingNewRequesters(e, r2);
		            }
	            });


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

	    for (String uf : AttributesHelper.getUserProfileFields()) {
		    if (!rights.containsKey(uf)) rights.put(uf, Boolean.FALSE);
	    }


	    if (contact != null) {
            contact.setAccessRightsToAttributes(rights);
        }
        return rights;
    }

    public void deleteContact(final Contact contact) {
//        client.doDeleteRequest(contact);

	    PersonalizationManager PM = new PersonalizationManager(PA.getMessenger());
	    PM.deleteUserContact(contact.getId(), new PersonalizationStringResponse() {
		    @Override
		    public void OnSuccess(String s) {
			    Log.i(TAG, "deleteUserContact succeeded");
			    mActivity.notification(true, "Contact deleted");
			    mActivity.onSuccessDeletingContact(contact);
		    }

		    @Override
		    public void OnError(Exception e) {
			    Log.e(TAG, e.getClass().getSimpleName() + ": " + e.getMessage());
			    mActivity.notification(false, "Delete contact failed");
		    }
	    });

    }

    public void updateContact(final Contact contact) {
//        client.doPutRequest(contact);


	    PersonalizationManager PM = new PersonalizationManager(PA.getMessenger());
	    PM.updateUserContact(contact, new PersonalizationStringResponse() {
		    @Override
		    public void OnSuccess(String s) {
			    Log.i(TAG, "updateUserContact succeeded");
			    mActivity.notification(true, "Contact updated");
			    mActivity.onSuccessUpdatingContact();
		    }

		    @Override
		    public void OnError(Exception e) {
			    Log.e(TAG, e.getClass().getSimpleName() + ": " + e.getMessage());
			    mActivity.notification(false, "Update contact failed");
		    }
	    });


	    saveRequester(contact);
    }


    public void onSuccessGetAllContacts(List<Contact> contacts) {
        Log.d(TAG, "onSuccessGetAllContacts: " + (contacts!=null?contacts.size():"NULL"));
        mContacts = contacts;
        mFragment.updateContactList(mContacts);
        for (final Contact contact : mContacts) {
            if (contact.getAlfredUserName() != null) {
//                client.doGetRequestByAlfredUsername(alfredUserId, contact.getAlfredUserName());


	            PersonalizationManager PM = new PersonalizationManager(PA.getMessenger());
	            PM.retrieveRequester(alfredUserId, contact.getAlfredUserName(), new PersonalizationObjectResponse() {
		            @Override
		            public void OnSuccess(JSONObject o) {
			            Log.i(TAG, "retrieveRequester succeeded");
			            mActivity.notification(true, "Requester retrieved");


			            Type type = new TypeToken<RequesterDto>() {}.getType();
			            RequesterDto dto = new Gson().fromJson(o.toString(), type);

			            Requester requester = RequesterMapper.toModel(dto);

			            mRequesters.put(contact.getAlfredUserName(), requester);
			            mActivity.onSuccessCreatingNewRequesters(requester);

		            }

		            @Override
		            public void OnError(Exception e) {
			            Log.e(TAG, e.getClass().getSimpleName() + ": " + e.getMessage());
			            mActivity.notification(false, "Retrieve requester failed");
		            }
	            });

            }
        }
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }



    public void setUserId(String userId) {
        this.mUserId = userId;
    }

    public void onErrorGettingRequester(Exception e) {
        if (mActivity != null) {
            mActivity.onErrorGettingRequester(e.getMessage());
        }
    }

    public void setAlfredUserId(String alfredUserId) {
        this.alfredUserId = alfredUserId;
    }

}
