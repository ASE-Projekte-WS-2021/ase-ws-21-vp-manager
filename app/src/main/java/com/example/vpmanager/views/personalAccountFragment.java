package com.example.vpmanager.views;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

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
import java.util.Collections;
import java.util.List;

public class personalAccountFragment extends Fragment {

    private ListView listView;
    private PieChart chart;
    private Switch sourceSwitch;
    private ImageView settings, sortAlphabetically, sortAppointments;
    private LinearLayout participatedLayout, plannedLayout, completedLayout;

    private static String martikelNumber;

    private TextView planned, participated, completed, remaining, sortVpCount;
    private ToggleButton removeCompleted, removePlanned, removeParticipated;

    private boolean sortAlphabeticallyActive, sortAppointmentsActive, sortVpCountActive, sortAlphabeticallyInvert, sortAppointmentsInvert, sortVpCountInvert;

    private double plannedVP, completedVP, participatedVP;

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
                                    sortAlphabeticallyActive = false;
                                    sortAppointmentsActive = false;
                                    sortVpCountActive = false;
                                    sortAlphabeticallyInvert = false;
                                    sortAppointmentsInvert = false;
                                    sortVpCountInvert = false;
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

        removeCompleted = view.findViewById(R.id.pa_remove_completed);
        removePlanned = view.findViewById(R.id.pa_remove_planned);
        removeParticipated = view.findViewById(R.id.pa_remove_participation);
        sortAlphabetically = view.findViewById(R.id.pa_sort_alphabetical);
        sortAppointments = view.findViewById(R.id.pa_sort_date);
        sortVpCount = view.findViewById(R.id.pa_sort_vp);
        completedLayout = view.findViewById(R.id.pa_completed_layout);
        listView = view.findViewById(R.id.pa_fragment_listView);
        participatedLayout = view.findViewById(R.id.pa_parcticipated_layout);
        plannedLayout = view.findViewById(R.id.pa_planned_layout);
        completedLayout = view.findViewById(R.id.pa_completed_layout);
        planned = view.findViewById(R.id.pa_planned);
        participated = view.findViewById(R.id.pa_participated);
        completed = view.findViewById(R.id.pa_completed);
        remaining = view.findViewById(R.id.pa_remaining);
        settings = view.findViewById(R.id.vp_settings_icon);
        sourceSwitch = view.findViewById(R.id.vp_source_switch);
        chart = view.findViewById(R.id.pa_fragment_pie_chart);

        listView.setAdapter(new CustomListViewAdapter(this.getContext() , this.getActivity(), navController));

        plannedVP = 0;
        completedVP = 0;
        participatedVP = 0;

        List<String> savedList = PA_ExpandableListDataPump.EXPANDABLE_LIST_DETAIL.get("Abgeschlossene Studien");
        if (savedList != null) {
            for (int i = 0; i < savedList.size(); i++) {
                String vps = savedList.get(i).split(";")[1];
                double studyVPS = 0;
                if (vps != null && !vps.equals(""))
                    studyVPS = Double.parseDouble(vps);
                completedVP += studyVPS;
            }
        }

        List<String> vpList = PA_ExpandableListDataPump.EXPANDABLE_LIST_DETAIL.get("Geplante Studien");
        if (vpList != null) {
            for (int i = 0; i < vpList.size(); i++) {
                String vps = vpList.get(i).split(";")[1];
                double studyVPS = 0;
                if (vps != null && !vps.equals(""))
                    studyVPS = Double.parseDouble(vps);
                plannedVP += studyVPS;
            }
        }
        List<String> passedVpList = PA_ExpandableListDataPump.EXPANDABLE_LIST_DETAIL.get("Vergangene Studien");
        if (passedVpList != null) {
            for (int i = 0; i < passedVpList.size(); i++) {
                String vps = passedVpList.get(i).split(";")[1];
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

        sourceSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            switchPieChart(isChecked);
        });
        settings.setOnClickListener(v -> {
            personalAccountFragment fragment = personalAccountFragment.this;
            CustomAlertDialog dialog = new CustomAlertDialog(fragment, Float.toString(sumVPs), martikelNumber);
            dialog.show();
        });
        removeCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> filterListViewColorTags(isChecked, R.color.pieChartSafe));
        removePlanned.setOnCheckedChangeListener((buttonView, isChecked) -> filterListViewColorTags(isChecked, R.color.pieChartPlanned));
        removeParticipated.setOnCheckedChangeListener((buttonView, isChecked) -> filterListViewColorTags(isChecked, R.color.pieChartParticipation));

        sortAlphabetically.setOnClickListener(v -> filterListViewTextTags(sortAlphabeticallyActive, "names"));
        sortAppointments.setOnClickListener(v -> filterListViewTextTags(sortAppointmentsActive, "dates"));
        sortVpCount.setOnClickListener(v -> filterListViewTextTags(sortVpCountActive, "vps"));

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
                        }
                    }
                }
            }
        }
        if (!isSourceWeb) {
            setPieChartData(completedVP, participatedVP, plannedVP);
            plannedLayout.setVisibility(View.VISIBLE);
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
        participated.setText("Teilgenommen: " + participationVP + " VP");
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


    private void filterListViewTextTags(boolean active, String type)
    {
        switch(type)
        {
            case "names":
                if(!sortAlphabeticallyInvert && !sortAlphabeticallyActive)
                {
                    sortAlphabeticallyInvert = false;
                    sortAlphabeticallyActive = !active;
                }
                else if(sortAlphabeticallyActive && !sortAlphabeticallyInvert)
                {
                    sortAlphabeticallyInvert = true;
                    sortAlphabeticallyActive = true;
                }
                else
                {
                    sortAlphabeticallyInvert = false;
                    sortAlphabeticallyActive = false;
                }
                if(sortAlphabeticallyActive)
                    sortAlphabetically.setBackgroundColor(Color.LTGRAY);
                else
                    sortAlphabetically.setBackgroundColor(Color.WHITE);
                if(sortAppointmentsActive) {
                    sortAppointmentsActive = false;
                    sortAppointments.setBackgroundColor(Color.WHITE);
                }
                if(sortVpCountActive) {
                    sortVpCountActive = false;
                    sortVpCount.setBackgroundColor(Color.WHITE);
                }
                sortAppointmentsInvert = false;
                sortVpCountInvert = false;
                break;
            case "dates":
                if(!sortAppointmentsInvert && !sortAppointmentsActive)
                {
                    sortAppointmentsInvert = false;
                    sortAppointmentsActive = !active;
                }
                else if(sortAppointmentsActive && !sortAppointmentsInvert)
                {
                    sortAppointmentsInvert = true;
                    sortAppointmentsActive = true;
                }
                else
                {
                    sortAppointmentsInvert = false;
                    sortAppointmentsActive = false;
                }
                if(sortAppointmentsActive)
                    sortAppointments.setBackgroundColor(Color.LTGRAY);
                else
                    sortAppointments.setBackgroundColor(Color.WHITE);

                if(sortVpCountActive) {
                    sortVpCount.setBackgroundColor(Color.WHITE);
                    sortVpCountActive = false;
                }
                if(sortAlphabeticallyActive) {
                    sortAlphabeticallyActive = false;
                    sortAlphabetically.setBackgroundColor(Color.WHITE);
                }
                sortAlphabeticallyInvert =false;
                sortVpCountInvert = false;
                break;
            case "vps":

                if(!sortVpCountInvert && !sortVpCountActive)
                {
                    sortVpCountInvert = false;
                    sortVpCountActive = !active;
                }
                else if(sortVpCountActive && !sortVpCountInvert)
                {
                    sortVpCountInvert = true;
                    sortVpCountActive = true;
                }
                else
                {
                    sortVpCountInvert = false;
                    sortVpCountActive = false;
                }
                if(sortVpCountActive)
                    sortVpCount.setBackgroundColor(Color.LTGRAY);
                else
                    sortVpCount.setBackgroundColor(Color.WHITE);
                if(sortAlphabeticallyActive) {
                    sortAlphabeticallyActive = false;
                    sortAlphabetically.setBackgroundColor(Color.WHITE);
                }
                if(sortAppointmentsActive) {
                    sortAppointmentsActive = false;
                    sortAppointments.setBackgroundColor(Color.WHITE);
                }
                sortAlphabeticallyInvert = false;
                sortAppointmentsInvert = false;
                break;
            default:
                break;
        }
        filterListViewTextContent();
    }

    private void filterListViewTextContent() {
        CustomListViewAdapter adapter;

        if(sortAlphabeticallyActive)
        {
            adapter = new CustomListViewAdapter(this.getContext() , this.getActivity(), navController, sortByName(sortAlphabeticallyInvert));
        }
        else if(sortAppointmentsActive)
        {
            adapter = new CustomListViewAdapter(this.getContext() , this.getActivity(), navController, sortByDate(sortAppointmentsInvert));
        }
        else if(sortVpCountActive)
        {
            adapter = new CustomListViewAdapter(this.getContext() , this.getActivity(), navController, sortByVPS(sortVpCountInvert));
        }
        else
        {
            adapter = new CustomListViewAdapter(this.getContext() , this.getActivity(), navController);
        }

        setNewListViewAdapter(adapter);

        System.out.println("sortlist" + adapter.getObjects());


        if(!removeCompleted.isChecked())
            filterListViewColorTags(false, R.color.pieChartSafe);
        if(!removeParticipated.isChecked())
            filterListViewColorTags(false, R.color.pieChartParticipation);
        if(!removePlanned.isChecked())
            filterListViewColorTags(false, R.color.pieChartPlanned);
    }

    private void setNewListViewAdapter(CustomListViewAdapter adapter) {
        CustomListViewAdapter currentAdapter = (CustomListViewAdapter) listView.getAdapter();
        currentAdapter.getObjects().clear();
        currentAdapter.notifyDataSetChanged();

        listView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }

    private void filterListViewColorTags(boolean state, int color) {
        if(state)
        {
            CustomListViewAdapter adapter = new CustomListViewAdapter(this.getContext() , this.getActivity(), navController);
            listView.setAdapter(adapter);
            if(!removePlanned.isChecked())
                filterListViewColorTags(removePlanned.isChecked(), R.color.pieChartPlanned);
            if(!removeCompleted.isChecked())
                filterListViewColorTags(removeCompleted.isChecked(), R.color.pieChartSafe);
            if(!removeParticipated.isChecked())
                filterListViewColorTags(removeParticipated.isChecked(), R.color.pieChartParticipation);

            if(sortAlphabeticallyActive)
                filterListViewTextTags(false, "names");
            if(sortAppointmentsActive)
                filterListViewTextTags(false, "dates");
            if(sortVpCountActive)
                filterListViewTextTags(false, "vps");
        }
        else {
            CustomListViewAdapter adapter = (CustomListViewAdapter) listView.getAdapter();
            List<StudyObject> removeList = new ArrayList<>();


            for (StudyObject object: adapter.getObjects()) {
                if(listView != null && object != null) {
                    if (object.getColor() == color) {
                        removeList.add(object);
                    }
                }
            }

            for(StudyObject object: removeList)
            {
                adapter.getObjects().remove(object);
            }

            listView.setAdapter(null);
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);

            System.out.println(adapter.getObjects());
        }
    }

    private ArrayList<StudyObject> sortByName(boolean invert)
    {
        CustomListViewAdapter adapter = (CustomListViewAdapter) listView.getAdapter();
        ArrayList<StudyObject> list = new ArrayList<>();
        for (int i = 0; i < adapter.getObjects().size(); i++) {
            if(listView != null && adapter.getObjects().get(i) != null) {
                StudyObject item = adapter.getObjects().get(i);
                list.add(item);
            }
        }
        StudyObject[] studyList = new StudyObject[list.size()];
        System.out.println("list size: " + list.size());
        for(int i = 0; i <list.size(); i++)
        {
            studyList[i] = list.get(i);
        }

        for(int i = 0; i < studyList.length; i++)
        {

            for(int k = 0; k < studyList.length-1; k++)
            {
                if(studyList[k].getTitle().compareToIgnoreCase(studyList[k+1].getTitle()) < 0)
                {
                    StudyObject tempStudy = studyList[k];
                    studyList[k]= studyList[k+1];
                    studyList[k+1]= tempStudy;
                }
            }
        }
        list.clear();
        for(StudyObject ob: studyList) {
            list.add(ob);
        }
        System.out.println("list after clear" + list.size());
        if(!invert)
            Collections.reverse(list);

        return list;
    }

    private ArrayList<StudyObject> sortByDate(boolean invert)
    {
        CustomListViewAdapter adapter = (CustomListViewAdapter) listView.getAdapter();
        ArrayList<StudyObject> list = new ArrayList<>();
        for (int i = 0; i < adapter.getObjects().size(); i++) {
            if(listView != null && adapter.getObjects().get(i) != null) {
                StudyObject item = adapter.getObjects().get(i);
                list.add(item);
            }
        }
        StudyObject[] studyList = new StudyObject[list.size()];
        for(int i = 0; i <list.size(); i++)
        {
            studyList[i] = list.get(i);
        }

        for(int i = 0; i < studyList.length; i++)
        {
            for(int k = 0; k < studyList.length-1; k++) {

                String date1 = studyList[k].getDate().substring(studyList[k].getDate().indexOf(",")+2);
                String date2 = studyList[k+1].getDate().substring(studyList[k+1].getDate().indexOf(",")+2);

                if (date1.compareToIgnoreCase(date2) < 0) {
                    StudyObject tempStudy = studyList[k];
                    studyList[k] = studyList[k + 1];
                    studyList[k + 1] = tempStudy;
                }
            }
        }
        list.clear();
        for(StudyObject ob: studyList) {
            list.add(ob);
        }

        if(!invert)
            Collections.reverse(list);

        return list;
    }

    private ArrayList<StudyObject> sortByVPS(boolean invert)
    {
        CustomListViewAdapter adapter = (CustomListViewAdapter) listView.getAdapter();
        ArrayList<StudyObject> list = new ArrayList<>();
        for (int i = 0; i < adapter.getObjects().size(); i++) {
            if(listView != null && adapter.getObjects().get(i) != null) {
                StudyObject item = adapter.getObjects().get(i);
                list.add(item);
            }
        }
        StudyObject[] studyList = new StudyObject[list.size()];
        for(int i = 0; i <list.size(); i++)
        {
            studyList[i] = list.get(i);
        }

        for(int i = 0; i < studyList.length; i++)
        {
            for(int k = 0; k < studyList.length-1; k++)
            {
                if(Float.parseFloat(studyList[k].getVps()) > Float.parseFloat(studyList[k+1].getVps()))
                {
                    StudyObject tempStudy = studyList[k];
                    studyList[k]= studyList[k+1];
                    studyList[k+1]= tempStudy;
                }
            }
        }
        list.clear();
        for(StudyObject ob: studyList) {
            list.add(ob);
        }

        if(!invert)
            Collections.reverse(list);

        return list;
    }
}









