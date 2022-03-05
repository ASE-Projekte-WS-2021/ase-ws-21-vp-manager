package com.example.vpmanager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class createStudyFragment_finalStep_two extends Fragment {

    ListView dateList;
    ArrayList<String> dates = new ArrayList<>();
    ArrayAdapter<String> datePickerAdapter;


    public createStudyFragment_finalStep_two() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_study_final_step_two, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setupView(view);

    }

    private void setupView(View view){
        dateList = view.findViewById(R.id.confirmDates);
        setupDatePicker();

    }

    private void setupDatePicker() {
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