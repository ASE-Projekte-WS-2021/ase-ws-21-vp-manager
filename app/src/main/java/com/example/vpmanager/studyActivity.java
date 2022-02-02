package com.example.vpmanager;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class studyActivity extends AppCompatActivity{

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);

        //Dummy data for TextView
        TextView textView1 = findViewById(R.id.text1);
        TextView textView2 = findViewById(R.id.text2);
        TextView textView3 = findViewById(R.id.text3);
        TextView textView4;
        TextView textView5;


        ListView dateList = findViewById(R.id.listViewDates);
        ArrayList<String> arrayList = new ArrayList<>();

        //Dummy Data
        arrayList.add("Termin 1");
        arrayList.add("Termin 2");
        arrayList.add("Termin 3");

        //Arraylist for date picker
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
        dateList.setAdapter(arrayAdapter);


        String studyId = getIntent().getStringExtra("studyId");
        String dbData1 = getIntent().getStringExtra("newData");
        String dbData2 = getIntent().getStringExtra("newData2");

        //store DB Data in textViews
            textView1.setText(dbData1);
            textView2.setText(dbData2);
            textView3.setText(studyId);
        }
    }





