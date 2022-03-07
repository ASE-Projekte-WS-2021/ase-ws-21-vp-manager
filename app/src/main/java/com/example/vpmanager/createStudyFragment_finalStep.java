package com.example.vpmanager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class createStudyFragment_finalStep extends Fragment {

    TextView title;
    TextView vp;
    TextView category;
    TextView execution;
    TextView location;

    //Parameter:
    //Return values:
    //Sets the current fragment for the activity
    public createStudyFragment_finalStep() {
        createStudyActivity.currentFragment = 7;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_study_final_step, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setupView(view);
        loadData();
    }

    //Parameter:
    //Return values:
    //Connects the code with the view
    private void setupView(View view){
        title = view.findViewById(R.id.confirmTitle);
        vp = view.findViewById(R.id.confirmVP);
        category = view.findViewById(R.id.confirmCategory);
        execution = view.findViewById(R.id.confirmExecution);
        location = view.findViewById(R.id.confirmLocation);
    }

    //Parameter:
    //Return values:
    //Loads data recieved from the activity into the textfields
    private void loadData() {
        Bundle bundle = getArguments();
        System.out.println(bundle);
        if(bundle != null){
            title.setText(bundle.getString("title"));
            vp.setText(bundle.getString("vp"));
            category.setText(bundle.getString("category"));
            execution.setText(bundle.getString("exe"));
            location.setText(bundle.getString("location"));
        }
    }
}