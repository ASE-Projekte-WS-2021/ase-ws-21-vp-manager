package com.example.vpmanager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class createStudyFragment_finalStep_two extends Fragment {

    TextView desc;
    TextView contact;


    //Parameter:
    //Return values:
    //Sets the current fragment for the activity
    public createStudyFragment_finalStep_two() {
        createStudyActivity.currentFragment = 8;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_study_final_step_two, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setupView(view);
        loadData();
    }

    //Parameter:
    //Return values:
    //Connects the code with the view
    private void setupView(View view) {
        desc = view.findViewById(R.id.confirmDesc);
        contact = view.findViewById(R.id.confirmContact);


    }

    //Parameter:
    //Return values:
    //Loads data recieved from the activity into the textfields
    private void loadData() {
        Bundle bundle = getArguments();
        System.out.println(bundle);
        if (bundle != null) {
            desc.setText(bundle.getString("desc"));
            contact.setText(bundle.getString("contact"));
        }
    }



}

