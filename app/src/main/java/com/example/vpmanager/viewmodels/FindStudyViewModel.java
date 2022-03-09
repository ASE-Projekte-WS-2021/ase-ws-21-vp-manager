package com.example.vpmanager.viewmodels;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.vpmanager.models.StudyMetaInfoModel;
import com.example.vpmanager.repositories.StudyListRepository;

import java.util.ArrayList;

public class FindStudyViewModel extends ViewModel {

    private ArrayList<StudyMetaInfoModel> mStudyMetaInfo;
    private StudyListRepository mRepo;

    //Parameter:
    //Return Values:
    //get (always the same while app open) instance of the repo and fill ArrayList with StudyMetaInfoModel Objects
    public void init() {
        Log.d("FindStudyViewModel", "init start");
        mRepo = StudyListRepository.getInstance();
        //instance is the same but different data can be retrieved!
        mStudyMetaInfo = mRepo.getStudyMetaInfo();
        Log.d("FindStudyViewModel", "mStudyMetaInfo after init:" + mStudyMetaInfo.size());
        Log.d("FindStudyViewModel", "init end");
    }

    //Parameter:
    //Return Values: an arrayList with metaInfos of all currently existing studies
    //returns the arrayList of the viewModel
    public ArrayList<StudyMetaInfoModel> getStudyMetaInfo() {
        Log.d("FindStudyViewModel", "getStudyMetaInfo start + end (returns an ArrayList of StudyInfoModels)");
        return mStudyMetaInfo;
    }

    //Methods could be put together (Always get instance of repo, fill array with data from repo and return it afterwards)
}
