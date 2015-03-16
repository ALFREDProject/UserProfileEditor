package eu.alfred.personalization_manager.gui.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A fragment that launches other parts of the demo application.
 */
public class SectionFragment extends Fragment {

    private String title;
    private int layout;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }

    public String getTitle() {
        return title;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(layout, container, false);
    }
}
