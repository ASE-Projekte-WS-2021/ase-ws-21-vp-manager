package com.example.vpmanager.views.createStudy;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vpmanager.Config;
import com.example.vpmanager.R;
import com.example.vpmanager.views.MainActivity;

public class CreateStudyFragment_Base extends Fragment {

    //Parameter:
    //Return values:
    //Sets the current fragment for the activity
    public CreateStudyFragment_Base() {
        CreateStudyFragment.currentFragment = Config.createFragmentBase;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_study_base, container, false);
        MainActivity.currentFragment = "createBase";
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }

}