package com.example.vpmanager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class createStudyFragment_StepThree extends Fragment {
    Spinner categories;
    Spinner executionType;

    public createStudyFragment_StepThree() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_study_step_three, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setupView(view);
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
    }



    private void setupView(View view){
        categories = view.findViewById(R.id.createCategories);
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.createCategoryList, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categories.setAdapter(categoryAdapter);

        executionType = view.findViewById(R.id.createExecutionType);
        ArrayAdapter<CharSequence> executionTypeAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.createExecutionTypeList, android.R.layout.simple_spinner_item);
        executionTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        executionType.setAdapter(executionTypeAdapter);
    }
}