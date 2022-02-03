package com.example.vpmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.UUID;

public class homeActivity extends AppCompatActivity {

    Button findStudyButton;
    Button createStudyButton;
    PieChart pieChart;
    //Unique ID Strings
    public static String uniqueID = null;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";
    accessDatabase accessDatabase = new accessDatabase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Checks if a new user needs to be registered
        registerNewUser();
        setContentView(R.layout.activity_home);
        setupClickables();
        //Testdaten
        setPieChartData(5, 3, 2);
    }

    //Parameter:
    //Return values:
    //Sets clicklistner on navigation items
    private void setupClickables() {
        findStudyButton = findViewById(R.id.findStudyHome);
        createStudyButton = findViewById(R.id.createStudyHome);
        pieChart = findViewById(R.id.piechart);

        findStudyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View V) {
                Intent intent = new Intent(homeActivity.this, findStudyActivity.class);
                startActivity(intent);
            }
        });

        createStudyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View V) {
                Intent intent = new Intent(homeActivity.this, createStudyActivity.class);
                startActivity(intent);
            }
        });

        pieChart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View V) {
                Intent intent = new Intent(homeActivity.this, personalAccountActivity.class);
                startActivity(intent);
            }
        });

    }

    //Parameter: completedVP, participationVP, plannedVP
    //Return values:
    //Setting the slices of the pie-chart according to the completed/participated/planned VP hours of the user
    private void setPieChartData(double completedVP, double participationVP, double plannedVP) {
        int max = 1500;
        int scaledCompletedVP = (int) completedVP * 100;
        int scaledParticipationVP = (int) participationVP * 100;
        int scaledPlannedVP = (int) plannedVP * 100;
        int remaining = max - (scaledCompletedVP + scaledParticipationVP + scaledPlannedVP);
        if (remaining < 0) {
            remaining = 0;
        }
        pieChart.addPieSlice(
                new PieModel(
                        "Safe",
                        scaledCompletedVP,
                        Color.parseColor("#7CFC00")));
        pieChart.addPieSlice(
                new PieModel(
                        "Participation",
                        scaledParticipationVP,
                        Color.parseColor("#ffa500")));
        pieChart.addPieSlice(
                new PieModel(
                        "Planned",
                        scaledPlannedVP,
                        Color.parseColor("#ff0000")));
        pieChart.addPieSlice(
                new PieModel(
                        "Remaining",
                        remaining,
                        Color.parseColor("#808080")));

        pieChart.startAnimation();
    }

    private void registerNewUser(){
        String deviceID = id(this);
        accessDatabase.createNewUser(deviceID);
    }

    //Generates an unique id for every installation of the app.
    //Source: https://ssaurel.medium.com/how-to-retrieve-an-unique-id-to-identify-android-devices-6f99fd5369eb
    public synchronized static String id(Context context) {
        if (uniqueID == null) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(
                    PREF_UNIQUE_ID, Context.MODE_PRIVATE);
            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(PREF_UNIQUE_ID, uniqueID);
                editor.apply();
            }
        }
        return uniqueID;
    }
}

