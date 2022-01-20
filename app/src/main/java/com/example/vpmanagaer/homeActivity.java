package com.example.vpmanagaer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;


public class homeActivity extends AppCompatActivity {

    Button findStudyButton;
    Button createStudyButton;
    PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        createStudyButton = findViewById(R.id.findStudyHome);
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
}

