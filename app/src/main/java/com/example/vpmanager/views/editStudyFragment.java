package com.example.vpmanager.views;

import static com.example.vpmanager.PA_ExpandableListDataPump.dbDatesList;
import static com.example.vpmanager.PA_ExpandableListDataPump.dbStudiesList;
import static com.example.vpmanager.PA_ExpandableListDataPump.getAllDates;
import static com.example.vpmanager.PA_ExpandableListDataPump.getAllStudies;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.vpmanager.PA_ExpandableListDataPump;
import com.example.vpmanager.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class editStudyFragment extends Fragment {

    NavController navController;

    String currentStudyId;
    String currentUserId;

    EditText title;
    EditText vp;
    EditText category;
    EditText execution;
    EditText location;
    EditText description;
    EditText dates;
    EditText contact;

    Button saveButton;
    ArrayList<String> studyDateIds;
    //Parameter:
    //Return values:
    //Sets the current fragment for the activity
    public editStudyFragment() {
        createStudyActivity.currentFragment = 7;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getRequiredInfos();

        return inflater.inflate(R.layout.fragment_edit_study, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        setupView(view);
        loadData();
    }

    private void getRequiredInfos() {
        //Get the studyId early
        currentStudyId = getArguments().getString("studyId");
        currentUserId = mainActivity.createUserId(getActivity());
    }

    //Parameter:
    //Return values:
    //Connects the code with the view and sets Listener for Saving
    private void setupView(View view){
        title = view.findViewById(R.id.edit_study_title);
        vp = view.findViewById(R.id.edit_study_vp);
        category = view.findViewById(R.id.edit_study_category);
        execution = view.findViewById(R.id.edit_study_execution);
        location = view.findViewById(R.id.edit_study_location);
        description = view.findViewById(R.id.edit_study_description);
        contact = view.findViewById(R.id.edit_study_contact);
        dates = view.findViewById(R.id.edit_study_dates);

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



    //Parameter:
    //Return values: Map with all inputs from Edittexts
    //Loads data in a Map for the Database
    private Map<String, Object> createDataMap()
    {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("category", category.getText() );
        dataMap.put("contact", contact.getText() );
        dataMap.put("dates", studyDateIds );
        dataMap.put("description", description.getText() );
        dataMap.put("executionType", execution.getText() );
        dataMap.put("name", title.getText() );

        if(execution.getText().equals("Remote"))
            dataMap.put("platform", location.getText() );
        else if(execution.getText().equals("Präsenz"))
        {
            String[] address = location.getText().toString().split("\n");
            dataMap.put("location", address[0]);
            dataMap.put("street", address[1]);
            dataMap.put("room", address[2]);
        }

        return dataMap;
    }

    //Parameter:
    //Return values:
    //Loads data recieved from the database into the edittexts
    private void loadData() {

        getAllStudies(() -> {
            for(Map<String, Object> map : dbStudiesList)
            {
                if(Objects.requireNonNull(map.get("id")).toString().equals(currentStudyId))
                {
                    title.setText(Objects.requireNonNull(map.get("name")).toString());
                    vp.setText(Objects.requireNonNull(map.get("vps")).toString());
                    category.setText(Objects.requireNonNull(map.get("category")).toString());
                    execution.setText(Objects.requireNonNull(map.get("executionType")).toString());

                    description.setText(Objects.requireNonNull(map.get("description")).toString());

                    contact.setText(Objects.requireNonNull(map.get("contact")).toString());
                    //dates.setText(Objects.requireNonNull(map.get("id")).toString());

                    if(execution.getText().equals("Remote")) {
                        location.setText(Objects.requireNonNull(map.get("platform")).toString());
                    }
                    else
                    {
                        String locationString = Objects.requireNonNull(map.get("location")).toString() + "\n "
                                +Objects.requireNonNull(map.get("street")).toString()+ "\n "
                                +Objects.requireNonNull(map.get("room")).toString();

                        location.setText(locationString);
                    }

                    StringBuilder dateList = new StringBuilder();

                    getAllDates(finished -> {
                        if(finished)
                        {
                            for(Map<String, Object> dateMap: dbDatesList)
                            {
                                if(dateMap != null && Objects.requireNonNull(dateMap.get("studyId")).toString().equals(currentStudyId))
                                {
                                    dateList.append(Objects.requireNonNull(dateMap.get("date")).toString()).append("\n");
                                    studyDateIds.add(Objects.requireNonNull(dateMap.get("id")).toString());
                                }
                            }
                        }
                    });
                    dates.setText(dateList);
                }
            }
        });
        }


    private void saveDataToDatabase(Map<String, Object> data)
    {
        PA_ExpandableListDataPump.updateStudyInDataBase(data, currentStudyId);
    }
}