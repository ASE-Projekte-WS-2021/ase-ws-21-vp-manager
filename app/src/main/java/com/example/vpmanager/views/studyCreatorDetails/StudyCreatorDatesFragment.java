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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StudyCreatorDatesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudyCreatorDatesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private StudyCreatorViewModel studyViewModel;
    private StudyDatesCreatorAdapter studyDatesAdapter;

    private String currentStudyId;

    //Show information about the dates of the study
    private RecyclerView datesList;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StudyCreatorDatesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment studyDates.
     */
    // TODO: Rename and change types and number of parameters
    public static StudyCreatorDatesFragment newInstance(String param1, String param2) {
        StudyCreatorDatesFragment fragment = new StudyCreatorDatesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_creator_study_dates, container, false);
        prepareComponents();
        initViews(view);
        studyViewModel.fetchStudyDates(currentStudyId);
        return view;
    }

    private void prepareComponents() {
        currentStudyId = studyCreatorFragment.currentStudyId;
        studyViewModel = new ViewModelProvider(requireActivity()).get(StudyCreatorViewModel.class);
        studyViewModel.studyDatesFragment = this;
        studyViewModel.prepareRepo();
    }

    private void initViews(View view) {
        //View for the datesList
        datesList = view.findViewById(R.id.recyclerViewStudyFragment);
    }


    public void connectDatesAdapter() {
        //connect recyclerView with data here
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity());
        studyDatesAdapter = new StudyDatesCreatorAdapter(requireActivity(), studyViewModel.getStudyDates(), null);
        datesList.setAdapter(studyDatesAdapter);
        datesList.setLayoutManager(linearLayoutManager);
    }

}