package com.example.vpmanager.views.createStudy;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vpmanager.R;
import com.google.android.material.textfield.TextInputEditText;

public class createStudyFragment_StepFour_Remote extends Fragment {

    public static TextInputEditText textInputEditTextPlatform;
    public static TextInputEditText textInputEditTextOptionalPlatform;

    //Parameter:
    //Return values:
    //Sets the current fragment for the activity
    public createStudyFragment_StepFour_Remote() {
        CreateStudyFragment.currentFragment = 4;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_study_step_four_remote, container, false);
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
        textInputEditTextPlatform = view.findViewById(R.id.inputFieldPlatform);
        textInputEditTextOptionalPlatform = view.findViewById(R.id.inputFieldPlatformOptional);
    }

    //Parameter:
    //Return values:
    //Loads data recieved from the activity into the inputfields
    private void loadData() {

        if (CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("platform") != null) {
            textInputEditTextPlatform.setText(
                    CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("platform").toString());
        }
        if (CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("platform2") != null) {
            textInputEditTextOptionalPlatform.setText(
                    CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("platform2").toString());
        }
        /*
        Bundle bundle = getArguments();
        if(bundle != null){
            textInputEditTextPlatform.setText(bundle.getString("platform"));
            textInputEditTextOptionalPlatform.setText(bundle.getString("platform2"));
        }
         */
    }
}