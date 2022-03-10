package com.example.vpmanager.createStudy;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vpmanager.R;

public class createStudyFragment_Base extends Fragment {


    //Parameter:
    //Return values:
    //Sets the current fragment for the activity
    public createStudyFragment_Base() {
        createStudyActivity.currentFragment = 0;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_study_base, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }

}