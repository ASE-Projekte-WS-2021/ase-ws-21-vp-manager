package com.example.vpmanager.views.createStudy;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.vpmanager.Config;
import com.example.vpmanager.R;
import com.example.vpmanager.views.MainActivity;

import java.util.ArrayList;

public class CreateStudyFragment_finalStep_three extends Fragment {


    //A recyclerView with custom item layout can be used here too.
    private ListView dateList;
    private ArrayAdapter<String> dateListAdapter;
    private ArrayList<String> currentDatesInProcess = new ArrayList<>();


    //Parameter:
    //Return values:
    //Sets the current fragment for the activity
    public CreateStudyFragment_finalStep_three() {
        CreateStudyFragment.currentFragment = Config.createFragmentEight;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MainActivity.currentFragment = "createFinalStepThree";
        return inflater.inflate(R.layout.fragment_create_study_final_step_three, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setupView(view);
        loadData();
    }


    //Parameter:
    //Return values:
    //Connects the code with the view
    private void setupView(View view) {
        dateList = view.findViewById(R.id.confirmDates);
    }


    //Parameter:
    //Return values:
    //Loads data received from the activity into the listview
    private void loadData() {
        if (!CreateStudyFragment.createStudyViewModel.datesCreationProcessData.isEmpty()) {
            currentDatesInProcess = CreateStudyFragment.createStudyViewModel.datesCreationProcessData;
            dateListAdapter = new ArrayAdapter<String>(requireActivity(), android.R.layout.simple_list_item_1,
                    currentDatesInProcess);
            dateList.setAdapter(dateListAdapter);
        }
    }
}