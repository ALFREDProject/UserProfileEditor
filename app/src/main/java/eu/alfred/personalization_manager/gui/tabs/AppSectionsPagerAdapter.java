package eu.alfred.personalization_manager.gui.tabs;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import eu.alfred.userprofile.R;

/**
 * A {@link android.support.v4.app.FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
 * sections of the app.
 */
public class AppSectionsPagerAdapter extends FragmentPagerAdapter {

    ArrayList<SectionFragment> sections;
    private final PersonalSectionFragment sfPersonal;
    private final ContactsSectionFragment sfContact;
    private final HealthSectionFragment sfHealth;


    public AppSectionsPagerAdapter(FragmentManager fm, Context ctx) {
        super(fm);
        sections = new ArrayList<SectionFragment>();

        //General section
        sfPersonal = new PersonalSectionFragment();
        sfPersonal.setTitle(ctx.getString(R.string.tab_personal_title));
        sfPersonal.setLayout(R.layout.userprofile_personal_tab);
        sections.add(sfPersonal);

        //Contact Section
        sfContact = new ContactsSectionFragment();
        sfContact.setTitle(ctx.getString(R.string.tab_contacts_title));
        sfContact.setLayout(R.layout.userprofile_contactlist_tab);
        sections.add(sfContact);

        //Health Section
        sfHealth = new HealthSectionFragment();
        sfHealth.setTitle(ctx.getString(R.string.tab_health_title));
        sfHealth.setLayout(R.layout.userprofile_health_tab);
        sections.add(sfHealth);
    }

    public PersonalSectionFragment getSfPersonal() {
        return sfPersonal;
    }
    public ContactsSectionFragment getSfContacts() {
        return sfContact;
    }
    public HealthSectionFragment getSfHealth() {
        return sfHealth;
    }

    @Override
    public Fragment getItem(int i) {
        if (i < sections.size()) {
            return sections.get(i);
        } else {
            return null;
        }
    }

    @Override
    public int getCount() {
        return sections.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position < sections.size()) {
            return sections.get(position).getTitle();
        } else {
            return "NoTab";
        }
    }
}
