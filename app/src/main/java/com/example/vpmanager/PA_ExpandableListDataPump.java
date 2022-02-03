package com.example.vpmanager;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PA_ExpandableListDataPump {


    private static FirebaseFirestore db;
    private static CollectionReference datesRef;
    private static HashMap<String, String> StudyID_name = new HashMap<>();
    private static HashMap<String, String> StudyID_vps = new HashMap<>();
    private static HashMap<String, String> StudyID_date = new HashMap<>();

    private static HashMap<String, String> ownStudyID_name = new HashMap<>();
    private static HashMap<String, String> ownStudyID_vps = new HashMap<>();
    private static HashMap<String, ArrayList<String>> ownStudyID_dates = new HashMap<>();

    private static HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

    //Parameters
    //Return Values: Returns a HashMap with test data for the personal account view
    //this function generates an ArrayList for each category which is displayed in the overview in the personal Account
    public static HashMap<String, List<String>> getData()
    {
         getBookedDates(new FirestoreCallbackDates() {
            @Override
            public void onCallback() {
                getNameAndVPS();
            }
        });

        return expandableListDetail;
    }

    //Parameters
    //Return Values:
    //this function restructures the hashmaps into strings for the expandable List
    private static void forOwnStudieGetDates() {
        getOwnStudies(new FirestoreCallbackOwnStudy() {
            @Override
            public void onCallback() {
                for (int i = 0; i < ownStudyID_name.keySet().size(); i++) {
                    String key = ownStudyID_name.keySet().toArray(new String[0])[i];
                    getAllBookedDatesForOwnStudies(key, new FirestoreCallbackBookedDatesOfOwnStudy() {
                        @Override
                        public void onCallback() {
                            createListEntries();
                        }
                    });
                }
            }
        });
    }
    private static void createListEntries() {
        List<String> ownStudies = new ArrayList<>();
        String[] ownStudyKeys = ownStudyID_name.keySet().toArray(new String[0]);
        for (int i = 0; i < ownStudyID_name.size(); i++) {
            String StudyName = ownStudyID_name.get(ownStudyKeys[i]);
            String StudyVPS = ownStudyID_vps.get(ownStudyKeys[i]);
            ArrayList<String> dates = ownStudyID_dates.get(ownStudyKeys[i]);
            if (dates == null){
                continue;
            }
            for (int k = 0; k < dates.size(); k++) {
                String StudyDate = dates.get(k);
                ownStudies.add(StudyName + "," + StudyVPS + "," + StudyDate + "," + ownStudyKeys[i]);
            }
        }

        List<String> plannedStudies = new ArrayList<String>();
        String[] keys = StudyID_date.keySet().toArray(new String[0]);
        for (int i = 0; i < StudyID_date.size(); i++) {
            String StudyName = StudyID_name.get(keys[i]);
            String StudyDate = StudyID_date.get(keys[i]);
            String StudyVPS = StudyID_vps.get(keys[i]);

            plannedStudies.add(StudyName + "," + StudyVPS + "," + StudyDate + "," + keys[i]);
        }

        expandableListDetail.put("Geplante Studien", plannedStudies);
        expandableListDetail.put("Eigene Studien", ownStudies);
    }

    //Parameters
    //Return Values:
    //this function searches for each date where the user id is mentioned for the name and VPS of the study
    private static void getNameAndVPS()
    {
        String[] key = StudyID_date.keySet().toArray(new String[0]);

        for(int i = 0; i < StudyID_date.size(); i++)
        {
            String StudyID = key[i];
            getStudies(StudyID, new FirestoreCallbackStudy() {
                @Override
                public void onCallback() {
                    forOwnStudieGetDates();
                }
            });
        }
    }

    //Parameters
    //Return Values: Returns a HashMap with test data for the personal account view
    //this function searches through all existing dates in the DB for the users ID and adds the matches in a Hashmap. Key is the study and value the date.
    private static void getBookedDates(FirestoreCallbackDates firestoreCallbackDates)
    {
        String uniqueID = homeActivity.uniqueID;
        db = FirebaseFirestore.getInstance();
        datesRef = db.collection("dates");

        datesRef.whereEqualTo("userId", uniqueID).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                StudyID_date.put(document.getString("studyId"), document.getString("date"));
                            }
                            firestoreCallbackDates.onCallback();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    //Parameters: id of the Study the user has set a date in
    //Return Values:
    //this function adds all names and vps to hashmaps if the user has a booked date in it.
    private static void getStudies(String studyID, FirestoreCallbackStudy firestoreCallbackStudies)
    {
        String uniqueID = homeActivity.uniqueID;
        db = FirebaseFirestore.getInstance();
        datesRef = db.collection("studies");

        datesRef.whereEqualTo("id", studyID).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                StudyID_name.put(studyID, document.getString("name"));
                                StudyID_vps.put(studyID, document.getString("vps"));
                            }
                            firestoreCallbackStudies.onCallback();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    //Parameters
    //Return Values:
    //this function searches for all studies the user created and adds them to a list.
    private static void getOwnStudies(FirestoreCallbackOwnStudy firestoreCallbackOwnStudy)
    {
        String uniqueID = homeActivity.uniqueID;
        db = FirebaseFirestore.getInstance();
        datesRef = db.collection("studies");

        datesRef.whereEqualTo("creator", uniqueID).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ownStudyID_name.put(document.getString("id"), document.getString("name"));
                                ownStudyID_vps.put(document.getString("id"), document.getString("vps"));
                            }
                            firestoreCallbackOwnStudy.onCallback();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    //Parameters: id of study in DB
    //Return Values:
    //this function uses the studyID to find all dates which belong to the study. Then all Dates where the userID is not null are added to a list.
    private static void getAllBookedDatesForOwnStudies(String StudyID,FirestoreCallbackBookedDatesOfOwnStudy firestoreCallbackBookedDatesOfOwnStudy)
    {
        String uniqueID = homeActivity.uniqueID;
        db = FirebaseFirestore.getInstance();
        datesRef = db.collection("dates");

        ArrayList<String> dates = new ArrayList<>();

        datesRef.whereEqualTo("studyId", StudyID).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getString("userId") != null && document.getString("userId") != "" && document.getString("userId") != "null")
                                {
                                    dates.add(document.getString("date"));
                                }
                            }
                            ownStudyID_dates.put(StudyID, dates);
                            firestoreCallbackBookedDatesOfOwnStudy.onCallback();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public interface FirestoreCallbackDates {
        void onCallback();
    }

    public interface FirestoreCallbackStudy {
        void onCallback();
    }

    public interface FirestoreCallbackOwnStudy{
        void onCallback();
    }

    public interface FirestoreCallbackBookedDatesOfOwnStudy{
        void onCallback();
    }
}
