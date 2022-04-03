package com.example.vpmanager.viewmodels;

import androidx.lifecycle.ViewModel;

import com.example.vpmanager.views.createStudy.CreateStudyFragment;
import com.example.vpmanager.interfaces.CreateStudyListener;
import com.example.vpmanager.repositories.CreateStudyRepository;
import com.example.vpmanager.views.mainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CreateStudyViewModel extends ViewModel implements CreateStudyListener {


    //For storing data during the creation process
    public Map<String, Object> studyCreationProcessData = new HashMap<>();
    public ArrayList<String> datesCreationProcessData = new ArrayList<>();

    private CreateStudyRepository mCreationRepo;
    public CreateStudyFragment createStudyFragment;



    //Parameter:
    //Return values:
    //Prepare the createStudy repository; ViewModel is set as the listener for the callback
    public void prepareRepo() {
        mCreationRepo = CreateStudyRepository.getInstance();
        mCreationRepo.setFirestoreCallback(this);
    }


    //Parameter:
    //Return values:
    //save study data in database
    //Before saving the study a new ID and the CREATOR needs to be added to the map
    public void saveStudyInDb() {

        String studyId = getNewId();
        studyCreationProcessData.put("id", studyId);
        studyCreationProcessData.put("creator", mainActivity.uniqueID);
        studyCreationProcessData.put("studyStateClosed", false);

        if (!datesCreationProcessData.isEmpty()) {
            //local list of all dateIds to pass to the studyMap
            ArrayList<String> dateIds = new ArrayList<>();

            for (int i = 0; i < datesCreationProcessData.size(); i++) {
                //for every date-String a new map is built and stored in the db
                Map<String, Object> newDate = new HashMap<>();
                String dateId = getNewId();

                newDate.put("id", dateId);
                newDate.put("studyId", studyId);
                newDate.put("userId", null);
                newDate.put("date", datesCreationProcessData.get(i));
                newDate.put("selected", false);
                newDate.put("participated", false);

                dateIds.add(dateId);
                //Repo call
                mCreationRepo.createNewDate(newDate, dateId);
            }
            studyCreationProcessData.put("dates", dateIds);
        }
        //Repo call
        mCreationRepo.createNewStudy(studyCreationProcessData, studyId);
    }


    //Parameter:
    //Return values: String
    //Get study ID
    private String getNewId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public void onStudyCreated(Boolean created) {
    }

}
