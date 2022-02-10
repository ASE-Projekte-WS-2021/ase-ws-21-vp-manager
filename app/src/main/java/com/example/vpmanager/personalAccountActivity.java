package com.example.vpmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class personalAccountActivity extends AppCompatActivity {

    MaterialToolbar topAppBarPersonalAcc;
    DrawerLayout drawerLayoutPersonalAcc;
    NavigationView navigationViewPersonalAcc;

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

        topAppBarPersonalAcc = findViewById(R.id.topAppBarPersonalAccount);
        setSupportActionBar(topAppBarPersonalAcc);
        drawerLayoutPersonalAcc = findViewById(R.id.drawerLayoutPersonalAccount);
        navigationViewPersonalAcc = findViewById(R.id.navigationViewPersonalAccount);

        listView = findViewById(R.id.pa_expandableList);
        chart = findViewById(R.id.pa_piechart);

        expandableListDetail = PA_ExpandableListDataPump.getData();
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        adapter = new PA_ExpandableListAdapter(this, expandableListTitle, expandableListDetail);

        listView.setAdapter(adapter);

        //For NavigationDrawer to open
        topAppBarPersonalAcc.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View V) {
                drawerLayoutPersonalAcc.open();
            }
        });

        //Handle click on single item in drawer here
        navigationViewPersonalAcc.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Intent homeIntent = new Intent(personalAccountActivity.this, homeActivity.class);
                        startActivity(homeIntent);
                        break;
                    case R.id.nav_search:
                        Intent searchIntent = new Intent(personalAccountActivity.this, findStudyActivity.class);
                        startActivity(searchIntent);
                        break;
                    case R.id.nav_create:
                        Intent createIntent = new Intent(personalAccountActivity.this, createStudyActivity.class);
                        startActivity(createIntent);
                        break;
                    case R.id.nav_overview:
                        break;
                    case R.id.nav_own:
                        //Added later
                        break;
                }
                drawerLayoutPersonalAcc.close();
                return true;
            }
        });

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

        List<String> vpList = expandableListDetail.get("Geplante Studien");
        if (vpList != null) {
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
                        getString(R.string.pieSliceLabelSafe),
                        scaledCompletedVP,
                        getResources().getColor(R.color.pieChartSafe)));
        //Color.parseColor(String.valueOf(ContextCompat.getColor(this, R.color.pieChartSafe)))));
        chart.addPieSlice(
                new PieModel(
                        getString(R.string.pieSliceLabelParticipation),
                        scaledParticipationVP,
                        getResources().getColor(R.color.pieChartParticipation)));
        chart.addPieSlice(
                new PieModel(
                        getString(R.string.pieSliceLabelPlanned),
                        scaledPlannedVP,
                        getResources().getColor(R.color.pieChartPlanned)));
        chart.addPieSlice(
                new PieModel(
                        getString(R.string.pieSliceLabelRemaining),
                        remaining,
                        getResources().getColor(R.color.pieChartRemaining)));

        chart.startAnimation();
    }
}
