package com.example.vpmanager;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class studyActivity extends AppCompatActivity{

    ListView dateList;
    String currentStudyId;

    ArrayList<String> studyDetails;

    ArrayList<ArrayList<String>> dateInfo;
    ArrayList<String> allDates;
    ArrayList<String> dateIds;
    ArrayList<String> userIdsOfDates;

    ArrayList<String> savedDateItem;
    ArrayAdapter availableDatesAdapter;
    ArrayAdapter savedDateAdapter;

    FirebaseFirestore db;
    DocumentReference studyRef;
    CollectionReference datesRef;
    accessDatabase accessDatabase = new accessDatabase();

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);
        //Get the studyId early
        currentStudyId = getIntent().getStringExtra("studyId");
        savedDateItem = new ArrayList<>();
        savedDateItem.add("Sie haben sich bereits für einen Termin eingetragen.");

        setupStudyDetails(new FirestoreCallbackStudy() {
            @Override
            public void onCallback(ArrayList<String> arrayList) {
                loadStudyData();
            }
        });

        setupDateListView(new FirestoreCallbackDates() {
            @Override
            public  void onCallback(ArrayList<ArrayList<String>> arrayList) {
                loadDatesData();
            }
        });
    }

    public interface FirestoreCallbackStudy {
        void onCallback(ArrayList<String> arrayList);
    }

    public interface FirestoreCallbackDates {
        void onCallback(ArrayList<ArrayList<String>> arrayList);
    }

    private void loadStudyData() {

        TextView headerText  = findViewById(R.id.activityHeader);
        TextView description = findViewById(R.id.description);
        TextView vpValue     = findViewById(R.id.vpValue);
        TextView category    = findViewById(R.id.category);
        TextView studyType   = findViewById(R.id.type);
        //Textview for further studyType data (depending on type)
        TextView remoteData  = findViewById(R.id.remotyStudy);
        TextView localData   = findViewById(R.id.localStudy);

        //store DB Data Strings in textViews
        headerText.setText(studyDetails.get(0));
        description.setText(studyDetails.get(1));
        vpValue.setText(studyDetails.get(2));
        category.setText(studyDetails.get(3));
        studyType.setText(studyDetails.get(4));

        // set further studyType data
        if (studyDetails.get(4).equals("Remote")) {
            remoteData.setText(studyDetails.get(5));
        } else{
            localData.setText(studyDetails.get(6) + "\t\t" + studyDetails.get(7) + "\t\t" + studyDetails.get(8));
        }
    }

    private void loadDatesData() {
        dateList = findViewById(R.id.listViewDates);
        allDates = new ArrayList<>();
        dateIds = new ArrayList<>();
        userIdsOfDates = new ArrayList<>();

        //store ids and dates in different ArrayLists
        for (int i = 0; i < dateInfo.size(); i++) {
            dateIds.add(dateInfo.get(i).get(0));
            allDates.add(dateInfo.get(i).get(1));
            userIdsOfDates.add(dateInfo.get(i).get(2));
        }

        Log.d("userIdsOfAllDates", userIdsOfDates.toString());
        //makes date selection unavailable if user already picked a date from this study
        if (userIdsOfDates.contains(homeActivity.id(this))){
            setSavedDateAdapter();
            setupSelectedDateClickListener();
        }else{
            //Set adapter to display all available dates for the study
            //availableDatesAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, allDates);
            //dateList.setAdapter(availableDatesAdapter);
            setAllDatesAdapter();
            setupClickListener();
        }
    }

    private void setupStudyDetails(FirestoreCallbackStudy firestoreCallbackStudy) {

        db = FirebaseFirestore.getInstance();
        studyRef = db.collection("studies").document(currentStudyId);
        studyDetails = new ArrayList<>();

        studyRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        studyDetails.add(0, document.getString("name"));
                        studyDetails.add(1, document.getString("description"));
                        studyDetails.add(2, document.getString("vps"));
                        studyDetails.add(3, document.getString("category"));
                        studyDetails.add(4, document.getString("executionType"));
                        studyDetails.add(5, document.getString("platform"));
                        studyDetails.add(6, document.getString("location"));
                        studyDetails.add(7, document.getString("street"));
                        studyDetails.add(8, document.getString("room"));

                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        firestoreCallbackStudy.onCallback(studyDetails);
                    }else{
                        Log.d(TAG, "No such document");
                    }
                }else{
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private void setupDateListView(FirestoreCallbackDates firestoreCallbackDates) {

        db = FirebaseFirestore.getInstance();
        datesRef = db.collection("dates");
        dateInfo = new ArrayList<>();

        datesRef.whereEqualTo("studyId", currentStudyId).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ArrayList<String> idDateUser = new ArrayList<>();
                                idDateUser.add(0, document.getString("id"));
                                idDateUser.add(1, document.getString("date"));
                                idDateUser.add(2, document.getString("userId"));

                                dateInfo.add(idDateUser);
                            }
                            firestoreCallbackDates.onCallback(dateInfo);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void setupClickListener() {

        dateList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String dateId = dateIds.get(position);

                selectDateAlert(dateId);
            }
        });
    }

    private void setupSelectedDateClickListener() {

        dateList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                unSelectDateAlert();
            }
        });
    }

    private void unSelectDateAlert() {

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        unSelectDate(); //get dateId out of array and then datenbankanfrage
                        setAllDatesAdapter();
                        setupClickListener();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Möchten Sie sich für diesen Termin wieder austragen?")
                .setPositiveButton("Ja", dialogClickListener)
                .setNegativeButton("Nein", dialogClickListener).show();
    }

    private void selectDateAlert(String dateId){

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        selectDate(dateId);
                        setSavedDateAdapter();
                        setupSelectedDateClickListener();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Möchten Sie sich für diesen Termin eintragen?")
                .setPositiveButton("Ja", dialogClickListener)
                .setNegativeButton("Nein", dialogClickListener).show();
    }

    private void setSavedDateAdapter(){
        savedDateAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, savedDateItem);
        dateList.setAdapter(savedDateAdapter);
    }

    private void setAllDatesAdapter(){
        availableDatesAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, allDates);
        dateList.setAdapter(availableDatesAdapter);
    }

    private void selectDate(String dateId){

        String userId = homeActivity.id(this);

        Map<String, Object> updateDataMap = new HashMap<>();
        updateDataMap.put("selected", true);
        updateDataMap.put("userId", userId);

        accessDatabase.selectDate(updateDataMap, dateId);
    }

    private void unSelectDate(){
        int datePosition = userIdsOfDates.indexOf(homeActivity.id(this));
        String dateId = dateIds.get(datePosition);

        accessDatabase.unselectDate(dateId);
    }
}