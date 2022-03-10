package com.example.vpmanager.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vpmanager.R;
import com.google.android.material.textfield.TextInputEditText;

public class createStudyFragment_StepOne extends Fragment {


    TextInputEditText textInputEditTextTitle;
    TextInputEditText textInputEditTextVP;
    //Parameter:
    //Return values:
    //Sets the current fragment for the activity
    public createStudyFragment_StepOne() {
        createStudyActivity.currentFragment = 1;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_study_step_one, container, false);
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
        textInputEditTextTitle = view.findViewById(R.id.inputFieldTitle);
        textInputEditTextVP = view.findViewById(R.id.inputFieldVP);
    }

    //Parameter:
    //Return values:
    //Loads data recieved from the activity into the inputfields
    private void loadData() {
        Bundle bundle = getArguments();
        if(bundle != null){
            textInputEditTextTitle.setText(bundle.getString("title"));
            textInputEditTextVP.setText(bundle.getString("vp"));
        }
    }

}