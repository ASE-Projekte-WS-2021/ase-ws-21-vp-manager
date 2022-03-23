package com.example.vpmanager.views.createStudy;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.vpmanager.R;
import com.google.android.material.textfield.TextInputEditText;

public class createStudyFragment_StepOne extends Fragment {

    public static TextInputEditText textInputEditTextTitle;
    public static TextInputEditText textInputEditTextVP;
    public static Spinner categories;
    public static Spinner executionType;

    //Parameter:
    //Return values:
    //Sets the current fragment for the activity
    public createStudyFragment_StepOne() {
        CreateStudyFragment.currentFragment = 1;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_study_step_one, container, false);
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

        textInputEditTextTitle = view.findViewById(R.id.inputFieldTitle);
        textInputEditTextVP = view.findViewById(R.id.inputFieldVP);

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


    //Parameter:
    //Return values:
    //Loads data recieved from the activity into the inputfields
    private void loadData() {

        if (CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("name") != null){
            textInputEditTextTitle.setText(CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("name").toString());
        }
        if (CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("vps") != null){
            textInputEditTextVP.setText(CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("vps").toString());
        }
        if (CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("category") != null){
            String cat = CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("category").toString();
            if (cat.equals("VR")) {
                categories.setSelection(1);
            }
            if (cat.equals("AR")) {
                categories.setSelection(2);
            }
            if (cat.equals("Diary Study")) {
                categories.setSelection(3);
            }
            if (cat.equals("Sonstige")) {
                categories.setSelection(4);
            }
        }
        if (CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("executionType") != null) {
            String exe = CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("executionType").toString();
            if (exe.equals("Remote")) {
                executionType.setSelection(1);
            }
            if (exe.equals("Präsenz")) {
                executionType.setSelection(2);
            }
        }
        /*
        Bundle bundle = getArguments();
        if(bundle != null){
            textInputEditTextTitle.setText(bundle.getString("title"));
            textInputEditTextVP.setText(bundle.getString("vp"));

            if(bundle.getString("category").equals("VR")){
                categories.setSelection(1);
            }
            if(bundle.getString("category").equals("AR")){
                categories.setSelection(2);
            }
            if(bundle.getString("category").equals("Diary Study")){
                categories.setSelection(3);
            }
            if(bundle.getString("category").equals("Sonstige")){
                categories.setSelection(4);
            }

            if(bundle.getString("exe").equals("Remote")){
                executionType.setSelection(1);
            }
            if(bundle.getString("exe").equals("Präsenz")){
                executionType.setSelection(2);
            }
        }
         */
    }

}