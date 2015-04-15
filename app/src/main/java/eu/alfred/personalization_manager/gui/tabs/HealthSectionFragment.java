package eu.alfred.personalization_manager.gui.tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import eu.alfred.personalization_manager.db_administrator.model.BloodType;
import eu.alfred.userprofile.R;

/**
* Created by Arturo.Brotons on 26/02/2015.
*/
public class HealthSectionFragment extends SectionFragment {

    private Spinner spBloodType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);
        setSpinnersContent(view);
        hideSomeViews(view);
        return view;
    }

    private void hideSomeViews(View view) {
        view.findViewById(R.id.up_health_id_label).setVisibility(View.GONE);
        view.findViewById(R.id.txtHealthId).setVisibility(View.GONE);
        view.findViewById(R.id.up_health_userID_label).setVisibility(View.GONE);
        view.findViewById(R.id.txtHealthUserID).setVisibility(View.GONE);
    }

    private void setSpinnersContent(View view) {
        spBloodType = (Spinner) view.findViewById(R.id.spHealthBloodType);
        spBloodType.setAdapter(new ArrayAdapter<BloodType>(view.getContext(), android.R.layout.simple_list_item_1, BloodType.values()));

    }
}
