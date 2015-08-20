package eu.alfred.personalization_manager.gui.tabs;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import eu.alfred.internal.wrapper.healthmonitor.resource.Resource;
import eu.alfred.personalization_manager.controller.health.HealthListener;
import eu.alfred.personalization_manager.db_administrator.model.BloodType;
import eu.alfred.personalization_manager.db_administrator.model.ModifiedRankinScale;
import eu.alfred.userprofile.R;

/**
 * Created by Arturo.Brotons on 26/02/2015.
 */
public class HealthSectionFragment extends SectionFragment implements HealthListener {

    final public String TAG = "HealthSF";


    private Spinner spBloodType;
    private Spinner spAbilityLevel;
    private LinearLayout containerList;
    private List<Resource> results = new ArrayList<Resource>();
    private int total;
    private ViewGroup container;
    private Bundle bundle;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        Log.d(TAG, "onCreateView");

        this.container = container;
        this.bundle = savedInstanceState;
        setSpinnersContent(view);
        hideInternalViews(view);
        containerList = (LinearLayout) view.findViewById(R.id.up_health_container_list);
        repopulate(view, this.container, this.bundle);
        return view;
    }

    private void hideInternalViews(View view) {
        view.findViewById(R.id.up_health_id_label).setVisibility(View.GONE);
        view.findViewById(R.id.txtHealthId).setVisibility(View.GONE);
        view.findViewById(R.id.up_health_userID_label).setVisibility(View.GONE);
        view.findViewById(R.id.txtHealthUserID).setVisibility(View.GONE);


        view.findViewById(R.id.spHealthBloodType).setVisibility(View.GONE);
        view.findViewById(R.id.up_health_bloodType_label).setVisibility(View.GONE);
        view.findViewById(R.id.spHealthAbilityLevel).setVisibility(View.GONE);
        view.findViewById(R.id.up_health_abilityLevel_label).setVisibility(View.GONE);
    }

    private void setSpinnersContent(View view) {
        spBloodType = (Spinner) view.findViewById(R.id.spHealthBloodType);
        spBloodType.setAdapter(new ArrayAdapter<BloodType>(view.getContext(), android.R.layout.simple_list_item_1, BloodType.values()));

        spAbilityLevel = (Spinner) view.findViewById(R.id.spHealthAbilityLevel);
        spAbilityLevel.setAdapter(new ArrayAdapter<ModifiedRankinScale>(view.getContext(), android.R.layout.simple_list_item_1, ModifiedRankinScale.values()));


    }

    @Override
    public void notification(List<Resource> results) {
        this.results = results;
    }

    @Override
    public void addResource(Resource newRes) {
        Log.d(TAG, "addResource: " + newRes.toJsonString());
        if (results == null) {
            results = new ArrayList<Resource>();
        }
        Resource res = getResourcesByName(newRes.getResourceName());
        if (res == null) {
            results.add(newRes);
        } else {
            int index = results.indexOf(res);
            results.remove(res);
            results.add(index, newRes);
        }
        addResourceToView(container, bundle, newRes);
    }

    @Override
    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public void end(Boolean success) {

    }

    private Resource getResourcesByName(String resourceName) {
        Resource res = null;
        if (results != null && resourceName != null && !resourceName.isEmpty()) {
            for (Resource result : results) {
                if (resourceName.equals(result.getResourceName())) {
                    res = result;
                }
            }
        }
        return res;
    }

    private void repopulate(View view, ViewGroup container, Bundle savedInstanceState) {

        if (containerList != null) {
            for (Resource result : this.results) {
                addResourceToView(container, savedInstanceState, result);
            }
        }
    }

    private void addResourceToView(ViewGroup container, Bundle savedInstanceState, Resource result) {
        if (containerList != null) {
            TextView textView = (TextView) getLayoutInflater(savedInstanceState).inflate(R.layout.userprofile_health_textview, container, false);
            textView.setText(result.getLabel());

            EditText editText = (EditText) getLayoutInflater(savedInstanceState).inflate(R.layout.userprofile_health_edittext, container, false);

            String text = "";
            if (result.getValues().size() > 0) {
                text += result.getValues().get(0).getStringValue();
            }
            if (result.getUnit() != null && !result.getUnit().isEmpty()) {
                text += result.getUnit();
            }
            editText.setText(text);
            editText.setKeyListener(null);

            containerList.addView(textView);
            containerList.addView(editText);
        }
    }
}
