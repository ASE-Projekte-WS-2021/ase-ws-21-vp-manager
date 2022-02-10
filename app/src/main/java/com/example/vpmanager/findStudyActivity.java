package com.example.vpmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import java.util.ArrayList;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class findStudyActivity extends AppCompatActivity {

    MaterialToolbar topAppBarFind;
    DrawerLayout drawerLayoutFind;
    NavigationView navigationViewFind;

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
        setupView();
        setupListView(new FirestoreCallback() {
            @Override
            public void onCallback(ArrayList<ArrayList<String>> arrayList) {
                loadData();
            }
        });
    }

    private void setupView() {
        topAppBarFind = findViewById(R.id.topAppBarFind);
        setSupportActionBar(topAppBarFind);
        drawerLayoutFind = findViewById(R.id.drawerLayoutFind);
        navigationViewFind = findViewById(R.id.navigationViewFind);
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
            studyNamesAndVps.add(studyIdNameVp.get(i).get(1) + "\t\t(" + studyIdNameVp.get(i).get(2) + getString(R.string.vpHours));
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

        //For NavigationDrawer to open
        topAppBarFind.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View V) {
                drawerLayoutFind.open();
            }
        });

        //Handle click on single item in drawer here
        navigationViewFind.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Intent homeIntent = new Intent(findStudyActivity.this, homeActivity.class);
                        startActivity(homeIntent);
                        break;
                    case R.id.nav_search:
                        break;
                    case R.id.nav_create:
                        Intent createIntent = new Intent(findStudyActivity.this, createStudyActivity.class);
                        startActivity(createIntent);
                        break;
                    case R.id.nav_overview:
                        Intent overviewIntent = new Intent(findStudyActivity.this, personalAccountActivity.class);
                        startActivity(overviewIntent);
                        break;
                    case R.id.nav_own:
                        //Added later
                        break;
                }
                drawerLayoutFind.close();
                return true;
            }
        });

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
