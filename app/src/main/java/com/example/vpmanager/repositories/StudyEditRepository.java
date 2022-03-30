package com.example.vpmanager.repositories;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.vpmanager.interfaces.EditStudyDetailsListener;
import com.example.vpmanager.interfaces.StudyDatesListener;
import com.example.vpmanager.interfaces.StudyUpdatedListener;
import com.example.vpmanager.models.DateModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class StudyEditRepository {

    private static StudyEditRepository instance;
    private FirebaseFirestore db;

    private Map<String, Object> studyEditDetailsMap = new HashMap<>();
    private ArrayList<DateModel> studyEditDatesArrayList = new ArrayList<>();

    private EditStudyDetailsListener editStudyDetailsListener;
    private StudyDatesListener editStudyDatesListener;
    private StudyUpdatedListener studyUpdatedListener;

    public static StudyEditRepository getInstance() {
        if (instance == null) {
            instance = new StudyEditRepository();
        }
        return instance;
    }

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

    public void getStudyEditDates(String currentStudyIdEdit){
        loadStudyEditDates(currentStudyIdEdit);
    }

    private void loadStudyEditDetails(String currentStudyIdEdit) {
        db = FirebaseFirestore.getInstance();
        CollectionReference studyDocRef = db.collection("studies");
        studyEditDetailsMap.clear(); //reset and save new details in it

        studyDocRef.whereEqualTo("id", currentStudyIdEdit).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
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
                        } else {
                            Log.d("loadStudyEditDetails", "Error: the document does not exist!");
                        }
                        editStudyDetailsListener.onEditStudyDetailsReady(studyEditDetailsMap);
                    }
                } else {
                    Log.d("loadStudyEditDetails", "Error:" + task.getException());
                }
            }
        });
    }

    private void loadStudyEditDates(String currentStudyIdEdit){
        db = FirebaseFirestore.getInstance();
        CollectionReference datesRef = db.collection("dates");
        studyEditDatesArrayList.clear();

        datesRef.whereEqualTo("studyId", currentStudyIdEdit).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                studyEditDatesArrayList.add(
                                        new DateModel(
                                                document.getString("id"),      //id of the date
                                                document.getString("date"),    //the date itself
                                                document.getString("studyId"), //id of corresponding study
                                                document.getString("userId"),  //id of user who selected the date
                                                document.getBoolean("selected")) //boolean if selected or not
                                );
                            }
                            editStudyDatesListener.onStudyDatesReady(studyEditDatesArrayList);
                        } else {
                            Log.d("loadStudyEditDates", "Error:" + task.getException());
                        }
                    }
                });
    }

    //no callback yet. only if the study is updated completely
    public void updateDates(Map<String, Object> specificDate, String specificDateId){
        db = FirebaseFirestore.getInstance();
        db.collection("dates").document(specificDateId)
                .set(specificDate, SetOptions.merge())
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Write/update successful!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error writing/updating document", e));
    }

    public void updateStudy(Map<String, Object> updatedStudyMap, String idOfUpdatedStudy){
        Log.d("updateStudy", "Map: " + updatedStudyMap.toString());
        Log.d("updateStudy", "studyId: " + idOfUpdatedStudy);
        db = FirebaseFirestore.getInstance();
        db.collection("studies").document(idOfUpdatedStudy)
                .set(updatedStudyMap)
                .addOnSuccessListener(aVoid -> studyUpdatedListener.onStudyUpdated(),
                        aVoid -> Log.d(TAG, "Update study successful!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error updating study document", e));
    }


    public void deleteDate(String dateId){
        db = FirebaseFirestore.getInstance();
        db.collection("dates").document(dateId)
                .delete()
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Deleted study successful!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error deleting study document", e));
    }
}
