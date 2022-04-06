package com.example.vpmanager.repositories;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.example.vpmanager.interfaces.CreateStudyListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class CreateStudyRepository {

    private static CreateStudyRepository instance;
    private FirebaseFirestore db;
    private CreateStudyListener createStudyListener;


    //Parameter:
    //Return values: CreateStudyRepository
    //Returns instance of the CreateStudyRepository
    public static CreateStudyRepository getInstance() {
        if (instance == null) {
            instance = new CreateStudyRepository();
        }
        return instance;
    }


    //Parameter:
    //Return values:
    //Firestore callback; sets the createStudyListener
    public void setFirestoreCallback(CreateStudyListener createStudyListener) {
        this.createStudyListener = createStudyListener;
    }


    //Parameter: newStudy, studyId
    //Return values:
    //is called at the very end and requires all dates to be created!
    public void createNewStudy(Map<String, Object> newStudy, String studyId) {
        db = FirebaseFirestore.getInstance();
        db.collection("studies").document(studyId)
                .set(newStudy)
                .addOnSuccessListener(aVoid -> createStudyListener.onStudyCreated(true),
                        aVoid -> Log.d(TAG, "DocumentSnapshot successfully written!"))
                .addOnFailureListener(aVoid -> createStudyListener.onStudyCreated(false),
                        e -> Log.w(TAG, "Error writing document", e));
    }

    //Parameter: newDate, studyId
    //Return values:
    //is called several times before "createNewStudy" because a study needs all dateIds
    public void createNewDate(Map<String, Object> newDate, String dateId) {
        db = FirebaseFirestore.getInstance();
        db.collection("dates").document(dateId)
                .set(newDate)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully written!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));
    }
}
