package com.example.vpmanager.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.vpmanager.models.StudyMetaInfoModel;
import com.example.vpmanager.repositories.StudyListRepository;

import java.util.ArrayList;
import java.util.List;

public class FindStudyViewModel extends ViewModel {

    private ArrayList<StudyMetaInfoModel> mStudyMetaInfo;
    private StudyListRepository mRepo;

    //get (always the same while app open) instance of the repo and fill ArrayList with StudyMetaInfoModel Objects
    public void init(){
        Log.d("FindStudyViewModel", "init start");

        //This part isn't needed because we always want to fetch new data from the db
        /*
        if (mStudyMetaInfo != null){
            Log.d("FindStudyViewModel",
                    "method init wasn't called because the MutableLiveDataList of " +
                            "StudyMetaInfoModels wasn't null --> not updated");
            Log.d("FindStudyViewModel", "init end");
            return;
        }
         */

        mRepo = StudyListRepository.getInstance();
        //instance is the same but different data can be retrieved!
        mStudyMetaInfo = mRepo.getStudyMetaInfo();
        Log.d("FindStudyViewModel", "mStudyMetaInfo after init:" + mStudyMetaInfo.size());
        Log.d("FindStudyViewModel", "init end");
    }

    public ArrayList<StudyMetaInfoModel> getStudyMetaInfo(){
        Log.d("FindStudyViewModel", "getStudyMetaInfo start + end (returns a MutableLiveDataList of StudyInfoModels)");
        return mStudyMetaInfo;
    }

    //Methods could be put together (Always get instance of repo, fill array with data from repo and return it after)!
}
