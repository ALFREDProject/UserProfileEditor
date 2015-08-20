package eu.alfred.personalization_manager.gui.tabs;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A fragment that launches other parts of the demo application.
 */
public class SectionFragment extends Fragment {

    private static final String TAG = "Fragment";
    private String title = "";
    private int layout;
    private Context context;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }

    public String getTitle() {
        return title;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView " + getTitle());
        return inflater.inflate(layout, container, false);
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView " + getTitle());
        super.onDestroyView();
    }
}
