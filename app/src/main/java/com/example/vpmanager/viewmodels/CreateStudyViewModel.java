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

    public void prepareRepo(){
        mCreationRepo = CreateStudyRepository.getInstance();
        //ViewModel is set as the listener for the callback
        mCreationRepo.setFirestoreCallback(this);
    }

    public void saveStudyInDb(){
        //before saving the study, a new ID and the CREATOR needs to be added to the map
        String studyId = getNewId();
        studyCreationProcessData.put("id", studyId);
        studyCreationProcessData.put("creator", mainActivity.uniqueID);

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

                dateIds.add(dateId);
                //Repo call
                mCreationRepo.createNewDate(newDate, dateId);
            }
            studyCreationProcessData.put("dates", dateIds);
        }
        //Repo call
        mCreationRepo.createNewStudy(studyCreationProcessData, studyId);
    }

    private String getNewId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public void onStudyCreated(Boolean created) {
        /*
        if (created){
            createStudyFragment.playAnimation();
        }else{
            createStudyFragment.showSnackBar();
        }
         */
    }
}
