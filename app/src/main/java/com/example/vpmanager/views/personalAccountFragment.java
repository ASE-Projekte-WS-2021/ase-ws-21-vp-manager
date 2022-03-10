package com.example.vpmanager.views;

import static com.example.vpmanager.views.mainActivity.uniqueID;

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

import com.example.vpmanager.PA_ExpandableListAdapter;
import com.example.vpmanager.PA_ExpandableListDataPump;
import com.example.vpmanager.R;

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
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_account, container, false);
        loadData(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }


    private void loadData(View view) {

        PA_ExpandableListDataPump.getAllDates(new PA_ExpandableListDataPump.FirestoreCallbackDates() {
            @Override
            public void onCallback(boolean finished) {
                if (finished)
                    PA_ExpandableListDataPump.getAllStudies(new PA_ExpandableListDataPump.FirestoreCallbackStudy() {
                        @Override
                        public void onCallback() {
                            PA_ExpandableListDataPump.createListEntries();
                            setupView(view);
                            setupClickListener();
                        }
                    });
            }
        });
    }

    private void setupView(View view) {
        listView = view.findViewById(R.id.pa_fragment_expandableList);
        chart = view.findViewById(R.id.pa_fragment_pie_chart);
        expandableListDetail = PA_ExpandableListDataPump.EXPANDABLE_LIST_DETAIL;
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
                double studyVPS = 0;
                if(vps != null && !vps.equals(""))
                    studyVPS = Double.parseDouble(vps);
                plannedVP += studyVPS;
            }
        }
        List<String> passedVpList = expandableListDetail.get("Vergangene Studien");
        if (passedVpList != null) {
            for (int i = 0; i < passedVpList.size(); i++) {
                String vps = passedVpList.get(i).split(",")[1];
                double studyVPS = 0;
                if(vps != null && !vps.equals(""))
                    studyVPS = Double.parseDouble(vps);
                participatedVP += studyVPS;
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
                if(PA_ExpandableListDataPump.navigateToStudyCreatorFragment(uniqueID, studyTitle)) {
                    navController.navigate(R.id.action_personalAccountFragment_to_studyCreatorFragment, args);
                }
                else {
                    navController.navigate(R.id.action_personalAccountFragment_to_studyFragment, args);
                }
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
