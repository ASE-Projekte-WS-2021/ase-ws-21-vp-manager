package com.example.vpmanager.viewmodels;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.vpmanager.models.StudyMetaInfoModel;
import com.example.vpmanager.repositories.StudyListRepository;
import com.example.vpmanager.views.FindStudyFragment;

import java.util.ArrayList;

public class FindStudyViewModel extends ViewModel implements StudyListRepository.StudyMetaDataListener {

    //not best practise in my opinion
    public FindStudyFragment findStudyFragment;

    private ArrayList<StudyMetaInfoModel> mStudyMetaInfo;
    private StudyListRepository mStudyListRepo;

    //Parameter:
    //Return Values:
    //Gets (always the same while app open) instance of the repository class and fills an arrayList with StudyMetaInfoModel-objects
    public void fetchStudyMetaData() {
        mStudyListRepo = StudyListRepository.getInstance();
        //Instance is the same but different data can be retrieved!
        mStudyListRepo.setFirestoreCallback(this);
        mStudyListRepo.getStudyMetaInfo();
    }

    //Parameter:
    //Return Values: an arrayList containing StudyMetaInfoModel-objects of all currently existing studies
    //Returns the filled arrayList of study data
    public ArrayList<StudyMetaInfoModel> getStudyMetaInfo() {
        Log.d("FindStudyViewModel", "getStudyMetaInfo start + end (returns an ArrayList of StudyInfoModels)");
        return mStudyMetaInfo;
    }

    //Parameter: an arrayList containing StudyMetaInfoModel-objects of all currently existing studies
    //Return Values:
    //Updates a list of study data when the DB call is finished and connects the adapter for the data to be rendered in the fragment
    @Override
    public void onStudyMetaDataReady(ArrayList<StudyMetaInfoModel> studyMetaInfosArrayList) {
        mStudyMetaInfo = studyMetaInfosArrayList;
        findStudyFragment.connectStudyListAdapter();
    }
}
