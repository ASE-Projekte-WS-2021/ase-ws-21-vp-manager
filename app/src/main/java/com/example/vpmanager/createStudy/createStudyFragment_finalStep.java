package com.example.vpmanager.createStudy;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vpmanager.R;

public class createStudyFragment_finalStep extends Fragment {

    TextView title;
    TextView vp;
    TextView category;
    TextView execution;
    TextView location;
    TextView desc;
    TextView contact;

    //Parameter:
    //Return values:
    //Sets the current fragment for the activity
    public createStudyFragment_finalStep() {
        createStudyActivity.currentFragment = 6;
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
        desc = view.findViewById(R.id.confirmDesc);
        contact = view.findViewById(R.id.confirmContact);

    }

    //Parameter:
    //Return values:
    //Loads data recieved from the activity into the textfields
    private void loadData() {
        Bundle bundle = getArguments();
        System.out.println(bundle);
        if(bundle != null){
            String titleTxt = getString(R.string.createStudyTitle) + ": " + bundle.getString("title");
            String vpTxT = getString(R.string.createOfferedVP) + ": " + bundle.getString("vp");
            String categoryTxT = getString(R.string.fragment_create_study_pageThreeTitle) + ": " + bundle.getString("category");
            String executionTxT = getString(R.string.createStudyExe) + ": " + bundle.getString("exe");
            String locationTxT = getString(R.string.fragment_create_study_pageFourTitle) + ": " + bundle.getString("location");
            title.setText(titleTxt);
            vp.setText(vpTxT);
            category.setText(categoryTxT);
            execution.setText(executionTxT);
            location.setText(locationTxT);
        }
    }
}