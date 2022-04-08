package com.example.vpmanager.views.studyCreatorDetails;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vpmanager.R;
import com.example.vpmanager.adapter.StudyDatesCreatorAdapter;
import com.example.vpmanager.viewmodels.StudyCreatorViewModel;


public class StudyCreatorDatesFragment extends Fragment {

    private StudyCreatorViewModel studyViewModel;
    private StudyDatesCreatorAdapter studyDatesAdapter;

    private String currentStudyId;

    //Show information about the dates of the study
    private RecyclerView datesList;


    public StudyCreatorDatesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_creator_study_appointments, container, false);
        prepareComponents();
        initViews(view);
        studyViewModel.fetchStudyDates(currentStudyId);
        return view;
    }


    //Parameter:
    //Return values:
    //Sets current study ID and the ViewModel
    private void prepareComponents() {
        currentStudyId = StudyCreatorFragment.currentStudyId;
        studyViewModel = new ViewModelProvider(getParentFragment()).get(StudyCreatorViewModel.class);
        studyViewModel.studyDatesFragment = this;
        studyViewModel.prepareRepo();
    }

    //Parameter: view
    //Return values:
    //Sets the view for the dateslist
    private void initViews(View view) {
        datesList = view.findViewById(R.id.recyclerViewStudyFragment);
    }

    //Parameter:
    //Return values:
    //Connects the recyclerView with data
    public void connectDatesAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity());
        studyDatesAdapter = new StudyDatesCreatorAdapter(requireActivity(), studyViewModel.getStudyDates(), null);
        datesList.setAdapter(studyDatesAdapter);
        datesList.setLayoutManager(linearLayoutManager);
    }

}