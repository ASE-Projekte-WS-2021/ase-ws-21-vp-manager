package com.example.vpmanager.viewmodels;

import androidx.lifecycle.ViewModel;

import com.example.vpmanager.interfaces.StudyDatesListener;
import com.example.vpmanager.interfaces.StudyDetailsListener;
import com.example.vpmanager.models.DateModel;
import com.example.vpmanager.models.StudyDetailModel;
import com.example.vpmanager.repositories.StudyRepository;
import com.example.vpmanager.views.studyCreatorDetails.StudyCreatorDatesFragment;
import com.example.vpmanager.views.studyCreatorDetails.StudyCreatorDetailsFragment;

import java.util.ArrayList;

public class StudyCreatorViewModel extends ViewModel implements StudyDetailsListener, StudyDatesListener {

    public StudyCreatorDetailsFragment studyCreatorDetailsFragment;
    public StudyCreatorDatesFragment studyDatesFragment;

    private StudyDetailModel mStudyDetails;
    private ArrayList<DateModel> mStudyDates;

    private ArrayList<String> userIdsOfDates = new ArrayList<>();
    private DateModel selectedDateObject;

    private StudyRepository mStudyRepo;

    public void prepareRepo() {
        mStudyRepo = StudyRepository.getInstance();
        //Instance is the same but different data can be retrieved!
        mStudyRepo.setFirestoreCallback(this, this, null);
    }

    public void fetchStudyDetails(String currentStudyId) {
        mStudyRepo.getStudyDetails(currentStudyId);
    }

    public void fetchStudyDates(String currentStudyId) {
        mStudyRepo.getAllStudyDates(currentStudyId);
    }

    public StudyDetailModel getStudyDetails() {
        return mStudyDetails;
    }

    public ArrayList<DateModel> getStudyDates() {
        return mStudyDates;
    }

    @Override
    public void onStudyDetailsReady(StudyDetailModel studyDetailModel) {
        mStudyDetails = studyDetailModel;
        studyCreatorDetailsFragment.setStudyDetails();
    }

    @Override
    public void onStudyDatesReady(ArrayList<DateModel> datesArrayList) {
        mStudyDates = datesArrayList;
        studyDatesFragment.connectDatesAdapter();
    }
}
