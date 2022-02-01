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
    ArrayList<ArrayList<String>> allStudyInfo;
    ArrayList<String> allStudyNames;
    ArrayList<String> allStudyIds;

    accessDatabase accessDatabase = new accessDatabase();

    FirebaseFirestore db;
    CollectionReference studiesRef;
    ArrayList<ArrayList<String>> studyInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_study);
        setupListView(new FirestoreCallback() {
            @Override
            public void onCallback(ArrayList<ArrayList<String>> arrayList) {
                Log.d("inTheArrayListMethod", arrayList.toString());
                loadData();
            }
        });
        setupClickListener();
    }

    private void loadData() {
        studyList = findViewById(R.id.listView); //ListView in the activity_find_study.xml

        allStudyNames = new ArrayList<>();
        Log.d("findStudyActivity", allStudyInfo.toString());

        for (int i = 0; i < allStudyInfo.size(); i++) {
            for (int j = 0; j < allStudyInfo.get(i).size(); j++) {
                System.out.println("hallo");
                System.out.print(allStudyInfo.get(i).get(j) + " ");
            }
            System.out.println("next entry");
        }
        //get just the name out of the info
        for (int i = 0; i < allStudyInfo.size(); i++) {
            allStudyNames.add(allStudyInfo.get(i).get(1) + "\t\t" + allStudyInfo.get(i).get(2) + "Vp");
        }
        Log.d("afterGettingAllNames", allStudyNames.toString());

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, allStudyNames);
        studyList.setAdapter(arrayAdapter);
    }

    public interface FirestoreCallback {
        void onCallback(ArrayList<ArrayList<String>> arrayList);
    }


    private void setupListView(FirestoreCallback firestoreCallback) {

        db = FirebaseFirestore.getInstance();
        studiesRef = db.collection("studies");
        allStudyInfo = new ArrayList<>();

        studiesRef.orderBy("name", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ArrayList<String> idNameVph = new ArrayList<>();
                                idNameVph.add(0, document.getString("id"));
                                idNameVph.add(1, document.getString("name"));
                                idNameVph.add(2, document.getString("vps"));
                                allStudyInfo.add(idNameVph);

                            }
                            firestoreCallback.onCallback(allStudyInfo);

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

                String testData = " - Studienbeschreibung";
                intent.putExtra("newData", testData);
                String testData2 = " - Kategorie ...";
                intent.putExtra("newData2", testData2);
                //need the studyId here
                String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                intent.putExtra("deviceID", deviceID);

                startActivity(intent);
            }
        });
    }
}
