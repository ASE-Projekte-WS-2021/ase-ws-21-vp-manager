package com.example.vpmanagaer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import android.provider.Settings;



public class findStudyActivity extends AppCompatActivity {

    //
    //

    ListView entryList;


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_study);


        //after FireBase connection:
        //[DatabaseReference dbRef;]
        //[dbRef = FireBaseDataBase.getInstance().getReference;]


        entryList = findViewById(R.id.listView);

        ArrayList<String> arrayList = new ArrayList<>();

        //Dummy Data for ListView
        arrayList.add("eintrag 1");
        arrayList.add("eintrag 2");
        arrayList.add("eintrag 3");
        arrayList.add("eintrag 4");
        arrayList.add("eintrag 5");



        //Returns a view for each object in a collection of data
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);


        // method for Firebase connection:
        // [dbRef.addChildeventListener(new Childeventlistener){ Override methods here }]



        entryList.setAdapter(arrayAdapter);

        entryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //get values from DB, give them to studyactivity
                Intent intent = new Intent(findStudyActivity.this, studyActivity.class);

                String testData = " - Studienbeschreibung";
                intent.putExtra("newData", testData);
                String testData2 = " - Kategorie ...";
                intent.putExtra("newData2",testData2);


                // create User Device ID
                // give ID to following activity
                String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                intent.putExtra("deviceID",deviceID);

                startActivity(intent);
            }
        });

    }





}
