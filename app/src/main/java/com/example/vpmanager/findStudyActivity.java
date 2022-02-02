package com.example.vpmanager;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import android.provider.Settings;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class findStudyActivity extends AppCompatActivity {

    ListView studyList;

    ArrayList<ArrayList<String>> studyIdNameVp;
    ArrayList<String> studyNamesAndVps;
    ArrayList<String> studyIds;

    FirebaseFirestore db;
    CollectionReference studiesRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_study);

        setupListView(new FirestoreCallback() {
            @Override
            public void onCallback(ArrayList<ArrayList<String>> arrayList) {
                Log.d("onCallback (onCreate)", arrayList.toString());
                loadData();
            }
        });
        //Log.d("before setupListener", studyList.toString());
        //setupClickListener();
    }

    private void loadData() {

        studyList = findViewById(R.id.listView);
        studyNamesAndVps = new ArrayList<>();
        studyIds = new ArrayList<>();
        Log.d("loadData (allStudyInfo)", studyIdNameVp.toString());

        //For loop for testing
        for (int i = 0; i < studyIdNameVp.size(); i++) { //Amount of studies
            for (int j = 0; j < studyIdNameVp.get(i).size(); j++) { //Amount of info of one study
                System.out.print(studyIdNameVp.get(i).get(j));
            }
            System.out.println("next entry");
        }

        //Store the names and the vps in an ArrayList
        //Store the ids in the same order in another ArrayList
        for (int i = 0; i < studyIdNameVp.size(); i++) {
            studyNamesAndVps.add(studyIdNameVp.get(i).get(1) + "\t" + "\t" + studyIdNameVp.get(i).get(2) + "VP-Stunden");
            studyIds.add(studyIdNameVp.get(i).get(0));
        }
        Log.d("loadData (studyName+Vp)", studyNamesAndVps.toString());
        Log.d("loadData (studyIds", studyIds.toString());

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, studyNamesAndVps);
        studyList.setAdapter(arrayAdapter);

        setupClickListener();
    }

    //For getting all the Information of all studies
    public interface FirestoreCallback {
        void onCallback(ArrayList<ArrayList<String>> arrayList);
    }

    //set up ListView after the data is loaded
    private void setupListView(FirestoreCallback firestoreCallback) {

        db = FirebaseFirestore.getInstance();
        studiesRef = db.collection("studies");
        studyIdNameVp = new ArrayList<>();

        studiesRef.orderBy("name", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //local ArrayList
                                ArrayList<String> idNameVph = new ArrayList<>();
                                idNameVph.add(0, document.getString("id"));
                                idNameVph.add(1, document.getString("name"));
                                idNameVph.add(2, document.getString("vps"));
                                studyIdNameVp.add(idNameVph);
                            }
                            firestoreCallback.onCallback(studyIdNameVp);

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void setupClickListener() {

        studyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //get values from DB, give them to studyactivity
                Intent intent = new Intent(findStudyActivity.this, studyActivity.class);

                //get the Id of the study that is clicked on
                String studyId = studyIds.get(position);
                intent.putExtra("studyId", studyId);

                String testData = " - Studienbeschreibung";
                intent.putExtra("newData", testData);
                String testData2 = " - Kategorie ...";
                intent.putExtra("newData2", testData2);

                startActivity(intent);
            }
        });
    }
}
