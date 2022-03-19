package com.example.vpmanager.createStudy;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vpmanager.R;
import com.google.android.material.textfield.TextInputEditText;

public class createStudyFragment_StepTwo extends Fragment {

    TextInputEditText textInputEditTextContactMail;
    TextInputEditText textInputEditTextContactPhone;
    TextInputEditText textInputEditTextContactSkype;
    TextInputEditText textInputEditTextContactDiscord;
    TextInputEditText textInputEditTextContactOthers;
    //Parameter:
    //Return values:
    //Sets the current fragment for the activity
    public createStudyFragment_StepTwo() {
        createStudyActivity.currentFragment = 2;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
    private void setupView(View view){
        textInputEditTextContactMail = view.findViewById(R.id.inputContact1);
        textInputEditTextContactMail.setFocusable(false);
        textInputEditTextContactPhone = view.findViewById(R.id.inputContact2);
        textInputEditTextContactSkype = view.findViewById(R.id.inputContact3);
        textInputEditTextContactDiscord = view.findViewById(R.id.inputContact4);
        textInputEditTextContactOthers = view.findViewById(R.id.inputContact5);
    }

    //Parameter:
    //Return values:
    //Loads data recieved from the activity into the inputfields
    private void loadData() {
        Bundle bundle = getArguments();
        if(bundle != null){
            textInputEditTextContactMail.setText(bundle.getString("contact"));
            textInputEditTextContactPhone.setText(bundle.getString("contact2"));
            textInputEditTextContactSkype.setText(bundle.getString("contact3"));
            textInputEditTextContactDiscord.setText(bundle.getString("contact4"));
            textInputEditTextContactOthers.setText(bundle.getString("contact5"));
        }
    }
}