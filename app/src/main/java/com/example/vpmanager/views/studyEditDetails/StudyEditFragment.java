package com.example.vpmanager.views.studyEditDetails;

import static com.example.vpmanager.PA_ExpandableListDataPump.DB_DATES_LIST;
import static com.example.vpmanager.PA_ExpandableListDataPump.DB_STUDIES_LIST;
import static com.example.vpmanager.PA_ExpandableListDataPump.getAllDates;
import static com.example.vpmanager.PA_ExpandableListDataPump.getAllStudies;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import com.example.vpmanager.PA_ExpandableListDataPump;
import com.example.vpmanager.R;
import com.example.vpmanager.adapter.viewPagerAdapter;
import com.example.vpmanager.views.mainActivity;
import com.example.vpmanager.views.studyDetails.StudyFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class StudyEditFragment extends Fragment {

    private NavController navController;

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private StudyEditDetailsFragment studyEditDetailsFragment;
    private StudyEditDatesFragment studyEditDatesFragment;

    String currentStudyId;
    String currentUserId;

    EditText title;
    EditText name;
    EditText vp;
    Spinner categories;
    Spinner execution;
    EditText location;
    EditText description;
    EditText contact;

    Button saveButton;
    List<String> studyDateIds;

    //Parameter:
    //Return values:
    //Sets the current fragment for the activity
    public StudyEditFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_study, container, false);
        getRequiredInfos();

        viewPager = (ViewPager) view.findViewById(R.id.view_pager_edit_study);
        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout_edit_study);

        studyEditDetailsFragment = new StudyEditDetailsFragment();
        studyEditDatesFragment = new StudyEditDatesFragment();

        tabLayout.setupWithViewPager(viewPager);

        viewPagerAdapter viewPagerAdapter = new viewPagerAdapter(getParentFragmentManager(), 0);
        viewPagerAdapter.addFragment(studyEditDetailsFragment, "Details");
        viewPagerAdapter.addFragment(studyEditDatesFragment, "Termine");
        viewPager.setAdapter(viewPagerAdapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);
        super.onViewCreated(view, savedInstanceState);

        //TODO: wie im "StudyFragment" müssen diese methoden in die einzelnen Fragments ausgelagert werden
        //setupView(view);
        //loadData();
    }

    private void getRequiredInfos() {
        currentStudyId = getArguments().getString("studyId");
        currentUserId = mainActivity.createUserId(getActivity());
    }

    //Parameter:
    //Return values:
    //Connects the code with the view and sets Listener for Saving
    /*
    private void setupView(View view) {
        title = view.findViewById(R.id.edit_study_title);
        name = view.findViewById(R.id.edit_study_name);
        vp = view.findViewById(R.id.edit_study_vp);

        //category = view.findViewById(R.id.edit_study_category);

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


        execution = view.findViewById(R.id.edit_study_execution);
        location = view.findViewById(R.id.edit_study_location);
        description = view.findViewById(R.id.edit_study_description);
        contact = view.findViewById(R.id.edit_study_contact);
        //dates = view.findViewById(R.id.edit_study_dates);

        saveButton = view.findViewById(R.id.edit_saving_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, Object> dataMap = createDataMap();

                new Thread(() -> saveDataToDatabase(dataMap)).start();

                Bundle args = new Bundle();
                args.putString("studyId", currentStudyId);
                navController.navigate(R.id.action_editStudyFragment_to_studyCreatorFragment, args);
            }
        });
    }

     */


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
                if (Objects.requireNonNull(map.get("id")).toString().equals(currentStudyId)) {

                    loadSpinnerData(Objects.requireNonNull(map.get("category")).toString(), Objects.requireNonNull(map.get("executionType")).toString());
                    title.setText(Objects.requireNonNull(map.get("name")).toString());
                    name.setText(Objects.requireNonNull(map.get("name")).toString());
                    vp.setText(Objects.requireNonNull(map.get("vps")).toString());

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
                                if (dateMap != null && Objects.requireNonNull(dateMap.get("studyId")).toString().equals(currentStudyId)) {
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


    private void saveDataToDatabase(Map<String, Object> data) {
        PA_ExpandableListDataPump.updateStudyInDataBase(data, currentStudyId);
    }

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
}