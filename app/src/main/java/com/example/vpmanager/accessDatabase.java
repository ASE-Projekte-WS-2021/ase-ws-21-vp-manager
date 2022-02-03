package com.example.vpmanager;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class accessDatabase {

    FirebaseFirestore db;

    //HomeActivity --------------------------------------------------------------------------------!
    //Adds a new user (installation of the app) to the database, if it doesn't already exist
    public void createNewUser(String deviceId){

        db = FirebaseFirestore.getInstance();
        //A DocumentReference refers to a document location
        final DocumentReference userDocRef = db.collection("users").document(deviceId);

        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                //A DocumentSnapshot contains data read from a document
                DocumentSnapshot snapshot = transaction.get(userDocRef);
                if (!snapshot.exists()) {
                    Map<String, Object> user = new HashMap<>();
                    user.put("deviceId", deviceId);
                    transaction.set(userDocRef, user);
                }
                return null;
            }
        }).addOnSuccessListener(aVoid -> Log.d(TAG, "Transaction success!"
        )).addOnFailureListener(e -> Log.w(TAG, "Transaction failure.", e));
    }

    //CreateStudyActivity -------------------------------------------------------------------------!
    public void addNewStudy(Map<String, Object> newStudy, String studyID) {

        db = FirebaseFirestore.getInstance();

        db.collection("studies").document(studyID)
                .set(newStudy)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully written!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));
    }

    public void addNewDate(Map<String, Object> newDate, String dateID) {

        db = FirebaseFirestore.getInstance();

        db.collection("dates").document(dateID)
                .set(newDate)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully written!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));
    }

    //FindStudyActivity ---------------------------------------------------------------------------!
    //in findStudyActivity

    //studyActivity -------------------------------------------------------------------------------!
    //in studyActivity

    //updateData should contain ("selected": true) and ("userId: the User Id)
    public void selectDate(Map<String, Object> updateData, String dateId){

        db = FirebaseFirestore.getInstance();

        db.collection("dates").document(dateId)
                .update(updateData)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully updated!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
    }

    public void unselectDate(String dateId){

        db = FirebaseFirestore.getInstance();

        db.collection("dates").document(dateId)
                .update("selected", false, "userId", null)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully updated!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
    }

}
