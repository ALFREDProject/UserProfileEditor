package eu.alfred.personalization_manager.gui.pref;

import android.app.ActionBar;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import eu.alfred.userprofile.R;

/**
 * Created by Arturo.Brotons on 15/04/2015.
 */
public class SettingsActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.settings);
    }

    @Override
    public boolean onNavigateUp() {
        finish();
        return true;
    }
}
