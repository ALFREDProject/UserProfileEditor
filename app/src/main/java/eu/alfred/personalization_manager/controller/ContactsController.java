package eu.alfred.personalization_manager.controller;

import eu.alfred.personalization_manager.gui.tabs.contacts.ContactActivity;

/**
 * Created by Arturo.Brotons on 02/03/2015.
 */
public class ContactsController {
    public ContactsController(ContactActivity contactActivity) {
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
}
