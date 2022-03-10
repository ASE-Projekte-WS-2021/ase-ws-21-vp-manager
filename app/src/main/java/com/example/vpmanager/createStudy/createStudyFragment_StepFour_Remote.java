package com.example.vpmanager.createStudy;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vpmanager.R;

public class createStudyFragment_StepFour_Remote extends Fragment {


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

    }
}