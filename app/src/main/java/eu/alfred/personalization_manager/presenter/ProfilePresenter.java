package eu.alfred.personalization_manager.presenter;

import eu.alfred.personalization_manager.db_administrator.api.VolleyWebServiceClient;
import eu.alfred.personalization_manager.gui.UserProfileActivity;

/**
 * Created by Arturo.Brotons on 27/01/2015.
 */
public class ProfilePresenter {
    final private UserProfileActivity activity;
    final private VolleyWebServiceClient client;

    public ProfilePresenter(UserProfileActivity activity, VolleyWebServiceClient client) {
        this.activity = activity;
        this.client = client;
    }


}
