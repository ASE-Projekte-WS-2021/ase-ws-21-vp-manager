package com.example.vpmanager.viewmodels;

import static com.example.vpmanager.views.mainActivity.uniqueID;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.vpmanager.adapter.CustomListViewAdapterAppointments;
import com.example.vpmanager.interfaces.GetAllDatesListener;
import com.example.vpmanager.interfaces.GetAllStudiesListener;
import com.example.vpmanager.repositories.HomeRepository;
import com.example.vpmanager.views.UpcomingAppointmentsFragment;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpcomingAppointViewModel extends ViewModel implements GetAllDatesListener, GetAllStudiesListener {

    private List<Map<String, Object>> dbDatesListUpAppoint = new ArrayList<>();
    private List<Map<String, Object>> dbStudiesListUpAppoint = new ArrayList<>();

    private List<String[]>[] arrivingDates = new List[]{null};  //final?

    private HashMap<String, String> getStudyIdByName = new HashMap<>();

    //new viewModel but same repo as homeFragment
    public UpcomingAppointmentsFragment upcomingAppointmentsFragment;
    private HomeRepository mHomeRepo;

    public void prepareRepo() {
        mHomeRepo = HomeRepository.getInstance();
        //Instance is the same but different data can be retrieved!
        mHomeRepo.setFirestoreCallbackUpAppoint(this, this);
    }

    public void getAllDatesAndStudies() {
        //dates is starting point, studies follow in the callback
        mHomeRepo.getAllDatesFromDb();
    }

    public void getAllStudies() {
        mHomeRepo.getAllStudiesFromDb();
    }

    @Override
    public void onAllDatesReady(Boolean success) {
        if (success) {
            getAllStudies();
        } else {
            Log.d("UpAppointViewModel", "Getting all dates from DB failed!");
        }
    }

    @Override
    public void onAllStudiesReady(Boolean success) {
        if (success) {
            dbDatesListUpAppoint = mHomeRepo.getDbDatesList();
            dbStudiesListUpAppoint = mHomeRepo.getDbStudiesList();

            arrivingDates[0] = getAllArrivingDates();
            finishSetupList(arrivingDates[0]);
        } else {
            Log.d("UpAppointViewModel", "Getting all studies from DB failed!");
        }
    }

    private List<String[]> getAllArrivingDates() {   //static?
        HashMap<String, String> studyIdList = new HashMap<>();

        List<String[]> arrivingDates = new ArrayList<>();

        for (Map<String, Object> map : dbDatesListUpAppoint) {
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

        for (Map<String, Object> map : dbStudiesListUpAppoint) {
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
                for (Map<String, Object> datemap : dbDatesListUpAppoint) {
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

            for (Map<String, Object> map : dbStudiesListUpAppoint) {
                if (map.get("id").toString().equals(key)) {
                    if (map.get("name").toString() != null) {
                        studyName = map.get("name").toString();
                    }
                }
            }

            if (studyName != null) {
                String[] listEntry = new String[3];
                listEntry[0] = studyName;
                listEntry[1] = date;
                listEntry[2] = key;

                arrivingDates.add(listEntry);
            }
        }
        return arrivingDates;
    }

    private void finishSetupList(List<String[]> dates) {

        if (dates != null) {

            ArrayList<String> listEntries = new ArrayList<>();
            HashMap<String, String> sortingMap = new HashMap<>();
            for (String[] listEntry : dates) {
                String name = listEntry[0];
                String date = listEntry[1];
                String studyID = listEntry[2];

                if (date != null && name != null) {
                    sortingMap.put(date, name);
                    getStudyIdByName.put(name, studyID);
                }
            }
            for (String key : sortingMap.keySet()) {
                listEntries.add(sortingMap.get(key) + "\t\t" + key);
            }
            Collections.reverse(listEntries);

            upcomingAppointmentsFragment.setListViewAdapter(new CustomListViewAdapterAppointments(
                    upcomingAppointmentsFragment.getContext(), upcomingAppointmentsFragment.getNavController(),
                    listEntries, getStudyIdByName, "UpcomingAppointmentsFragment"));
        }
    }

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
}
