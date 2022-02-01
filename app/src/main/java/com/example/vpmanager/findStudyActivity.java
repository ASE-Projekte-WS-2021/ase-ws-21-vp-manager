package com.example.vpmanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

import android.provider.Settings;

public class findStudyActivity extends AppCompatActivity {

    ListView studyList;
    ArrayList<Object> allStudyInfo;
    ArrayList<String> allStudyNames;


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_study);
        setupListView();
        setupClickListener();
    }

    private void setupListView(){

        studyList = findViewById(R.id.listView); //ListView in the activity_find_study.xml

        allStudyInfo = new ArrayList<>(accessDatabase.getStudyBasicInfo());
        allStudyNames = new ArrayList<>();
        Log.d("findStudyActivity", allStudyInfo.toString());

        //get just the name out of the info
        for (int i=0; i<allStudyInfo.size(); i++){
            ArrayList<Object> currentStudyInfo = new ArrayList<>();
            currentStudyInfo.add(allStudyInfo.get(i));
            allStudyNames.add(currentStudyInfo.get(1).toString()); //name on second position
        }
        Log.d("afterGettingAllNames", allStudyNames.toString());

        /*
        //Only returning the names as a string atm
        ArrayList<String> allStudyNames = new ArrayList<>();
        ArrayList<String> allStudies = accessDatabase.getAllStudies();
        allStudyNames.addAll(allStudies);
        Log.d("findStudyActivity", allStudyNames.toString());
         */

        //Dummy Data for ListView
        /*
        allStudyNames.add("eintrag 1");
        allStudyNames.add("eintrag 2");
        allStudyNames.add("eintrag 3");
        allStudyNames.add("eintrag 4");
        allStudyNames.add("eintrag 5");
         **/

        //Returns a view for each object in a collection of data
        //Android prebuilt layout used here
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, allStudyNames);
        studyList.setAdapter(arrayAdapter);
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
                intent.putExtra("newData2",testData2);
                //need the studyId here
                String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                intent.putExtra("deviceID",deviceID);

                startActivity(intent);
            }
        });
    }
}
