package com.example.vpmanager.views;

import static com.example.vpmanager.views.mainActivity.uniqueID;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.vpmanager.PA_ExpandableListAdapter;
import com.example.vpmanager.PA_ExpandableListDataPump;
import com.example.vpmanager.R;
import com.example.vpmanager.adapter.CustomListViewAdapter;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class personalAccountFragment extends Fragment {

    private ExpandableListView listView;
    private ListView newListView;
    private PA_ExpandableListAdapter adapter;
    private List<String> expandableListTitle;
    private HashMap<String, List<String>> expandableListDetail;
    private PieChart chart;
    private Switch sourceSwitch;
    private ImageView settings;
    private LinearLayout participatedLayout, plannedLayout, completedLayout;

    private static String martikelNumber;

    private TextView planned, participated, completed, remaining;

    private double plannedVP;
    private double completedVP;
    private double participatedVP;

    private NavController navController;
    private String jsonString;

    private float sumVPs;

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

    //Parameter: view is passed to setup view
    //Return values:
    //calls to database to get all the relevant data for this fragment
    private void loadData(View view) {

        PA_ExpandableListDataPump.getAllDates(new PA_ExpandableListDataPump.FirestoreCallbackDates() {
            @Override
            public void onCallback(boolean finished) {
                if (finished)
                    PA_ExpandableListDataPump.getAllStudies(new PA_ExpandableListDataPump.FirestoreCallbackStudy() {
                        @Override
                        public void onCallback() {
                            PA_ExpandableListDataPump.getVPandMatrikelnumber(new PA_ExpandableListDataPump.FirestoreCallbackUser() {
                                @Override
                                public void onCallback(String vps, String matrikelNumber) {
                                    if (vps != null && matrikelNumber != null && !vps.equals("") && !matrikelNumber.equals("")) {
                                        sumVPs = Float.parseFloat(vps);
                                        martikelNumber = matrikelNumber;
                                    } else {
                                        sumVPs = 15;
                                        martikelNumber = "";
                                    }
                                    PA_ExpandableListDataPump.createListEntries();
                                    setupView(view);
                                    setupClickListener();
                                }
                            });

                        }
                    });
            }
        });
    }

    //Parameter:
    //Return values:
    //Setting the view components and readint the content Lists for the piechart values
    private void setupView(View view) {
        newListView = view.findViewById(R.id.pa_fragment_listView);
        participatedLayout = view.findViewById(R.id.pa_parcticipated_layout);
        plannedLayout = view.findViewById(R.id.pa_planned_layout);
        completedLayout = view.findViewById(R.id.pa_completed_layout);
        planned = view.findViewById(R.id.pa_planned);
        participated = view.findViewById(R.id.pa_participated);
        completed = view.findViewById(R.id.pa_completed);
        remaining = view.findViewById(R.id.pa_remaining);
        settings = view.findViewById(R.id.vp_settings_icon);
        sourceSwitch = view.findViewById(R.id.vp_source_switch);
        listView = view.findViewById(R.id.pa_fragment_expandableList);
        chart = view.findViewById(R.id.pa_fragment_pie_chart);
        expandableListDetail = PA_ExpandableListDataPump.EXPANDABLE_LIST_DETAIL;
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        adapter = new PA_ExpandableListAdapter(getActivity(), expandableListTitle, expandableListDetail);
        //listView.setAdapter(adapter);

        newListView.setAdapter(new CustomListViewAdapter(this.getContext() , this.getActivity(), navController));

        plannedVP = 0;
        completedVP = 0;
        participatedVP = 0;

        List<String> vpList = expandableListDetail.get("Geplante Studien");
        if (vpList != null) {
            for (int i = 0; i < vpList.size(); i++) {
                String vps = vpList.get(i).split(",")[1];
                double studyVPS = 0;
                if (vps != null && !vps.equals(""))
                    studyVPS = Double.parseDouble(vps);
                plannedVP += studyVPS;
            }
        }
        List<String> passedVpList = expandableListDetail.get("Vergangene Studien");
        if (passedVpList != null) {
            for (int i = 0; i < passedVpList.size(); i++) {
                String vps = passedVpList.get(i).split(",")[1];
                double studyVPS = 0;
                if (vps != null && !vps.equals(""))
                    studyVPS = Double.parseDouble(vps);
                participatedVP += studyVPS;
            }
        }
        switchPieChart(sourceSwitch.isChecked());
    }


    //Parameter:
    //Return values:
    //Setup clicklisteners for all clickable objects
    private void setupClickListener() {
        listView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            String studyTitle = expandableListDetail.get(expandableListTitle.get(groupPosition))
                    .get(childPosition).split(",")[3];

            Bundle args = new Bundle();
            args.putString("studyId", studyTitle);
            if (PA_ExpandableListDataPump.navigateToStudyCreatorFragment(uniqueID, studyTitle)) {
                navController.navigate(R.id.action_homeFragment_to_studyCreatorFragment, args);
            } else {
                navController.navigate(R.id.action_homeFragment_to_studyFragment, args);
            }
            return false;
        });/*
        newListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle args = new Bundle();
                StudyObject study = (StudyObject) newListView.getChildAt(position).getTag();
                args.putString("studyId", study.getStudyId() );
                if (PA_ExpandableListDataPump.navigateToStudyCreatorFragment(uniqueID, study.getStudyId())) {
                    navController.navigate(R.id.action_homeFragment_to_studyCreatorFragment, args);
                } else {
                    navController.navigate(R.id.action_homeFragment_to_studyFragment, args);
                }
            }
        });*/
        sourceSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            switchPieChart(isChecked);
        });
        settings.setOnClickListener(v -> {
            personalAccountFragment fragment = personalAccountFragment.this;
            CustomAlertDialog dialog = new CustomAlertDialog(fragment, Float.toString(sumVPs), martikelNumber);
            dialog.show();
        });
    }

    //Parameter: boolean input to switch between app as source and web
    //Return values:
    //resets the piechart depending on switch position to get the new source and displaying them
    private void switchPieChart(boolean isSourceWeb) {
        if (isSourceWeb) {
            if (martikelNumber.isEmpty()) {
                CustomAlertReminder reminder = new CustomAlertReminder(this);
                reminder.show();
                sourceSwitch.setChecked(false);
                switchPieChart(false);
            } else {
                Thread getRequest = new Thread(() -> {
                    try {
                        createGetRequest();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                getRequest.start();

                try {
                    getRequest.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (jsonString != null && !jsonString.isEmpty() && jsonString.contains("matriculationNumber")) {
                    System.out.println("JSON: " + jsonString);
                    String[] entries = jsonString.split(",");
                    if (entries.length == 3) {
                        String VPS = entries[1].split(":")[1];
                        if (!VPS.isEmpty()) {
                            setPieChartData(Double.parseDouble(VPS), 0, 0);
                            plannedLayout.setVisibility(View.INVISIBLE);
                            participatedLayout.setVisibility(View.INVISIBLE);
                            completedLayout.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        }
        if (!isSourceWeb) {
            setPieChartData(completedVP, participatedVP, plannedVP);
            plannedLayout.setVisibility(View.VISIBLE);
            completedLayout.setVisibility(View.INVISIBLE);
            participatedLayout.setVisibility(View.VISIBLE);

        }
    }

    //Parameter: Input are double values to determine the size of the piechart slices
    //Return values:
    //Converts the inputs into percentage and adds slices to piechart
    private void setPieChartData(double completedVP, double participationVP, double plannedVP) {
        chart.clearChart();
        int max = (int) (sumVPs * 100);
        int scaledCompletedVP = (int) completedVP * 100;
        int scaledParticipationVP = (int) participationVP * 100;
        int scaledPlannedVP = (int) plannedVP * 100;
        int remainingVP = max - (scaledCompletedVP + scaledParticipationVP + scaledPlannedVP);
        if (remainingVP < 0) {
            remainingVP = 0;
        }


        chart.addPieSlice(
                new PieModel(
                        getString(R.string.pieSliceLabelSafe),
                        scaledCompletedVP,
                        getResources().getColor(R.color.pieChartSafe)));
        completed.setText("Erledigt: " + completedVP + " VP");
        //Color.parseColor(String.valueOf(ContextCompat.getColor(this, R.color.pieChartSafe)))));
        chart.addPieSlice(
                new PieModel(
                        getString(R.string.pieSliceLabelParticipation),
                        scaledParticipationVP,
                        getResources().getColor(R.color.pieChartParticipation)));
        participated.setText("Vergangene: " + participationVP + " VP");
        chart.addPieSlice(
                new PieModel(
                        getString(R.string.pieSliceLabelPlanned),
                        scaledPlannedVP,
                        getResources().getColor(R.color.pieChartPlanned)));
        planned.setText("Geplant: " + plannedVP + " VP");
        chart.addPieSlice(
                new PieModel(
                        getString(R.string.pieSliceLabelRemaining),
                        remainingVP,
                        getResources().getColor(R.color.pieChartRemaining)));
        remaining.setText("Übrig: " + remainingVP / 100 + " VP");


        //chart.setUseInnerPadding(false);
        chart.setInnerPaddingColor(getResources().getColor(R.color.cardview_light_background));
        chart.startAnimation();
    }

    //Parameter:
    //Return values:
    //Creates and calls a getRequest to get the saved number of vps from the universities website. saves the count in a string
    private void createGetRequest() throws IOException {
        HttpURLConnection urlConnection = null;
        URL url = new URL("https://vp.software-engineering.education/" + martikelNumber + "/vps");
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(10000 /* milliseconds */);
        urlConnection.setConnectTimeout(15000 /* milliseconds */);
        urlConnection.setDoOutput(true);
        urlConnection.connect();

        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        br.close();

        jsonString = sb.toString();
    }

    //Parameter: Input from CustomAlertDialog containing the count of vps and the users matrikelnumber
    //Return values:
    //Saves the input as local values and calls a database method to update the values in the users profile
    public void closeDialog(String vps, String matrikelnumber) {
        martikelNumber = matrikelnumber;
        sumVPs = Float.parseFloat(vps);

        switchPieChart(sourceSwitch.isChecked());

        PA_ExpandableListDataPump.saveVPandMatrikelnumber(vps, matrikelnumber);

    }
}









