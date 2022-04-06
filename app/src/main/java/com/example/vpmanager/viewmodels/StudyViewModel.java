package com.example.vpmanager.viewmodels;

import androidx.lifecycle.ViewModel;

import com.example.vpmanager.interfaces.SelectDateListener;
import com.example.vpmanager.interfaces.UnselectDateListener;
import com.example.vpmanager.interfaces.StudyDatesListener;
import com.example.vpmanager.interfaces.StudyDetailsListener;
import com.example.vpmanager.models.DateModel;
import com.example.vpmanager.models.StudyDetailModel;
import com.example.vpmanager.repositories.StudyRepository;
import com.example.vpmanager.views.studyDetails.StudyDatesFragment;
import com.example.vpmanager.views.studyDetails.StudyDetailsFragment;

import java.util.ArrayList;

public class StudyViewModel extends ViewModel implements StudyDetailsListener, StudyDatesListener,
        UnselectDateListener, SelectDateListener {

    public StudyDetailsFragment studyDetailsFragment;
    public StudyDatesFragment studyDatesFragment;

    private StudyDetailModel mStudyDetails;
    private ArrayList<DateModel> mStudyDates;
    private ArrayList<String> userIdsOfDates = new ArrayList<>();
    private DateModel selectedDateObject;

    private StudyRepository mStudyRepo;


    //Parameter:
    //Return values:
    //Gets instance of the study repository and sets the FirestoreCallback
    public void prepareRepo() {
        mStudyRepo = StudyRepository.getInstance();
        //Instance is the same but different data can be retrieved
        mStudyRepo.setFirestoreCallback(this, this, this, this);
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
        return removeExpiredDates(mStudyDates);
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
        studyDetailsFragment.setStudyDetails();
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
    public void onDateUnselected() {
        studyDatesFragment.showSnackBarDeselectionSuccessful();
        studyDatesFragment.reloadDates();
    }

    @Override
    public void onDateSelected(Boolean updated) {
        if (updated) {
            studyDatesFragment.showSnackBarSelectionSuccessful();
        } else {
            studyDatesFragment.showSnackBarSelectionUnsuccessful();
        }
        studyDatesFragment.reloadDates();
    }


    //Parameter: list
    //Return values: ArrayList<DateModel>
    //Removes the expired dates from the list view
    private ArrayList<DateModel> removeExpiredDates(ArrayList<DateModel> list) {
        ArrayList<DateModel> dateList = new ArrayList<>();

        for (DateModel date : list) {
            if (!DateModel.isDateInPast(date))
                dateList.add(date);
        }
        return dateList;
    }
}
