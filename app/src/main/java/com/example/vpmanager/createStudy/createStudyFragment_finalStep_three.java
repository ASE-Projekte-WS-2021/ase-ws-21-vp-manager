package com.example.vpmanager.createStudy;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.vpmanager.R;

import java.util.ArrayList;

public class createStudyFragment_finalStep_three extends Fragment {

    ListView dateList;
    ArrayList<String> dates = new ArrayList<>();
    ArrayAdapter<String> datePickerAdapter;


    //Parameter:
    //Return values:
    //Sets the current fragment for the activity
    public createStudyFragment_finalStep_three() {
        createStudyActivity.currentFragment = 9;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_study_final_step_three, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setupView(view);

    }

    //Parameter:
    //Return values:
    //Connects the code with the view
    private void setupView(View view){
        dateList = view.findViewById(R.id.confirmDates);
        loadData();

    }

    //Parameter:
    //Return values:
    //Loads data recieved from the activity into the listview
    private void loadData() {
        Bundle bundle = getArguments();
        System.out.println(bundle);
        if (bundle != null) {
            datePickerAdapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1,
                    bundle.getStringArrayList("dates"));
            dateList.setAdapter(datePickerAdapter);
        }
        datePickerAdapter.notifyDataSetChanged();
    }
}