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

import eu.alfred.api.personalization.model.Contact;
import eu.alfred.api.personalization.model.Gender;
import eu.alfred.api.personalization.model.Relation;
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
//    private ContactsController mController;

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
                    case HUSBANT: case SON: case FATHER: case BROTHER:
                    lOrd--; break;
                }
                switch (rRel) {
                    case HUSBANT: case SON: case FATHER: case BROTHER:
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


    private void createFakeContacts(View view) {
        contacts = new ArrayList<Contact>();
        adapter = new ContactListAdapter(view.getContext(), contacts);
        adapter.sort(contactComparator);

        ListView lvContacts = (ListView) view.findViewById(R.id.lvContacts);
        lvContacts.setAdapter(adapter);

    }


    public static void removeContact(int contactPos) {
        Contact c = contacts.remove(contactPos);
        adapter.remove(c);
    }

    public static int setContact(int contactPos, Contact contact) {
        if (contactPos >= 0) {
            contacts.set(contactPos, contact);
            adapter.remove(contact);
            adapter.insert(contact, contactPos);
        } else {
            adapter.add(contact);
        }
        adapter.sort(contactComparator);
        return adapter.getPosition(contact);
    }

    public static Contact getContact(int contactPos) {
        return adapter.getItem(contactPos);
    }

    public void updateContactList(ArrayList<Contact> contactsList) {
        Log.d(TAG, "updateContactList with " + contactsList.size() + " contacts." );
        for (Contact contact : contactsList) {
//            contacts.add(contact);
            setContact(-1, contact);
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
