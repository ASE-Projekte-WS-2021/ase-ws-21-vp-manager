package com.example.vpmanager.views.createStudy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.vpmanager.R;
import com.google.android.material.textfield.TextInputEditText;
import com.example.vpmanager.views.mainActivity;


public class createStudyFragment_StepFour_Presence extends Fragment {

    public static TextInputEditText textInputEditTextLocation;
    public static TextInputEditText textInputEditTextStreet;
    public static TextInputEditText textInputEditTextRoom;


    //Parameter:
    //Return values:
    //Sets the current fragment for the activity
    public createStudyFragment_StepFour_Presence() {
        CreateStudyFragment.currentFragment = 4;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainActivity.currentFragment = "createStepFourPresence";
        return inflater.inflate(R.layout.fragment_create_study_step_four_presence, container, false);
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
        textInputEditTextLocation = view.findViewById(R.id.inputFieldLocation);
        textInputEditTextStreet = view.findViewById(R.id.inputFieldStreet);
        textInputEditTextRoom = view.findViewById(R.id.inputFieldRoom);

    }

    //Parameter:
    //Return values:
    //Loads data received from the activity into the inputfields
    private void loadData() {
        if (CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("location") != null) {
            textInputEditTextLocation.setText(
                    CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("location").toString());
        }
        if (CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("street") != null) {
            textInputEditTextStreet.setText(
                    CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("street").toString());
        }
        if (CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("room") != null) {
            textInputEditTextRoom.setText(
                    CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("room").toString());
        }

    }
}