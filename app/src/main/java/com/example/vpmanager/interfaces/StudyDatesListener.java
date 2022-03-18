package com.example.vpmanager.interfaces;

import com.example.vpmanager.models.DateModel;

import java.util.ArrayList;

public interface StudyDatesListener {
    void onStudyDatesReady(ArrayList<DateModel> datesArrayList);
}
