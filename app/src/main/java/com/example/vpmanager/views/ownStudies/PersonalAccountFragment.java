package com.example.vpmanager.views.ownStudies;

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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.vpmanager.R;
import com.example.vpmanager.adapter.CustomListViewAdapter;
import com.example.vpmanager.models.StudyObjectPa;
import com.example.vpmanager.viewmodels.PersonalAccountViewModel;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class PersonalAccountFragment extends Fragment {

    private PersonalAccountViewModel personalAccountViewModel;
    private NavController navController;

    //done
    private LinearLayout customProgressBar;
    private View completedView, plannedView, participatedView, restView;
    private TextView completed, participated, planned, remaining;

    //Images and one text view (VP) for sorting
    private ImageView sortAlphabetically, sortAppointments;
    private TextView sortVpCount;
    //ToggleButtons for filtering
    private ToggleButton removeCompleted, removePlanned, removeParticipated;


    //ListView that contains the sorted and filtered list of a date x study combination
    private ListView listView;

    //booleans to indicate the sorting --> three variants + inverted
    /*
    private boolean sortAlphabeticallyActive, sortAppointmentsActive, sortVpCountActive,
            sortAlphabeticallyInvert, sortAppointmentsInvert, sortVpCountInvert;
     */

    // Doubles to show the specific vph in the progressbar legend
    //private double plannedVP, completedVP, participatedVP;
    // float to calculate the shares of the individual vps
    //private float sumVPs;

    //private String jsonString;
    //private static String martikelNumber;


    public PersonalAccountFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_account, container, false);
        prepareViewModel();
        setupView(view);
        personalAccountViewModel.getDatesStudiesVpsAndMatrikelNumberFromDb();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);
        super.onViewCreated(view, savedInstanceState);
    }

    private void prepareViewModel() {
        //ParentFragment (OwnStudyFragment) should be viewModelStoreOwner
        personalAccountViewModel = new ViewModelProvider(getParentFragment()).get(PersonalAccountViewModel.class);
        personalAccountViewModel.personalAccountFragment = this;
        personalAccountViewModel.prepareRepo();
    }

    //Parameter: view is passed to setup view
    //Return values:
    //calls to database to get all the relevant data for this fragment
    /*
    private void loadData() {

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


                                    setData();
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
     */

    //Parameter:
    //Return values:
    //Setting the view components and readint the content Lists for the piechart values
    private void setupView(View view) {
        customProgressBar = view.findViewById(R.id.customProgressBar);
        completedView = view.findViewById(R.id.progress_section_completed);
        plannedView = view.findViewById(R.id.progress_section_planned);
        participatedView = view.findViewById(R.id.progress_section_participated);
        restView = view.findViewById(R.id.progress_section_rest);

        completed = view.findViewById(R.id.pa_completed_progressBar);
        participated = view.findViewById(R.id.pa_participated_progressBar);
        planned = view.findViewById(R.id.pa_planned_progressBar);
        remaining = view.findViewById(R.id.pa_remaining_progressBar);

        sortAlphabetically = view.findViewById(R.id.pa_sort_alphabetical);
        sortAppointments = view.findViewById(R.id.pa_sort_date);
        sortVpCount = view.findViewById(R.id.pa_sort_vp);

        removeCompleted = view.findViewById(R.id.pa_remove_completed);
        removePlanned = view.findViewById(R.id.pa_remove_planned);
        removeParticipated = view.findViewById(R.id.pa_remove_participation);

        listView = view.findViewById(R.id.pa_fragment_listView);
    }

    /*
    private void setData(){

        listView.setAdapter(new CustomListViewAdapter(this.getContext(), navController)); //this.getActivity(),

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
        List<String> passedVpList = PA_ExpandableListDataPump.EXPANDABLE_LIST_DETAIL.get("Teilgenommene Studien");
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
     */

    //Parameter: Input are double values to determine the size of the progressbar slices
    //Return values:
    //Converts the inputs into percentage and adds slices to progressbar
    /*
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

        participated.setText("Teilgenommen: " + participationVP + " VP");

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
     */

    public void setProgressBarStrings(String completedString, String participatedString, String plannedString,
                                      String remainingString, float weightSum) {
        completed.setText(completedString);
        participated.setText(participatedString);
        planned.setText(plannedString);
        remaining.setText(remainingString);
        customProgressBar.setWeightSum(weightSum);
    }

    public void setProgressBarParts(LinearLayout.LayoutParams completedParams, LinearLayout.LayoutParams participatedParams,
                                    LinearLayout.LayoutParams plannedParams, LinearLayout.LayoutParams restParams) {
        completedView.setLayoutParams(completedParams);
        participatedView.setLayoutParams(participatedParams);
        plannedView.setLayoutParams(plannedParams);
        restView.setLayoutParams(restParams);

        listView.setAdapter(new CustomListViewAdapter(this.getContext(), navController)); //this.getActivity(),
        /*
        sortAlphabeticallyActive = false;
        sortAppointmentsActive = false;
        sortVpCountActive = false;
        sortAlphabeticallyInvert = false;
        sortAppointmentsInvert = false;
        sortVpCountInvert = false;
         */
        setupClickListener();
    }

    //Parameter:
    //Return values:
    //Setup clickListeners for all clickable objects
    private void setupClickListener() {
        sortAlphabetically.setOnClickListener(v -> personalAccountViewModel.filterListViewTextTags("names")); //sortAlphabeticallyActive,
        sortAppointments.setOnClickListener(v -> personalAccountViewModel.filterListViewTextTags("dates")); //sortAppointmentsActive,
        sortVpCount.setOnClickListener(v -> personalAccountViewModel.filterListViewTextTags("vps")); //sortVpCountActive,

        removeCompleted.setOnCheckedChangeListener((buttonView, isChecked) ->
                personalAccountViewModel.filterListViewColorTags(isChecked, R.color.pieChartSafe));
        removePlanned.setOnCheckedChangeListener((buttonView, isChecked) ->
                personalAccountViewModel.filterListViewColorTags(isChecked, R.color.pieChartPlanned));
        removeParticipated.setOnCheckedChangeListener((buttonView, isChecked) ->
                personalAccountViewModel.filterListViewColorTags(isChecked, R.color.pieChartParticipation));
    }

    //moved to viewModel
    /*
    private void filterListViewTextTags(String type) { //boolean active,
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
                if (sortAlphabeticallyActive) {
                    sortAlphabetically.setBackgroundColor(Color.LTGRAY);
                } else {
                    sortAlphabetically.setBackgroundColor(Color.WHITE);
                }
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
                if (sortAppointmentsActive) {
                    sortAppointments.setBackgroundColor(Color.LTGRAY);
                } else {
                    sortAppointments.setBackgroundColor(Color.WHITE);
                }
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
                if (sortVpCountActive) {
                    sortVpCount.setBackgroundColor(Color.LTGRAY);
                } else {
                    sortVpCount.setBackgroundColor(Color.WHITE);
                }
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
     */

    public void setAlphabeticallyToggle(String identifier, Boolean alphaActive) {
        if (identifier.equals("names")) {
            if (alphaActive) {
                sortAlphabetically.setBackgroundColor(Color.LTGRAY);
            } else {
                sortAlphabetically.setBackgroundColor(Color.WHITE);
            }
        } else if (identifier.equals("dates")) {
            if (alphaActive) {
                sortAlphabetically.setBackgroundColor(Color.WHITE);
            }
        } else if (identifier.equals("vps")) {
            if (alphaActive) {
                sortAlphabetically.setBackgroundColor(Color.WHITE);
            }
        }
    }

    public void setAppointmentsToggle(String identifier, Boolean appointActive) {
        if (identifier.equals("names")) {
            if (appointActive) {
                sortAppointments.setBackgroundColor(Color.WHITE);
            }
        } else if (identifier.equals("dates")) {
            if (appointActive) {
                sortAppointments.setBackgroundColor(Color.LTGRAY);
            } else {
                sortAppointments.setBackgroundColor(Color.WHITE);
            }
        } else if (identifier.equals("vps")) {
            if (appointActive) {
                sortAppointments.setBackgroundColor(Color.WHITE);
            }
        }
    }

    public void setVpToggle(String identifier, Boolean vpActive) {
        if (identifier.equals("names")) {
            if (vpActive) {
                sortVpCount.setBackgroundColor(Color.WHITE);
            }
        } else if (identifier.equals("dates")) {
            if (vpActive) {
                sortVpCount.setBackgroundColor(Color.WHITE);
            }
        } else if (identifier.equals("vps")) {
            if (vpActive) {
                sortVpCount.setBackgroundColor(Color.LTGRAY);
            } else {
                sortVpCount.setBackgroundColor(Color.WHITE);
            }
        }
    }

    public NavController getNavController(){
        return navController;
    }

    //returns the current set adapter of the list
    public CustomListViewAdapter getCurrentAdapter(){
        return (CustomListViewAdapter) listView.getAdapter();
    }

    public Boolean getColorToggleState(String identifier){
        if (identifier.equals("completed")){
            return !removeCompleted.isChecked();
        } else if (identifier.equals("participated")){
            return !removeParticipated.isChecked();
        } else { //if (identifier.equals("planned"))
            return !removePlanned.isChecked();
        }
    }

    public void setNewListViewAdapter(CustomListViewAdapter adapter) {
        //clearing the previous might not be necessary
        /*
        CustomListViewAdapter currentAdapter = (CustomListViewAdapter) listView.getAdapter();
        currentAdapter.getObjects().clear();
        currentAdapter.notifyDataSetChanged();
         */
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    //moved to viewModel
    /*
    private void filterListViewTextContent() {
        CustomListViewAdapter adapter;

        if (sortAlphabeticallyActive) {
            adapter = new CustomListViewAdapter(this.getContext(), navController, sortByName(sortAlphabeticallyInvert)); //this.getActivity(),
        } else if (sortAppointmentsActive) {
            adapter = new CustomListViewAdapter(this.getContext(), navController, sortByDate(sortAppointmentsInvert)); //this.getActivity(),
        } else if (sortVpCountActive) {
            adapter = new CustomListViewAdapter(this.getContext(), navController, sortByVPS(sortVpCountInvert)); //this.getActivity(),
        } else {
            adapter = new CustomListViewAdapter(this.getContext(), navController); //this.getActivity(),
        }

        setNewListViewAdapter(adapter);

        System.out.println("sortlist" + adapter.getObjects());

        if (!removeCompleted.isChecked()) {
            filterListViewColorTags(false, R.color.pieChartSafe);
        }
        if (!removeParticipated.isChecked()) {
            filterListViewColorTags(false, R.color.pieChartParticipation);
        }
        if (!removePlanned.isChecked()) {
            filterListViewColorTags(false, R.color.pieChartPlanned);
        }
    }
     */
    //moved to viewModel
    /*
    private void filterListViewColorTags(boolean state, int color) {
        if (state) {
            CustomListViewAdapter adapter = new CustomListViewAdapter(this.getContext(), navController); //this.getActivity(),
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
     */
    //moved to viewModel
    private ArrayList<StudyObjectPa> sortByName(boolean invert) {
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
        if (!invert) {
            Collections.reverse(list);
        }
        return list;
    }
    //moved to viewModel
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
        for (StudyObjectPa ob : studyList) {
            list.add(ob);
        }

        if (!invert) {
            Collections.reverse(list);
        }
        return list;
    }
    //moved to viewModel
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
        for (StudyObjectPa ob : studyList) {
            list.add(ob);
        }

        if (!invert) {
            Collections.reverse(list);
        }
        return list;
    }
}