package com.example.vpmanager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class createStudyFragment_StepOne extends Fragment {


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

    }

}