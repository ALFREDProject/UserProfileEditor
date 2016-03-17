package eu.alfred.personalization_manager.gui.tabs.contacts;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import eu.alfred.personalization_manager.controller.UserProfileController;
import alfred.eu.personalizationmanagerapi.client.model.Contact;
import eu.alfred.userprofile.R;

/**
 * Created by Arturo.Brotons on 27/02/2015.
 */
public class ContactFragment extends Fragment {

    UserProfileController userProfileController;
    Contact contact;

    public ContactFragment() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.userprofile_contact_detail, container, false);

        return view;
    }


    public void setContact(Contact contact) {
        this.contact = contact;
    }


}
