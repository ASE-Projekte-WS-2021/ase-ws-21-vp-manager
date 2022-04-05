package com.example.vpmanager.views.createStudy;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vpmanager.Config;
import com.example.vpmanager.R;
import com.example.vpmanager.views.mainActivity;


public class createStudyFragment_Base extends Fragment {


    //Parameter:
    //Return values:
    //Sets the current fragment for the activity
    public createStudyFragment_Base() {
        CreateStudyFragment.currentFragment = Config.createFragmentBase;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainActivity.currentFragment = "createBase";
        return inflater.inflate(R.layout.fragment_create_study_base, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }

}