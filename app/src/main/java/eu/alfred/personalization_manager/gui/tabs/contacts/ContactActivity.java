package eu.alfred.personalization_manager.gui.tabs.contacts;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import eu.alfred.personalization_manager.controller.ContactsController;
import eu.alfred.personalization_manager.db_administrator.model.Address;
import eu.alfred.personalization_manager.db_administrator.model.Contact;
import eu.alfred.personalization_manager.db_administrator.model.Gender;
import eu.alfred.personalization_manager.db_administrator.model.Relation;
import eu.alfred.personalization_manager.gui.tabs.ContactsSectionFragment;
import eu.alfred.userprofile.R;

/**
 * Created by Arturo.Brotons on 02/03/2015.
 */
public class ContactActivity extends Activity {

    private static final String TAG = "ContactActivity";
    ContactsController upController;
    private MenuItem miSave;
    private MenuItem miDelete;

    private int contactPos;

    private EditText etFirstName;
    private EditText etMiddleName;
    private EditText etLastName;
    private EditText etPreferredName;
    private EditText etPhone;
    private EditText etMobilePhone;
    private EditText etEmail;
    private EditText etContactStreet;
    private EditText etContactNumber;
    private EditText etContactPostalCode;
    private EditText etContactCity;
    private EditText etContactState;
    private AutoCompleteTextView autoContactCountry;
    private EditText etContactPostalStreet;
    private EditText etContactPostalNumber;
    private EditText etContactPostalPostalCode;
    private EditText etContactPostalCity;
    private EditText etContactPostalState;
    private AutoCompleteTextView autoContactPostalCountry;
    private RadioGroup rgGender;
    private DatePicker dpDateOfBirth;
    private Spinner spRelation;
    private boolean dirty = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userprofile_contact_detail);
        String contactId = getIntent().getExtras().getString("contact-id");
        contactPos = getIntent().getExtras().getInt("contact-pos");
        EditText tvContactId = (EditText) findViewById(R.id.txt_upc_id);
        tvContactId.setText(contactId);
        tvContactId.setKeyListener(null);
        tvContactId.setVisibility(View.GONE);
        upController = new ContactsController(this);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            //TODO Logo
//            actionBar.setIcon(R.drawable.ic_launcher);
            actionBar.setDisplayHomeAsUpEnabled(true);
            if (contactPos < 0) {
                actionBar.setTitle(R.string.edit_new_contact_activity_name);
            }
//            actionBar.setLogo(R.drawable.ic_launcher);
        }
        bindFields();
        if (contactPos < 0) {
            emptyForm();
        } else {
            fillForm(contactPos);
        }

    }

    private void bindFields() {
        etFirstName = (EditText) findViewById(R.id.txtFirstName);
        etMiddleName = (EditText) findViewById(R.id.txtMiddleName);
        etLastName = (EditText) findViewById(R.id.txtLastName);
        etPreferredName = (EditText) findViewById(R.id.txtPreferredName);
        etPhone = (EditText) findViewById(R.id.txtPhone);
        etMobilePhone = (EditText) findViewById(R.id.txtMobilePhone);
        etEmail = (EditText) findViewById(R.id.txtEmail);
        etContactStreet = (EditText) findViewById(R.id.txtContactStreet);
        etContactNumber = (EditText) findViewById(R.id.txtContactNumber);
        etContactPostalCode = (EditText) findViewById(R.id.txtContactPostalCode);
        etContactCity = (EditText) findViewById(R.id.txtContactCity);
        etContactState = (EditText) findViewById(R.id.txtContactState);
        autoContactCountry = (AutoCompleteTextView) findViewById(R.id.txtContactCountry);
        etContactPostalStreet = (EditText) findViewById(R.id.txtContactPostalStreet);
        etContactPostalNumber = (EditText) findViewById(R.id.txtContactPostalNumber);
        etContactPostalPostalCode = (EditText) findViewById(R.id.txtContactPostalPostalCode);
        etContactPostalCity = (EditText) findViewById(R.id.txtContactPostalCity);
        etContactPostalState = (EditText) findViewById(R.id.txtContactPostalState);
        autoContactPostalCountry = (AutoCompleteTextView) findViewById(R.id.txtContactPostalCountry);
        rgGender = (RadioGroup) findViewById(R.id.upGenderRadioGroup);
        dpDateOfBirth = (DatePicker) findViewById(R.id.upDateOfBirth);
        spRelation = (Spinner) findViewById(R.id.spRelation);
        ArrayAdapter<Relation> adapter = new ArrayAdapter<Relation>(this, android.R.layout.simple_list_item_1, Relation.values());
        spRelation.setAdapter(adapter);
        spRelation.setSelection(adapter.getPosition(Relation.OTHER)); //Default value

        autoContactCountry.setAdapter(new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.countries_array)));

        autoContactPostalCountry.setAdapter(new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.countries_array)));

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                dirty = true;
            }
        };

        etFirstName.addTextChangedListener(watcher);
        etMiddleName.addTextChangedListener(watcher);
        etLastName.addTextChangedListener(watcher);
        etPreferredName.addTextChangedListener(watcher);
        etPhone.addTextChangedListener(watcher);
        etMobilePhone.addTextChangedListener(watcher);
        etEmail.addTextChangedListener(watcher);
        etContactStreet.addTextChangedListener(watcher);
        etContactNumber.addTextChangedListener(watcher);
        etContactPostalCode.addTextChangedListener(watcher);
        etContactCity.addTextChangedListener(watcher);
        etContactState.addTextChangedListener(watcher);
        autoContactCountry.addTextChangedListener(watcher);
        etContactPostalStreet.addTextChangedListener(watcher);
        etContactPostalNumber.addTextChangedListener(watcher);
        etContactPostalPostalCode.addTextChangedListener(watcher);
        etContactPostalCity.addTextChangedListener(watcher);
        etContactPostalState.addTextChangedListener(watcher);
        autoContactPostalCountry.addTextChangedListener(watcher);



    }

    private void fillForm(int contactPos) {
        Contact contact = ContactsSectionFragment.getContact(contactPos);
        if (contact != null) {
            if (etFirstName != null) {
                etFirstName.setText(contact.getFirstName());
            }
            if (etMiddleName != null) {
                etMiddleName.setText(contact.getMiddleName());
            }
            if (etLastName != null) {
                etLastName.setText(contact.getLastName());
            }
            if (etPreferredName != null) {
                etPreferredName.setText(contact.getPrefferedName());
            }
            if (etPhone != null) {
                etPhone.setText(contact.getPhone());
            }
            if (etMobilePhone != null) {
                etMobilePhone.setText(contact.getMobilePhone());
            }
            if (etEmail != null) {
                etEmail.setText(contact.getEmail());
            }
            if (contact.getResidentialAddress() != null) {
                Address address = contact.getResidentialAddress();
                if (etContactStreet != null) {
                    etContactStreet.setText(address.getStreet());
                }
                if (etContactNumber != null) {
                    etContactNumber.setText(address.getNumber());
                }
                if (etContactPostalCode != null) {
                    etContactPostalCode.setText(address.getPostalCode());
                }
                if (etContactCity != null) {
                    etContactCity.setText(address.getCity());
                }
                if (etContactState != null) {
                    etContactState.setText(address.getState());
                }
                if (autoContactCountry != null) {
                    autoContactCountry.setText(address.getCountry());
                }
            }
            if (contact.getPostalAddress() != null) {
                Address address = contact.getPostalAddress();
                if (etContactPostalStreet != null) {
                    etContactPostalStreet.setText(address.getStreet());
                }
                if (etContactPostalNumber != null) {
                    etContactPostalNumber.setText(address.getNumber());
                }
                if (etContactPostalPostalCode != null) {
                    etContactPostalPostalCode.setText(address.getPostalCode());
                }
                if (etContactPostalCity != null) {
                    etContactPostalCity.setText(address.getCity());
                }
                if (etContactPostalState != null) {
                    etContactPostalState.setText(address.getState());
                }
                if (autoContactPostalCountry != null) {
                    autoContactPostalCountry.setText(address.getCountry());
                }
            }
            if(spRelation != null && contact.getRelationToUser()!= null &&
                    contact.getRelationToUser().length > 0) {
                ArrayAdapter<Relation> adapter = (ArrayAdapter<Relation>) spRelation.getAdapter();
                int position = adapter.getPosition(contact.getRelationToUser()[0]);
                spRelation.setSelection(position);
            }
            if (rgGender != null) {
                if (contact.getGender() == null) {
                    rgGender.clearCheck();
                } else {
                    if (Gender.MALE.equals(contact.getGender())) {
                        RadioButton b = (RadioButton) findViewById(R.id.upGenderMale);
                        b.setChecked(true);
                    } else {
                        RadioButton b = (RadioButton) findViewById(R.id.upGenderFemale);
                        b.setChecked(true);
                    }
                }
            }
            if (dpDateOfBirth != null && contact.getDateOfBirth() != null) {
                Date date = contact.getDateOfBirth();
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                dpDateOfBirth.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)-1, cal.get(Calendar.DATE));
            }
        }
        dirty = false;
    }

    public Contact extractContact(Contact contact) {
        if (contact == null) {
            contact = new Contact();
        }
        if (etFirstName != null) {
            String str = etFirstName.getText().toString();
            if(str!=null && !"".equals(str)){
                contact.setFirstName(str);
            }

        }
        if (etMiddleName != null) {
            String str = etMiddleName.getText().toString();
            if(str!=null && !"".equals(str)){
                contact.setMiddleName(str);
            }

        }
        if (etLastName != null) {
            String str = etLastName.getText().toString();
            if(str!=null && !"".equals(str)){
                contact.setLastName(str);
            }

        }
        if (etPreferredName != null) {
            String str = etPreferredName.getText().toString();
            if(str!=null && !"".equals(str)){
                contact.setPrefferedName(str);
            }

        }
        if (etPhone != null) {
            String str = etPhone.getText().toString();
            if(str!=null && !"".equals(str)){
                contact.setPhone(str);
            }

        }
        if (etMobilePhone != null) {
            String str = etMobilePhone.getText().toString();
            if(str!=null && !"".equals(str)){
                contact.setMobilePhone(str);
            }

        }
        if (etEmail != null) {
            String str = etEmail.getText().toString();
            if(str!=null && !"".equals(str)){
                contact.setEmail(str);
            }

        }

        //Selected RadioButton --> UserProfile Gender
        if (rgGender != null) {
            RadioButton b = (RadioButton) findViewById(rgGender.getCheckedRadioButtonId());
            if (b != null) {
                String genderStr = b.getText().toString();
                Gender gender = Gender.valueOf(genderStr.toUpperCase());
                contact.setGender(gender);
            }
        }

        if (dpDateOfBirth != null) {
            long dateLong = dpDateOfBirth.getCalendarView().getDate();
            contact.setDateOfBirth(new Date(dateLong));
        }

        /**/

        Address resAddress = new Address();
        if(etContactStreet != null) {
            String str = etContactStreet.getText().toString();
            if(str!=null && !"".equals(str)){
                resAddress.setStreet(str);
            }

        }
        if(etContactNumber != null) {
            String str = etContactNumber.getText().toString();
            if(str!=null && !"".equals(str)){
                resAddress.setNumber(str);
            }

        }
        if(etContactPostalCode != null) {
            String str = etContactPostalCode.getText().toString();
            if(str!=null && !"".equals(str)){
                resAddress.setPostalCode(str);
            }

        }
        if(etContactCity != null) {
            String str = etContactCity.getText().toString();
            if(str!=null && !"".equals(str)){
                resAddress.setCity(str);
            }

        }
        if(etContactState != null) {
            String str = etContactState.getText().toString();
            if(str!=null && !"".equals(str)){
                resAddress.setState(str);
            }

        }
        if(autoContactCountry != null) {
            String str = autoContactCountry.getText().toString();
            if(str!=null && !"".equals(str)){
                resAddress.setCountry(str);
            }

        }
        if (!resAddress.isEmpty()) {
            contact.setResidentialAddress(resAddress);
        }
        /*End of RESIDENTIAL address*/

        Address postalAddress = new Address();
        if(etContactPostalStreet != null) {
            String str = etContactPostalStreet.getText().toString();
            if(str!=null && !"".equals(str)){
                postalAddress.setStreet(str);
            }

        }
        if(etContactPostalNumber != null) {
            String str = etContactPostalNumber.getText().toString();
            if(str!=null && !"".equals(str)){
                postalAddress.setNumber(str);
            }

        }
        if(etContactPostalPostalCode != null) {
            String str = etContactPostalPostalCode.getText().toString();
            if(str!=null && !"".equals(str)){
                postalAddress.setPostalCode(str);
            }

        }
        if(etContactPostalCity != null) {
            String str = etContactPostalCity.getText().toString();
            if(str!=null && !"".equals(str)){
                postalAddress.setCity(str);
            }

        }
        if(etContactPostalState != null) {
            String str = etContactPostalState.getText().toString();
            if(str!=null && !"".equals(str)){
                postalAddress.setState(str);
            }

        }
        if(autoContactPostalCountry != null) {
            String str = autoContactPostalCountry.getText().toString();
            if(str!=null && !"".equals(str)){
                postalAddress.setCountry(str);
            }

        }
        if (!postalAddress.isEmpty()) {
            contact.setPostalAddress(postalAddress);
        }

        /*End of POSTAL address*/



        /**/

        if(spRelation != null) {
            Relation[] rels = {(Relation) spRelation.getSelectedItem()};
            contact.setRelationToUser(rels);
        }

        return contact;
    }

    public void emptyForm() {

        if (etFirstName != null) {
            etFirstName.setText("");
        }
        if (etMiddleName != null) {
            etMiddleName.setText("");
        }
        if (etLastName != null) {
            etLastName.setText("");
        }
        if (etPreferredName != null) {
            etPreferredName.setText("");
        }
        if (etPhone != null) {
            etPhone.setText("");
        }
        if (etMobilePhone != null) {
            etMobilePhone.setText("");
        }
        if (etEmail != null) {
            etEmail.setText("");
        }
        if (rgGender != null) {
            rgGender.clearCheck();
        }
        if (etContactStreet != null) {
            etContactStreet.setText("");
        }
        if (etContactNumber != null) {
            etContactNumber.setText("");
        }
        if (etContactPostalCode != null) {
            etContactPostalCode.setText("");
        }
        if (etContactCity != null) {
            etContactCity.setText("");
        }
        if (etContactState != null) {
            etContactState.setText("");
        }
        if (autoContactCountry != null) {
            autoContactCountry.setText("");
        }
        if (etContactPostalStreet != null) {
            etContactPostalStreet.setText("");
        }
        if (etContactPostalNumber != null) {
            etContactPostalNumber.setText("");
        }
        if (etContactPostalPostalCode != null) {
            etContactPostalPostalCode.setText("");
        }
        if (etContactPostalCity != null) {
            etContactPostalCity.setText("");
        }
        if (etContactPostalState != null) {
            etContactPostalState.setText("");
        }
        if (autoContactPostalCountry != null) {
            autoContactPostalCountry.setText("");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.m_edit_contact, menu);
        Log.d(TAG, "onCreateOptionsMenu(" + menu.size() + " options)");
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            Log.d(TAG, "Item (" + i + "): " + item.getTitle());
        }
        miSave = menu.findItem(R.id.action_save_contact);
        miDelete = menu.findItem(R.id.action_delete_contact);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Log.d(TAG, "onOptionsItemSelected(" + item.getTitle() + ")");

        switch (item.getItemId()) {
            case R.id.action_save_contact:
                saveContact();
                return true;
            case R.id.action_delete_contact:
                attemptDeleteContact();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void attemptDeleteContact(){
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.delete_contact_attempt_title))
                .setMessage(getString(R.string.delete_contact_attempt_message))
                .setIcon(R.drawable.ic_action_warning)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        deleteContact();
                    }})
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    private void deleteContact() {
        ContactsSectionFragment.removeContact(contactPos);
        notification(true, "Contact removed");
        finish();

    }

    private void saveContact() {
        Contact contact = extractContact(null);
        ContactsSectionFragment.setContact(contactPos, contact);
        notification(true, "Contact saved");
        dirty = false;
    }

    private boolean onExitAttempt() {
        if (dirty) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.exit_edited_contact_attempt_title))
                    .setMessage(getString(R.string.exit_edited_contact_attempt_message))
                    .setIcon(R.drawable.ic_action_warning)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            finish();

                        }})
                    .setNegativeButton(android.R.string.no, null)
                    .show();
        } else {
            finish();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigateUp() {
        return onExitAttempt();
    }

    @Override
    public void onBackPressed() {
        onExitAttempt();
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
}
