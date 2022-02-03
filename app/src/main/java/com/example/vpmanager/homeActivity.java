package com.example.vpmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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
    accessDatabase accessDatabase;
    //Unique ID Strings
    public static String uniqueID = null;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        uniqueID = createUserId(this);
        accessDatabase = new accessDatabase();
        //Checks if a new user needs to be registered
        registerNewUser();
        setupView();
        setClickListener();
        //testData
        setPieChartData(5, 3, 2);
    }

    //Parameter:
    //Return values:
    //Connects the code with the view
    private void setupView() {
        findStudyButton = findViewById(R.id.findStudyHome);
        createStudyButton = findViewById(R.id.createStudyHome);
        pieChart = findViewById(R.id.piechart);
    }

    //Parameter:
    //Return values:
    //Sets clickListener on navigation items
    private void setClickListener() {

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
                        getString(R.string.pieSliceLabelSafe),
                        scaledCompletedVP,
                        Color.parseColor(String.valueOf(ContextCompat.getColor(this, R.color.pieChartSafe)))));
        pieChart.addPieSlice(
                new PieModel(
                        getString(R.string.pieSliceLabelParticipation),
                        scaledParticipationVP,
                        Color.parseColor(String.valueOf(ContextCompat.getColor(this, R.color.pieChartParticipation)))));
        pieChart.addPieSlice(
                new PieModel(
                        getString(R.string.pieSliceLabelPlanned),
                        scaledPlannedVP,
                        Color.parseColor(String.valueOf(ContextCompat.getColor(this, R.color.pieChartPlanned)))));
        pieChart.addPieSlice(
                new PieModel(
                        getString(R.string.pieSliceLabelRemaining),
                        remaining,
                        Color.parseColor(String.valueOf(ContextCompat.getColor(this, R.color.pieChartRemaining)))));

        pieChart.startAnimation();
    }

    //Parameter:
    //Return values:
    //Registers a new user (installation of the app)  in the DB, if the user doesn't already exist
    private void registerNewUser() {
        String deviceID = createUserId(this);
        accessDatabase.createNewUser(deviceID);
    }

    //Parameter: context
    //Return values: uniqueID
    //Generates an unique id for every installation of the app.
    //Source: https://ssaurel.medium.com/how-to-retrieve-an-unique-id-to-identify-android-devices-6f99fd5369eb
    public synchronized static String createUserId(Context context) {
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

