package eu.alfred.personalization_manager.controller.auth;

import android.os.AsyncTask;
import android.util.Log;

import eu.alfred.internal.wrapper.authentication.AuthenticatedUser;
import eu.alfred.internal.wrapper.authentication.AuthenticationException;
import eu.alfred.internal.wrapper.authentication.AuthenticationServerWrapper;
import eu.alfred.internal.wrapper.authentication.login.LoginData;
import eu.alfred.internal.wrapper.authentication.login.LoginDataException;
import eu.alfred.internal.wrapper.authentication.registration.RegistrationData;

/**
 * Created by Arturo.Brotons on 13/08/2015.
 */
public class AuthController {

    final public String TAG = "AuthController";

    final AuthListener caller;

    AsyncTask<User, String, User> current;

    public AuthController(AuthListener caller) {
        this.caller = caller;
        current = null;
    }

    public void login(User user) {
        current = new AuthAttempt();
        current.execute(user);
    }

    public void register(User user) {
        current = new RegisterAttempt();
        current.execute(user);
    }

    public void cancel() {
        current.cancel(true);
    }

    public class AuthAttempt extends AsyncTask<User, String, User> {

        @Override
        protected User doInBackground(User... params) {
            publishProgress("Authenticating...");
            User user = params[0];
            Log.d(TAG, "AuthAttempt start: ");
            AuthenticationServerWrapper authWrapper = new AuthenticationServerWrapper();

            try {
                LoginData data = new LoginData
                        .Builder()
                        .setEmail(user.getEmail()) // "artur.brotons@gmail.com"
                        .setPassword(user.getPassword()) // "abcartagena2password"
                        .create();
                Log.d(TAG, "Authenticating...");
                AuthenticatedUser authedUser = authWrapper.login(data);
                Log.d(TAG, "Authenticating... SUCCESS");
                Log.d(TAG, "AuthAttempt Authed id: " + authedUser.getUserId());
                Log.d(TAG, "AuthAttempt Authed token: " + authedUser.getAccessToken());
                for (String role : authedUser.getRoles()) {
                    Log.d(TAG, "AuthAttempt Authed role: " + role);
                }

                user.setUserId(authedUser.getUserId());
                user.setAccessToken(authedUser.getAccessToken());
                user.setError(null);
                user.setRoles(authedUser.getRoles());

            } catch (Exception e) {
                Log.d(TAG, "Authenticating... FAIL");
                Log.d(TAG, "AuthAttempt Exception", e);
                String errorMsg = treatException(e);
                Log.d(TAG, "AuthAttempt " + errorMsg);
                Log.d(TAG, "AuthAttempt " + user);
                user.setError(errorMsg);
            }
            return user;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "AuthAttempt onPreExecute");
            caller.startWaiting();
        }

        @Override
        protected void onPostExecute(User user) {
            Log.d(TAG, "AuthAttempt onPostExecute" + user);
            caller.stopWaiting();
            caller.notification(true, user);
        }

        @Override
        protected void onCancelled(User user) {
            Log.d(TAG, "Cancelled for user :" + user.toString());
            caller.stopWaiting();
        }
    }

    private String treatException(Exception e) {
        String msg = e.getMessage();
        if (e instanceof LoginDataException) {
            msg = e.getMessage();
        } else if (e instanceof AuthenticationException) {
            AuthenticationException authEx = (AuthenticationException) e;
            Log.d(TAG, "AuthAttempt AuthenticationException Code: " + authEx.getHttpStatusCode(), e);
            int httpStatusCode = authEx.getHttpStatusCode();
            if (httpStatusCode == 401) {
                msg = "Email/password wrong.";
            } else if (e.getCause() != null && e.getCause().getMessage() != null) {
                String cause = e.getCause().getMessage();
                if (cause.contains("Unable to resolve host")) {
                    msg = "No Internet connection.\nPlease, enable WiFi or Cellular data";
                } else if (cause.contains("No route to host")) {
                    msg = "Server seems busy. Please, try later";
                }
            }
        }
        return msg;
    }

    public class RegisterAttempt extends AsyncTask<User, String, User> {

        @Override
        protected User doInBackground(User... params) {
            User user = params[0];
            Log.d(TAG, "RegisterAttempt start: ");
            AuthenticationServerWrapper authWrapper = new AuthenticationServerWrapper();

            String[] rolesArr = new String[user.getRoles().size()];
            for (int i = 0; i < user.getRoles().size(); i++) {
                rolesArr[i] = user.getRoles().get(i);
            }

            try {
                RegistrationData data = new RegistrationData
                        .Builder()
                        .setName(user.getName())
                        .setEmail(user.getEmail())
                        .setPassword(user.getPassword())
                        .addRoles(rolesArr)
                        .create();

                Log.d(TAG, "Registering...");
                AuthenticatedUser authedUser = authWrapper.register(data);
                Log.d(TAG, "Registering... SUCCESS");
                Log.d(TAG, "RegisterAttempt Authed id: " + authedUser.getUserId());
                Log.d(TAG, "RegisterAttempt Authed token: " + authedUser.getAccessToken());

                user.setUserId(authedUser.getUserId());
                user.setAccessToken(authedUser.getAccessToken());
                user.setError(null);
                user.setRoles(authedUser.getRoles());


            } catch (Exception e) {
                Log.d(TAG, "Registering... FAIL");
                Log.d(TAG, "RegisterAttempt Exception", e);
                String errorMsg = treatException(e);
                Log.d(TAG, "RegisterAttempt " + errorMsg);
                Log.d(TAG, "RegisterAttempt " + user);
                user.setError(errorMsg);
            }
            return user;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            caller.startWaiting();
        }

        @Override
        protected void onPostExecute(User user) {
            caller.stopWaiting();
            caller.notification(false, user);
        }

        @Override
        protected void onCancelled(User user) {
            Log.d(TAG, "RegisterAttempt cancelled for user :" + user.toString());
            caller.stopWaiting();
        }

        @Override
        protected void onCancelled() {
            Log.d(TAG, "RegisterAttempt cancelled");
            caller.stopWaiting();
        }
    }


}
