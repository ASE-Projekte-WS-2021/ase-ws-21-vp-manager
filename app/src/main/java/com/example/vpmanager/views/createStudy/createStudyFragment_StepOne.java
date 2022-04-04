package com.example.vpmanager.views.createStudy;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.example.vpmanager.Config;
import com.example.vpmanager.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class createStudyFragment_StepOne extends Fragment {

    public static TextInputEditText textInputEditTextTitle;
    public static TextInputEditText textInputEditTextVP;

    @SuppressLint("StaticFieldLeak")
    public static AutoCompleteTextView autoCompleteTextViewCategory;
    @SuppressLint("StaticFieldLeak")
    public static AutoCompleteTextView autoCompleteTextViewExecutionType;

    private ArrayList<String> dropdownListCategories, dropdownListExecutionType;
    private ArrayAdapter<String> arrayAdapterCategories, arrayAdapterExecutionType;


    //Parameter:
    //Return values:
    //Sets the current fragment for the activity
    public createStudyFragment_StepOne() {
        CreateStudyFragment.currentFragment = Config.createFragmentOne;
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

        autoCompleteTextViewCategory = view.findViewById(R.id.create_stepOne_textView_category);
        dropdownListCategories = new ArrayList<>();

        for (int i = 1; i < getResources().getStringArray(R.array.createCategoryList).length; i++) {
            dropdownListCategories.add(getResources().getStringArray(R.array.createCategoryList)[i]);
        }
        arrayAdapterCategories = new ArrayAdapter<>(getActivity(), R.layout.material_dropdown_item, dropdownListCategories);
        autoCompleteTextViewCategory.setAdapter(arrayAdapterCategories);

        autoCompleteTextViewExecutionType = view.findViewById(R.id.create_stepOne_textView_executionType);
        dropdownListExecutionType = new ArrayList<>();
        dropdownListExecutionType.add("Remote");
        dropdownListExecutionType.add("Präsenz");
        arrayAdapterExecutionType = new ArrayAdapter<>(getActivity(), R.layout.material_dropdown_item, dropdownListExecutionType);
        autoCompleteTextViewExecutionType.setAdapter(arrayAdapterExecutionType);
    }


    //Parameter:
    //Return values:
    //Loads data recieved from the activity into the inputfields
    private void loadData() {

        if (CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("name") != null) {
            textInputEditTextTitle.setText(CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("name").toString());
        }
        if (CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("vps") != null) {
            textInputEditTextVP.setText(CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("vps").toString());
        }
        if (CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("category") != null) {
            String cat = CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("category").toString();
            if (cat.equals(getString(R.string.fieldStudyText))) {
                //categories.setSelection(1);
                autoCompleteTextViewCategory.setText("Feldstudie", false);
            }
            if (cat.equals(getString(R.string.focusGroupText))) {
                //categories.setSelection(2);
                autoCompleteTextViewCategory.setText("Fokusgruppe", false);
            }
            if (cat.equals(getString(R.string.questionnaireText))) {
                //categories.setSelection(3);
                autoCompleteTextViewCategory.setText("Fragebogen", false);
            }
            if (cat.equals(getString(R.string.gamingText))) {
                //categories.setSelection(4);
                autoCompleteTextViewCategory.setText("Gamingstudie", false);
            }
            if (cat.equals(getString(R.string.interviewText))) {
                //categories.setSelection(4);
                autoCompleteTextViewCategory.setText("Interview", false);
            }
            if (cat.equals(getString(R.string.labStudyText))) {
                //categories.setSelection(4);
                autoCompleteTextViewCategory.setText("Laborstudien", false);
            }
            if (cat.equals(getString(R.string.diaryStudyText))) {
                //categories.setSelection(4);
                autoCompleteTextViewCategory.setText("Tagebuchstudie", false);
            }
            if (cat.equals(getString(R.string.usabilityText))) {
                //categories.setSelection(4);
                autoCompleteTextViewCategory.setText("Usability/UXstudie", false);
            }
            if (cat.equals(getString(R.string.othersText))) {
                //categories.setSelection(4);
                autoCompleteTextViewCategory.setText("Sonstige", false);
            }
        }
        if (CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("executionType") != null) {
            String exe = CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("executionType").toString();
            if (exe.equals(getString(R.string.remoteString))) {
                //executionType.setSelection(1);
                autoCompleteTextViewExecutionType.setText("Remote", false);
            }
            if (exe.equals(getString(R.string.presenceString))) {
                //executionType.setSelection(2);
                autoCompleteTextViewExecutionType.setText("Präsenz", false);
            }
        }

    }
}