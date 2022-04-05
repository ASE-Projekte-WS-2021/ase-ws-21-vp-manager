package com.example.vpmanager.viewmodels;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.vpmanager.interfaces.StudyMetaDataListener;
import com.example.vpmanager.models.StudyMetaInfoModel;
import com.example.vpmanager.repositories.OwnStudyRepository;
import com.example.vpmanager.views.ownStudies.SelfcreatedStudiesFragment;

import java.util.ArrayList;

public class OwnStudyViewModel extends ViewModel implements StudyMetaDataListener {

    public SelfcreatedStudiesFragment selfcreatedStudiesFragment;

    private ArrayList<StudyMetaInfoModel> mOwnStudyMetaInfo;
    private OwnStudyRepository mOwnStudyRepo;

    public void prepareRepo() {
        mOwnStudyRepo = OwnStudyRepository.getInstance();
        mOwnStudyRepo.setFirestoreCallback(this);
    }

    public void fetchOwnStudyMetaData() {
        mOwnStudyRepo.getOwnStudyMetaInfo();
    }

    //Parameter:
    //Return Values: an arrayList containing StudyMetaInfoModel-objects of all currently existing studies
    //Returns the filled arrayList of study data
    public ArrayList<StudyMetaInfoModel> getStudyMetaInfo() {
        Log.d("FindStudyViewModel", "getStudyMetaInfo start + end (returns an ArrayList of StudyInfoModels)");
        return mOwnStudyMetaInfo;
    }

    public ArrayList<StudyMetaInfoModel> getOwnStudyMetaInfo() {
        return mOwnStudyMetaInfo;
    }

    @Override
    public void onStudyMetaDataReady(ArrayList<StudyMetaInfoModel> ownStudiesMetaInfosArrayList) {
        mOwnStudyMetaInfo = ownStudiesMetaInfosArrayList;
        selfcreatedStudiesFragment.connectOwnStudyListAdapter();
    }
}
