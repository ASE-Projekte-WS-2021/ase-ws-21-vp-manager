package com.example.vpmanager.viewmodels;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.vpmanager.interfaces.EditStudyDetailsListener;
import com.example.vpmanager.interfaces.StudyDatesListener;
import com.example.vpmanager.interfaces.StudyUpdatedListener;
import com.example.vpmanager.models.DateModel;
import com.example.vpmanager.repositories.StudyEditRepository;
import com.example.vpmanager.views.mainActivity;
import com.example.vpmanager.views.studyEditDetails.StudyEditDatesFragment;
import com.example.vpmanager.views.studyEditDetails.StudyEditDetailsFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StudyEditViewModel extends ViewModel implements EditStudyDetailsListener,
        StudyDatesListener, StudyUpdatedListener {

    public Map<String, Object> studyEditProcessData = new HashMap<>();
    public ArrayList<DateModel> datesEditProcessDataObjects = new ArrayList<>();

    public StudyEditDetailsFragment studyEditDetailsFragment;
    public StudyEditDatesFragment studyEditDatesFragment;

    private StudyEditRepository mStudyEditRepo;

    public void prepareRepo(){
        mStudyEditRepo = StudyEditRepository.getInstance();
        //Instance is the same but different data can be retrieved!
        mStudyEditRepo.setFirestoreCallback(this, this, this);
    }

    public void fetchEditStudyDetailsAndDates(String currentStudyIdEdit) {
        mStudyEditRepo.getStudyEditDetails(currentStudyIdEdit);
        mStudyEditRepo.getStudyEditDates(currentStudyIdEdit);
    }

    public void updateStudyAndDates(){

        //studyId is already in the map and can be reused
        //the creator isn't loaded in the first place so is is added here
        studyEditProcessData.put("creator", mainActivity.uniqueID);

        //if no dates were loaded or added, nothing needs to be saved and added to the study
        if (!datesEditProcessDataObjects.isEmpty()) {
            //local list of all dateIds to pass to the studyMap
            //this list replaces an existing one and is added when all dates has been saved or updated
            ArrayList<String> dateIds = new ArrayList<>();

            for (int i = 0; i < datesEditProcessDataObjects.size(); i++) {
                //create a new map for every date object in the list
                Map<String, Object> localDateMap = new HashMap<>();
                String localDateId = datesEditProcessDataObjects.get(i).getDateId();

                localDateMap.put("id", localDateId);
                localDateMap.put("studyId", datesEditProcessDataObjects.get(i).getStudyId());

                //if another user already selected this date --> keep his/her id
                //this if statement is necessary because getUserId() might throw a nullPointer
                if (datesEditProcessDataObjects.get(i).getUserId() != null) {
                    //wenn sich jemand eingetragen hat will ich mit dem feld nix machen!!!!!!!!
                    //localDateMap.put("userId", datesEditProcessDataObjects.get(i).getUserId());
                } else {
                    //wenn sich keiner eingetragen hat bzw. der termin neu ist, möchte ich null (neu) setzen!!!!!!!
                    localDateMap.put("userId", null);
                }

                localDateMap.put("date", datesEditProcessDataObjects.get(i).getDate());

                if (datesEditProcessDataObjects.get(i).getSelected()){
                    //wenn jemand den termin gewählt hatte, kann ich das true wieder setzen oder einfach auslassen
                    //localDateMap.put("selected", true);
                } else {
                    //wenn keiner den termin gewählt hat oder er neu ist, muss ich false setzen!!!
                    localDateMap.put("selected", false);
                }

                dateIds.add(datesEditProcessDataObjects.get(i).getDateId());

                //die, in der größe variable, map wird ans repo übergeben und das setzt oder updated den jeweiligen termin!
                mStudyEditRepo.updateDates(localDateMap, localDateId);
            }
            //the previous list of dateIds needs to be removed!?
            studyEditProcessData.remove("dates");
            //the new list of dateIds needs to be saved in the study
            studyEditProcessData.put("dates", dateIds);
        }
        String studyId = studyEditProcessData.get("id").toString();
        mStudyEditRepo.updateStudy(studyEditProcessData, studyId);
    }

    public void addNewDateToList(String newDate, String studyId){
        Log.d("StudyEditViewModel", "addNewDateToList: before" + datesEditProcessDataObjects.toString());
        DateModel newDateObject = new DateModel(
                getNewId(), newDate, studyId, mainActivity.uniqueID, false
        );
        datesEditProcessDataObjects.add(newDateObject);
        Log.d("StudyEditViewModel", "addNewDateToList: after" + datesEditProcessDataObjects.toString());
        studyEditDatesFragment.notifyDatesObjectListChanged();
    }

    private String getNewId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public void onEditStudyDetailsReady(Map<String, Object> editDetailsMap) {
        studyEditProcessData = editDetailsMap;
        studyEditDetailsFragment.setDetails();
        Log.d("studyReady", ": " + studyEditProcessData.toString());
    }

    @Override
    public void onStudyDatesReady(ArrayList<DateModel> datesArrayList) {
        datesEditProcessDataObjects = datesArrayList;
        Log.d("datesReady 1", ": " + datesEditProcessDataObjects.toString());
        studyEditDatesFragment.notifyDatesObjectListChanged();
        //the dates fragment loads the dates when needed
    }

    @Override
    public void onStudyUpdated() {
        studyEditDetailsFragment.navigateToCreatorDetailsView();
    }
}
