package com.example.vpmanager.interfaces;

import com.example.vpmanager.models.StudyMetaInfoModel;

import java.util.ArrayList;

public interface StudyMetaDataListener {
    void onStudyMetaDataReady(ArrayList<StudyMetaInfoModel> studyMetaInfosArrayList);
}
