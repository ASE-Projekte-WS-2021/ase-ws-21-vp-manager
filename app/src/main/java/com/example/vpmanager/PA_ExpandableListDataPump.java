package com.example.vpmanager;

import static android.content.ContentValues.TAG;
import static com.example.vpmanager.views.mainActivity.uniqueID;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.vpmanager.views.StudyFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class PA_ExpandableListDataPump extends Activity {

    public static FirebaseFirestore db;
    public static CollectionReference datesRef;
    public static CollectionReference studiesRef;
    public static final HashMap<String, List<String>> EXPANDABLE_LIST_DETAIL = new HashMap<>();

    public static final List<Map<String, Object>> DB_DATES_LIST = new ArrayList<>();
    public static final List<Map<String, Object>> DB_STUDIES_LIST = new ArrayList<>();

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
                                DB_DATES_LIST.add(document.getData());
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
                                DB_STUDIES_LIST.add(document.getData());
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

        for (Map<String, Object> map : DB_DATES_LIST) {
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

        for (Map<String, Object> map : DB_STUDIES_LIST) {
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

    //Parameters
    //Return Values
    //this function iterates over the date List and finds all dates that the user has selected. Then it adds all dates belonging to studies which the user created, if said date is selected by another user
    public static List<String[]> getAllArrivingDates() {
        HashMap<String, String> studyIdList = new HashMap<>();

        List<String[]> arrivingDates = new ArrayList<>();

        for (Map<String, Object> map : DB_DATES_LIST) {
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
                    if(map.get(key) != null) {
                        userID = map.get(key).toString();
                        if (userID.equals(uniqueID)) {
                            saveDate = true;
                        }
                    }
                }
            }
            if (saveDate && studyId != null && date != null) {
                studyIdList.put(studyId, date);
            }
        }

        for (Map<String, Object> map : DB_STUDIES_LIST) {
            List<String> dateList = new ArrayList<>();
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
                for (Map<String, Object> datemap : DB_DATES_LIST) {
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

            for (Map<String, Object> map : DB_STUDIES_LIST) {
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


    //Parameters: Dataset to update, identifier for entry in database
    //Return Values
    //updates a given with the given dataset
    public static void updateStudyInDataBase(Map<String, Object> updateData, String studyID) {

        db = FirebaseFirestore.getInstance();

        DocumentReference ref =db.collection("studies").document(studyID);

        for(String key: updateData.keySet())
        {
               ref.update(key, updateData.get(key).toString())
                .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully updated!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
        }
    }


    //Parameters: identifier of user, identifier of study
    //Return Values
    //checks if a given user is the creator of a given study
    public static boolean navigateToStudyCreatorFragment(String currentUserId, String currentStudyId) {

        if(DB_STUDIES_LIST.size() <= 0)
        {
            new Thread(() -> getAllStudies(() -> {       })).start();

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (Map<String, Object> map : DB_STUDIES_LIST)
        {
            if (Objects.requireNonNull(map.get("creator")).toString().equals(currentUserId) &&
                    Objects.requireNonNull(map.get("id")).toString().equals(currentStudyId))
            {
                    return true;
            }
        }
        return false;
    }

    //Parameters
    //Return Values
    //searches the current user in database and gets the VP count and the matrikelnumber
    public static void getVPandMatrikelnumber(FirestoreCallbackUser firestoreCallbackUser)
    {
        db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");
        usersRef.whereEqualTo("deviceId", uniqueID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    String vps= "", matrikelNumber = "";
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        vps = document.getString("vps");
                        matrikelNumber = document.getString("matrikelNumber");
                    }
                    firestoreCallbackUser.onCallback(vps, matrikelNumber);
                }
            }
        }).addOnFailureListener(e -> firestoreCallbackUser.onCallback("",""));
    }

    //Parameters: count of the vp, matrikelnumber of user
    //Return Values
    //updates the current user in database with given matrikelnumber and VP count
    public static void saveVPandMatrikelnumber(String vp, String matrikelnumber)
    {
        Map<String, Object> updateData = new TreeMap<>();
        updateData.put("deviceId", uniqueID);
        updateData.put("vps", vp);
        updateData.put("matrikelNumber", matrikelnumber);

        db.collection("users").document(uniqueID)
                .update(updateData)
                .addOnSuccessListener(aVoid -> System.out.println("DocumentSnapshot successfully updated!"))
                .addOnFailureListener(e ->System.out.println("Error updating document"));

    }




    public interface FirestoreCallbackDates {
        void onCallback(boolean finished);
    }

    public interface FirestoreCallbackStudy {
        void onCallback();
    }

    public interface FirestoreCallbackUser
    {
        void onCallback(String vps, String matrikelNumber);
    }
}