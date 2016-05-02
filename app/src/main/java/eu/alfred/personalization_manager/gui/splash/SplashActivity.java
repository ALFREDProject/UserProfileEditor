package eu.alfred.personalization_manager.gui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

import eu.alfred.personalization_manager.controller.auth.AuthController;
import eu.alfred.personalization_manager.controller.auth.AuthListener;
import eu.alfred.personalization_manager.controller.auth.User;
import eu.alfred.personalization_manager.controller.helper.SharedPrefHelper;
import eu.alfred.personalization_manager.gui.UserProfileActivity;
import eu.alfred.personalization_manager.gui.animation.AndroidUtils;
import eu.alfred.ui.AppActivity;
import eu.alfred.userprofile.R;

/**
 * This is the first activity, started by the user when pressing the ALFRED launcher.
 * It's responsible of asking the user for the username and password or allowing
 * the user to register. So only if the information provided is correct and we can get
 * a valid session, then we can go to the next Activity: UserProfileActivity.
 *
 * Note: On this context, email means Alfred Username, not just some random email.
 */
/*public class SplashActivity extends FragmentActivity implements AuthListener {*/
public class SplashActivity extends AppActivity implements AuthListener {
    final public String TAG = "UPE:SplashActivity";
    final static String START_USER_PROFILE_EDITOR = "StartEditorAction";

    /* Form fields*/
    private EditText etFirstName;
    private EditText etMiddleName;
    private EditText etLastName;

    private EditText etEmail; //Alfred Username
    private EditText etPassword;

    private AuthController controller;

    /* Local storage of previous session username and password */
    private SharedPrefHelper prefs;
    private String prefMail; //Previously Alfred Username used if any
    private String prefPassword; //Not encrypted, shouldn't be needed
    private String prefUSerId;

    private User user;

    private boolean modeIsLogin = true; //True = login menu, False = register menu

    /* Advance GUI management */
    private LinearLayout groupLogin;
    private LinearLayout groupBtRegister;
    private LinearLayout groupBtName;


    //Animation
    View progressOverlay;
    private TextView progressText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controller = new AuthController(this);
        prefs = new SharedPrefHelper(this);
        setContentView(R.layout.splash_activity);
        bindForm();

        // Animation stuff
        progressOverlay = findViewById(R.id.progress_overlay);
        progressText = (TextView) findViewById(R.id.progress_overlay_text);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        /* Retrieve the previous session info */
        prefMail = prefs.get(SharedPrefHelper.CURRENT_UP_EMAIL);
        prefPassword = prefs.get(SharedPrefHelper.CURRENT_UP_PASSWORD);
        prefUSerId = prefs.get(SharedPrefHelper.CURRENT_UP_USERID);

        if (prefMail != null && !prefMail.isEmpty()) {
            etEmail.setText(prefMail);
        }
        if (prefPassword != null && !prefPassword.isEmpty()) {
            etPassword.setText(prefPassword);
        }

        properVisibility();
        autoLogin();
    }

    //Binds form elements if the GUI to variables.
    private void bindForm() {
        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etMiddleName = (EditText) findViewById(R.id.etMiddleName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);

        groupBtName = (LinearLayout) findViewById(R.id.groupBtName);
        groupLogin = (LinearLayout) findViewById(R.id.groupBtLogin);
        groupBtRegister = (LinearLayout) findViewById(R.id.groupBtRegister);
    }

    /**
     * Tries to make login from the last session
     */
    private void autoLogin() {

        if (prefMail != null && prefPassword != null && prefUSerId != null) {
	        Log.d(TAG, "autoLogin " + prefUSerId + "/" + prefMail + "/" + prefPassword);
            user = new User(prefMail, prefPassword);
            Log.d(TAG, String.format("User %s found. Logging in...", prefMail));
            controller.login(user);
        }
    }

    /**
     * When logout, we clear completely the credentials.
     */
    private void clearCredentials() {
        prefs.delete(SharedPrefHelper.CURRENT_UP_EMAIL);
        prefs.delete(SharedPrefHelper.CURRENT_UP_PASSWORD);
        prefs.delete(SharedPrefHelper.CURRENT_UP_USERID);
    }

    /**
     * Called when the user taps on Login button.
     * @param view needed so in the XML layout we can bind a button to a Java method.
     */
    public void login(View view) {
        Log.d(TAG, "Logging in...");
        if (checkForm()) {

            user = new User();

            user.setEmail(etEmail.getText().toString());
            user.setPassword(etPassword.getText().toString());
            Log.d(TAG, String.format("User: Email: %s, Passw: %s", user.getEmail(), user.getPassword()));
            controller.login(user);
        }
    }

    /**
     * Called when the user taps on Register button.
     * @param view needed so in the XML layout we can bind a button to a Java method.
     */
    public void register(View view) {
        if (checkForm()) {

            user = new User();

            user.setFirstName(etFirstName.getText().toString());
            user.setMiddleName(etMiddleName.getText().toString());
            user.setLastName(etLastName.getText().toString());
            user.setEmail(etEmail.getText().toString());
            user.setPassword(etPassword.getText().toString());

            //At least, one role is needed when registering due to Auth Server restrictions
            user.addRole("Developer"); // TODO Show proper Spinner or change to a proper role

	        Log.d(TAG, "register user " + user);
            controller.register(user);
        }
    }

    /**
     * Validates the form (non empty fields, correct email format, etc).
     * @return true if the form is correct, or false otherwise.
     */
    private boolean checkForm() {
        boolean emailCorrect, passwordCorrect, firstNameCorrect, lastNameCorrect, emailFormatCorrect;
        emailCorrect = isEditTextEmpty(etEmail);
        emailFormatCorrect = checkEmail(etEmail.getText().toString());
        passwordCorrect = isEditTextEmpty(etPassword);
        firstNameCorrect = isEditTextEmpty(etFirstName);
        lastNameCorrect = isEditTextEmpty(etLastName);
        if (!modeIsLogin && !firstNameCorrect) {
            markEditText(etFirstName);
        }
        if (!modeIsLogin && !lastNameCorrect) {
            markEditText(etLastName);
        }
        if (!emailCorrect || !emailFormatCorrect) {
            markEditText(etEmail);
        }
        if (!passwordCorrect) {
            markEditText(etPassword);
        }

        if (!emailFormatCorrect) {
            notification(false, "Email address not valid");
        }

        if(modeIsLogin)
            return emailCorrect && emailFormatCorrect && passwordCorrect;
        else
            return emailCorrect && emailFormatCorrect && passwordCorrect && firstNameCorrect && lastNameCorrect;
    }

    private boolean checkEmail(String text) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches();
    }

    /*
        Animation stuff, it darkens the screen and shows a spinning wheel that indicates that
        the server is being connected and the App is waiting for some login / register result.
     */
    @Override
    public void startWaiting() {
        Log.d(TAG, "startWaiting()");
        groupLogin.setEnabled(false);
        groupBtRegister.setEnabled(false);
        etEmail.setEnabled(false);
        etPassword.setEnabled(false);
        etFirstName.setEnabled(false);
        etMiddleName.setEnabled(false);
        etLastName.setEnabled(false);

        if (modeIsLogin) { //FIXME Message does not appear
            progressText.setText(R.string.progress_overlay_text_content_login);
        } else {
            progressText.setText(R.string.progress_overlay_text_content_register);
        }

        AndroidUtils.animateView(progressOverlay, View.VISIBLE, 0.9f, 1000);
    }

    /*
        Animation stuff, restores the visibility, hides any spinning wheel and makes
        all edit texts (fields) editable again.
     */
    @Override
    public void stopWaiting() {
        Log.d(TAG, "stopWaiting()");
        AndroidUtils.animateView(progressOverlay, View.GONE, 0, 1000);

        groupLogin.setEnabled(true);
        groupBtRegister.setEnabled(true);
        etEmail.setEnabled(true);
        etPassword.setEnabled(true);
        etFirstName.setEnabled(true);
        etMiddleName.setEnabled(true);
        etLastName.setEnabled(true);
    }

    private boolean isEditTextEmpty(EditText editText) {
        return editText != null &&
                editText.getText() != null &&
                !editText.getText().toString().isEmpty();
    }


    /**
     * It marks the edit text as incorrect (white text over red background) and adds some listener,
     * so if the user changes the text, the color scheme is restored.
     *
     * @param editText to mark.
     */
    private void markEditText(final EditText editText) {
        editText.setHintTextColor(getResources().getColor(R.color.incorrect_form_field_hint));
        editText.setBackgroundColor(getResources().getColor(R.color.incorrect_form_field_bg));
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Empty on purpose
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Empty on purpose
            }

            @Override
            public void afterTextChanged(Editable s) {
                editText.setBackgroundColor(getResources().getColor(R.color.transparent));
                editText.setHintTextColor(getResources().getColor(R.color.darkgray));
                editText.removeTextChangedListener(this);
            }
        });
    }

    /**
     * Once the login or registration is successful, the main activity is launched passing some
     * information as extra fields to the UserProfileActivity.
     * @param existingUser true: we need to retrieve the user from server, false: we're creating it
     */
    private void openUserProfileActivity(boolean existingUser) {

        Intent intent = new Intent(this, UserProfileActivity.class);
        intent.putExtra("userId", user.getUserId());
        intent.putExtra("accessToken", user.getAccessToken());
        intent.putExtra("existingUser", existingUser);
        if (!existingUser) {
            intent.putExtra("firstName", user.getFirstName());
            intent.putExtra("middleName", user.getMiddleName());
            intent.putExtra("lastName", user.getLastName());
        }
        intent.putExtra("email", user.getEmail());

        //Put here other extra params

	    Log.d(TAG, "open UserProfileActivity with user " + user);
        startActivityForResult(intent, 1);
    }

    /**
     * When the main activity UserProfileActivity is closed we need to know if the User
     * made logout or not, to clear the login credentials.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, String.format("onActivityResult {request: %d, result %d}", requestCode, resultCode));
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("result");
                Log.d(TAG, String.format("onActivityResult {result: %s}", result));
                if ("logout".equals(result)) {
                    clearCredentials();
                }

                //If recently registered user
                //Show login form instead of register form
                if (!modeIsLogin) switchMode();

            }
        }
    }

    /**
     * TODO It should cancel the request of Login / Register.
     * @param view needed so in the XML layout we can bind a button to a Java method.
     */
    public void cancel(View view) {
        controller.cancel();
    }

    /**
     * Switches between login and register mode
     * @param view needed so in the XML layout we can bind a button to a Java method.
     */
    public void switchMode(View view) {
        switchMode();
    }

    public void switchMode() {
        Log.d(TAG, "switchMode " + (modeIsLogin ? "Login -> Register" : "Register -> Login"));
        modeIsLogin = !modeIsLogin;
        properVisibility();
    }

    private void properVisibility() {
        if (modeIsLogin) {
            Log.d(TAG, "properVisibility for Login");
            groupBtName.setVisibility(View.GONE);
            groupBtRegister.setVisibility(View.GONE);
            groupLogin.setVisibility(View.VISIBLE);
        } else {
            Log.d(TAG, "properVisibility for Register");
            groupBtName.setVisibility(View.VISIBLE);
            groupBtRegister.setVisibility(View.VISIBLE);
            groupLogin.setVisibility(View.GONE);
        }
    }

    public void notification(boolean succeed, int resId) {
        String msg = getResources().getString(resId);
        notification(succeed, msg);
    }

    /**
     * Shows a Toast (temporary information message) with big green tick when success
     * or a big red cross when fails and a text message.
     * @param succeed true: success, false: fail
     * @param msg Message String to show
     */
    public void notification(boolean succeed, String msg) {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.toast_custom,
                (ViewGroup) findViewById(R.id.toastLayout));
        if (!succeed) {
            ImageView toastImg = (ImageView) view.findViewById(R.id.toastImg);
            toastImg.setImageResource(R.drawable.ic_toast_error);
        }

        ((TextView) view.findViewById(R.id.toastText)).setText(msg);

        Toast toast = new Toast(this);
        toast.setDuration(succeed?Toast.LENGTH_SHORT:Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }

    /**
     * mode is True when Login, False when Register
     * @param mode True when Login, False when Register
     * @param user from AuthenticationServer to create/edit their User Profile
     */
    @Override
    public void notification(boolean mode, User user) {
	    Log.d(TAG, "notification (mode " + (mode ? "login" : "registration") + "), user: " + user);
        if (user.getError() == null) { //All ok
            String msg = mode ? "Login succeed!" : "Registration succeed!";
            notification(true, msg);
            prefs.put(SharedPrefHelper.CURRENT_UP_EMAIL, user.getEmail());
            prefs.put(SharedPrefHelper.CURRENT_UP_PASSWORD, user.getPassword());
            prefs.put(SharedPrefHelper.CURRENT_UP_USERID, user.getUserId());
/*            prefs.put(GlobalsettingsKeys.userId, user.getUserId());*/

            openUserProfileActivity(mode);
        } else {
            notification(false, user.getError());
        }
    }
    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public void performAction(String s, Map<String, String> map) {
        Log.d("Perform Action string", s);
        switch (s) {
            case START_USER_PROFILE_EDITOR:
                Log.d("DDD Response to"," START EDITOR");
                break;
            default:
                break;
        }

        cade.sendActionResult(true);
    }

    @Override
    public void performWhQuery(String s, Map<String, String> map) {
        Log.d("Wh Query", s);
        cade.sendActionResult(true);
    }

    @Override
    public void performValidity(String s, Map<String, String> map) {
        Log.d("Perform Validity", "works!");
    }

    @Override
    public void performEntityRecognizer(String s, Map<String, String> map) {
        Log.d("Perform Entity Recog", "works!");
    }
}
