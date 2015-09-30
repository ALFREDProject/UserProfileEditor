package eu.alfred.personalization_manager.gui.tabs;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.alfred.internal.wrapper.healthmonitor.resource.Resource;
import eu.alfred.internal.wrapper.healthmonitor.resource.Value;
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
    private Map<Resource, String> resValues = new HashMap<Resource, String>();
    private int total = -1;
    private int count = 0;
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
        view.findViewById(R.id.up_health_loading_resources_label).setVisibility(View.GONE);
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
        if (allowedResource(newRes)) {
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
        } else {
            //Even if we don't show the resources,
            //we need to count it to hide the spinning wheel
            count++;
        }
    }

    /**
     * Health resources that we want to show
     * @param newRes Resource to check
     * @return
     */
    private boolean allowedResource(Resource newRes) {
        return !newRes.getResourceName().equals("birthdate");
    }

    @Override
    public void setTotal(int total) {
        this.total = total;
        count = 0;
    }

    @Override
    public void start() {
        if (containerList != null) {
            containerList.findViewById(R.id.up_health_loading_resources_label).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void end(Boolean success) {
        if (containerList != null) {
            containerList.findViewById(R.id.up_health_loading_resources_label).setVisibility(View.GONE);
        }
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
            start();
            count++;
            TextView textView = (TextView) getLayoutInflater(savedInstanceState).inflate(R.layout.userprofile_health_textview, container, false);
            textView.setText(result.getLabel());

            EditText editText = (EditText) getLayoutInflater(savedInstanceState).inflate(R.layout.userprofile_health_edittext, container, false);

            String text = "";
            if (result.getValues().size() > 0) {
                Value value = selectLastValue(result.getValues());
                if (value != null) {
                    try {
                        text += value.getIntValue();
                    } catch (NumberFormatException e) {
                        text += value.getStringValue();
                    }
                }
            }
            if (result.getUnit() != null && !result.getUnit().isEmpty()) {
                text += " " + result.getUnit();
            }
            editText.setText(text);
//            editText.setKeyListener(null);
            editText.addTextChangedListener(new ResourceTextWatcher(result));
//            editText.setOnEditorActionListener(new ResourceOnEditorActionListener(result));

            containerList.addView(textView);
            containerList.addView(editText);
//            updateResource(result, text);
            if (count >= total) {
                end(true);
            }
        }
    }

    private Value selectLastValue(List<Value> values) {
        Value lastVal = null;
        Date lastDate = new Date(0L);
        for (Value value : values) {
            if (value.getTimestamp().after(lastDate)) {
                lastVal = value;
                lastDate = value.getTimestamp();
            }
        }

        return lastVal;
    }

    private class ResourceTextWatcher implements TextWatcher {
        private final Resource mResource;

        public ResourceTextWatcher(Resource resource) {
            this.mResource = resource;
        }

        @Override
        public void afterTextChanged(Editable s) {
            String rawText = s.toString();
//            Log.d(TAG, "afterTextChanged: " + mResource.getResourceName() + " set to " + rawText);
            updateResource(mResource, rawText);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //Empty on purpose
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //Empty on purpose
        }
    }

    private void updateResource(Resource resource, String rawText) {
//        Log.d(TAG, "updateResource: " + resource.getResourceName() + " set to " + rawText);
        resValues.put(resource, rawText);
    }

    @Override
    public Map<Resource, String> getResourceValues() {
        return resValues;
    }
}
