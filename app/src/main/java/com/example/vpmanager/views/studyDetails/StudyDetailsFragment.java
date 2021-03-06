package com.example.vpmanager.views.studyDetails;

import android.os.Bundle;

import com.example.vpmanager.R;
import com.example.vpmanager.viewmodels.StudyViewModel;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class StudyDetailsFragment extends Fragment {

    private StudyViewModel studyViewModel;

    private String currentStudyId;

    //Show detailed information of a study
    private TextView headerText;
    private TextView description;
    private TextView vpValue;
    private TextView contactInfo;
    private TextView category;
    private TextView studyType;
    private TextView remoteData;
    private TextView localData;


    public StudyDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_study_details, container, false);
        prepareComponents();
        initDetailViews(view);
        Log.d("StudyDetails", "viewModelInstance" + studyViewModel.toString());
        studyViewModel.fetchStudyDetails(currentStudyId);
        return view;
    }


    //Parameter:
    //Return values:
    //Sets the current study ID and ViewModel
    private void prepareComponents() {
        currentStudyId = StudyFragment.currentStudyId;
        studyViewModel = new ViewModelProvider(requireActivity()).get(StudyViewModel.class);
        studyViewModel.studyDetailsFragment = this;
        studyViewModel.prepareRepo();
    }


    //Parameter: view
    //Return values:
    //Initializes the views for the study detail
    private void initDetailViews(View view) {
        headerText = view.findViewById(R.id.studyFragmentHeader);
        description = view.findViewById(R.id.descriptionStudyFragment);
        vpValue = view.findViewById(R.id.vpValueStudyFragment);
        contactInfo = view.findViewById(R.id.contactInformationStudyFragment);
        category = view.findViewById(R.id.categoryStudyFragment);
        studyType = view.findViewById(R.id.studyTypeStudyFragment);

        //Textview for further studyType data (depending on type)
        remoteData = view.findViewById(R.id.remoteStudyStudyFragment);
        localData = view.findViewById(R.id.localStudyStudyFragment);
    }


    //Parameter:
    //Return values:
    //Sets the text views for the study details
    public void setStudyDetails() {
        //id could be accessed
        headerText.setText(studyViewModel.getStudyDetails().getName());
        description.setText(studyViewModel.getStudyDetails().getDescription());

        String contactOne, contactTwo, contactThree, contactFour, contactFive, contactString = "";
        //mail
        contactOne = studyViewModel.getStudyDetails().getContactOne();
        //handy
        contactTwo = studyViewModel.getStudyDetails().getContactTwo();
        //Skype
        contactThree = studyViewModel.getStudyDetails().getContactThree();
        //Discord
        contactFour = studyViewModel.getStudyDetails().getContactFour();
        //Sonstiges
        contactFive = studyViewModel.getStudyDetails().getContactFive();

        if (contactOne != null && !contactOne.isEmpty()) {
            contactString += "Mail: " + contactOne + "\n";
        }
        if (contactTwo != null && !contactTwo.isEmpty()) {
            contactString += "Telefon: " + contactTwo + "\n";
        }
        if (contactThree != null && !contactThree.isEmpty()) {
            contactString += "Skype: " + contactThree + "\n";
        }
        if (contactFour != null && !contactFour.isEmpty()) {
            contactString += "Discord: " + contactFour + "\n";
        }
        if (contactFive != null && !contactFive.isEmpty()) {
            contactString += "Andere: " + contactFive + "\n";
        }

        vpValue.setText(studyViewModel.getStudyDetails().getVps() + " VP");

        contactInfo.setText(contactString);

        category.setText(studyViewModel.getStudyDetails().getCategory());
        studyType.setText(studyViewModel.getStudyDetails().getExecutionType());
        if (studyViewModel.getStudyDetails().getExecutionType().equals(getString(R.string.remoteString))) {
            remoteData.setText(studyViewModel.getStudyDetails().getRemotePlatformOne());
            if (studyViewModel.getStudyDetails().getRemotePlatformTwo() != null) {
                String platforms = remoteData.getText().toString() + " & " + studyViewModel.getStudyDetails().getRemotePlatformTwo();
                remoteData.setText(platforms);
            }
            //remotePlatformTwo could be accessed
        } else {
            String locationString = studyViewModel.getStudyDetails().getLocation() + "\t\t" +
                    studyViewModel.getStudyDetails().getStreet() + "\t\t" + studyViewModel.getStudyDetails().getRoom();
            localData.setText(locationString);
        }
    }
}