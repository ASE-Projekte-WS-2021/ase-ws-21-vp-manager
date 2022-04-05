package com.example.vpmanager.viewmodels;

import static com.example.vpmanager.views.mainActivity.uniqueID;

import android.util.Log;
import android.widget.LinearLayout;

import androidx.lifecycle.ViewModel;

import com.example.vpmanager.R;
import com.example.vpmanager.adapter.CustomStudyListAdapter;
import com.example.vpmanager.interfaces.GetAllDatesListener;
import com.example.vpmanager.interfaces.GetAllStudiesListener;
import com.example.vpmanager.interfaces.GetVpAndMatNrListener;
import com.example.vpmanager.models.StudyObjectPa;
import com.example.vpmanager.repositories.PersonalAccountRepository;
import com.example.vpmanager.views.ownStudies.PersonalAccountFragment;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonalAccountViewModel extends ViewModel implements GetAllDatesListener, GetAllStudiesListener, GetVpAndMatNrListener {

    public HashMap<String, List<String>> expandableListDetail = new HashMap<>();

    private float sumVPs;
    private String matrikelNumber;
    private double plannedVP, completedVP, participatedVP;

    //booleans for using the filters
    private boolean sortAlphabeticallyActive, sortAppointmentsActive, sortVpCountActive,
            sortAlphabeticallyInvert, sortAppointmentsInvert, sortVpCountInvert;

    public PersonalAccountFragment personalAccountFragment;
    private PersonalAccountRepository mOwnStudyRepo;

    public void prepareRepo() {
        mOwnStudyRepo = PersonalAccountRepository.getInstance();
        //Instance is the same but different data can be retrieved!
        mOwnStudyRepo.setFirestoreCallback(this, this, this);
    }


    public void getDatesStudiesVpsAndMatrikelNumberFromDb() {
        //dates is the starting point. other db calls are made from its callback!
        mOwnStudyRepo.getAllDatesFromDb();
    }

    public void fetchAllStudiesFromDb() {
        mOwnStudyRepo.getAllStudiesFromDb();
    }

    public void fetchVpAndMatrikelNumber() {
        mOwnStudyRepo.getVpAndMatrikelNumber();
    }

    @Override
    public void onAllDatesReady(Boolean success) {
        if (success) {
            fetchAllStudiesFromDb();
        } else {
            Log.d("PAViewModel", "Getting all dates from DB failed!");
        }
    }

    @Override
    public void onAllStudiesReady(Boolean success) {
        if (success) {
            fetchVpAndMatrikelNumber();
        } else {
            Log.d("PAViewModel", "Getting all studies from DB failed!");
        }
    }

    @Override
    public void onAllDataReady(String vps, String matNr, List<Map<String, Object>> datesList, List<Map<String, Object>> studiesList) {
        if (vps != null && matNr != null && !vps.equals("") && !matNr.equals("")) {
            sumVPs = Float.parseFloat(vps);
            matrikelNumber = matNr;
        } else {
            sumVPs = 15;
            matrikelNumber = "";
        }
        createListEntries(datesList, studiesList);
    }

    private void createListEntries(List<Map<String, Object>> datesList, List<Map<String, Object>> studiesList) {

        HashMap<String, HashMap<String, String>> datesMap = new HashMap<>();
        HashMap<String, HashMap<String, String>> studiesMap = new HashMap<>();

        //datesList is directly used from the repo! No such list in the viewModel!
        for (Map<String, Object> map : datesList) {
            String id = null;
            HashMap<String, String> tempMap = new HashMap<>();
            for (String key : map.keySet()) {
                if (key.equals("id")) {
                    id = map.get(key).toString();
                }
                Object value = map.get(key);
                if (value == null) {
                    tempMap.put(key, "null");
                } else {
                    tempMap.put(key, map.get(key).toString());
                }
            }
            if (id != null) {
                datesMap.put(id, tempMap);
            }
        }

        //studiesList is directly used from the repo! No such list in the viewModel!
        for (Map<String, Object> map : studiesList) {
            String id = null;
            HashMap<String, String> tempMap = new HashMap<>();
            for (String key : map.keySet()) {
                if (key.equals("id")) {
                    id = map.get(key).toString();
                }
                Object value = map.get(key);
                if (value == null) {
                    tempMap.put(key, "null");
                } else {
                    tempMap.put(key, map.get(key).toString());
                }
            }
            if (id != null) {
                studiesMap.put(id, tempMap);
            }
        }

        List<String> ownStudies = new ArrayList<>();
        List<String> passedStudies = new ArrayList<>();
        List<String> completedStudies = new ArrayList<>();

        for (String id : datesMap.keySet()) {
            HashMap<String, String> dateEntry = datesMap.get(id);
            if (dateEntry != null) {
                String dateString = dateEntry.get("date");
                String selected = dateEntry.get("selected");
                String userId = dateEntry.get("userId");
                boolean participated = Boolean.parseBoolean(dateEntry.get("participated"));
                String StudyId = dateEntry.get("studyId");

                if (Boolean.parseBoolean(selected) && userId != null) {
                    if (userId.equals(uniqueID)) {
                        HashMap<String, String> study = studiesMap.get(StudyId);
                        if (study != null) {
                            String studyNameString = study.get("name");
                            String studyVPSString = study.get("vps");
                            boolean studyIsClosed = Boolean.parseBoolean(study.get("studyStateClosed"));

                            if (participated) {
                                if (studyIsClosed) {
                                    completedStudies.add(studyNameString + ";" + studyVPSString + ";" + dateString + ";" + StudyId);
                                } else {
                                    passedStudies.add(studyNameString + ";" + studyVPSString + ";" + dateString + ";" + StudyId);
                                }
                            } else {
                                ownStudies.add(studyNameString + ";" + studyVPSString + ";" + dateString + ";" + StudyId);
                            }
                        }
                    }
                }
            }
        }
        //maybe clear needed
        //expandableListDetail.clear();
        expandableListDetail.put("Abgeschlossene Studien", completedStudies);
        expandableListDetail.put("Teilgenommene Studien", passedStudies); //=> teilgenommene Studien
        expandableListDetail.put("Geplante Studien", ownStudies);

        personalAccountFragment.setNewListViewAdapter(new CustomStudyListAdapter(personalAccountFragment.getContext(), personalAccountFragment.getNavController(), expandableListDetail));

        calculateProgressBarData();
    }

    private void calculateProgressBarData(){

        plannedVP = 0;
        completedVP = 0;
        participatedVP = 0;

        List<String> savedList = expandableListDetail.get("Abgeschlossene Studien");
        if (savedList != null) {
            for (int i = 0; i < savedList.size(); i++) {
                String vps = savedList.get(i).split(";")[1];
                double studyVPS = 0;
                if (vps != null && !vps.equals(""))
                    studyVPS = Double.parseDouble(vps);
                completedVP += studyVPS;
            }
        }

        List<String> vpList = expandableListDetail.get("Geplante Studien");
        if (vpList != null) {
            for (int i = 0; i < vpList.size(); i++) {
                String vps = vpList.get(i).split(";")[1];
                double studyVPS = 0;
                if (vps != null && !vps.equals(""))
                    studyVPS = Double.parseDouble(vps);
                plannedVP += studyVPS;
            }
        }
        List<String> passedVpList = expandableListDetail.get("Teilgenommene Studien");
        if (passedVpList != null) {
            for (int i = 0; i < passedVpList.size(); i++) {
                String vps = passedVpList.get(i).split(";")[1];
                double studyVPS = 0;
                if (vps != null && !vps.equals(""))
                    studyVPS = Double.parseDouble(vps);
                participatedVP += studyVPS;
            }
        }

        //calculate the remainingVP
        int max = (int) (sumVPs * 100);
        int scaledCompletedVP = (int) completedVP * 100;
        int scaledParticipationVP = (int) participatedVP * 100;
        int scaledPlannedVP = (int) plannedVP * 100;
        int remainingVP = max - (scaledCompletedVP + scaledParticipationVP + scaledPlannedVP);
        if (remainingVP < 0) {
            remainingVP = 0;
        }

        String completedString = "Erledigt: " + completedVP + " VP";
        String participatedString = "Teilgenommen: " + participatedVP + " VP";
        String plannedString = "Geplant: " + plannedVP + " VP";
        String remainingString = "Übrig: " + remainingVP / 100 + " VP";
        float weightSum = sumVPs*100;

        personalAccountFragment.setProgressBarStrings(completedString, participatedString, plannedString, remainingString, weightSum);

        LinearLayout.LayoutParams paramCompleted = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT,
                (float) completedVP*100
        );
        LinearLayout.LayoutParams paramParticipated = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT,
                (float) participatedVP*100
        );
        LinearLayout.LayoutParams paramPlanned = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT,
                (float) plannedVP*100
        );
        LinearLayout.LayoutParams paramRest = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT,
                (float) (sumVPs - (completedVP + participatedVP + plannedVP))*100
        );

        setInitialStateOfFilter();
        personalAccountFragment.setProgressBarParts(paramCompleted, paramParticipated, paramPlanned, paramRest);
    }

    private void setInitialStateOfFilter(){
        //initial state of the filter
        sortAlphabeticallyActive = false;
        sortAppointmentsActive = false;
        sortVpCountActive = false;
        sortAlphabeticallyInvert = false;
        sortAppointmentsInvert = false;
        sortVpCountInvert = false;
    }

    public void filterListViewTextTags (String type) {
        switch (type) {
            case "names":
                if (!sortAlphabeticallyInvert && !sortAlphabeticallyActive) {
                    sortAlphabeticallyInvert = false;
                    sortAlphabeticallyActive = !sortAlphabeticallyActive;
                } else if (sortAlphabeticallyActive && !sortAlphabeticallyInvert) {
                    sortAlphabeticallyInvert = true;
                    sortAlphabeticallyActive = true;
                } else {
                    sortAlphabeticallyInvert = false;
                    sortAlphabeticallyActive = false;
                }
                personalAccountFragment.setAlphabeticallyToggle("names", sortAlphabeticallyActive, sortAlphabeticallyInvert);

                if (sortAppointmentsActive) {
                    //first change color with correct boolean, then reset the boolean afterwards!
                    personalAccountFragment.setAppointmentsToggle("names", sortAppointmentsActive, sortAppointmentsInvert);
                    sortAppointmentsActive = false;
                }
                if (sortVpCountActive) {
                    //first change color with correct boolean, then reset the boolean afterwards!
                    personalAccountFragment.setVpToggle("names", sortVpCountActive, sortVpCountInvert);
                    sortVpCountActive = false;
                }
                sortAppointmentsInvert = false;
                sortVpCountInvert = false;
                break;
            case "dates":
                if (!sortAppointmentsInvert && !sortAppointmentsActive) {
                    sortAppointmentsInvert = false;
                    sortAppointmentsActive = !sortAppointmentsActive;
                } else if (sortAppointmentsActive && !sortAppointmentsInvert) {
                    sortAppointmentsInvert = true;
                    sortAppointmentsActive = true;
                } else {
                    sortAppointmentsInvert = false;
                    sortAppointmentsActive = false;
                }
                personalAccountFragment.setAppointmentsToggle("dates", sortAppointmentsActive, sortAppointmentsInvert);

                if (sortVpCountActive) {
                    //first change color with correct boolean, then reset the boolean afterwards!
                    personalAccountFragment.setVpToggle("dates", sortVpCountActive, sortVpCountInvert);
                    sortVpCountActive = false;
                }
                if (sortAlphabeticallyActive) {
                    //first change color with correct boolean, then reset the boolean afterwards!
                    personalAccountFragment.setAlphabeticallyToggle("dates", sortAlphabeticallyActive, sortAlphabeticallyInvert);
                    sortAlphabeticallyActive = false;
                }
                sortAlphabeticallyInvert = false;
                sortVpCountInvert = false;
                break;
            case "vps":
                if (!sortVpCountInvert && !sortVpCountActive) {
                    sortVpCountInvert = false;
                    sortVpCountActive = !sortVpCountActive;
                } else if (sortVpCountActive && !sortVpCountInvert) {
                    sortVpCountInvert = true;
                    sortVpCountActive = true;
                } else {
                    sortVpCountInvert = false;
                    sortVpCountActive = false;
                }
                personalAccountFragment.setVpToggle("vps", sortVpCountActive, sortVpCountInvert);

                if (sortAlphabeticallyActive) {
                    //first change color with correct boolean, then reset the boolean afterwards!
                    personalAccountFragment.setAlphabeticallyToggle("vps", sortAlphabeticallyActive, sortAlphabeticallyInvert);
                    sortAlphabeticallyActive = false;
                }
                if (sortAppointmentsActive) {
                    //first change color with correct boolean, then reset the boolean afterwards!
                    personalAccountFragment.setAppointmentsToggle("vps", sortAppointmentsActive, sortAppointmentsInvert);
                    sortAppointmentsActive = false;
                }
                sortAlphabeticallyInvert = false;
                sortAppointmentsInvert = false;
                break;
            default:
                break;
        }
        System.out.println("Nach Vp normal: " + sortVpCountActive);
        System.out.println("Nach Vp inverted: " + sortVpCountInvert);
        filterListViewTextContent();
    }

    private void filterListViewTextContent() {

        if(!sortAlphabeticallyActive && !sortAppointmentsActive && !sortVpCountActive) {
            personalAccountFragment.applyColorFilter();
        }
        else {
            if (sortAlphabeticallyActive) {
                personalAccountFragment.setNewListViewAdapter(new CustomStudyListAdapter(personalAccountFragment.getContext(),
                        personalAccountFragment.getNavController(), sortByName(sortAlphabeticallyInvert))); //this.getActivity(),
            } else if (sortAppointmentsActive) {
                personalAccountFragment.setNewListViewAdapter(new CustomStudyListAdapter(personalAccountFragment.getContext(),
                        personalAccountFragment.getNavController(), sortByDate(sortAppointmentsInvert))); //this.getActivity(),
            } else if (sortVpCountActive) {
                personalAccountFragment.setNewListViewAdapter(new CustomStudyListAdapter(personalAccountFragment.getContext(),
                        personalAccountFragment.getNavController(), sortByVPS(sortVpCountInvert))); //this.getActivity(),
            }
        }
    }

    public void filterListViewColorTags(boolean completedState, boolean plannedState, boolean participatedState) {

        CustomStudyListAdapter adapter = new CustomStudyListAdapter(personalAccountFragment.getContext(), personalAccountFragment.getNavController(), expandableListDetail); //this.getActivity(),

        ArrayList<StudyObjectPa> newList = new ArrayList<>();
        ArrayList<StudyObjectPa> currentList = adapter.mStudyMetaInfos;

        if(!(completedState && plannedState && participatedState)) {
            for (StudyObjectPa ob : currentList) {

                switch (ob.getColor()) {
                    case R.color.pieChartSafe:
                        if (completedState)
                            newList.add(ob);
                        break;
                    case R.color.pieChartParticipation:
                        if (participatedState)
                            newList.add(ob);
                        break;
                    case R.color.pieChartPlanned:
                        if (plannedState)
                            newList.add(ob);
                        break;
                }
            }
            adapter = new CustomStudyListAdapter(personalAccountFragment.getContext(), personalAccountFragment.getNavController(), newList);
            personalAccountFragment.setNewListViewAdapter(adapter);
        }
        else
        {
            personalAccountFragment.setNewListViewAdapter(adapter);
        }

    }


    private ArrayList<StudyObjectPa> sortByName(boolean invert) {
        CustomStudyListAdapter adapter = personalAccountFragment.getCurrentAdapter();
        ArrayList<StudyObjectPa> list = new ArrayList<>();
        for (int i = 0; i < adapter.mStudyMetaInfos.size(); i++) {
            if (adapter.mStudyMetaInfos.get(i) != null) {               //listView != null &&  warum sollte liste null sein?
                StudyObjectPa item = adapter.mStudyMetaInfos.get(i);
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
                if (studyList[k].getTitle().compareToIgnoreCase(studyList[k + 1].getTitle()) > 0) {
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

    private ArrayList<StudyObjectPa> sortByDate(boolean invert) {
        CustomStudyListAdapter adapter = personalAccountFragment.getCurrentAdapter();
        ArrayList<StudyObjectPa> list = new ArrayList<>();
        for (int i = 0; i < adapter.mStudyMetaInfos.size(); i++) {
            if (adapter.mStudyMetaInfos.get(i) != null) {             //listView != null && warum sollte list null sein?
                StudyObjectPa item = adapter.mStudyMetaInfos.get(i);
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

    private ArrayList<StudyObjectPa> sortByVPS(boolean invert) {
        CustomStudyListAdapter adapter = personalAccountFragment.getCurrentAdapter();
        ArrayList<StudyObjectPa> list = new ArrayList<>();
        for (int i = 0; i < adapter.mStudyMetaInfos.size(); i++) {
            if (adapter.mStudyMetaInfos.get(i) != null) {               //listView != null &&  warum sollte list null sein?
                StudyObjectPa item = adapter.mStudyMetaInfos.get(i);
                list.add(item);
            }
        }
        StudyObjectPa[] studyList = new StudyObjectPa[list.size()];
        for (int i = 0; i < list.size(); i++) {
            studyList[i] = list.get(i);
        }

        for (int i = 0; i < studyList.length; i++) {
            for (int k = 0; k < studyList.length - 1; k++) {

                String first_number = studyList[k].getVps().replace("VP-Stunden","").trim();
                String second_number = studyList[k+1].getVps().replace("VP-Stunden","").trim();

                if (Float.parseFloat(first_number) > Float.parseFloat(second_number)) {
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
