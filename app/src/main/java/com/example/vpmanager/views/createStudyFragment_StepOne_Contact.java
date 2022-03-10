package com.example.vpmanager.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vpmanager.R;
import com.google.android.material.textfield.TextInputEditText;

public class createStudyFragment_StepOne_Contact extends Fragment {

    TextInputEditText textInputEditTextContact;
    TextInputEditText textInputEditTextContact2;
    TextInputEditText textInputEditTextContact3;
    //Parameter:
    //Return values:
    //Sets the current fragment for the activity
    public createStudyFragment_StepOne_Contact() {
        createStudyActivity.currentFragment = 2;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_study_step_one_contact, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setupView(view);
        loadData();
    }

    private void setupView(View view){
        textInputEditTextContact = view.findViewById(R.id.inputContact1);
        textInputEditTextContact2 = view.findViewById(R.id.inputContact2);
        textInputEditTextContact3 = view.findViewById(R.id.inputContact3);
    }

    private void loadData() {
        Bundle bundle = getArguments();
        if(bundle != null){
            textInputEditTextContact.setText(bundle.getString("contact"));
            textInputEditTextContact2.setText(bundle.getString("contact2"));
            textInputEditTextContact3.setText(bundle.getString("contact3"));
        }
    }
}