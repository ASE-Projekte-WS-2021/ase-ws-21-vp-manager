package com.example.vpmanager.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.vpmanager.R;

public class createStudyFragment_StepThree extends Fragment {
    Spinner categories;
    Spinner executionType;

    //Parameter:
    //Return values:
    //Sets the current fragment for the activity
    public createStudyFragment_StepThree() {
        createStudyActivity.currentFragment = 4;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_study_step_three, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setupView(view);

    }


    //Parameter:
    //Return values:
    //Connects the code with the view
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