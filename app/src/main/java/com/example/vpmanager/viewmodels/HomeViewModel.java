package com.example.vpmanager.viewmodels;

import static com.example.vpmanager.views.mainActivity.uniqueID;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.vpmanager.Config;
import com.example.vpmanager.adapter.CustomListViewAdapterAppointments;
import com.example.vpmanager.interfaces.GetAllDatesListener;
import com.example.vpmanager.interfaces.GetAllStudiesListener;
import com.example.vpmanager.interfaces.GetVpAndMatNrListener;
import com.example.vpmanager.repositories.HomeRepository;
import com.example.vpmanager.views.HomeFragment;

import java.io.IOException;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HomeViewModel extends ViewModel implements GetAllDatesListener, GetAllStudiesListener, GetVpAndMatNrListener {

    //also saved in viewModel to filter arrivingDates later
    private List<Map<String, Object>> dbDatesListHome = new ArrayList<>();
    private List<Map<String, Object>> dbStudiesListHome = new ArrayList<>();

    public HashMap<String, List<String>> expandableListDetailHome = new HashMap<>();

    public HomeFragment homeFragment;
    private HomeRepository mHomeRepo;

    private float sumVPsHome;
    private String matrikelNumberHome, jsonString;
    private double plannedVP, completedVP, participatedVP;


    //Parameter:
    //Return values:
    //Gets instance of the homeRepository and sets Firestore Callback
    public void prepareRepo() {
        mHomeRepo = HomeRepository.getInstance();
        //Instance is the same but different data can be retrieved
        mHomeRepo.setFirestoreCallback(this, this, this);
    }

    public void getDatesStudiesVpsAndMatrikelNumberFromDb() {
        //dates is the starting point. other db calls are made from its callback!
        mHomeRepo.getAllDatesFromDb();
    }

    public void fetchAllStudiesFromDb() {
        mHomeRepo.getAllStudiesFromDb();
    }

    public void fetchVpAndMatrikelNumber() {
        mHomeRepo.getVpAndMatrikelNumber();
    }

    @Override
    public void onAllDatesReady(Boolean success) {
        if (success) {
            fetchAllStudiesFromDb();
        } else {
            Log.d("HomeViewModel", "Getting all dates from DB failed!");
        }
    }

    @Override
    public void onAllStudiesReady(Boolean success) {
        if (success) {
            fetchVpAndMatrikelNumber();
        } else {
            Log.d("HomeViewModel", "Getting all studies from DB failed!");
        }
    }

    @Override
    public void onAllDataReady(String vpsHome, String matNrHome,
                               List<Map<String, Object>> datesListHome, List<Map<String, Object>> studiesListHome) {
        if (vpsHome != null && matNrHome != null && !vpsHome.equals("") && !matNrHome.equals("")) {
            sumVPsHome = Float.parseFloat(vpsHome);
            matrikelNumberHome = matNrHome;
        } else {
            sumVPsHome = Config.vpSum;
            matrikelNumberHome = "";
        }
        //copy lists from repo
        dbDatesListHome = datesListHome;
        dbStudiesListHome = studiesListHome;
        createListEntriesHome();
    }


    //Parameter:
    //Return values:
    //Loads the list entries for the home fragment in HashMap
    private void createListEntriesHome() {

        HashMap<String, HashMap<String, String>> datesMap = new HashMap<>();
        HashMap<String, HashMap<String, String>> studiesMap = new HashMap<>();

        //datesList is directly used from the repo
        for (Map<String, Object> map : dbDatesListHome) {
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

        //studiesList is directly used from the repo
        for (Map<String, Object> map : dbStudiesListHome) {
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
        expandableListDetailHome.put("Abgeschlossene Studien", completedStudies);
        expandableListDetailHome.put("Teilgenommene Studien", passedStudies);
        expandableListDetailHome.put("Geplante Studien", ownStudies);

        setupProgressBar();
    }


    //Parameter:
    //Return values:
    //Sets up the progress bar for the user specific data
    private void setupProgressBar() {

        int maximum = (int) sumVPsHome * Config.progressBarMultiplicator;
        homeFragment.setProgressBarMaximum(maximum);

        //starts Thread to get infos from internet
        if (!matrikelNumberHome.isEmpty()) {
            Thread getRequest = new Thread(() -> {
                try {
                    jsonString = mHomeRepo.createGetRequest(matrikelNumberHome);

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
                String[] entries = jsonString.split(",");
                if (entries.length == 3) {
                    String VPS = entries[Config.listEntryIndexOne].split(":")[Config.listEntryIndexOne];
                    float vpCount = Float.parseFloat(VPS);
                    String progressText = vpCount + "/" + (int) sumVPsHome;
                    if (vpCount > sumVPsHome) {
                        int progress = (int) sumVPsHome * Config.progressBarMultiplicator;
                        homeFragment.setProgressBarProgress(progress, progressText);
                    } else {
                        int progress = (int) vpCount * Config.progressBarMultiplicator;
                        homeFragment.setProgressBarProgress(progress, progressText);
                    }
                }
            }
        } else {
            homeFragment.hideProgressBar();
        }

        setupDatesList();
    }


    //Parameter:
    //Return values:
    //Sets up the list for user specific upcoming appointments
    private void setupDatesList() {
        final List<String[]>[] arrivingDates = new List[]{null};
        arrivingDates[Config.listEntryIndexZero] = getAllArrivingDates();
        finishSetupList(arrivingDates[Config.listEntryIndexZero]);
    }

    //Parameter:
    //Return values: List<String[]>
    //Receives all upcoming appointments
    private List<String[]> getAllArrivingDates() {
        HashMap<String, String> studyIdList = new HashMap<>();

        List<String[]> arrivingDates = new ArrayList<>();

        for (Map<String, Object> map : dbDatesListHome) {
            boolean saveDate = false;
            String studyId = null;
            String userID = null;
            String date = null;
            for (String key : map.keySet()) {
                if (key.equals("studyId")) {
                    studyId = Objects.requireNonNull(map.get(key)).toString();
                }
                if (key.equals("date")) {
                    date = Objects.requireNonNull(map.get(key)).toString();
                }
                if (key.equals("userId")) {
                    if (map.get(key) != null) {
                        userID = map.get(key).toString();
                        if (userID.equals(uniqueID)) {
                            saveDate = true;
                        }
                    }
                }
            }
            if (saveDate && studyId != null && date != null && !isDateInPast(date)) {
                studyIdList.put(studyId, date);
            }
        }

        for (Map<String, Object> map : dbStudiesListHome) {
            boolean getDate = false;
            String studyID = null;
            String creator = null;
            for (String key : map.keySet()) {
                if (key.equals("creator")) {
                    creator = map.get(key).toString();
                    if (creator.equals(uniqueID)) {
                        getDate = true;
                    }
                }
                if (key.equals("id")) {
                    studyID = map.get(key).toString();
                }
            }
            if (getDate && studyID != null) {
                for (Map<String, Object> datemap : dbDatesListHome) {
                    boolean saveDate = false;
                    boolean dateBooked = false;
                    String date = null;
                    for (String key : datemap.keySet()) {
                        if (key.equals("studyId")) {
                            String dateStudyId = datemap.get(key).toString();
                            if (dateStudyId.equals(studyID)) {
                                saveDate = true;
                            }
                        }
                        if (key.equals("date")) {
                            date = datemap.get(key).toString();
                        }
                        if (key.equals("selected")) {
                            dateBooked = Boolean.parseBoolean(datemap.get(key).toString());
                        }
                    }
                    if (saveDate && dateBooked && date != null) {
                        studyIdList.put(studyID, date);
                    }
                }
            }
        }

        for (String key : studyIdList.keySet()) {
            String studyName = null;
            String date = studyIdList.get(key);

            for (Map<String, Object> map : dbStudiesListHome) {
                if (map.get("id").toString().equals(key)) {
                    if (map.get("name").toString() != null) {
                        studyName = map.get("name").toString();
                    }
                }
            }

            if (studyName != null) {
                String[] listEntry = new String[Config.listEntryIndexThree];
                listEntry[Config.listEntryIndexZero] = studyName;
                listEntry[Config.listEntryIndexOne] = date;
                listEntry[Config.listEntryIndexTwo] = key;

                arrivingDates.add(listEntry);
            }
        }
        return arrivingDates;
    }


    //Parameter:
    //Return values: List<String[]>
    //Receives the date of the users last participation
    private String getLastParticipationDate() {
        ArrayList<String> datesList = new ArrayList<>();

        for (Map<String, Object> map : dbDatesListHome) {
            boolean saveDate = false;
            String studyId = null;
            String userID = null;
            String date = null;
            for (String key : map.keySet()) {
                if (key.equals("date")) {
                    date = Objects.requireNonNull(map.get(key)).toString();
                }
                if (key.equals("userId")) {
                    if (map.get(key) != null) {
                        userID = map.get(key).toString();
                        if (userID.equals(uniqueID)) {
                            saveDate = true;
                        }
                    }
                }
            }
            if (saveDate && date != null && !isDateInPast(date)) {
                datesList.add(date);
            }
        }

        String[] studyList = new String[datesList.size()];
        for (int i = 0; i < datesList.size(); i++) {
            studyList[i] = datesList.get(i);
        }

        for (int i = 0; i < studyList.length; i++) {
            for (int k = 0; k < studyList.length - 1; k++) {

                String date1 = studyList[k].substring(studyList[k].indexOf(",") + 2);
                String date2 = studyList[k + 1].substring(studyList[k + 1].indexOf(",") + 2);

                date1 = date1.replaceAll("um", "");
                date1 = date1.replaceAll("Uhr", "");
                date2 = date2.replaceAll("um", "");
                date2 = date2.replaceAll("Uhr", "");

                Format format = new SimpleDateFormat("dd.MM.yyyy hh:mm");
                try {

                    Date d1_Date = (Date) format.parseObject(date1);
                    Date d2_Date = (Date) format.parseObject(date2);

                    if (d2_Date.before(d1_Date)) {
                        String tempString = studyList[k];
                        studyList[k] = studyList[k + 1];
                        studyList[k + 1] = tempString;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        datesList.clear();
        for (String s : studyList) {
            datesList.add(s);
        }
        if (datesList.size() > 0)
            return datesList.get(0);
        return null;
    }


    //Parameter:
    //Return values: List<String[]>
    //Completes the list setup; finalizes values
    private void finishSetupList(List<String[]> dates) {
        HashMap<String, String> getStudyIdByName = new HashMap<>();

        ArrayList<String> upComingAppointments = new ArrayList<>();

        if (dates != null) {

            HashMap<String, String> dateNameList = new HashMap<>();

            for (String[] listEntry : dates) {
                String name = listEntry[Config.listEntryIndexZero];
                String date = listEntry[Config.listEntryIndexOne];
                String studyID = listEntry[Config.listEntryIndexTwo];

                if (date != null && name != null) {
                    dateNameList.put(date, name);
                    getStudyIdByName.put(name, studyID);
                }
            }

            ArrayList<String> tempList = sortList(dateNameList);

            int studyDisplayCount = 2;

            if (tempList.size() < 3) {
                homeFragment.disableAllAppointmentsButton();
                studyDisplayCount = 3;
            }

            for (String date : tempList) {
                if (upComingAppointments.size() < studyDisplayCount) {
                    upComingAppointments.add(date);
                }
            }
            homeFragment.setListViewAdapter(new CustomListViewAdapterAppointments(homeFragment.getContext(),
                    homeFragment.getNavController(), upComingAppointments, getStudyIdByName, "HomeFragment"));

        }
        setTextsForOverView();
    }


    //Parameter:
    //Return values:
    //Sets the text for the view descriptions
    private void setTextsForOverView() {

        completedVP = 0;
        participatedVP = 0;
        plannedVP = 0;

        List<String> savedList = expandableListDetailHome.get("Abgeschlossene Studien");
        if (savedList != null) {
            for (int i = 0; i < savedList.size(); i++) {
                String vps = savedList.get(i).split(";")[Config.listEntryIndexOne];
                double studyVPS = 0;
                if (vps != null && !vps.equals(""))
                    studyVPS = Double.parseDouble(vps);
                completedVP += studyVPS;
            }
        }
        List<String> vpList = expandableListDetailHome.get("Geplante Studien");
        if (vpList != null) {
            for (int i = 0; i < vpList.size(); i++) {
                String vps = vpList.get(i).split(";")[Config.listEntryIndexOne];
                double studyVPS = 0;
                if (vps != null && !vps.equals(""))
                    studyVPS = Double.parseDouble(vps);
                plannedVP += studyVPS;
            }
        }
        List<String> passedVpList = expandableListDetailHome.get("Teilgenommene Studien");
        if (passedVpList != null) {
            for (int i = 0; i < passedVpList.size(); i++) {
                String vps = passedVpList.get(i).split(";")[Config.listEntryIndexOne];
                double studyVPS = 0;
                if (vps != null && !vps.equals(""))
                    studyVPS = Double.parseDouble(vps);
                participatedVP += studyVPS;
            }
        }

        float collectedVPS = (float) (completedVP + participatedVP + plannedVP);

        float missingVPS = (float) sumVPsHome - (float) (completedVP + participatedVP + plannedVP);
        String missing;
        if (missingVPS < 0) {
            missing = "0";
        } else {
            missing = "" + missingVPS;
        }
        homeFragment.setMissingAndCollectedVpText(missing, Float.toString(collectedVPS));

        setLastAppointmentText();
    }


    //Parameter:
    //Return values:
    //Sets the text for the last participated appointment
    private void setLastAppointmentText() {
        String date = getLastParticipationDate();
        if (date == null) {
            homeFragment.loadDefaultorEmptyVersion();
        } else {
            homeFragment.setPlannedCompletionDate(date);
        }
    }


    //Parameter: date
    //Return values: boolean
    //Checks if date has already expired
    private boolean isDateInPast(String date) { //static?

        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        String currentDate = day + "." + (month + 1) + "." + year;
        //
        Date currentTime = Calendar.getInstance().getTime();

        Pattern pattern = Pattern.compile("\\d\\d:\\d\\d:\\d\\d");
        Matcher matcher = pattern.matcher(currentTime.toString());
        if (matcher.find()) {
            currentDate += " " + matcher.group(0);
            currentDate = currentDate.substring(0, currentDate.lastIndexOf(":"));
        }

        String testDate = date.substring(date.indexOf(",") + 2);
        testDate = testDate.replaceAll("um", "");
        testDate = testDate.replaceAll("Uhr", "");

        Format format = new SimpleDateFormat("dd.MM.yyyy hh:mm");
        try {

            Date c_Date = (Date) format.parseObject(currentDate);
            Date t_Date = (Date) format.parseObject(testDate);

            if (t_Date.before(c_Date)) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }


    //Parameter: HashMap<String, toSort
    //Return values: ArrayList<String>
    //Sorts the list elements chronologically
    private ArrayList<String> sortList(HashMap<String, String> toSort) {
        ArrayList<String> list = new ArrayList<>();

        String[][] dateList = new String[toSort.size()][Config.listEntryIndexTwo];
        int position = 0;
        for (String key : toSort.keySet()) {
            dateList[position][Config.listEntryIndexZero] = key;
            dateList[position][Config.listEntryIndexTwo] = toSort.get(key);
            position++;
        }

        for (int i = 0; i < dateList.length; i++) {
            for (int k = 0; k < dateList.length - 1; k++) {

                String date1 = dateList[k][Config.listEntryIndexZero].substring(dateList[k][0].indexOf(",") + 2);
                String date2 = dateList[k + 1][Config.listEntryIndexZero].substring(dateList[k + 1][Config.listEntryIndexZero].indexOf(",") + 2);

                date1 = date1.replaceAll("um", "");
                date1 = date1.replaceAll("Uhr", "");
                date2 = date2.replaceAll("um", "");
                date2 = date2.replaceAll("Uhr", "");

                Format format = new SimpleDateFormat("dd.MM.yyyy hh:mm");
                try {

                    Date d1_Date = (Date) format.parseObject(date1);
                    Date d2_Date = (Date) format.parseObject(date2);

                    if (d2_Date.before(d1_Date)) {
                        String[] tempDate = dateList[k];
                        dateList[k] = dateList[k + 1];
                        dateList[k + 1] = tempDate;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        list.clear();
        for (String[] date : dateList) {
            list.add(date[Config.listEntryIndexOne] + "\t\t" + date[Config.listEntryIndexZero]);
        }

        return list;
    }


    //Parameter: HashMap<String, toSort
    //Return values: ArrayList<String>
    //Saves the user associated VP data and matrikelnumber; updates the current data first and then saves to the database.
    public void saveVpAndMatrikelNumber(String vps, String mNumber) {
        sumVPsHome = Float.parseFloat(vps);
        matrikelNumberHome = mNumber;
        mHomeRepo.saveVpAndMatrikelNumber(vps, mNumber);
    }
}
