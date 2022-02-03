package com.example.vpmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

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
                loadData();
            }
        });
    }

    // Parameter:
    // Return values:
    // Get DB values from arraylist and load study names and VP data in ListView
    private void loadData() {
        studyList = findViewById(R.id.listView);
        studyNamesAndVps = new ArrayList<>();
        studyIds = new ArrayList<>();
        //Store the names and the vps in an ArrayList
        //Store the ids in the same order in another ArrayList
        for (int i = 0; i < studyIdNameVp.size(); i++) {
            studyNamesAndVps.add(studyIdNameVp.get(i).get(1) + "\t\t(" + studyIdNameVp.get(i).get(2) + R.string.vpHours);
            studyIds.add(studyIdNameVp.get(i).get(0));
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, studyNamesAndVps);
        studyList.setAdapter(arrayAdapter);
        setupClickListener();
    }

    // Parameter:
    // Return values:
    // For getting all the Information of all studies
    public interface FirestoreCallback {
        void onCallback(ArrayList<ArrayList<String>> arrayList);
    }

    // Parameter: firestoreCallback
    // Return values:
    // set up ListView after the data is loaded
    private void setupListView(FirestoreCallback firestoreCallback) {

        db = FirebaseFirestore.getInstance();
        studiesRef = db.collection(getString(R.string.collectionPathStudies));
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
                        }
                    }
                });
    }

    // Parameter:
    // Return values:
    // set up CLickListener for list elements to open associated study view
    private void setupClickListener() {
        studyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(findStudyActivity.this, studyActivity.class);

                //get the id of the study that is clicked on
                String studyId = studyIds.get(position);
                intent.putExtra("studyId", studyId);

                startActivity(intent);
            }
        });
    }
}
