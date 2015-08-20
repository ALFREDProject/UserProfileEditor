package eu.alfred.personalization_manager.gui;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import eu.alfred.internal.wrapper.authentication.AuthenticatedUser;
import eu.alfred.internal.wrapper.authentication.AuthenticationException;
import eu.alfred.internal.wrapper.authentication.AuthenticationServerWrapper;
import eu.alfred.internal.wrapper.authentication.login.LoginData;
import eu.alfred.internal.wrapper.authentication.login.LoginDataException;
import eu.alfred.internal.wrapper.healthmonitor.DataType;
import eu.alfred.internal.wrapper.healthmonitor.HealthMonitorServerException;
import eu.alfred.internal.wrapper.healthmonitor.HealthMonitorServerWrapper;
import eu.alfred.internal.wrapper.healthmonitor.resource.Resource;
import eu.alfred.internal.wrapper.healthmonitor.resource.Value;
import eu.alfred.personalization_manager.controller.UserProfileController;
import eu.alfred.personalization_manager.controller.auth.User;
import eu.alfred.personalization_manager.db_administrator.model.UserProfile;
import eu.alfred.personalization_manager.gui.pref.SettingsActivity;
import eu.alfred.personalization_manager.gui.tabs.AppSectionsPagerAdapter;
import eu.alfred.personalization_manager.gui.tabs.contacts.ContactActivity;
import eu.alfred.userprofile.R;


public class UserProfileActivity extends FragmentActivity implements ActionBar.TabListener {

    final public String TAG = "UserProfileActivity";


/*    private Button btNew;
    private Button btSave;
    private Button btDelete;*/

    UserProfileController upController;
    private MenuItem miNew;
    private MenuItem miSave;
    private MenuItem miDelete;
    private MenuItem miSettings;
    private MenuItem miLogout;
    private AppSectionsPagerAdapter mSections;
    ViewPager mViewPager;

    /**
     * First method called when running an App
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        /*intent.putExtra("userId", user.getUserId());
        intent.putExtra("accessToken", user.getAccessToken());
        intent.putExtra("existingUser", existingUser);
        if (!existingUser) {
            intent.putExtra("firstName", user.getFirstName());
            intent.putExtra("middleName", user.getMiddleName());
            intent.putExtra("lastName", user.getLastName());
        }
        intent.putExtra("email", user.getEmail());*/
        String userId, accessToken,
                email = null,
                firstName = null,
                middleName = null,
                lastName = null;
        User user = new User();
        userId = getIntent().getExtras().getString("userId");
        user.setUserId(userId);

        accessToken = getIntent().getExtras().getString("accessToken");
        user.setAccessToken(accessToken);

        boolean existingUser = getIntent().getExtras().getBoolean("existingUser");
        email = getIntent().getExtras().getString("email");
        user.setEmail(email);

        if (!existingUser) {
            firstName = getIntent().getExtras().getString("firstName");
            user.setFirstName(firstName);

            middleName = getIntent().getExtras().getString("middleName");
            user.setMiddleName(middleName);

            lastName = getIntent().getExtras().getString("lastName");
            user.setLastName(lastName);
        }

        Log.d(TAG, "Put extra user was: " + user.toString());

        setContentView(R.layout.activity_main);
        setTitle(R.string.activity_name);

        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mSections = new AppSectionsPagerAdapter(getSupportFragmentManager(), this);

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home/Up button should not be enabled, since there is no hierarchical
        // parent.
        actionBar.setHomeButtonEnabled(false);

        // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
        // user swipes between sections.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSections);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                hideSoftKeyboard(); //To save space on screen
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSections.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSections.getPageTitle(i))
                            .setTabListener(this));
        }

        upController = new UserProfileController(this);

        //TODO Logo
/*        actionBar.setIcon(R.drawable.ic_launcher);
        actionBar.setLogo(R.drawable.ic_launcher);*/

        //bindViewsToFields();

//        upController = UserControllerFactory.create(this);

        upController.setContactsFragment(mSections.getSfContacts());
        upController.setHealthFragment(mSections.getSfHealth());
        if (existingUser) {
            upController.initRetrieving(user);
        } else {
            upController.initCreating(user);
        }
    }




    /**
     * If first run of the App, onCreate() -> onStart().
     * If App comes from background, onRestart() -> onStart().
     */
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.m_user_profile, menu);
        Log.d(TAG, "onCreateOptionsMenu(" + menu.size() + " options)");
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            Log.d(TAG, "Item (" + i + "): " + item.getTitle());
        }
        miNew = menu.findItem(R.id.action_new_profile);
        miSave = menu.findItem(R.id.action_save_profile);
        miDelete = menu.findItem(R.id.action_delete_profile);
        miSettings = menu.findItem(R.id.action_settings);
        miLogout = menu.findItem(R.id.action_logout);
        setMenuItemsVisibleForEditing(false);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.d(TAG, "onPrepareOptionsMenu()");
        miNew = menu.findItem(R.id.action_new_profile);
        miSave = menu.findItem(R.id.action_save_profile);
        miDelete = menu.findItem(R.id.action_delete_profile);
        miSettings = menu.findItem(R.id.action_settings);
        miLogout = menu.findItem(R.id.action_logout);
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Hides or shows action menu items: New / Save and Delete
     *
     * @param editMode false if it's a new user profile, true if it's for editing the existing one.
     */
    public void setMenuItemsVisibleForEditing(boolean editMode) {
        Log.d(TAG, String.format("setMenuItemsVisibleForEditing(%b)", editMode));
//        if (miNew != null) miNew.setVisible(!editMode);
        if (miSave != null) miSave.setVisible(editMode);
//        if (miDelete != null) miDelete.setVisible(editMode);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Log.d(TAG, "onOptionsItemSelected(" + item.getTitle() + ")");

        switch (item.getItemId()) {
            case R.id.action_new_profile:
                newProfile();
                return true;
            case R.id.action_save_profile:
                saveProfile();
                return true;
            case R.id.action_delete_profile:
                onAttemptDeletingProfile();
                return true;
            case R.id.action_settings:
                openSettings();
                return true;
            case R.id.action_logout:
                onAttemptLoggingOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openSettings() {
        // Display the fragment as the main content.
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void newProfile() {
        Log.d(TAG, "newProfile()");
        UserProfile up = extractForm();
        upController.newProfile(up);
    }

    private void onAttemptDeletingProfile(){
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.delete_profile_attempt_title))
                .setMessage(getString(R.string.delete_profile_attempt_message))
                .setIcon(R.drawable.ic_action_warning)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        deleteProfile();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void onAttemptLoggingOut() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.logout_attempt_title))
                .setMessage(getString(R.string.logout_attempt_message))
                .setIcon(R.drawable.ic_action_warning)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        logout();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void logout() {
        Log.d(TAG, "logout()");
        upController.logout();
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result","logout");
        setResult(RESULT_OK,returnIntent);
        finish();
    }

    private void deleteProfile() {
        Log.d(TAG, "deleteProfile()");
        UserProfile up = extractForm();
        upController.deleteProfile(up);
    }

    private void saveProfile() {
        Log.d(TAG, "saveProfile()");
        UserProfile up = extractForm();
        upController.updateProfile(up);
    }

    public void fillForm(UserProfile up) {
        mSections.getSfPersonal().fillForm(up);

    }

    private UserProfile extractForm() {
        UserProfile up = new UserProfile();
        up.setId(upController.getStoredUserProfileId());

        up = mSections.getSfPersonal().extractProfile(up);

        return up;
    }

    public void emptyForm() {
        mSections.getSfPersonal().emptyForm();
    }

    public void fillFormId(String upId) {

    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }



    public void addContact(View view) {
        Log.d(TAG, "New contact");
        Intent intent = new Intent(this, ContactActivity.class);
        String userId = upController.getStoredUserProfileId();
        intent.putExtra("contact-id", "new");
        intent.putExtra("contact-pos", -1);
        intent.putExtra("user-id", userId);
        startActivity(intent);
    }

    public void tryMe(View view) {
        Runnable runnable = new Runnable() {
            public void run() {
                Log.d(TAG, "start: ");
                AuthenticationServerWrapper authWrapper = new AuthenticationServerWrapper();
                List<String> roles = new ArrayList<String>();
                try {
                    LoginData data = new LoginData
                            .Builder()
                            .setEmail("artur.brotons@gmail.com")
                            .setPassword("abcartagena2password")
                            .create();
                    Log.d(TAG, "Authenticating...");
                    AuthenticatedUser authedUser = authWrapper.login(data);
                    Log.d(TAG, "Authed: " + authedUser.getUserId());
                    Log.d(TAG, "Authed: " + authedUser.getAccessToken());

                    Log.d(TAG, "Health Wrapper creating");
                    HealthMonitorServerWrapper healthWrapper = new HealthMonitorServerWrapper(authedUser.getUserId(), authedUser.getAccessToken());
//                    HealthMonitorServerWrapper healthWrapper = new HealthMonitorServerWrapper("55a79aaaa65be2261b375d72", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJfaWQiOiI1NWE3OWFhYWE2NWJlMjI2MWIzNzVkNzIiLCJpYXQiOjE0MzcxMzEyMDc1ODgsImV4cCI6MTQzNzE0OTIwNzU4OH0.BRPmQFEmx605LMrcDvu2o42sHeCI5RXpub-SLR1-IaY");
                    Log.d(TAG, "Health Wrapper created");
                    Log.d(TAG, "Health Wrapper getting resource list");
                    List<String> resourceList = healthWrapper.getResourceList(DataType.STATIC);
                    Log.d(TAG, "Health Wrapper acquired resource list");
                    for (String s : resourceList) {
                        Log.d(TAG, s + " (Health Wrapper before)");
                    }
//                    Resource ccRes = healthWrapper.createHealthMonitorServerResource("fallriskscore", "Fall Risk Score", DataType.STATIC, "Float");
//                    Value<Float> val = new Value<Float>(3.7f);
//                    healthWrapper.updateResource(ccRes, val);

//                    Value<String> val2 = new Value<String>("Alzheimer");
//                    healthWrapper.updateResource(ccRes, val2);

                    Log.d(TAG, "Health Wrapper getting resource list");
                    List<String> resourceList2 = healthWrapper.getResourceList(DataType.STATIC);
                    Log.d(TAG, "Health Wrapper acquired resource list");
                    for (String s : resourceList2) {
                        Log.d(TAG, s + " (Health Wrapper after)");
                        Resource resource = healthWrapper.getResource(s, null);
                        for (Value value : resource.getValues()) {
                            Log.d(TAG, resource.getResourceName() + "<" + resource.getUnit() + ">: " + value.getStringValue());
                        }

                    }


                } catch (LoginDataException e) {
                    e.printStackTrace();
                } catch (AuthenticationException e) {
                    e.printStackTrace();
                } catch (HealthMonitorServerException e) {
                    e.printStackTrace();
                }
            }
        };

        new Thread(runnable).start();

    }

    /**
     * Hides the soft keyboard
     */
    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
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
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed Called");
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }

    public UserProfileController getUserProfileController() {
        return upController;
    }
}
