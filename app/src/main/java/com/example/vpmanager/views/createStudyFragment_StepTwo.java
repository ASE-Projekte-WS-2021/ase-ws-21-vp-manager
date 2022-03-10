package com.example.vpmanager.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vpmanager.R;
import com.google.android.material.textfield.TextInputEditText;

public class createStudyFragment_StepTwo extends Fragment {

    TextInputEditText textInputEditTextDesc;

    //Parameter:
    //Return values:
    //Sets the current fragment for the activity
    public createStudyFragment_StepTwo() {
        createStudyActivity.currentFragment = 3;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_study_step_two, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setupView(view);
        loadData();
    }

    private void setupView(View view){
        textInputEditTextDesc = view.findViewById(R.id.inputFieldStudyDesc);

    }

    private void loadData() {
        Bundle bundle = getArguments();
        if(bundle != null){
            textInputEditTextDesc.setText(bundle.getString("desc"));
        }
    }
}