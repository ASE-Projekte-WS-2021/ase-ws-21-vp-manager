package com.example.vpmanager.repositories;

import static android.content.ContentValues.TAG;

import android.util.Log;


import com.example.vpmanager.interfaces.EditStudyDetailsListener;
import com.example.vpmanager.interfaces.StudyDatesListener;
import com.example.vpmanager.interfaces.StudyUpdatedListener;
import com.example.vpmanager.models.DateModel;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StudyEditRepository {

    private static StudyEditRepository instance;
    private FirebaseFirestore db;

    private Map<String, Object> studyEditDetailsMap = new HashMap<>();
    private ArrayList<DateModel> studyEditDatesArrayList = new ArrayList<>();

    private EditStudyDetailsListener editStudyDetailsListener;
    private StudyDatesListener editStudyDatesListener;
    private StudyUpdatedListener studyUpdatedListener;


    //Parameter:
    //Return values: StudyEditRepository
    //Creates an instance of the repo and returns always the same one
    public static StudyEditRepository getInstance() {
        if (instance == null) {
            instance = new StudyEditRepository();
        }
        return instance;
    }

    //Parameter: editStudyDetailsListener, editStudyDatesListener,studyUpdatedListener
    //Return values:
    //Firestore callback; set Listeners
    public void setFirestoreCallback(EditStudyDetailsListener editStudyDetailsListener,
                                     StudyDatesListener editStudyDatesListener,
                                     StudyUpdatedListener studyUpdatedListener) {
        this.editStudyDetailsListener = editStudyDetailsListener;
        this.editStudyDatesListener = editStudyDatesListener;
        this.studyUpdatedListener = studyUpdatedListener;
    }


    public void getStudyEditDetails(String currentStudyIdEdit) {
        loadStudyEditDetails(currentStudyIdEdit);
    }

    public void getStudyEditDates(String currentStudyIdEdit) {
        loadStudyEditDates(currentStudyIdEdit);
    }


    //Parameter: currentStudyIdEdit
    //Return values:
    //Loads all study detail values from database
    private void loadStudyEditDetails(String currentStudyIdEdit) {
        db = FirebaseFirestore.getInstance();
        CollectionReference studyDocRef = db.collection("studies");
        studyEditDetailsMap.clear(); //reset and save new details in it

        studyDocRef.whereEqualTo("id", currentStudyIdEdit).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    if (document != null) {
                        studyEditDetailsMap.put("id", document.getString("id"));
                        studyEditDetailsMap.put("name", document.getString("name"));
                        studyEditDetailsMap.put("vps", document.getString("vps"));
                        studyEditDetailsMap.put("category", document.getString("category"));
                        studyEditDetailsMap.put("executionType", document.getString("executionType"));
                        studyEditDetailsMap.put("contact", document.getString("contact"));
                        studyEditDetailsMap.put("contact2", document.getString("contact2"));
                        studyEditDetailsMap.put("contact3", document.getString("contact3"));
                        studyEditDetailsMap.put("contact4", document.getString("contact4"));
                        studyEditDetailsMap.put("contact5", document.getString("contact5"));
                        studyEditDetailsMap.put("description", document.getString("description"));
                        studyEditDetailsMap.put("platform", document.getString("platform"));
                        studyEditDetailsMap.put("platform2", document.getString("platform2"));
                        studyEditDetailsMap.put("location", document.getString("location"));
                        studyEditDetailsMap.put("street", document.getString("street"));
                        studyEditDetailsMap.put("room", document.getString("room"));
                        studyEditDetailsMap.put("studyStateClosed", (Boolean) document.getBoolean("studyStateClosed"));
                    } else {
                        Log.d("loadStudyEditDetails", "Error: the document does not exist!");
                    }
                    editStudyDetailsListener.onEditStudyDetailsReady(studyEditDetailsMap);
                }
            } else {
                Log.d("loadStudyEditDetails", "Error:" + task.getException());
            }
        });
    }


    //Parameter: id of current study that is edited
    //Return values:
    //Loads all study date values from database
    private void loadStudyEditDates(String currentStudyIdEdit) {
        db = FirebaseFirestore.getInstance();
        CollectionReference datesRef = db.collection("dates");
        studyEditDatesArrayList.clear();

        datesRef.whereEqualTo("studyId", currentStudyIdEdit).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            studyEditDatesArrayList.add(
                                    new DateModel(
                                            document.getString("id"),      //id of the date
                                            document.getString("date"),    //the date itself
                                            document.getString("studyId"), //id of corresponding study
                                            document.getString("userId"),  //id of user who selected the date
                                            document.getBoolean("selected"), //boolean if selected or not
                                            document.getBoolean("participated")) //boolean if participated or not
                            );
                        }
                        editStudyDatesListener.onStudyDatesReady(studyEditDatesArrayList);
                    } else {
                        Log.d("loadStudyEditDates", "Error:" + task.getException());
                    }
                });
    }

    //Parameter: specific Date object, id of specific date object
    //Return values:
    //Updates dates in database; no callback yet: only if the study is updated completely
    public void updateDates(Map<String, Object> specificDate, String specificDateId) {
        db = FirebaseFirestore.getInstance();
        db.collection("dates").document(specificDateId)
                .set(specificDate, SetOptions.merge())
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Write/update successful!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error writing/updating document", e));
    }

    //Parameter: map of new study info, idOfUpdatedStudy
    //Return values:
    //Updates studies in database
    public void updateStudy(Map<String, Object> updatedStudyMap, String idOfUpdatedStudy) {
        db = FirebaseFirestore.getInstance();
        db.collection("studies").document(idOfUpdatedStudy)
                .set(updatedStudyMap)
                .addOnSuccessListener(aVoid -> studyUpdatedListener.onStudyUpdated(),
                        aVoid -> Log.d(TAG, "Update study successful!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error updating study document", e));
    }


    //Parameter: id of date object
    //Return values:
    //Deletes data from database
    public void deleteDate(String dateId) {
        db = FirebaseFirestore.getInstance();
        db.collection("dates").document(dateId)
                .delete()
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Deleted study successful!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error deleting study document", e));
    }
}
