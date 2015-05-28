package eu.alfred.personalization_manager.gui.tabs;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import eu.alfred.personalization_manager.controller.ContactsController;
import eu.alfred.personalization_manager.db_administrator.model.Contact;
import eu.alfred.personalization_manager.db_administrator.model.Gender;
import eu.alfred.personalization_manager.db_administrator.model.Relation;
import eu.alfred.personalization_manager.gui.tabs.contacts.ContactActivity;
import eu.alfred.userprofile.R;

/**
 * Created by Arturo.Brotons on 26/02/2015.
 */
public class ContactsSectionFragment extends SectionFragment {


    private static final String TAG = "ContactsSectionFragment";
    private Context mContext;
    static private ContactListAdapter adapter;
    static private List<Contact> contacts;
    static private Comparator<Contact> contactComparator;
    private ContactsController mController;

    static {
        contactComparator = new Comparator<Contact>() {
            @Override
            public int compare(Contact lhs, Contact rhs) {
                int relComp = compareRelation(lhs.getRelationToUser(), rhs.getRelationToUser());
                if(relComp == 0) {
                    return toStr(lhs).compareToIgnoreCase(toStr(rhs));
                } else {
                    return relComp;
                }
            }

            private int compareRelation(Relation[] lRels, Relation[] rRels) {
                Relation lRel = Relation.OTHER, rRel = Relation.OTHER;
                if (lRels != null && lRels.length > 0) {
                    lRel = lRels[0];
                }
                if (rRels != null && rRels.length > 0) {
                    rRel = rRels[0];
                }
                int lOrd = lRel.ordinal();
                int rOrd = rRel.ordinal();
                switch (lRel) {
                    case HUSBAND: case SON: case FATHER: case BROTHER:
                    lOrd--; break;
                }
                switch (rRel) {
                    case HUSBAND: case SON: case FATHER: case BROTHER:
                    rOrd--; break;
                }
                return lOrd - rOrd;
            }

            private String toStr(Contact contact) {
                String contactStr = "";
                if (contact.getLastName() != null) {
                    contactStr += contact.getLastName().trim();
                }
                if (contact.getFirstName() != null) {
                    if(!"".equals(contactStr)) contactStr += " ";
                    contactStr += contact.getFirstName().trim();
                }
                if (contact.getMiddleName() != null) {
                    if(!"".equals(contactStr)) contactStr += " ";
                    contactStr += contact.getMiddleName().trim();
                }
                return contactStr;
            }
        };

//        contacts = genContacts();
    }

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = super.onCreateView(inflater, container, savedInstanceState);


        mContext = view.getContext();
        createFakeContacts(view);
        return view;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    public void init() {
        mController.setFragment(this);

        mController.getAllContacts();
    }

    //TODO Retrieve real data from server
    private void createFakeContacts(View view) {
        contacts = new ArrayList<Contact>();
        adapter = new ContactListAdapter(view.getContext(), contacts);
        adapter.sort(contactComparator);

        ListView lvContacts = (ListView) view.findViewById(R.id.lvContacts);
        lvContacts.setAdapter(adapter);

    }

/*
    private static List<Contact> genContacts() {
        List<Contact> contacts = new ArrayList<Contact>();

        String[] fNames = {
                "Anne", "John", "Harold", "Joanna", "Harlan",
                "Carol", "Patrick", "Myriam", "Jane", "Peter",
                "Helene", "Patrick", "Rick", "Alenne", "Mary",
                "Melissa", "Jose", "Laura", "Anais", "Giancarlo"};
        String[] mNames = {
                null, "M.", null, "Alene", "Theunis",
                null, "L.", null, null, "Dean",
                null, null, null, null, null,
                null, "Manuel", null, null, null};
        String[] lNames = {
                "Van Andringa", "Grimm", "Grimm", "Suronne", "Bouchard",
                "Bouvier", "Garris", "Garris", "Garris", "Smitherson",
                "Collard", "Grimm", "Grimm", "Collard", "Grimm",
                "Frayssinous", "Soler Falcon", "Garrido Gomez", "Vallaud-Belkacem", "Belucci"};
        String[] number = {
                "89", "70", "", "1946", "",
                "68", "78", "", "", "",
                "", "", "", "", "",
                "", "", "", "", ""};
        Relation[] relation = {
                Relation.NURSE, Relation.SON, Relation.HUSBAND, Relation.SISTER, Relation.CARER,
                Relation.DOCTOR, Relation.BROTHER, Relation.RELATIVE, Relation.RELATIVE, Relation.OTHER,
                Relation.SISTER, Relation.SON, Relation.RELATIVE, Relation.RELATIVE, Relation.DAUGHTER,
                Relation.FRIEND, Relation.OTHER, Relation.RELATIVE, Relation.RELATIVE, Relation.FRIEND};
        Gender[] gender = {
                Gender.FEMALE, Gender.MALE, Gender.MALE, Gender.FEMALE, Gender.MALE,
                Gender.FEMALE, Gender.MALE, Gender.FEMALE, Gender.FEMALE, Gender.MALE,
                Gender.FEMALE, Gender.MALE, Gender.MALE, Gender.FEMALE, Gender.FEMALE,
                Gender.FEMALE, Gender.MALE, Gender.FEMALE, Gender.FEMALE, Gender.MALE};
        String[] lPhones = {
                "+33 964 13 09 39",
                "+33 979 92 90 59",
                "+33 963 74 59 65",
                "+33 906 94 14 74",
                "+33 920 38 20 64",
                "+33 931 39 25 15",
                "+33 958 90 21 24",
                "+33 986 03 59 55",
                "+33 936 25 66 52",
                "+33 941 71 44 75",
                "+33 964 13 09 39",
                "+33 979 92 90 59",
                "+33 963 74 59 65",
                "+33 906 94 14 74",
                "+33 920 38 20 64",
                "+33 931 39 25 15",
                "+33 958 90 21 24",
                "+33 986 03 59 55",
                "+33 936 25 66 52",
                "+33 941 71 44 75"};
        String[] mPhones = {
                "+33 657 65 95 38",
                "+33 618 96 29 17",
                "+33 658 81 47 33",
                "+33 674 84 14 39",
                "+33 691 15 25 77",
                "+33 662 11 30 47",
                "+33 678 80 61 66",
                "+33 673 06 53 05",
                "+33 642 90 44 87",
                "+33 696 58 54 14",
                "+33 957 65 95 38",
                "+33 618 96 29 17",
                "+33 658 81 47 33",
                "+33 674 84 14 39",
                "+33 691 15 25 77",
                "+33 662 11 30 47",
                "+33 678 80 61 66",
                "+33 673 06 53 05",
                "+33 642 90 44 87",
                "+33 696 58 54 14"};
        GregorianCalendar calendars[] = {
                new GregorianCalendar(1989,  7,  5),
                new GregorianCalendar(1970,  5, 21),
                new GregorianCalendar(1941, 10, 17),
                new GregorianCalendar(1946,  6, 10),
                new GregorianCalendar(1952,  7, 19),
                new GregorianCalendar(1968, 12, 24),
                new GregorianCalendar(1978,  3, 30),
                new GregorianCalendar(1980,  1,  7),
                new GregorianCalendar(2010,  3, 11),
                new GregorianCalendar(1940,  8, 23),
                new GregorianCalendar(1989,  6,  7),
                new GregorianCalendar(1970,  8, 29),
                new GregorianCalendar(1941, 12, 30),
                new GregorianCalendar(1946, 12, 25),
                new GregorianCalendar(1952,  1, 12),
                new GregorianCalendar(1968,  9, 15),
                new GregorianCalendar(1978, 10,  1),
                new GregorianCalendar(1980,  5, 22),
                new GregorianCalendar(1971,  9,  3),
                new GregorianCalendar(1940,  3, 13)
        };

        for (int i = 0; i < fNames.length; i++) {

            Contact contact = new Contact();
            contact.setFirstName(fNames[i]);
            contact.setMiddleName(mNames[i]);
            contact.setLastName(lNames[i]);
            contact.setGender(gender[i]);
            contact.setPhone(lPhones[i]);
            contact.setMobilePhone(mPhones[i]);
            contact.setDateOfBirth(calendars[i].getTime());
            Relation[] relations = new Relation[1];
            relations[0] = relation[i];
            contact.setRelationToUser(relations);
            contact.setEmail(toMail(contact, number[i]));
            contacts.add(contact);
        }
        return contacts;
    }

    private static String toMail(Contact contact, String number) {
        String str = "";
        if (contact.getFirstName() != null) {
            str += contact.getFirstName().toLowerCase();
        }
        if (contact.getMiddleName() != null) {
            str += contact.getMiddleName().toLowerCase();
        }
        if (contact.getLastName() != null) {
            str += contact.getLastName().toLowerCase();
        }

        str = str.toLowerCase();
        str = str.replace(" ", "");
        str = str.replace(".", "");


        return str + number + "@mail.eu";
    }

*/
    public static void removeContact(int contactPos) {
        Contact c = contacts.remove(contactPos);
        adapter.remove(c);
    }

    public static void setContact(int contactPos, Contact contact) {
        if (contactPos >= 0) {
            contacts.set(contactPos, contact);
            adapter.remove(contact);
            adapter.insert(contact, contactPos);
        } else {
            adapter.add(contact);
        }
        adapter.sort(contactComparator);
    }

    public static Contact getContact(int contactPos) {
        return adapter.getItem(contactPos);
    }

    public void updateContactList(ArrayList<Contact> contactsList) {
        for (Contact contact : contactsList) {
            contacts.add(contact);
        }
    }

    class ContactListAdapter extends ArrayAdapter<Contact> {

        ContactListAdapter(Context context, List<Contact> items) {
            super(context, 0, items);
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final Contact contact = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.contact_item_list, parent, false);
            }

            //The contact item has two lines of text
            TextView tvFirstTextLine = (TextView) convertView.findViewById(R.id.tvContactItemFirstLine);
            TextView tvSecondTextLine = (TextView) convertView.findViewById(R.id.tvContactItemSecondLine);

            String firstLineStr = "";
            String secondLineStr = "";
            if (contact.getFirstName() != null) {
                firstLineStr += contact.getFirstName().trim();
            }
            if (contact.getMiddleName() != null) {
                if(!"".equals(firstLineStr)) firstLineStr += " ";
                firstLineStr += contact.getMiddleName().trim();
            }
            if (contact.getLastName() != null) {
                if(!"".equals(firstLineStr)) firstLineStr += " ";
                firstLineStr += contact.getLastName().trim();
            }
            if (contact.getRelationToUser() != null && contact.getRelationToUser().length > 0) {
//                if (contact.getRelationToUser()[0] != Relation.OTHER) {
//                    if(!"".equals(secondLineStr)) secondLineStr += " ";
                secondLineStr += contact.getRelationToUser()[0].toString();
//            }
            }
            tvFirstTextLine.setText(firstLineStr);

            tvSecondTextLine.setText(secondLineStr);

            //Set image depending on genre
            ImageView contactIcon = (ImageView) convertView.findViewById(R.id.contact_icon);

            if (contact.getGender() != null) {
                if (contact.getGender().equals(Gender.MALE)) {
                    contactIcon.setImageResource(R.drawable.ic_contact);
                } else {
                    contactIcon.setImageResource(R.drawable.ic_contact_female);
                }
            } else {
                contactIcon.setImageResource(R.drawable.ic_contact_nogenre);
            }

            if(contact.getRelationToUser() != null && contact.getRelationToUser().length > 0){
                Relation rel = contact.getRelationToUser()[0];
                switch (rel) {
                    case DOCTOR: case NURSE: case CARER:
                        contactIcon.setImageResource(R.drawable.ic_contact_doctor); break;
                    default: break;
                }
            }

            String callNumber = null;
            if (contact.getPhone() != null) {
                callNumber = contact.getPhone();
            } else if (contact.getMobilePhone() != null) {
                callNumber = contact.getMobilePhone();
            }

            RelativeLayout contactItem = (RelativeLayout) convertView.findViewById(R.id.rl_contact_item);
            contactItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openContact(position, contact);
                }
            });
            if (callNumber != null) {
                final String finalCallNumber = callNumber;
                contactIcon.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + finalCallNumber));
                        startActivity(intent);
                    }
                });
            } else {
                contactIcon.setOnClickListener(null);
            }
            return convertView;
        }
    }

    private void openContact(int position, Contact contact) {
        Log.d(TAG, String.format("Open contact %d %s", position, contact.getFirstName()));
        Intent intent = new Intent(mContext, ContactActivity.class);
        intent.putExtra("contact-pos", position);
        intent.putExtra("contact-id", contact.getId());
        intent.putExtra("user-id", contact.getUserID());
        mContext.startActivity(intent);
    }


}
