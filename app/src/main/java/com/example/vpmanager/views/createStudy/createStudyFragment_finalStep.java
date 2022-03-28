package com.example.vpmanager.views.createStudy;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vpmanager.R;

public class createStudyFragment_finalStep extends Fragment {

    private TextView titleTextView;
    private TextView vpTextView;
    private TextView categoryTextView;
    private TextView executionTextView;
    private TextView locationTextView;

    //Parameter:
    //Return values:
    //Sets the current fragment for the activity
    public createStudyFragment_finalStep() {
        CreateStudyFragment.currentFragment = 6;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        titleTextView = view.findViewById(R.id.confirmTitle);
        vpTextView = view.findViewById(R.id.confirmVP);
        categoryTextView = view.findViewById(R.id.confirmCategory);
        executionTextView = view.findViewById(R.id.confirmExecution);
        locationTextView = view.findViewById(R.id.confirmLocation);
    }

    //Parameter:
    //Return values:
    //Loads data recieved from the activity into the textfields
    private void loadData() {
        if (CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("name") != null){
            String title = "Titel:" + "\n" + CreateStudyFragment.createStudyViewModel.studyCreationProcessData
                    .get("name").toString();
            titleTextView.setText(title);
        }
        if (CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("vps") != null){
            String vps = "VP-Stunden:" + "\n" + CreateStudyFragment.createStudyViewModel.studyCreationProcessData
                    .get("vps").toString();
                    vpTextView.setText(vps);
        }
        if (CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("category") != null){
            String category = "Kategorie:" + "\n" + CreateStudyFragment.createStudyViewModel.studyCreationProcessData
                    .get("category").toString();
            categoryTextView.setText(category);
        }
        if (CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("executionType") != null){
            String executionType = "Art der Durchführung:" + "\n" + CreateStudyFragment.createStudyViewModel
                    .studyCreationProcessData.get("executionType").toString();
            executionTextView.setText(executionType);
        }

        if (CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("location") != null){
            String locationTxT = getString(R.string.fragment_create_study_pageFourTitle) + ": " + "\n";

            //if location is not null it is always added
            String location = CreateStudyFragment.createStudyViewModel.studyCreationProcessData
                    .get("location").toString();
            locationTxT += location;

            //if street exists, add street to string
            if (CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("street") != null){
                String street = CreateStudyFragment.createStudyViewModel.studyCreationProcessData
                        .get("street").toString();
                locationTxT += "\n" + street;
            }

            //if room exists, add room to string
            if (CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("room") != null){
                String room = CreateStudyFragment.createStudyViewModel.studyCreationProcessData
                        .get("room").toString();
                locationTxT += "\n" + room;
            }

            locationTextView.setText(locationTxT);

        } else {
            String platformTxT = getString(R.string.fragment_create_study_pageFourTitleTwo) + ": " + "\n";

            //if primary platform is not null it is always added
            String platform = CreateStudyFragment.createStudyViewModel.studyCreationProcessData
                    .get("platform").toString();
            platformTxT += platform;

            //if secondary platform is not null, add secondary platform to string
            if (CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("platform2") != null){
                String platform2 = CreateStudyFragment.createStudyViewModel.studyCreationProcessData
                        .get("platform2").toString();
                platformTxT += "\n" + platform2;
            }

            locationTextView.setText(platformTxT);
        }
        /*
        Bundle bundle = getArguments();
        System.out.println(bundle);
        if(bundle != null){
            String titleTxt = getString(R.string.createStudyTitle) + ": " + bundle.getString("title");
            String vpTxT = getString(R.string.createOfferedVP) + ": " + bundle.getString("vp");
            String categoryTxT = getString(R.string.fragment_create_study_pageThreeTitle) + ": " + bundle.getString("category");
            String executionTxT = getString(R.string.createStudyExe) + ": " + bundle.getString("exe");
            String locationTxT = getString(R.string.fragment_create_study_pageFourTitle) + ": " + bundle.getString("location");
            titleTextView.setText(titleTxt);
            vpTextView.setText(vpTxT);
            categoryTextView.setText(categoryTxT);
            executionTextView.setText(executionTxT);
            locationTextView.setText(locationTxT);
        }
         */
    }
}