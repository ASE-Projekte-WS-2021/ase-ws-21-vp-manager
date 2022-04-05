package com.example.vpmanager.views.createStudy;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vpmanager.Config;
import com.example.vpmanager.R;
import com.example.vpmanager.views.mainActivity;


public class createStudyFragment_finalStep_two extends Fragment {


    private TextView descriptionTextView;
    private TextView contactTextView;


    //Parameter:
    //Return values:
    //Sets the current fragment for the activity
    public createStudyFragment_finalStep_two() {
        CreateStudyFragment.currentFragment = Config.createFragmentSeven;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainActivity.currentFragment = "createFinalStepTwo";
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
        descriptionTextView = view.findViewById(R.id.confirmDesc);
        contactTextView = view.findViewById(R.id.confirmContact);
    }


    //Parameter:
    //Return values:
    //Loads data received from the activity into the textfields
    private void loadData() {
        if (CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("description") != null) {
            String desc = CreateStudyFragment.createStudyViewModel.studyCreationProcessData
                    .get("description").toString();
            descriptionTextView.setText(desc);
        }

        String contactTxT = ""; // + "\n"
        if (CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("contact") != null) {
            String contactMail = CreateStudyFragment.createStudyViewModel.studyCreationProcessData
                    .get("contact").toString();
            contactTxT += contactMail;
        }
        if (CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("contact2") != null) {
            String contactPhone = CreateStudyFragment.createStudyViewModel.studyCreationProcessData
                    .get("contact2").toString();
            contactTxT += "\n" + "Tel.: " + contactPhone;
        }
        if (CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("contact3") != null) {
            String contactSkype = CreateStudyFragment.createStudyViewModel.studyCreationProcessData
                    .get("contact3").toString();
            contactTxT += "\n" + "Skype: " + contactSkype;
        }
        if (CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("contact4") != null) {
            String contactDiscord = CreateStudyFragment.createStudyViewModel.studyCreationProcessData
                    .get("contact4").toString();
            contactTxT += "\n" + "Discord: " + contactDiscord;
        }
        if (CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("contact5") != null) {
            String contactOther = CreateStudyFragment.createStudyViewModel.studyCreationProcessData
                    .get("contact5").toString();
            contactTxT += "\n" + "Sonstige: " + contactOther;
        }
        contactTextView.setText(contactTxT);

    }
}

