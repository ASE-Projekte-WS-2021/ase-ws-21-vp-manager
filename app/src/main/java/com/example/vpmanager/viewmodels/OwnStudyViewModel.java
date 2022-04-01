package com.example.vpmanager.viewmodels;

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

    public ArrayList<StudyMetaInfoModel> getOwnStudyMetaInfo() {
        return mOwnStudyMetaInfo;
    }

    @Override
    public void onStudyMetaDataReady(ArrayList<StudyMetaInfoModel> ownStudiesMetaInfosArrayList) {
        mOwnStudyMetaInfo = ownStudiesMetaInfosArrayList;
        selfcreatedStudiesFragment.connectOwnStudyListAdapter();
    }
}
