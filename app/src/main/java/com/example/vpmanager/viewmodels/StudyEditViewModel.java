package com.example.vpmanager.viewmodels;


import androidx.lifecycle.ViewModel;

import com.example.vpmanager.interfaces.EditStudyDetailsListener;
import com.example.vpmanager.interfaces.StudyDatesListener;
import com.example.vpmanager.interfaces.StudyUpdatedListener;
import com.example.vpmanager.models.DateModel;
import com.example.vpmanager.repositories.StudyEditRepository;
import com.example.vpmanager.views.MainActivity;
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

    public ArrayList<String> idsOfAllInitialDates = new ArrayList<>();

    public StudyEditDetailsFragment studyEditDetailsFragment;
    public StudyEditDatesFragment studyEditDatesFragment;

    private StudyEditRepository mStudyEditRepo;


    //Parameter:
    //Return values:
    //Gets instance of the Study Edit repository and sets the FirestoreCallback
    public void prepareRepo() {
        mStudyEditRepo = StudyEditRepository.getInstance();
        //Instance is the same but different data can be retrieved
        mStudyEditRepo.setFirestoreCallback(this, this, this);
    }

    public void fetchEditStudyDetailsAndDates(String currentStudyIdEdit) {
        mStudyEditRepo.getStudyEditDetails(currentStudyIdEdit);
        mStudyEditRepo.getStudyEditDates(currentStudyIdEdit);
    }


    //Parameter:
    //Return values:
    //Manages the changes made in the edit process
    public void updateStudyAndDates() {

        //studyId is already in the map and can be reused
        //the creator isn't loaded in the first place so it is added here
        studyEditProcessData.put("creator", MainActivity.uniqueID);

        /*these two for-loops manage the deletion of dates in the db, that were in the db in the
        beginning but got deleted in the edit process*/
        ArrayList<String> idsOfTheProcessData = new ArrayList<>();
        for (int i = 0; i < datesEditProcessDataObjects.size(); i++) {
            idsOfTheProcessData.add(datesEditProcessDataObjects.get(i).getDateId());
        }
        for (int i = 0; i < idsOfAllInitialDates.size(); i++) {
            String currentDateIdOfAllInitialIds = idsOfAllInitialDates.get(i);
            if (!idsOfTheProcessData.contains(currentDateIdOfAllInitialIds)) {
                mStudyEditRepo.deleteDate(currentDateIdOfAllInitialIds);
            }
        }

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

               /* if another user already selected this date --> keep his/her id
                this if statement is necessary because getUserId() might throw a nullPointer*/
                if (datesEditProcessDataObjects.get(i).getUserId() != null) {
                    //if someone selected the date, we don't want to do anything
                } else {
                    //if no one selected the date or it is new, we want to set selected as null
                    localDateMap.put("userId", null);
                }

                localDateMap.put("date", datesEditProcessDataObjects.get(i).getDate());

                if (datesEditProcessDataObjects.get(i).getSelected()) {
                    //if someone selected the date, true can be set or nothing should happen
                } else {
                    //set true if no one selected this date
                    localDateMap.put("selected", false);
                }

                if (datesEditProcessDataObjects.get(i).getParticipation()) {
                } else {
                    localDateMap.put("participated", false);

                }

                dateIds.add(datesEditProcessDataObjects.get(i).getDateId());

                mStudyEditRepo.updateDates(localDateMap, localDateId);
            }
            //remove the previous list of dateIds
            studyEditProcessData.remove("dates");

            studyEditProcessData.put("dates", dateIds);
        }

        updateStudyFieldsWithInput();

        String studyId = studyEditProcessData.get("id").toString();
        mStudyEditRepo.updateStudy(studyEditProcessData, studyId);
    }


    //Parameter:
    //Return values:
    //Manages the changes made for fields with existing input values
    private void updateStudyFieldsWithInput() {
        studyEditProcessData.put("name", studyEditDetailsFragment.title.getText().toString());
        if (!studyEditDetailsFragment.vph.getText().toString().isEmpty()) {
            studyEditProcessData.put("vps", studyEditDetailsFragment.vph.getText().toString());
        } else {
            studyEditProcessData.put("vps", "0");
        }
        studyEditProcessData.put("category", studyEditDetailsFragment.categories.getText().toString());
        studyEditProcessData.put("executionType", studyEditDetailsFragment.executionType.getText().toString());

        studyEditProcessData.put("contact", studyEditDetailsFragment.contactMail.getText().toString());

        studyEditProcessData.put("contact2", studyEditDetailsFragment.contactPhone.getText().toString());
        studyEditProcessData.put("contact3", studyEditDetailsFragment.contactSkype.getText().toString());
        studyEditProcessData.put("contact4", studyEditDetailsFragment.contactDiscord.getText().toString());
        studyEditProcessData.put("contact5", studyEditDetailsFragment.contactOther.getText().toString());

        studyEditProcessData.put("description", studyEditDetailsFragment.description.getText().toString());

        if (studyEditDetailsFragment.executionType.getText().toString().equals("Remote")) {
            studyEditProcessData.put("platform", studyEditDetailsFragment.platformOne.getText().toString());
            studyEditProcessData.put("platform2", studyEditDetailsFragment.platformTwo.getText().toString());

            //the data not needed is deleted from the map. Later when the changed study is stored in the db,
            //unset fields are overwritten
            studyEditProcessData.remove("location");
            studyEditProcessData.remove("street");
            studyEditProcessData.remove("room");
        }
        if (studyEditDetailsFragment.executionType.getText().toString().equals("Pr??senz")) {
            studyEditProcessData.put("location", studyEditDetailsFragment.location.getText().toString());
            studyEditProcessData.put("street", studyEditDetailsFragment.street.getText().toString());
            studyEditProcessData.put("room", studyEditDetailsFragment.room.getText().toString());

            studyEditProcessData.remove("platform");
            studyEditProcessData.remove("platform2");
        }
    }


    //Parameter: newDate, studyId
    //Return values:
    //Gets DateModel objects and adds new element
    public void addNewDateToList(String newDate, String studyId) {
        DateModel newDateObject = new DateModel(
                getNewId(), newDate, studyId, MainActivity.uniqueID, false, false
        );
        datesEditProcessDataObjects.add(newDateObject);
        studyEditDatesFragment.notifyDatesObjectListChanged();
    }


    private String getNewId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public void onEditStudyDetailsReady(Map<String, Object> editDetailsMap) {
        studyEditProcessData = editDetailsMap;
        studyEditDetailsFragment.setDetails();
    }

    //called when the editFragment is opened
    @Override
    public void onStudyDatesReady(ArrayList<DateModel> datesArrayList) {
        datesEditProcessDataObjects = datesArrayList;
        //all initial dates need to be saved in a list!!!
        for (int i = 0; i < datesArrayList.size(); i++) {
            idsOfAllInitialDates.add(datesArrayList.get(i).getDateId());
        }

        studyEditDatesFragment.setupRecyclerView();
    }

    @Override
    public void onStudyUpdated() {
    }
}
