package com.example.vpmanager.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vpmanager.R;
import com.google.android.material.textfield.TextInputEditText;

public class createStudyFragment_StepFour_Remote extends Fragment {

    TextInputEditText textInputEditTextPlatform;
    TextInputEditText textInputEditTextOptionalPlatform;
    //Parameter:
    //Return values:
    //Sets the current fragment for the activity
    public createStudyFragment_StepFour_Remote() {
        createStudyActivity.currentFragment = 5;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_study_step_four_remote, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setupView(view);
        loadData();
    }

    private void setupView(View view){
        textInputEditTextPlatform = view.findViewById(R.id.inputFieldPlatform);
        textInputEditTextOptionalPlatform = view.findViewById(R.id.inputFieldPlatformOptional);

    }

    private void loadData() {
        Bundle bundle = getArguments();
        if(bundle != null){
            textInputEditTextPlatform.setText(bundle.getString("platform"));
            textInputEditTextOptionalPlatform.setText(bundle.getString("platform2"));
        }
    }
}