package com.example.vpmanager.views;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.example.vpmanager.models.StudyObjectPa;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class personalAccountFragment extends Fragment {

    private ListView listView;
    private ImageView sortAlphabetically, sortAppointments;

    private static String martikelNumber;

    private TextView planned, participated, completed, remaining, sortVpCount;
    private ToggleButton removeCompleted, removePlanned, removeParticipated;


    private View completedView, plannedView, participatedView, restView;
    private LinearLayout  customProgressBar;

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
        listView = view.findViewById(R.id.pa_fragment_listView);
        planned = view.findViewById(R.id.pa_planned_progressBar);
        participated = view.findViewById(R.id.pa_participated_progressBar);
        completed = view.findViewById(R.id.pa_completed_progressBar);
        remaining = view.findViewById(R.id.pa_remaining_progressBar);

        customProgressBar = view.findViewById(R.id.customProgressBar);
        completedView = view.findViewById(R.id.progress_section_completed);
        plannedView = view.findViewById(R.id.progress_section_planned);
        participatedView = view.findViewById(R.id.progress_section_participated);
        restView = view.findViewById(R.id.progress_section_rest);

        listView.setAdapter(new CustomListViewAdapter(this.getContext(), this.getActivity(), navController));

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
        setProgressBarData(completedVP, participatedVP, plannedVP);
    }


    //Parameter:
    //Return values:
    //Setup clicklisteners for all clickable objects
    private void setupClickListener() {

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


    //Parameter: Input are double values to determine the size of the progressbar slices
    //Return values:
    //Converts the inputs into percentage and adds slices to progressbar
    private void setProgressBarData(double completedVP, double participationVP, double plannedVP) {
        int max = (int) (sumVPs * 100);
        int scaledCompletedVP = (int) completedVP * 100;
        int scaledParticipationVP = (int) participationVP * 100;
        int scaledPlannedVP = (int) plannedVP * 100;
        int remainingVP = max - (scaledCompletedVP + scaledParticipationVP + scaledPlannedVP);
        if (remainingVP < 0) {
            remainingVP = 0;
        }

        completed.setText("Erledigt: " + completedVP + " VP");

        participated.setText("Vergangene: " + participationVP + " VP");

        planned.setText("Geplant: " + plannedVP + " VP");

        remaining.setText("Übrig: " + remainingVP / 100 + " VP");

        customProgressBar.setWeightSum(sumVPs*100);

        LinearLayout.LayoutParams param;

        param = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT,
                (float) completedVP*100
        );
        completedView.setLayoutParams(param);

        param = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT,
                (float) participatedVP*100
        );
        participatedView.setLayoutParams(param);

        param = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT,
                (float) plannedVP*100
        );
        plannedView.setLayoutParams(param);

        param = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT,
                (float) (sumVPs - (completedVP + participatedVP + plannedVP))*100
        );
        restView.setLayoutParams(param);
    }


    private void filterListViewTextTags(boolean active, String type) {
        switch (type) {
            case "names":
                if (!sortAlphabeticallyInvert && !sortAlphabeticallyActive) {
                    sortAlphabeticallyInvert = false;
                    sortAlphabeticallyActive = !active;
                } else if (sortAlphabeticallyActive && !sortAlphabeticallyInvert) {
                    sortAlphabeticallyInvert = true;
                    sortAlphabeticallyActive = true;
                } else {
                    sortAlphabeticallyInvert = false;
                    sortAlphabeticallyActive = false;
                }
                if (sortAlphabeticallyActive)
                    sortAlphabetically.setBackgroundColor(Color.LTGRAY);
                else
                    sortAlphabetically.setBackgroundColor(Color.WHITE);
                if (sortAppointmentsActive) {
                    sortAppointmentsActive = false;
                    sortAppointments.setBackgroundColor(Color.WHITE);
                }
                if (sortVpCountActive) {
                    sortVpCountActive = false;
                    sortVpCount.setBackgroundColor(Color.WHITE);
                }
                sortAppointmentsInvert = false;
                sortVpCountInvert = false;
                break;
            case "dates":
                if (!sortAppointmentsInvert && !sortAppointmentsActive) {
                    sortAppointmentsInvert = false;
                    sortAppointmentsActive = !active;
                } else if (sortAppointmentsActive && !sortAppointmentsInvert) {
                    sortAppointmentsInvert = true;
                    sortAppointmentsActive = true;
                } else {
                    sortAppointmentsInvert = false;
                    sortAppointmentsActive = false;
                }
                if (sortAppointmentsActive)
                    sortAppointments.setBackgroundColor(Color.LTGRAY);
                else
                    sortAppointments.setBackgroundColor(Color.WHITE);

                if (sortVpCountActive) {
                    sortVpCount.setBackgroundColor(Color.WHITE);
                    sortVpCountActive = false;
                }
                if (sortAlphabeticallyActive) {
                    sortAlphabeticallyActive = false;
                    sortAlphabetically.setBackgroundColor(Color.WHITE);
                }
                sortAlphabeticallyInvert = false;
                sortVpCountInvert = false;
                break;
            case "vps":

                if (!sortVpCountInvert && !sortVpCountActive) {
                    sortVpCountInvert = false;
                    sortVpCountActive = !active;
                } else if (sortVpCountActive && !sortVpCountInvert) {
                    sortVpCountInvert = true;
                    sortVpCountActive = true;
                } else {
                    sortVpCountInvert = false;
                    sortVpCountActive = false;
                }
                if (sortVpCountActive)
                    sortVpCount.setBackgroundColor(Color.LTGRAY);
                else
                    sortVpCount.setBackgroundColor(Color.WHITE);
                if (sortAlphabeticallyActive) {
                    sortAlphabeticallyActive = false;
                    sortAlphabetically.setBackgroundColor(Color.WHITE);
                }
                if (sortAppointmentsActive) {
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

        if (sortAlphabeticallyActive) {
            adapter = new CustomListViewAdapter(this.getContext(), this.getActivity(), navController, sortByName(sortAlphabeticallyInvert));
        } else if (sortAppointmentsActive) {
            adapter = new CustomListViewAdapter(this.getContext(), this.getActivity(), navController, sortByDate(sortAppointmentsInvert));
        } else if (sortVpCountActive) {
            adapter = new CustomListViewAdapter(this.getContext(), this.getActivity(), navController, sortByVPS(sortVpCountInvert));
        } else {
            adapter = new CustomListViewAdapter(this.getContext(), this.getActivity(), navController);
        }

        setNewListViewAdapter(adapter);

        System.out.println("sortlist" + adapter.getObjects());


        if (!removeCompleted.isChecked())
            filterListViewColorTags(false, R.color.pieChartSafe);
        if (!removeParticipated.isChecked())
            filterListViewColorTags(false, R.color.pieChartParticipation);
        if (!removePlanned.isChecked())
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
        if (state) {
            CustomListViewAdapter adapter = new CustomListViewAdapter(this.getContext(), this.getActivity(), navController);
            listView.setAdapter(adapter);
            if (!removePlanned.isChecked())
                filterListViewColorTags(removePlanned.isChecked(), R.color.pieChartPlanned);
            if (!removeCompleted.isChecked())
                filterListViewColorTags(removeCompleted.isChecked(), R.color.pieChartSafe);
            if (!removeParticipated.isChecked())
                filterListViewColorTags(removeParticipated.isChecked(), R.color.pieChartParticipation);

            if (sortAlphabeticallyActive)
                filterListViewTextTags(false, "names");
            if (sortAppointmentsActive)
                filterListViewTextTags(false, "dates");
            if (sortVpCountActive)
                filterListViewTextTags(false, "vps");
        } else {
            CustomListViewAdapter adapter = (CustomListViewAdapter) listView.getAdapter();
            List<StudyObjectPa> removeList = new ArrayList<>();


            for (StudyObjectPa object : adapter.getObjects()) {
                if (listView != null && object != null) {
                    if (object.getColor() == color) {
                        removeList.add(object);
                    }
                }
            }

            for (StudyObjectPa object : removeList) {
                adapter.getObjects().remove(object);
            }

            listView.setAdapter(null);
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);

            System.out.println(adapter.getObjects());
        }
    }

    private ArrayList<StudyObjectPa> sortByName(boolean invert)
    {
        CustomListViewAdapter adapter = (CustomListViewAdapter) listView.getAdapter();
        ArrayList<StudyObjectPa> list = new ArrayList<>();
        for (int i = 0; i < adapter.getObjects().size(); i++) {
            if (listView != null && adapter.getObjects().get(i) != null) {
                StudyObjectPa item = adapter.getObjects().get(i);
                list.add(item);
            }
        }
        StudyObjectPa[] studyList = new StudyObjectPa[list.size()];
        System.out.println("list size: " + list.size());
        for (int i = 0; i < list.size(); i++) {
            studyList[i] = list.get(i);
        }

        for (int i = 0; i < studyList.length; i++) {

            for (int k = 0; k < studyList.length - 1; k++) {
                if (studyList[k].getTitle().compareToIgnoreCase(studyList[k + 1].getTitle()) < 0) {
                    StudyObjectPa tempStudy = studyList[k];
                    studyList[k] = studyList[k + 1];
                    studyList[k + 1] = tempStudy;
                }
            }
        }
        list.clear();
        for (StudyObjectPa ob : studyList) {
            list.add(ob);
        }
        System.out.println("list after clear" + list.size());
        if (!invert)
            Collections.reverse(list);

        return list;
    }

    private ArrayList<StudyObjectPa> sortByDate(boolean invert) {
        CustomListViewAdapter adapter = (CustomListViewAdapter) listView.getAdapter();
        ArrayList<StudyObjectPa> list = new ArrayList<>();
        for (int i = 0; i < adapter.getObjects().size(); i++) {
            if (listView != null && adapter.getObjects().get(i) != null) {
                StudyObjectPa item = adapter.getObjects().get(i);
                list.add(item);
            }
        }
        StudyObjectPa[] studyList = new StudyObjectPa[list.size()];
        for (int i = 0; i < list.size(); i++) {
            studyList[i] = list.get(i);
        }

        for (int i = 0; i < studyList.length; i++) {
            for (int k = 0; k < studyList.length - 1; k++) {

                String date1 = studyList[k].getDate().substring(studyList[k].getDate().indexOf(",") + 2);
                String date2 = studyList[k + 1].getDate().substring(studyList[k + 1].getDate().indexOf(",") + 2);

                date1 = date1.replaceAll("um", "");
                date1 = date1.replaceAll("Uhr", "");
                date2 = date2.replaceAll("um", "");
                date2 = date2.replaceAll("Uhr", "");

                Format format = new SimpleDateFormat("dd.MM.yyyy hh:mm");
                try {

                    Date d1_Date = (Date) format.parseObject(date1);
                    Date d2_Date = (Date) format.parseObject(date2);

                    if (d2_Date.before(d1_Date)) {
                        StudyObjectPa tempStudy = studyList[k];
                        studyList[k] = studyList[k + 1];
                        studyList[k + 1] = tempStudy;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        list.clear();
        for(StudyObjectPa ob: studyList) {
            list.add(ob);
        }

        if (!invert)
            Collections.reverse(list);

        return list;
    }

    private ArrayList<StudyObjectPa> sortByVPS(boolean invert) {
        CustomListViewAdapter adapter = (CustomListViewAdapter) listView.getAdapter();
        ArrayList<StudyObjectPa> list = new ArrayList<>();
        for (int i = 0; i < adapter.getObjects().size(); i++) {
            if (listView != null && adapter.getObjects().get(i) != null) {
                StudyObjectPa item = adapter.getObjects().get(i);
                list.add(item);
            }
        }
        StudyObjectPa[] studyList = new StudyObjectPa[list.size()];
        for (int i = 0; i < list.size(); i++) {
            studyList[i] = list.get(i);
        }

        for (int i = 0; i < studyList.length; i++) {
            for (int k = 0; k < studyList.length - 1; k++) {
                if (Float.parseFloat(studyList[k].getVps()) > Float.parseFloat(studyList[k + 1].getVps())) {
                    StudyObjectPa tempStudy = studyList[k];
                    studyList[k] = studyList[k + 1];
                    studyList[k + 1] = tempStudy;
                }
            }
        }
        list.clear();
        for(StudyObjectPa ob: studyList) {
            list.add(ob);
        }

        if (!invert)
            Collections.reverse(list);

        return list;
    }
}









