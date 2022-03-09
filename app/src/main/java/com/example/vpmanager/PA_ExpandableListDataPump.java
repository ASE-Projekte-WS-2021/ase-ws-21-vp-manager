package com.example.vpmanager;

import static com.example.vpmanager.views.mainActivity.uniqueID;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PA_ExpandableListDataPump extends Activity {

    public static FirebaseFirestore db;
    public static CollectionReference datesRef;
    public static CollectionReference studiesRef;
    public static final HashMap<String, List<String>> EXPANDABLE_LIST_DETAIL = new HashMap<>();

    public static final List<Map<String, Object>> dbDatesList = new ArrayList<>();
    public static final List<Map<String, Object>> dbStudiesList = new ArrayList<>();

    //Parameters
    //Return Values:
    //this function saves all Dates from DB to list
    public static void getAllDates(FirestoreCallbackDates firestoreCallbackDates) {
        db = FirebaseFirestore.getInstance();
        datesRef = db.collection("dates");
        datesRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                dbDatesList.add(document.getData());
                            }
                            firestoreCallbackDates.onCallback(true);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        firestoreCallbackDates.onCallback(false);
                    }
                });
    }

    //Parameters
    //Return Values:
    //this function saves all Studie Documents from DB to static List
    public static void getAllStudies(FirestoreCallbackStudy firestoreCallbackStudies) {
        db = FirebaseFirestore.getInstance();
        studiesRef = db.collection("studies");
        studiesRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                dbStudiesList.add(document.getData());
                                createListEntries();
                            }
                            firestoreCallbackStudies.onCallback();
                        }
                    }
                });
    }

    //Parameters
    //Return Values:
    //this function uses the date list and checks in which the users id is marked. By doing a check with the study list the name and vp count are found additionally to the date
    //The List is than safed in a static variable to make it accessable from all other fragments
    public static void createListEntries() {

        HashMap<String, HashMap<String, String>> datesMap = new HashMap<>();
        HashMap<String, HashMap<String, String>> studiesMap = new HashMap<>();

        for (Map<String, Object> map : dbDatesList) {
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

        for (Map<String, Object> map : dbStudiesList) {
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

        for (String id : datesMap.keySet()) {
            HashMap<String, String> dateEntry = datesMap.get(id);
            if (dateEntry != null) {
                String dateString = dateEntry.get("date");
                String selected = dateEntry.get("selected");
                String userId = dateEntry.get("userId");

                String StudyId = dateEntry.get("studyId");

                if (Boolean.parseBoolean(selected) && userId != null) {
                    if (userId.equals(uniqueID)) {
                        HashMap<String, String> study = studiesMap.get(StudyId);
                        if (study != null) {
                            String studyNameString = study.get("name");
                            String studyVPSString = study.get("vps");

                            if(isDateInPast(dateString))
                            {
                                passedStudies.add(studyNameString + "," + studyVPSString + "," + dateString + "," + StudyId);
                            }
                            else
                                ownStudies.add(studyNameString + "," + studyVPSString + "," + dateString + "," + StudyId);
                        }
                    }
                }
            }
        }

        EXPANDABLE_LIST_DETAIL.put("Vergangene Studien", passedStudies);
        EXPANDABLE_LIST_DETAIL.put("Geplante Studien", ownStudies);
    }


    //Parameters: string date
    //Return Values: boolean to tell if a date is in the future or past
    //this function parses a given string to a date and checks if the date is already in the past, or in the future
    private static boolean isDateInPast(String date) {
        Date currentTime = Calendar.getInstance().getTime();

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        try {
            Date meetingDate = format.parse(date);

            if (currentTime.after(meetingDate))
                return true;
            else
                return false;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }





    public interface FirestoreCallbackDates {
        void onCallback(boolean finished);
    }

    public interface FirestoreCallbackStudy {
        void onCallback();
    }
}