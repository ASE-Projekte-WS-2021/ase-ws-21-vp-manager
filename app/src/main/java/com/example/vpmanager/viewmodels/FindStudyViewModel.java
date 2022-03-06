package com.example.vpmanager.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.vpmanager.models.StudyMetaInfoModel;
import com.example.vpmanager.repositories.StudyListRepository;

import java.util.List;

public class FindStudyViewModel extends ViewModel {

    private MutableLiveData<List<StudyMetaInfoModel>> mStudyMetaInfo;
    private StudyListRepository mRepo;

    //init gets called before getStudyMetaInfo in the Fragment!
    public void init(){
        Log.d("FindStudyViewModel", "init start");

        if (mStudyMetaInfo != null){
            Log.d("FindStudyViewModel",
                    "method init wasn't called because the MutableLiveDataList of " +
                            "StudyMetaInfoModels wasn't null --> not updated");
            Log.d("FindStudyViewModel", "init end");
            return;
        }

        mRepo = StudyListRepository.getInstance();
        Log.d("FindStudyViewModel",
                "mRepo.getStudyMetaInfo() will be called. This returns a MutableLiveDataList of StudyMetaInfoModel");
        mStudyMetaInfo = mRepo.getStudyMetaInfo();

        Log.d("FindStudyViewModel", "init end");
    }

    public LiveData<List<StudyMetaInfoModel>> getStudyMetaInfo(){
        Log.d("FindStudyViewModel", "getStudyMetaInfo start + end (returns a MutableLiveDataList of StudyInfoModels)");
        return mStudyMetaInfo;
    }
}
