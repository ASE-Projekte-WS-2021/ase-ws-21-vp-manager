package com.example.vpmanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class studyActivity extends AppCompatActivity {

    MaterialToolbar topAppBarStudy;
    DrawerLayout drawerLayoutStudy;
    NavigationView navigationViewStudy;

    ListView dateList;
    String currentStudyId;
    String currentUserId;

    ArrayList<String> studyDetails;

    ArrayList<ArrayList<String>> freeAndOwnDatesInfo;
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

    TextView headerText;
    TextView description;
    TextView vpValue;
    TextView category;
    TextView studyType;
    TextView remoteData;
    TextView localData;
    TextView contactInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);
        //Get the studyId early
        currentStudyId = getIntent().getStringExtra("studyId");
        currentUserId = homeActivity.createUserId(this);
        savedDateItem = new ArrayList<>();
        savedDateItem.add(getString(R.string.dropDateView));
        setupView();

        // Parameter:
        // Return values:
        // load necessary data for clicked study
        setupStudyDetails(new FirestoreCallbackStudy() {
            @Override
            public void onCallback(ArrayList<String> arrayList) {
                loadStudyData();
            }
        });

        // Parameter:
        // Return values:
        // load available dates for date selection ListView for clicked study
        setupDateListView(new FirestoreCallbackDates() {
            @Override
            public void onCallback(ArrayList<ArrayList<String>> arrayList) {
                loadDatesData();
            }
        });
    }

    private void setupView() {
        topAppBarStudy = findViewById(R.id.topAppBarStudy);
        setSupportActionBar(topAppBarStudy);
        drawerLayoutStudy = findViewById(R.id.drawerLayoutStudy);
        navigationViewStudy = findViewById(R.id.navigationViewStudy);
        navigationViewStudy.getMenu().getItem(1).setChecked(true);
    }

    // Parameter:
    // Return values:
    // set up view one after data is loaded
    public interface FirestoreCallbackStudy {
        void onCallback(ArrayList<String> arrayList);
    }

    // Parameter:
    // Return values:
    // set up view 2 after data is loaded
    public interface FirestoreCallbackDates {
        void onCallback(ArrayList<ArrayList<String>> arrayList);
    }

    // Parameter:
    // Return values:
    // Get DB values from arraylist and load study data in associated textViews
    private void loadStudyData() {

        headerText = findViewById(R.id.activityHeader);
        description = findViewById(R.id.description);
        vpValue = findViewById(R.id.vpValue);
        category = findViewById(R.id.category);
        studyType = findViewById(R.id.type);
        //Textview for further studyType data (depending on type)
        remoteData = findViewById(R.id.remotyStudy);
        localData = findViewById(R.id.localStudy);
        contactInfo = findViewById(R.id.contactInformation);

        //store DB Data Strings in textViews
        headerText.setText(studyDetails.get(0));
        description.setText(studyDetails.get(1));
        String vpHours = studyDetails.get(2) + " VP";
        vpValue.setText(vpHours);
        contactInfo.setText(studyDetails.get(9));
        category.setText(studyDetails.get(3));
        studyType.setText(studyDetails.get(4));

        // set further studyType data
        if (studyDetails.get(4).equals(getString(R.string.remoteString))) {
            remoteData.setText(studyDetails.get(5));
        } else {
            String locationString = studyDetails.get(6) + "\t\t" + studyDetails.get(7) + "\t\t" + studyDetails.get(8);
            localData.setText(locationString);
        }
    }

    // Parameter:
    // Return values:
    // Get DB values from arraylist and load date data in associated textViews
    private void loadDatesData() {
        dateList = findViewById(R.id.listViewDates);
        allDates = new ArrayList<>();
        dateIds = new ArrayList<>();
        userIdsOfDates = new ArrayList<>();

        //store ids and dates in different ArrayLists
        for (int i = 0; i < freeAndOwnDatesInfo.size(); i++) {
            dateIds.add(freeAndOwnDatesInfo.get(i).get(0));
            allDates.add(freeAndOwnDatesInfo.get(i).get(1));
            userIdsOfDates.add(freeAndOwnDatesInfo.get(i).get(2));
        }

        setupNavigationListener();

        //makes date selection unavailable if user already picked a date from this study
        if (userIdsOfDates.contains(homeActivity.createUserId(this))) {
            setSavedDateAdapter();
            setupSelectedDateClickListener();
        } else {
            setAllDatesAdapter();
            setupClickListener();
        }
    }

    // set up ClickListener for the app bar and the navigation drawer
    private void setupNavigationListener() {

        //For NavigationDrawer to open
        topAppBarStudy.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View V) {
                drawerLayoutStudy.open();
            }
        });

        //Handle click on single item in drawer here
        navigationViewStudy.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Intent homeIntent = new Intent(studyActivity.this, homeActivity.class);
                        startActivity(homeIntent);
                        break;
                    case R.id.nav_search:
                        Intent searchIntent = new Intent(studyActivity.this, findStudyActivity.class);
                        startActivity(searchIntent);
                        break;
                    case R.id.nav_create:
                        Intent createIntent = new Intent(studyActivity.this, createStudyActivity.class);
                        startActivity(createIntent);
                        break;
                    case R.id.nav_overview:
                        Intent overviewIntent = new Intent(studyActivity.this, personalAccountActivity.class);
                        startActivity(overviewIntent);
                        break;
                    case R.id.nav_own:
                        //Added later
                        break;
                }
                drawerLayoutStudy.close();
                return true;
            }
        });

    }

    // Parameters: firestoreCallbackStudy
    // Return values:
    // load DB Data for study details in arraylist
    private void setupStudyDetails(FirestoreCallbackStudy firestoreCallbackStudy) {

        db = FirebaseFirestore.getInstance();
        studyRef = db.collection(getString(R.string.collectionPathStudies)).document(currentStudyId);
        studyDetails = new ArrayList<>();

        studyRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
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
                        studyDetails.add(9, document.getString("contact"));

                        firestoreCallbackStudy.onCallback(studyDetails);
                    }
                }
            }
        });
    }

    // Parameter: firestoreCallbackDates
    // Return values:
    // load DB data for available dates in arraylist
    private void setupDateListView(FirestoreCallbackDates firestoreCallbackDates) {

        db = FirebaseFirestore.getInstance();
        datesRef = db.collection(getString(R.string.collectionPathDates));
        freeAndOwnDatesInfo = new ArrayList<>();

        //only the unselected dates should be retrieved here!
        datesRef.whereEqualTo("studyId", currentStudyId).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ArrayList<String> idDateUser = new ArrayList<>();
                                if (Objects.equals(document.getBoolean("selected"), true) &&
                                        !Objects.equals(document.getString("userId"), currentUserId)) {
                                    continue;
                                }
                                idDateUser.add(0, document.getString("id"));
                                idDateUser.add(1, document.getString("date"));
                                idDateUser.add(2, document.getString("userId"));

                                freeAndOwnDatesInfo.add(idDateUser);
                            }
                            firestoreCallbackDates.onCallback(freeAndOwnDatesInfo);
                        }
                    }
                });
    }

    // Parameter:
    // Return values:
    // set up CLickListener for date list items to open register Pop-up
    private void setupClickListener() {

        dateList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String dateId = dateIds.get(position);
                selectDateAlert(dateId);
            }
        });
    }

    // Parameter:
    // Return values:
    // set up CLickListener for register Pop-up options
    private void setupSelectedDateClickListener() {

        dateList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                unSelectDateAlert();
            }
        });
    }

    // Parameter:
    // Return values:
    // cancel registered appointment
    private void unSelectDateAlert() {

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        unSelectDate();
                        setAllDatesAdapter();
                        setupClickListener();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.dropDateQuestion))
                .setPositiveButton(getString(R.string.yes), dialogClickListener)
                .setNegativeButton(getString(R.string.no), dialogClickListener).show();
    }

    // Parameter: dateId
    // Return values:
    // approve selected appointment
    private void selectDateAlert(String dateId) {

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
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
        builder.setMessage(getString(R.string.selectDateQuestion))
                .setPositiveButton(getString(R.string.yes), dialogClickListener)
                .setNegativeButton(getString(R.string.no), dialogClickListener).show();
    }

    // Parameter:
    // Return values:
    // set ArrayAdapter for saved dates
    private void setSavedDateAdapter() {
        savedDateAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, savedDateItem);
        dateList.setAdapter(savedDateAdapter);
    }

    // Parameter:
    // Return values:
    // set ArrayAdapter for all available dates
    private void setAllDatesAdapter() {
        availableDatesAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, allDates);
        dateList.setAdapter(availableDatesAdapter);
    }

    // Parameter: dateId
    // Return values:
    // update DB for registered appointment
    private void selectDate(String dateId) {

        String userId = homeActivity.createUserId(this);

        Map<String, Object> updateDataMap = new HashMap<>();
        updateDataMap.put("selected", true);
        updateDataMap.put("userId", userId);

        accessDatabase.selectDate(updateDataMap, dateId);
        reloadActivity();
    }

    // Parameter:
    // Return values:
    // update DB for canceled appointment
    private void unSelectDate() {
        int datePosition = userIdsOfDates.indexOf(homeActivity.createUserId(this));
        String dateId = dateIds.get(datePosition);
        accessDatabase.unselectDate(dateId);
        reloadActivity();
    }

    // Parameter:
    // Return values:
    // reload activity
    private void reloadActivity() {
        finish();
        startActivity(getIntent());
    }

    public ArrayList<String> getStudyDetails() {
        return studyDetails;
    }
}