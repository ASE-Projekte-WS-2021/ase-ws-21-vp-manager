package com.example.vpmanager;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.example.vpmanager.views.mainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PA_ExpandableListDataPump extends Activity {

    private static FirebaseFirestore db;
    private static CollectionReference datesRef, studiesRef;
    private static final HashMap<String, String> STUDY_ID_NAME = new HashMap<>();
    private static final HashMap<String, String> STUDY_ID_VPS = new HashMap<>();
    private static final HashMap<String, String> STUDY_ID_DATE = new HashMap<>();
    private static final HashMap<String, String> OWN_STUDY_ID_NAME = new HashMap<>();
    private static final HashMap<String, String> OWN_STUDY_ID_VPS = new HashMap<>();
    private static final HashMap<String, ArrayList<String>> OWN_STUDY_ID_DATES = new HashMap<>();
    private static final HashMap<String, List<String>> EXPANDABLE_LIST_DETAIL = new HashMap<String, List<String>>();

    //Parameters
    //Return Values: Returns a HashMap with test data for the personal account view
    //this function generates an ArrayList for each category which is displayed in the overview in the personal Account
    public static HashMap<String, List<String>> getData() {
        getBookedDates(new FirestoreCallbackDates() {
            @Override
            public void onCallback() {
                getNameAndVPS();
            }
        });

        getOwnStudies(new FirestoreCallbackOwnStudy() {
            @Override
            public void onCallback() {
                for (int i = 0; i < OWN_STUDY_ID_NAME.keySet().size(); i++) {
                    String key = OWN_STUDY_ID_NAME.keySet().toArray(new String[0])[i];
                    getAllBookedDatesForOwnStudies(key, new FirestoreCallbackBookedDatesOfOwnStudy() {
                        @Override
                        public void onCallback() {

                        }
                    });
                }
            }
        });

        createListEntries();
        return EXPANDABLE_LIST_DETAIL;
    }

    private static void createListEntries() {
        List<String> ownStudies = new ArrayList<>();
        String[] ownStudyKeys = OWN_STUDY_ID_NAME.keySet().toArray(new String[0]);
        for (int i = 0; i < OWN_STUDY_ID_NAME.size(); i++) {
            String StudyName = OWN_STUDY_ID_NAME.get(ownStudyKeys[i]);
            String StudyVPS = OWN_STUDY_ID_VPS.get(ownStudyKeys[i]);
            ArrayList<String> dates = OWN_STUDY_ID_DATES.get(ownStudyKeys[i]);
            if (dates == null) {
                continue;
            }
            for (int k = 0; k < dates.size(); k++) {
                String StudyDate = dates.get(k);
                ownStudies.add(StudyName + "," + StudyVPS + "," + StudyDate + "," + ownStudyKeys[i]);
            }
        }

        List<String> plannedStudies = new ArrayList<String>();
        String[] keys = STUDY_ID_DATE.keySet().toArray(new String[0]);
        for (int i = 0; i < STUDY_ID_DATE.size(); i++) {
            String StudyName = STUDY_ID_NAME.get(keys[i]);
            String StudyDate = STUDY_ID_DATE.get(keys[i]);
            String StudyVPS = STUDY_ID_VPS.get(keys[i]);

            plannedStudies.add(StudyName + "," + StudyVPS + "," + StudyDate + "," + keys[i]);
        }

        EXPANDABLE_LIST_DETAIL.put("Geplante Studien", plannedStudies);
        EXPANDABLE_LIST_DETAIL.put("Eigene Studien", ownStudies);
    }

    //Parameters
    //Return Values:
    //this function searches for each date where the user id is mentioned for the name and VPS of the study
    private static void getNameAndVPS() {
        String[] key = STUDY_ID_DATE.keySet().toArray(new String[0]);

        for (int i = 0; i < STUDY_ID_DATE.size(); i++) {
            String StudyID = key[i];
            getStudies(StudyID, new FirestoreCallbackStudy() {
                @Override
                public void onCallback() {
                }
            });
        }
    }

    //Parameters
    //Return Values: Returns a HashMap with test data for the personal account view
    //this function searches through all existing dates in the DB for the users ID and adds the matches in a Hashmap. Key is the study and value the date.
    private static void getBookedDates(FirestoreCallbackDates firestoreCallbackDates) {
        String uniqueID = mainActivity.uniqueID; //before: homeActivity.uniqueID
        db = FirebaseFirestore.getInstance();
        datesRef = db.collection("dates");

        datesRef.whereEqualTo("userId", uniqueID).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                STUDY_ID_DATE.put(document.getString("studyId"), document.getString("date"));
                            }
                            firestoreCallbackDates.onCallback();
                        }
                    }
                });
    }

    //Parameters: id of the Study the user has set a date in
    //Return Values:
    //this function adds all names and vps to hashmaps if the user has a booked date in it.
    private static void getStudies(String studyID, FirestoreCallbackStudy firestoreCallbackStudies) {
        String uniqueID = mainActivity.uniqueID; //before: homeActivity.uniqueID
        db = FirebaseFirestore.getInstance();
        studiesRef = db.collection("studies");

        studiesRef.whereEqualTo("id", studyID).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                STUDY_ID_NAME.put(studyID, document.getString("name"));
                                STUDY_ID_VPS.put(studyID, document.getString("vps"));
                            }
                            firestoreCallbackStudies.onCallback();
                        }
                    }
                });
    }

    //Parameters
    //Return Values:
    //this function searches for all studies the user created and adds them to a list.
    private static void getOwnStudies(FirestoreCallbackOwnStudy firestoreCallbackOwnStudy) {
        String uniqueID = mainActivity.uniqueID; //before: homeActivity.uniqueID
        db = FirebaseFirestore.getInstance();
        studiesRef = db.collection("studies");

        studiesRef.whereEqualTo("creator", uniqueID).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                OWN_STUDY_ID_NAME.put(document.getString("id"), document.getString("name"));
                                OWN_STUDY_ID_VPS.put(document.getString("id"), document.getString("vps"));
                            }
                            firestoreCallbackOwnStudy.onCallback();
                        }
                    }
                });
    }

    //Parameters: id of study in DB
    //Return Values:
    //this function uses the studyID to find all dates which belong to the study. Then all Dates where the userID is not null are added to a list.
    private static void getAllBookedDatesForOwnStudies(String StudyID, FirestoreCallbackBookedDatesOfOwnStudy firestoreCallbackBookedDatesOfOwnStudy) {
        String uniqueID = mainActivity.uniqueID; //before: homeActivity.uniqueID
        db = FirebaseFirestore.getInstance();
        datesRef = db.collection("dates");

        ArrayList<String> dates = new ArrayList<>();

        datesRef.whereEqualTo("studyId", StudyID).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getString("userId") != null && !document.getString("userId").equals("") && !document.getString("userId").equals("null")) {
                                    dates.add(document.getString("date"));
                                }
                            }
                            OWN_STUDY_ID_DATES.put(StudyID, dates);
                            firestoreCallbackBookedDatesOfOwnStudy.onCallback();
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

    public interface FirestoreCallbackOwnStudy {
        void onCallback();
    }

    public interface FirestoreCallbackBookedDatesOfOwnStudy {
        void onCallback();
    }
}
