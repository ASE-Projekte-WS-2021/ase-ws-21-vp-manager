package com.example.vpmanager;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
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

public class studyActivity extends AppCompatActivity{

    ListView dateList;
    String currentStudyId;

    ArrayList<String> studyDetails;

    ArrayList<ArrayList<String>> dateAndId;
    ArrayList<String> allDates;
    ArrayList<String> dateIds;

    FirebaseFirestore db;
    DocumentReference studyRef;
    CollectionReference datesRef;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);
        //Get the studyId early
        currentStudyId = getIntent().getStringExtra("studyId");

        //verschachtelt
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
            localData.setText(studyDetails.get(6) + "\t\t" + studyDetails.get(6) + "\t\t" + studyDetails.get(6));
        }
    }

    private void loadDatesData() {
        dateList = findViewById(R.id.listViewDates);
        allDates = new ArrayList<>();
        dateIds = new ArrayList<>();

        //store ids and dates in different ArrayLists
        for (int i = 0; i < dateAndId.size(); i++) {
            allDates.add(dateAndId.get(i).get(1));
            dateIds.add(dateAndId.get(i).get(0));
        }

        //Set adapter to display all available dates for the study
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, allDates);
        dateList.setAdapter(arrayAdapter);

        setupClickListener();
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
        dateAndId = new ArrayList<>();

        datesRef.whereEqualTo("studyId", currentStudyId).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ArrayList<String> dateAndID = new ArrayList<>();
                                dateAndID.add(0, document.getString("id"));
                                dateAndID.add(1, document.getString("date"));
                                dateAndId.add(dateAndID);
                            }
                            firestoreCallbackDates.onCallback(dateAndId);
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

                //start pop up here and add userId to a date
            }
        });
    }
}