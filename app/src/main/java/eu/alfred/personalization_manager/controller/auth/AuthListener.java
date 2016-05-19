package eu.alfred.personalization_manager.controller.auth;

/**
 * Created by Arturo.Brotons on 14/08/2015.
 */
public interface AuthListener {

    void stopWaiting();
    void startWaiting();

    void notification(boolean mode, User user);

}
