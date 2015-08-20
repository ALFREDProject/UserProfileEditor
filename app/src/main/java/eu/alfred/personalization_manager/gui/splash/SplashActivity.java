package eu.alfred.personalization_manager.gui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
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

import eu.alfred.personalization_manager.controller.auth.AuthController;
import eu.alfred.personalization_manager.controller.auth.AuthListener;
import eu.alfred.personalization_manager.controller.auth.User;
import eu.alfred.personalization_manager.controller.helper.SharedPrefHelper;
import eu.alfred.personalization_manager.gui.UserProfileActivity;
import eu.alfred.personalization_manager.gui.animation.AndroidUtils;
import eu.alfred.userprofile.R;

/**
 * Created by Arturo.Brotons on 31/07/2015.
 */
public class SplashActivity extends FragmentActivity implements AuthListener {
    final public String TAG = "SplashActivity";
    private EditText etEmail;
    private EditText etPassword;
    private AuthController controller;
    private User user;
    private boolean modeIsLogin = true; //True = login menu, False = register menu
    private LinearLayout groupLogin;
    private LinearLayout groupBtRegister;
    private SharedPrefHelper prefs;
    private EditText etFirstName;
    private EditText etMiddleName;
    private EditText etLastName;
    private LinearLayout groupBtName;


    //Animation
    View progressOverlay;
    private String prefMail;
    private String prefPassword;
    private String prefUSerId;
    private TextView progressText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controller = new AuthController(this);
        prefs = new SharedPrefHelper(this);
        setContentView(R.layout.splash_activity);

        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etMiddleName = (EditText) findViewById(R.id.etMiddleName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);

        groupBtName = (LinearLayout) findViewById(R.id.groupBtName);
        groupLogin = (LinearLayout) findViewById(R.id.groupBtLogin);
        groupBtRegister = (LinearLayout) findViewById(R.id.groupBtRegister);

        progressOverlay = findViewById(R.id.progress_overlay);
        progressText = (TextView) findViewById(R.id.progress_overlay_text);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
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

    private void autoLogin() {


        if (prefMail != null && prefPassword != null && prefUSerId != null) {
            user = new User(prefMail, prefPassword);
            Log.d(TAG, String.format("User %s found. Logging in...", prefMail));
            controller.login(user);
        }
    }

    private void clearCredentials() {
        prefs.delete(SharedPrefHelper.CURRENT_UP_EMAIL);
        prefs.delete(SharedPrefHelper.CURRENT_UP_PASSWORD);
        prefs.delete(SharedPrefHelper.CURRENT_UP_USERID);
    }

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

    public void register(View view) {
        if (checkForm()) {

            user = new User();

            user.setFirstName(etFirstName.getText().toString());
            user.setMiddleName(etMiddleName.getText().toString());
            user.setLastName(etLastName.getText().toString());
            user.setEmail(etEmail.getText().toString());
            user.setPassword(etPassword.getText().toString());
            user.addRole("Developer");

            controller.register(user);
        }
    }

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


    private void markEditText(final EditText editText) {
        editText.setHintTextColor(getResources().getColor(R.color.incorrect_form_field_hint));
        editText.setBackgroundColor(getResources().getColor(R.color.incorrect_form_field_bg));
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                editText.setBackgroundColor(getResources().getColor(R.color.transparent));
                editText.setHintTextColor(getResources().getColor(R.color.darkgray));
                editText.removeTextChangedListener(this);
            }
        });
    }

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

        //TODO put other extra params
//        startActivity(intent);
        startActivityForResult(intent, 1);
    }

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

    public void cancel(View view) {
        controller.cancel();
    }

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
            Log.d(TAG, "");
            groupBtName.setVisibility(View.VISIBLE);
            groupBtRegister.setVisibility(View.VISIBLE);
            groupLogin.setVisibility(View.GONE);
        }
    }

    public void notification(boolean succeed, int resId) {
        String msg = getResources().getString(resId);
        notification(succeed, msg);
    }

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
        if (user.getError() == null) { //All ok
            String msg = mode ? "Login succeed!" : "Registration succeed!";
            notification(true, msg);
            prefs.put(SharedPrefHelper.CURRENT_UP_EMAIL, user.getEmail());
            prefs.put(SharedPrefHelper.CURRENT_UP_PASSWORD, user.getPassword());
            prefs.put(SharedPrefHelper.CURRENT_UP_USERID, user.getUserId());

            openUserProfileActivity(mode);
        } else {
            notification(false, user.getError());
        }
    }
}
