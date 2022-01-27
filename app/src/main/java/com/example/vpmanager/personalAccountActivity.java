package com.example.vpmanager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


public class personalAccountActivity extends AppCompatActivity {

    ExpandableListView listView;
    PA_ExpandableListAdapter adapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    PieChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_account);

       setupView();
       setPieChartData(2,4,1);
    }

    //Parameters:
    //Return Values:
    //this method sets all the listeners for the expandable list and initialises the piechart
    private void setupView() {

        listView = findViewById(R.id.pa_expandableList);
        chart = findViewById(R.id.pa_piechart);

        expandableListDetail = PA_ExpandableListDataPump.getData();
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        adapter = new PA_ExpandableListAdapter(this, expandableListTitle, expandableListDetail);

        listView.setAdapter(adapter);

        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                String groupTitle = expandableListTitle.get(groupPosition);
                String studyTitle = expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition).split(",")[0];

                Intent intent = new Intent();
                intent.putExtra("Studienname", studyTitle);
                intent.putExtra("UserID", UUID.randomUUID().toString());

                return false;
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
        chart.addPieSlice(
                new PieModel(
                        "Safe",
                        scaledCompletedVP,
                        Color.parseColor("#7CFC00")));
        chart.addPieSlice(
                new PieModel(
                        "Participation",
                        scaledParticipationVP,
                        Color.parseColor("#ffa500")));
        chart.addPieSlice(
                new PieModel(
                        "Planned",
                        scaledPlannedVP,
                        Color.parseColor("#ff0000")));
        chart.addPieSlice(
                new PieModel(
                        "Remaining",
                        remaining,
                        Color.parseColor("#808080")));

        chart.startAnimation();
    }
}
