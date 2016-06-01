package eu.alfred.personalization_manager.gui.tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import eu.alfred.api.personalization.model.UserProfile;
import eu.alfred.api.personalization.model.Address;
import eu.alfred.api.personalization.model.EducationLevel;
import eu.alfred.api.personalization.model.EmploymentStatus;
import eu.alfred.api.personalization.model.Gender;
import eu.alfred.api.personalization.model.Language;
import eu.alfred.api.personalization.model.MaritalStatus;
import eu.alfred.api.personalization.model.MobilityLevel;
import eu.alfred.api.personalization.model.MyersBriggsTypeIndicator;
import eu.alfred.upeditor.R;

/**
* Created by Arturo.Brotons on 26/02/2015.
*/
public class PersonalSectionFragment extends SectionFragment {

    private Spinner spLanguage;
    private Spinner spMaritalStatus;
    private Spinner spEducationLevel;
    private Spinner spEmploymentStatus;
    private Spinner spMobilityLevel;
    private Spinner spMyersBriggsIndicator;

    private EditText etFirstName;
    private EditText etMiddleName;
    private EditText etLastName;
    private EditText etPreferredName;
    private RadioGroup rgGender;
    private DatePicker dpDateOfBirth;
    private EditText etPhone;
    private EditText etMobilePhone;
    private EditText etUsername;
    private EditText etEmail;
    private View mView;
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
    private AutoCompleteTextView autoCitizenship;
    private AutoCompleteTextView autoNationality;
    private EditText etSocialSecurityNumber;
    private EditText etProfession;
    private EditText etHealthInsurance;

    private DatePicker dpAnniversaryDate;
    private String upId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);
        bindViews(view);
        setSpinnersContent(view);
        hideUnnecessaryFields(view);
        mView = view;
        return view;
    }

    private void hideUnnecessaryFields(View view) {
        View up_status_socialSecurityIdentifier_label = view.findViewById(R.id.up_status_socialSecurityIdentifier_label);
        if(up_status_socialSecurityIdentifier_label!=null) {
            up_status_socialSecurityIdentifier_label.setVisibility(View.GONE);
        }
        View txtSocialSecurityIdentifier = view.findViewById(R.id.txtSocialSecurityIdentifier);
        if(txtSocialSecurityIdentifier!=null) {
            txtSocialSecurityIdentifier.setVisibility(View.GONE);
        }
        View up_myers_briggs_indicator_label = view.findViewById(R.id.up_myers_briggs_indicator_label);
        if(up_myers_briggs_indicator_label!=null) {
            up_myers_briggs_indicator_label.setVisibility(View.GONE);
        }
        View txtMyersBriggsIndicator = view.findViewById(R.id.txtMyersBriggsIndicator);
        if(txtMyersBriggsIndicator!=null) {
            txtMyersBriggsIndicator.setVisibility(View.GONE);
        }
        View upAnniversaryDateLabel = view.findViewById(R.id.upAnniversaryDateLabel);
        if(upAnniversaryDateLabel!=null) {
            upAnniversaryDateLabel.setVisibility(View.GONE);
        }
        View upAnniversaryDate = view.findViewById(R.id.upAnniversaryDate);
        if(upAnniversaryDate!=null) {
            upAnniversaryDate.setVisibility(View.GONE);
        }
    }

    private void bindViews(View view) {
        etFirstName = (EditText) view.findViewById(R.id.txtFirstName);
        etMiddleName = (EditText) view.findViewById(R.id.txtMiddleName);
        etLastName = (EditText) view.findViewById(R.id.txtLastName);
        etPreferredName = (EditText) view.findViewById(R.id.txtPreferredName);
        rgGender = (RadioGroup) view.findViewById(R.id.upGenderRadioGroup);
        dpDateOfBirth = (DatePicker) view.findViewById(R.id.upDateOfBirth);
        dpAnniversaryDate = (DatePicker) view.findViewById(R.id.upAnniversaryDate);
        etPhone = (EditText) view.findViewById(R.id.txtPhone);
        etMobilePhone = (EditText) view.findViewById(R.id.txtMobilePhone);

        etUsername = (EditText) view.findViewById(R.id.txtUsername);
        etUsername.setKeyListener(null); //This prevents the user to modify their user profile.
        etUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEmailInfo();
            }
        });
        /***/
        etEmail = (EditText) view.findViewById(R.id.txtEmail);
        etContactStreet = (EditText) view.findViewById(R.id.txtContactStreet);
        etContactNumber = (EditText) view.findViewById(R.id.txtContactNumber);
        etContactPostalCode = (EditText) view.findViewById(R.id.txtContactPostalCode);
        etContactCity = (EditText) view.findViewById(R.id.txtContactCity);
        etContactState = (EditText) view.findViewById(R.id.txtContactState);
        autoContactCountry = (AutoCompleteTextView) view.findViewById(R.id.txtContactCountry);
        etContactPostalStreet = (EditText) view.findViewById(R.id.txtContactPostalStreet);
        etContactPostalNumber = (EditText) view.findViewById(R.id.txtContactPostalNumber);
        etContactPostalPostalCode = (EditText) view.findViewById(R.id.txtContactPostalPostalCode);
        etContactPostalCity = (EditText) view.findViewById(R.id.txtContactPostalCity);
        etContactPostalState = (EditText) view.findViewById(R.id.txtContactPostalState);
        autoContactPostalCountry = (AutoCompleteTextView) view.findViewById(R.id.txtContactPostalCountry);
        autoCitizenship = (AutoCompleteTextView) view.findViewById(R.id.txtCitizenship);
        autoNationality = (AutoCompleteTextView) view.findViewById(R.id.txtNationality);
        etSocialSecurityNumber = (EditText) view.findViewById(R.id.txtSocialSecurityIdentifier);

        etProfession = (EditText) view.findViewById(R.id.txtProfession);
        etHealthInsurance = (EditText) view.findViewById(R.id.txtHealthInsurance);

        autoContactCountry.setAdapter(new ArrayAdapter<String>(
                view.getContext(),
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.countries_array)));

        autoContactPostalCountry.setAdapter(new ArrayAdapter<String>(
                view.getContext(),
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.countries_array)));
        autoCitizenship.setAdapter(new ArrayAdapter<String>(
                view.getContext(),
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.nationalities_array)));
        autoNationality.setAdapter(new ArrayAdapter<String>(
                view.getContext(),
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.nationalities_array)));

    }

    private void setSpinnersContent(View view) {
        spLanguage = (Spinner) view.findViewById(R.id.txtLanguage);
        spLanguage.setAdapter(new ArrayAdapter<Language>(view.getContext(), android.R.layout.simple_list_item_1, Language.values()));

        spMaritalStatus = (Spinner) view.findViewById(R.id.txtMaritalStatus);
        spMaritalStatus.setAdapter(new ArrayAdapter<MaritalStatus>(view.getContext(), android.R.layout.simple_list_item_1, MaritalStatus.values()));

        spEducationLevel = (Spinner) view.findViewById(R.id.txtEducationLevel);
        spEducationLevel.setAdapter(new ArrayAdapter<EducationLevel>(view.getContext(), android.R.layout.simple_list_item_1, EducationLevel.values()));

        spEmploymentStatus = (Spinner) view.findViewById(R.id.txtEmploymentStatus);
        spEmploymentStatus.setAdapter(new ArrayAdapter<EmploymentStatus>(view.getContext(), android.R.layout.simple_list_item_1, EmploymentStatus.values()));

        spMobilityLevel = (Spinner) view.findViewById(R.id.txtMobilityLevel);
        spMobilityLevel.setAdapter(new ArrayAdapter<MobilityLevel>(view.getContext(), android.R.layout.simple_list_item_1, MobilityLevel.values()));

        spMyersBriggsIndicator = (Spinner) view.findViewById(R.id.txtMyersBriggsIndicator);
        spMyersBriggsIndicator.setAdapter(new ArrayAdapter<MyersBriggsTypeIndicator>(view.getContext(), android.R.layout.simple_list_item_1, MyersBriggsTypeIndicator.values()));
    }

    public void fillForm(UserProfile up) {
        //        up = upController.load(userId);
        if (up != null) {
            upId = up.getId();
            if(etFirstName!=null) {
                etFirstName.setText(up.getFirstName());
            }
            if(etMiddleName!=null) {
                etMiddleName.setText(up.getMiddleName());
            }
            if(etLastName!=null) {
                etLastName.setText(up.getLastName());
            }
            if(etPreferredName!=null) {
                etPreferredName.setText(up.getPrefferedName());
            }
            if(etPhone!=null) {
                etPhone.setText(up.getPhone());
            }
            if(etMobilePhone!=null) {
                etMobilePhone.setText(up.getMobilePhone());
            }
            if(etUsername!=null) {
                etUsername.setText(up.getAlfredUserName());
            }
            if(etEmail!=null) {
                etEmail.setText(up.getEmail());
            }
            if (up.getGender() == null) {
                rgGender.clearCheck();
            } else {
                if (Gender.MALE.equals(up.getGender())) {
                    RadioButton b = (RadioButton) mView.findViewById(R.id.upGenderMale);
                    b.setChecked(true);
                } else {
                    RadioButton b = (RadioButton) mView.findViewById(R.id.upGenderFemale);
                    b.setChecked(true);
                }
            }
            if (dpDateOfBirth != null && up.getDateOfBirth() != null) {
                Date date = up.getDateOfBirth();
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                dpDateOfBirth.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
            }
            if (dpAnniversaryDate != null && up.getAnniversaryDate() != null) {
                Date date = up.getAnniversaryDate();
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                dpAnniversaryDate.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
            }

            /**/

            if (up.getResidentialAddress() != null) {
                Address address = up.getResidentialAddress();
                if(etContactStreet != null){
                    etContactStreet.setText(address.getStreet());
                }
                if(etContactNumber != null){
                    etContactNumber.setText(address.getNumber());
                }
                if(etContactPostalCode != null){
                    etContactPostalCode.setText(address.getPostalCode());
                }
                if(etContactCity != null){
                    etContactCity.setText(address.getCity());
                }
                if(etContactState != null){
                    etContactState.setText(address.getState());
                }
                if(autoContactCountry != null){
                    autoContactCountry.setText(address.getCountry());
                }
            }
            if (up.getPostalAddress() != null) {
                Address address = up.getPostalAddress();
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
            if(autoCitizenship != null){
                autoCitizenship.setText(up.getCitizenship());
            }
            if(autoNationality != null){
                autoNationality.setText(up.getNationality());
            }
            if(etSocialSecurityNumber != null){
                etSocialSecurityNumber.setText(up.getSocialSecurityNumber());
            }
            if(etProfession != null){
                etProfession.setText(up.getProfession());
            }
            if(etHealthInsurance != null){
                etHealthInsurance.setText(up.getHealthInsurance());
            }

            /**/

            if(spLanguage != null && up.getLanguage()!= null) {
                ArrayAdapter<Language> adapter = (ArrayAdapter<Language>) spLanguage.getAdapter();
                int position = adapter.getPosition(up.getLanguage());
                spLanguage.setSelection(position);
            }
            if(spMaritalStatus != null && up.getMaritalStatus()!= null) {
                ArrayAdapter<MaritalStatus> adapter = (ArrayAdapter<MaritalStatus>) spMaritalStatus.getAdapter();
                int position = adapter.getPosition(up.getMaritalStatus());
                spMaritalStatus.setSelection(position);
            }
            if(spEducationLevel != null && up.getEducationLevel()!= null) {
                ArrayAdapter<EducationLevel> adapter = (ArrayAdapter<EducationLevel>) spEducationLevel.getAdapter();
                int position = adapter.getPosition(up.getEducationLevel());
                spEducationLevel.setSelection(position);
            }
            if(spEmploymentStatus != null && up.getEmploymentStatus()!= null) {
                ArrayAdapter<EmploymentStatus> adapter = (ArrayAdapter<EmploymentStatus>) spEmploymentStatus.getAdapter();
                int position = adapter.getPosition(up.getEmploymentStatus());
                spEmploymentStatus.setSelection(position);
            }
            if(spMobilityLevel!= null && up.getMobilityLevel()!= null) {
                ArrayAdapter<MobilityLevel> adapter = (ArrayAdapter<MobilityLevel>) spMobilityLevel.getAdapter();
                int position = adapter.getPosition(up.getMobilityLevel());
                spMobilityLevel.setSelection(position);
            }
            if(spMyersBriggsIndicator != null && up.getMyersBriggsIndicator()!= null) {
                ArrayAdapter<MyersBriggsTypeIndicator> adapter = (ArrayAdapter<MyersBriggsTypeIndicator>) spMyersBriggsIndicator.getAdapter();
                int position = adapter.getPosition(up.getMyersBriggsIndicator());
                spMyersBriggsIndicator.setSelection(position);
            }

        }
    }

    public UserProfile extractProfile(UserProfile up) {
        if (up == null) {
            up = new UserProfile();
        }
        if (upId != null && up.getId() == null) {
            up.setId(upId);
        }
        if (etFirstName != null) {
            String str = etFirstName.getText().toString();
            if(str!=null && !"".equals(str)){
                up.setFirstName(str);
            }

        }
        if (etMiddleName != null) {
            String str = etMiddleName.getText().toString();
            if(str!=null && !"".equals(str)){
                up.setMiddleName(str);
            }

        }
        if (etLastName != null) {
            String str = etLastName.getText().toString();
            if(str!=null && !"".equals(str)){
                up.setLastName(str);
            }

        }
        if (etPreferredName != null) {
            String str = etPreferredName.getText().toString();
            if(str!=null && !"".equals(str)){
                up.setPrefferedName(str);
            }

        }
        if (etPhone != null) {
            String str = etPhone.getText().toString();
            if(str!=null && !"".equals(str)){
                up.setPhone(str);
            }

        }
        if (etMobilePhone != null) {
            String str = etMobilePhone.getText().toString();
            if(str!=null && !"".equals(str)){
                up.setMobilePhone(str);
            }

        }
        if (etUsername != null) {
            String str = etUsername.getText().toString();
            if(str!=null && !"".equals(str)){
                up.setAlfredUserName(str);
            }

        }
        if (etEmail != null) {
            String str = etEmail.getText().toString();
            if(str!=null && !"".equals(str)){
                up.setEmail(str);
            }

        }

        //Selected RadioButton --> UserProfile Gender
        if (rgGender != null) {
            RadioButton b = (RadioButton) mView.findViewById(rgGender.getCheckedRadioButtonId());
            if (b != null) {
                String genderStr = b.getText().toString();
                Gender gender = Gender.valueOf(genderStr.toUpperCase());
                up.setGender(gender);
            }
        }

        if (dpDateOfBirth != null) {
            up.setDateOfBirth(getCleanDate(dpDateOfBirth));
        }
        if (dpAnniversaryDate != null) {
            up.setAnniversaryDate(getCleanDate(dpAnniversaryDate));
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
            up.setResidentialAddress(resAddress);
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
            up.setPostalAddress(postalAddress);
        }

        /*End of POSTAL address*/

        if(autoCitizenship != null) {
            String str = autoCitizenship.getText().toString();
            if(str!=null && !"".equals(str)){
                up.setCitizenship(str);
            }

        }
        if(autoNationality != null) {
            String str = autoNationality.getText().toString();
            if(str!=null && !"".equals(str)){
                up.setNationality(str);
            }

        }
        if(etSocialSecurityNumber != null) {
            String str = etSocialSecurityNumber.getText().toString();
            if(str!=null && !"".equals(str)){
                up.setSocialSecurityNumber(str);
            }

        }
        if(etProfession != null) {
            String str = etProfession.getText().toString();
            if(str!=null && !"".equals(str)){
                up.setProfession(str);
            }

        }
        if(etHealthInsurance != null) {
            String str = etHealthInsurance.getText().toString();
            if(str!=null && !"".equals(str)){
                up.setHealthInsurance(str);
            }

        }

        /**/

        if(spLanguage != null) {
            up.setLanguage( (Language) spLanguage.getSelectedItem());
        }
        if(spMaritalStatus != null) {
            up.setMaritalStatus((MaritalStatus) spMaritalStatus.getSelectedItem());
        }
        if(spEducationLevel != null) {
            up.setEducationLevel((EducationLevel) spEducationLevel.getSelectedItem());
        }
        if(spEmploymentStatus != null) {
            up.setEmploymentStatus((EmploymentStatus) spEmploymentStatus.getSelectedItem());
        }
        if(spMobilityLevel != null) {
            up.setMobilityLevel((MobilityLevel) spMobilityLevel.getSelectedItem());
        }
        if(spMyersBriggsIndicator != null) {
            up.setMyersBriggsIndicator((MyersBriggsTypeIndicator) spMyersBriggsIndicator.getSelectedItem());
        }

        return up;
    }

    private Date getCleanDate(DatePicker dpDate) {
        long dateLong = dpDate.getCalendarView().getDate();

        //Drop hours, mins, secs and millisecs
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateLong);
        calendar.set(Calendar.DST_OFFSET, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR, 0);


        return new Date(calendar.getTimeInMillis());
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
        if (etUsername != null) {
            etUsername.setText("");
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
        if (autoCitizenship != null) {
            autoCitizenship.setText("");
        }
        if (autoNationality != null) {
            autoNationality.setText("");
        }
        if (etSocialSecurityNumber != null) {
            etSocialSecurityNumber.setText("");
        }
        if (etProfession != null) {
            etProfession.setText("");
        }
        if (etHealthInsurance != null) {
            etHealthInsurance.setText("");
        }
    }

    private void showEmailInfo() {
        Toast.makeText(getActivity(),
                getResources().getString(R.string.up_email_info),
                Toast.LENGTH_SHORT).show();
    }
}
