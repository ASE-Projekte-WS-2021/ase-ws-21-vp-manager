package com.example.vpmanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class personalAccountFragment extends Fragment {

    private ExpandableListView listView;
    private PA_ExpandableListAdapter adapter;
    private List<String> expandableListTitle;
    private HashMap<String, List<String>> expandableListDetail;
    private PieChart chart;

    private NavController navController;

    public personalAccountFragment() {
        //Empty public constructor required?
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_personal_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView(view);
        setupClickListener();
    }

    private void setupView(View view) {
        navController = Navigation.findNavController(view);

        listView = view.findViewById(R.id.pa_fragment_expandableList);
        chart = view.findViewById(R.id.pa_fragment_pie_chart);

        expandableListDetail = PA_ExpandableListDataPump.getData();
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        adapter = new PA_ExpandableListAdapter(getActivity(), expandableListTitle, expandableListDetail);
        listView.setAdapter(adapter);

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

    private void setupClickListener() {
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                                        int childPosition, long id) {
                String studyTitle = expandableListDetail.get(expandableListTitle.get(groupPosition))
                        .get(childPosition).split(",")[3];

                Bundle args = new Bundle();
                args.putString("studyId", studyTitle);
                navController.navigate(R.id.action_personalAccountFragment_to_studyFragment, args);
                /*
                Intent intent = new Intent(getActivity(), studyActivity.class);
                intent.putExtra("studyId", studyTitle);
                startActivity(intent);
                 */
                return false;
            }
        });
    }

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
