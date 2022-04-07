package com.example.vpmanager.views.createStudy;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.fragment.app.Fragment;

import com.example.vpmanager.Config;
import com.example.vpmanager.R;
import com.google.android.material.textfield.TextInputEditText;
import com.example.vpmanager.views.MainActivity;

import java.util.ArrayList;
import java.util.Arrays;


public class CreateStudyFragment_StepFour_Presence extends Fragment {

    public static TextInputEditText textInputEditTextLocation;
    public static TextInputEditText textInputEditTextStreet;
    public static TextInputEditText textInputEditTextRoom;
    @SuppressLint("StaticFieldLeak")
    public static AutoCompleteTextView autoCompleteTextViewPreset;
    private ArrayList<String> dropdownListPreset;
    private ArrayAdapter<String> arrayAdapterPreset;


    //Parameter:
    //Return values:
    //Sets the current fragment for the activity
    public CreateStudyFragment_StepFour_Presence() {
        CreateStudyFragment.currentFragment = Config.createFragmentFour;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MainActivity.currentFragment = "createStepFourPresence";
        return inflater.inflate(R.layout.fragment_create_study_step_four_presence, container, false);
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
        textInputEditTextLocation = view.findViewById(R.id.inputFieldLocation);
        textInputEditTextStreet = view.findViewById(R.id.inputFieldStreet);
        textInputEditTextRoom = view.findViewById(R.id.inputFieldRoom);
        autoCompleteTextViewPreset = view.findViewById(R.id.create_stepFour_local_Preset);
        dropdownListPreset = new ArrayList<>();
        dropdownListPreset.addAll(Arrays.asList(getResources().getStringArray(R.array.createPresetLabList)));
        arrayAdapterPreset = new ArrayAdapter<>(getActivity(), R.layout.item_material_dropdown, dropdownListPreset);
        autoCompleteTextViewPreset.setAdapter(arrayAdapterPreset);
        presetOnChangeListener();

    }


    private void presetOnChangeListener() {
        autoCompleteTextViewPreset.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                for (int i = 0; i < getResources().getStringArray(R.array.createPresetLabList).length; i++) {
                    if (s.toString().equals(getResources().getStringArray(R.array.createPresetLabList)[i])) {
                        textInputEditTextLocation.setText(getResources().getStringArray(R.array.createPresetLocations)[i]);
                        textInputEditTextStreet.setText(getResources().getStringArray(R.array.createPresetAddress)[i]);
                        textInputEditTextRoom.setText(getResources().getStringArray(R.array.createPresetRooms)[i]);
                        break;
                    }
                }
            }
        });
    }

    //Parameter:
    //Return values:
    //Loads data received from the activity into the inputfields
    private void loadData() {
        if (CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("location") != null) {
            textInputEditTextLocation.setText(
                    CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("location").toString());
        }
        if (CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("street") != null) {
            textInputEditTextStreet.setText(
                    CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("street").toString());
        }
        if (CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("room") != null) {
            textInputEditTextRoom.setText(
                    CreateStudyFragment.createStudyViewModel.studyCreationProcessData.get("room").toString());
        }

    }
}