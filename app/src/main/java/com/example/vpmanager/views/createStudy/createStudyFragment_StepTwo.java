package com.example.vpmanager.views.createStudy;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vpmanager.R;
import com.example.vpmanager.views.mainActivity;
import com.google.android.material.textfield.TextInputEditText;


public class createStudyFragment_StepTwo extends Fragment {

    public static TextInputEditText textInputEditTextContactMail;
    public static TextInputEditText textInputEditTextContactPhone;
    public static TextInputEditText textInputEditTextContactSkype;
    public static TextInputEditText textInputEditTextContactDiscord;
    public static TextInputEditText textInputEditTextContactOthers;

    //Parameter:
    //Return values:
    //Sets the current fragment for the activity
    public createStudyFragment_StepTwo() {
        CreateStudyFragment.currentFragment = 2;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainActivity.currentFragment = "createStepTwo";
        return inflater.inflate(R.layout.fragment_create_study_step_two, container, false);
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
        textInputEditTextContactMail = view.findViewById(R.id.inputContact1);
        textInputEditTextContactMail.setText(mainActivity.uniqueID);
        textInputEditTextContactMail.setFocusable(false);
        textInputEditTextContactMail.setTextColor(ContextCompat.getColor(getActivity(), R.color.pieChartRemaining));

        textInputEditTextContactPhone = view.findViewById(R.id.inputContact2);
        textInputEditTextContactSkype = view.findViewById(R.id.inputContact3);
        textInputEditTextContactDiscord = view.findViewById(R.id.inputContact4);
        textInputEditTextContactOthers = view.findViewById(R.id.inputContact5);
    }

    //Parameter:
    //Return values:
    //Loads data received from the activity into the inputfields
    private void loadData() {

        if (CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("contact2") != null) {
            textInputEditTextContactPhone.setText(
                    CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("contact2").toString());
        }
        if (CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("contact3") != null) {
            textInputEditTextContactSkype.setText(
                    CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("contact3").toString());
        }
        if (CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("contact4") != null) {
            textInputEditTextContactDiscord.setText(
                    CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("contact4").toString());
        }
        if (CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("contact5") != null) {
            textInputEditTextContactOthers.setText(
                    CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("contact5").toString());
        }
    }
}