package com.example.vpmanager.viewmodels;

import androidx.lifecycle.ViewModel;

import com.example.vpmanager.interfaces.SelectUnselectDateListener;
import com.example.vpmanager.interfaces.StudyDatesListener;
import com.example.vpmanager.interfaces.StudyDetailsListener;
import com.example.vpmanager.models.DateModel;
import com.example.vpmanager.models.StudyDetailModel;
import com.example.vpmanager.repositories.StudyRepository;
import com.example.vpmanager.views.studyDetails.StudyCreatorDetailsFragment;
import com.example.vpmanager.views.studyDetails.StudyDatesFragment;
import com.example.vpmanager.views.studyDetails.StudyDetailsFragment;

import java.util.ArrayList;

public class StudyCreatorViewModel extends ViewModel implements StudyDetailsListener, StudyDatesListener, SelectUnselectDateListener {

    public StudyCreatorDetailsFragment studyCreatorDetailsFragment;
    public StudyDatesFragment studyDatesFragment;

    private StudyDetailModel mStudyDetails;
    private ArrayList<DateModel> mStudyDates;

    private ArrayList<String> userIdsOfDates = new ArrayList<>();
    private DateModel selectedDateObject;

    private StudyRepository mStudyRepo;

    public void prepareRepo() {
        mStudyRepo = StudyRepository.getInstance();
        //Instance is the same but different data can be retrieved!
        mStudyRepo.setFirestoreCallback(this, this, this);
    }

    public void fetchStudyDetails(String currentStudyId) {
        mStudyRepo.getStudyDetails(currentStudyId);
    }

    public void fetchStudyDates(String currentStudyId, String currentUserId) {
        mStudyRepo.getStudyDates(currentStudyId, currentUserId);
    }

    public void selectDate(String dateId, String currentUserId) {
        mStudyRepo.selectDate(dateId, currentUserId);
    }

    public void unselectDate() {
        String selectedDateId = selectedDateObject.getDateId();
        mStudyRepo.unselectDate(selectedDateId);
    }

    public StudyDetailModel getStudyDetails() {
        return mStudyDetails;
    }

    public ArrayList<DateModel> getStudyDates() {
        return mStudyDates;
    }

    public ArrayList<String> getUserIdsOfDates() {
        return userIdsOfDates;
    }

    public DateModel getSelectedDateObject() {
        return selectedDateObject;
    }

    public void findSelectedDateObject(String currentUserId) {
        if (userIdsOfDates.contains(currentUserId)) {
            //get the dateModelObject at the same position and safe it in a variable
            int position = userIdsOfDates.indexOf(currentUserId);
            selectedDateObject = new DateModel();
            selectedDateObject = mStudyDates.get(position);
        }
    }

    @Override
    public void onStudyDetailsReady(StudyDetailModel studyDetailModel) {
        mStudyDetails = studyDetailModel;
        studyCreatorDetailsFragment.setStudyDetails();
    }

    @Override
    public void onStudyDatesReady(ArrayList<DateModel> datesArrayList) {
        mStudyDates = datesArrayList;

        //set the list of userIds
        userIdsOfDates.clear();
        for (int i = 0; i < mStudyDates.size(); i++) {
            userIdsOfDates.add(mStudyDates.get(i).getUserId());
        }
        studyDatesFragment.setDatesView();
    }

    @Override
    public void onDateActionFinished() {
        studyDatesFragment.reloadDates();
    }
}
