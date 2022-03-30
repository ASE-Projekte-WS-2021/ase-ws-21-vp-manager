package com.example.vpmanager.views.studyEditDetails;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.vpmanager.R;
import com.example.vpmanager.viewmodels.StudyEditViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Objects;

public class StudyEditDetailsFragment extends Fragment {

    private String currentStudyIdEdit;
    private StudyEditViewModel studyEditViewModel;
    private NavController navController;

    public static TextInputEditText title;
    public static TextInputEditText vph;
    @SuppressLint("StaticFieldLeak")
    public static AutoCompleteTextView categories;
    @SuppressLint("StaticFieldLeak")
    public static AutoCompleteTextView executionType;

    private TextInputEditText contactMail;
    private TextInputEditText contactPhone;
    private TextInputEditText contactSkype;
    private TextInputEditText contactDiscord;
    private TextInputEditText contactOther;

    private TextInputEditText description;

    private TextInputEditText platformOne;
    private TextInputEditText platformTwo;

    private TextInputEditText location;
    private TextInputEditText street;
    private TextInputEditText room;

    private Button saveButton;

    public StudyEditDetailsFragment(){
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_study_details, container, false);
        prepareComponents();
        setupView(view);
        //dates also need to be loaded here!!
        studyEditViewModel.fetchEditStudyDetailsAndDates(currentStudyIdEdit);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);
        super.onViewCreated(view, savedInstanceState);
    }

    private void prepareComponents() {
        currentStudyIdEdit = StudyEditFragment.currentStudyIdEdit;
        //maybe "this"?
        studyEditViewModel = new ViewModelProvider(getParentFragment()).get(StudyEditViewModel.class);
        studyEditViewModel.studyEditDetailsFragment = this;
        studyEditViewModel.prepareRepo();
        Log.d("DetailsFragment", "viewModelStore: " + getParentFragment().toString());
    }

    //Parameter:
    //Return values:
    //Connects the code with the view and sets Listener for Saving
    private void setupView(View view) {

        title = view.findViewById(R.id.edit_study_title);
        vph = view.findViewById(R.id.edit_study_vph);

        ArrayList<String> dropdownListCategories, dropdownListExecutionType;
        ArrayAdapter<String> arrayAdapterCategories, arrayAdapterExecutionType;

        categories = view.findViewById(R.id.edit_study_category);
        dropdownListCategories = new ArrayList<>();;
        dropdownListCategories.add("VR");
        dropdownListCategories.add("AR");
        dropdownListCategories.add("Diary Study");
        dropdownListCategories.add("Sonstige");
        arrayAdapterCategories = new ArrayAdapter<>(getActivity(), R.layout.material_dropdown_item, dropdownListCategories);
        categories.setAdapter(arrayAdapterCategories);

        executionType = view.findViewById(R.id.edit_study_executionType);
        dropdownListExecutionType = new ArrayList<>();
        dropdownListExecutionType.add("Remote");
        dropdownListExecutionType.add("Präsenz");
        arrayAdapterExecutionType = new ArrayAdapter<>(getActivity(), R.layout.material_dropdown_item, dropdownListExecutionType);
        executionType.setAdapter(arrayAdapterExecutionType);
        /*
        categories = view.findViewById(R.id.edit_study_category);
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.createCategoryList, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categories.setAdapter(categoryAdapter);


        execution = view.findViewById(R.id.edit_study_execution);
        ArrayAdapter<CharSequence> executionTypeAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.createExecutionTypeList, android.R.layout.simple_spinner_item);
        executionTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        execution.setAdapter(executionTypeAdapter);
         */

        contactMail = view.findViewById(R.id.edit_study_contact_mail);
        contactPhone = view.findViewById(R.id.edit_study_contact_phone);
        contactSkype = view.findViewById(R.id.edit_study_contact_skype);
        contactDiscord = view.findViewById(R.id.edit_study_contact_discord);
        contactOther = view.findViewById(R.id.edit_study_contact_others);

        description = view.findViewById(R.id.edit_study_description);

        location = view.findViewById(R.id.edit_study_location);
        street = view.findViewById(R.id.edit_study_street);
        room = view.findViewById(R.id.edit_study_room);

        platformOne = view.findViewById(R.id.edit_study_platform_one);
        platformTwo = view.findViewById(R.id.edit_study_platform_two);

        saveButton = view.findViewById(R.id.edit_study_saving_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                studyEditViewModel.updateStudyAndDates();
            }
        });
    }

    public void navigateToCreatorDetailsView(){
        Bundle args = new Bundle();
        args.putString("studyId", currentStudyIdEdit);
        args.putBoolean("fromEditFragment", true);
        navController.navigate(R.id.action_editStudyFragment_to_studyCreatorFragment, args);
    }

    public void setDetails(){

        Log.d("DetailsFragment", "setDetails: " + studyEditViewModel.studyEditProcessData.toString());
        //title, vph, category, executionType
        if (studyEditViewModel.studyEditProcessData.get("name") != null){
            title.setText(studyEditViewModel.studyEditProcessData.get("name").toString());
        }
        if (studyEditViewModel.studyEditProcessData.get("vps") != null){
            vph.setText(studyEditViewModel.studyEditProcessData.get("vps").toString());
        }
        if (studyEditViewModel.studyEditProcessData.get("category") != null){
            String cat = studyEditViewModel.studyEditProcessData.get("category").toString();
            if (cat.equals("VR")) {
                categories.setText("VR", false);
            }
            if (cat.equals("AR")) {
                categories.setText("AR", false);
            }
            if (cat.equals("Diary Study")) {
                categories.setText("Diary Study", false);
            }
            if (cat.equals("Sonstige")) {
                categories.setText("Sonstige", false);
            }
        }
        if (studyEditViewModel.studyEditProcessData.get("executionType") != null) {
            String exe = studyEditViewModel.studyEditProcessData.get("executionType").toString();
            if (exe.equals("Remote")) {
                executionType.setText("Remote", false);
            }
            if (exe.equals("Präsenz")) {
                executionType.setText("Präsenz", false);
            }
        }

        //contacts
        //mail is never null (mandatory mail)
        if (studyEditViewModel.studyEditProcessData.get("contact") != null){
            contactMail.setText(studyEditViewModel.studyEditProcessData.get("contact").toString());
            contactMail.setFocusable(false);
            contactMail.setTextColor(ContextCompat.getColor(getActivity(), R.color.pieChartRemaining));
        }
        if (studyEditViewModel.studyEditProcessData.get("contact2") != null){
            contactPhone.setText(studyEditViewModel.studyEditProcessData.get("contact2").toString());
        }
        if (studyEditViewModel.studyEditProcessData.get("contact3") != null){
            contactSkype.setText(studyEditViewModel.studyEditProcessData.get("contact3").toString());
        }
        if (studyEditViewModel.studyEditProcessData.get("contact4") != null){
            contactDiscord.setText(studyEditViewModel.studyEditProcessData.get("contact4").toString());
        }
        if (studyEditViewModel.studyEditProcessData.get("contact5") != null){
            contactOther.setText(studyEditViewModel.studyEditProcessData.get("contact5").toString());
        }

        //description
        if (studyEditViewModel.studyEditProcessData.get("description") != null){
            description.setText(studyEditViewModel.studyEditProcessData.get("description").toString());
        }

        //platformOne, platformTwo
        if (studyEditViewModel.studyEditProcessData.get("platform") != null){
            platformOne.setText(studyEditViewModel.studyEditProcessData.get("platform").toString());
        }
        if (studyEditViewModel.studyEditProcessData.get("platform2") != null){
            platformTwo.setText(studyEditViewModel.studyEditProcessData.get("platform2").toString());
        }

        //location, street, room
        if (studyEditViewModel.studyEditProcessData.get("location") != null){
            location.setText(studyEditViewModel.studyEditProcessData.get("location").toString());
        }
        if (studyEditViewModel.studyEditProcessData.get("street") != null){
            street.setText(studyEditViewModel.studyEditProcessData.get("street").toString());
        }
        if (studyEditViewModel.studyEditProcessData.get("room") != null){
            room.setText(studyEditViewModel.studyEditProcessData.get("room").toString());
        }
    }

    //Parameter:
    //Return values: Map with all inputs from Edittexts
    //Loads data in a Map for the Database
    /*
    private Map<String, Object> createDataMap() {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("category", categories.getSelectedItem());
        dataMap.put("contact", contact.getText());
        //dataMap.put("dates", studyDateIds);
        dataMap.put("description", description.getText());
        dataMap.put("executionType", execution.getSelectedItem());
        dataMap.put("name", title.getText());

        if (execution.getSelectedItem().equals("Remote"))
            dataMap.put("platform", location.getText());
        else if (execution.getSelectedItem().equals("Präsenz")) {
            String[] address = location.getText().toString().split("\n");
            dataMap.put("location", address[0]);
            dataMap.put("street", address[1]);
            dataMap.put("room", address[2]);
        }

        return dataMap;
    }

     */

    //Parameter:
    //Return values:
    //Loads data recieved from the database into the edittexts
    /*
    private void loadData() {
        studyDateIds = new ArrayList<>();
        getAllStudies(() -> {
            for (Map<String, Object> map : DB_STUDIES_LIST) {
                if (Objects.requireNonNull(map.get("id")).toString().equals(currentStudyIdEdit)) {

                    loadSpinnerData(Objects.requireNonNull(map.get("category")).toString(), Objects.requireNonNull(map.get("executionType")).toString());
                    title.setText(Objects.requireNonNull(map.get("name")).toString());
                    //name.setText(Objects.requireNonNull(map.get("name")).toString());
                    vph.setText(Objects.requireNonNull(map.get("vps")).toString());

                    description.setText(Objects.requireNonNull(map.get("description")).toString());

                    contact.setText(Objects.requireNonNull(map.get("contact")).toString());
                    //dates.setText(Objects.requireNonNull(map.get("id")).toString());

                    if (execution.getSelectedItem().equals("Remote")) {
                        location.setText(Objects.requireNonNull(map.get("platform")).toString());
                    } else {
                        if (map.get("location") != null) {
                            String locationString = Objects.requireNonNull(map.get("location")).toString() + "\n "
                                    + Objects.requireNonNull(map.get("street")).toString() + "\n "
                                    + Objects.requireNonNull(map.get("room")).toString();

                            location.setText(locationString);
                        }
                    }

                    StringBuilder dateList = new StringBuilder();

                    getAllDates(finished -> {
                        if (finished) {
                            for (Map<String, Object> dateMap : DB_DATES_LIST) {
                                if (dateMap != null && Objects.requireNonNull(dateMap.get("studyId")).toString().equals(currentStudyIdEdit)) {
                                    dateList.append(Objects.requireNonNull(dateMap.get("date")).toString()).append("\n");
                                    System.out.println("Pog: " +dateMap.get("id"));
                                    if (dateMap.get("id") != null) {
                                        studyDateIds.add(Objects.requireNonNull(dateMap.get("id")).toString());
                                    }
                                }
                            }
                        }
                    });
                    //  dates.setText(dateList);
                }
            }
        });
    }
     */

    /*
    private void saveDataToDatabase(Map<String, Object> data) {
        PA_ExpandableListDataPump.updateStudyInDataBase(data, currentStudyIdEdit);
    }
     */

    /*
    private void loadSpinnerData(String categoryType, String executionType) {

        if(categoryType != null){
            if(categoryType.equals("VR")){
                categories.setSelection(1);
            }
            if(categoryType.equals("AR")){
                categories.setSelection(2);
            }
            if(categoryType.equals("Diary Study")){
                categories.setSelection(3);
            }
            if(categoryType.equals("Sonstige")){
                categories.setSelection(4);
            }
            if(executionType != null) {
                if (executionType.equals("Remote")) {
                    execution.setSelection(1);
                }
                if (executionType.equals("Präsenz")) {
                    execution.setSelection(2);
                }
            }
        }
    }
     */

}
