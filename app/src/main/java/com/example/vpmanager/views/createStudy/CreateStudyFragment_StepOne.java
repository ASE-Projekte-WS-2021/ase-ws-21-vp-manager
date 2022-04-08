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
import com.example.vpmanager.views.MainActivity;

import java.util.ArrayList;

public class CreateStudyFragment_StepOne extends Fragment {

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
    public CreateStudyFragment_StepOne() {
        CreateStudyFragment.currentFragment = Config.createFragmentOne;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MainActivity.currentFragment = "createStepOne";
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
        arrayAdapterCategories = new ArrayAdapter<>(getActivity(), R.layout.item_material_dropdown, dropdownListCategories);
        autoCompleteTextViewCategory.setAdapter(arrayAdapterCategories);

        autoCompleteTextViewExecutionType = view.findViewById(R.id.create_stepOne_textView_executionType);
        dropdownListExecutionType = new ArrayList<>();
        dropdownListExecutionType.add("Remote");
        dropdownListExecutionType.add("Präsenz");
        arrayAdapterExecutionType = new ArrayAdapter<>(getActivity(), R.layout.item_material_dropdown, dropdownListExecutionType);
        autoCompleteTextViewExecutionType.setAdapter(arrayAdapterExecutionType);
    }


    //Parameter:
    //Return values:
    //Loads data recieved from the activity into the input fields
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
                autoCompleteTextViewCategory.setText(getString(R.string.fieldStudyText), false);
            }
            if (cat.equals(getString(R.string.focusGroupText))) {
                autoCompleteTextViewCategory.setText(getString(R.string.focusGroupText), false);
            }
            if (cat.equals(getString(R.string.questionnaireText))) {
                autoCompleteTextViewCategory.setText(getString(R.string.questionnaireText), false);
            }
            if (cat.equals(getString(R.string.gamingText))) {
                autoCompleteTextViewCategory.setText(getString(R.string.gamingText), false);
            }
            if (cat.equals(getString(R.string.interviewText))) {
                autoCompleteTextViewCategory.setText(getString(R.string.interviewText), false);
            }
            if (cat.equals(getString(R.string.labStudyText))) {
                autoCompleteTextViewCategory.setText(getString(R.string.labStudyText), false);
            }
            if (cat.equals(getString(R.string.diaryStudyText))) {
                autoCompleteTextViewCategory.setText(getString(R.string.diaryStudyText), false);
            }
            if (cat.equals(getString(R.string.usabilityText))) {
                autoCompleteTextViewCategory.setText(getString(R.string.usabilityText), false);
            }
            if (cat.equals(getString(R.string.othersText))) {
                autoCompleteTextViewCategory.setText(getString(R.string.othersText), false);
            }
        }
        if (CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("executionType") != null) {
            String exe = CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("executionType").toString();
            if (exe.equals(getString(R.string.remoteString))) {
                autoCompleteTextViewExecutionType.setText(getString(R.string.remoteString), false);
            }
            if (exe.equals(getString(R.string.presenceString))) {
                autoCompleteTextViewExecutionType.setText(getString(R.string.presenceString), false);
            }
        }

    }
}