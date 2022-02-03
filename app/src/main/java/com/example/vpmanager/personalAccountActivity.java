package com.example.vpmanager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import androidx.appcompat.app.AppCompatActivity;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


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
    }

    //Parameters:
    //Return Values:
    //this method sets all the listeners for the expandable list and initialises the piechart and extracts the VPS from the expandable List for the pieChart
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
                String studyTitle = expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition).split(",")[3];

                Intent intent = new Intent(personalAccountActivity.this, studyActivity.class);
                intent.putExtra("studyId", studyTitle);
                startActivity(intent);
                return false;
            }
        });


        double plannedVP = 0;
        double completedVP = 0;
        double participatedVP = 0;

        List<String> vpList =  expandableListDetail.get("Geplante Studien");
        if(vpList != null) {
            for (int i = 0; i < vpList.size(); i++) {
                String vps = vpList.get(i).split(",")[1];
                double studyVPS = Double.parseDouble(vps);
                plannedVP += studyVPS;
            }
        }
        setPieChartData(completedVP, participatedVP, plannedVP);
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
