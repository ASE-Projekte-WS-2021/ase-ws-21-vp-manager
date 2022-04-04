package com.example.vpmanager.views.createStudy;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vpmanager.Config;
import com.example.vpmanager.R;
import com.google.android.material.textfield.TextInputEditText;
import com.example.vpmanager.views.mainActivity;


public class createStudyFragment_StepThree extends Fragment {

    public static TextInputEditText textInputEditTextDesc;


    //Parameter:
    //Return values:
    //Sets the current fragment for the activity
    public createStudyFragment_StepThree() { CreateStudyFragment.currentFragment = Config.createFragmentThree; }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainActivity.currentFragment = "createStepThree";
        return inflater.inflate(R.layout.fragment_create_study_step_three, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setupView(view);
        loadData();
    }

    //Parameter:
    //Return values:
    //Connects the code with the view
    private void setupView(View view) {
        textInputEditTextDesc = view.findViewById(R.id.inputFieldStudyDesc);
    }

    //Parameter:
    //Return values:
    //Loads data recieved from the activity into the inputfields
    private void loadData() {

        if (CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("description") != null) {
            textInputEditTextDesc.setText(
                    CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("description").toString());
        }
    }
}