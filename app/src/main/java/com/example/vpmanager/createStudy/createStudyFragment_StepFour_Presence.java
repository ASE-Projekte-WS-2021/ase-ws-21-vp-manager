package com.example.vpmanager.createStudy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.vpmanager.R;
import com.google.android.material.textfield.TextInputEditText;

public class createStudyFragment_StepFour_Presence extends Fragment {

    TextInputEditText textInputEditTextLocation;
    TextInputEditText textInputEditTextStreet;
    TextInputEditText textInputEditTextRoom;
    //Parameter:
    //Return values:
    //Sets the current fragment for the activity
    public createStudyFragment_StepFour_Presence() {
        createStudyActivity.currentFragment = 5;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_study_step_four_presence, container, false);
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
        textInputEditTextLocation = view.findViewById(R.id.inputFieldLocation);
        textInputEditTextStreet = view.findViewById(R.id.inputFieldStreet);
        textInputEditTextRoom = view.findViewById(R.id.inputFieldRoom);

    }

    //Parameter:
    //Return values:
    //Loads data recieved from the activity into the inputfields
    private void loadData() {
        Bundle bundle = getArguments();
        if(bundle != null){
            textInputEditTextLocation.setText(bundle.getString("location"));
            textInputEditTextStreet.setText(bundle.getString("street"));
            textInputEditTextRoom.setText(bundle.getString("room"));
        }
    }
}