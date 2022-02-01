package com.example.vpmanager;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class accessDatabase {

    FirebaseFirestore db;
    CollectionReference studiesRef;
    ArrayList<ArrayList<String>> studyInfo;

    //HomeActivity --------------------------------------------------------------------------------!
    //Adds a new user (installation of the app) to the database, if it doesn't already exist
    public void createNewUser(String deviceId){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
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

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("studies").document(studyID)
                .set(newStudy)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully written!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));

    }

    public void addNewDate(Map<String, Object> newDate, String dateID) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("dates").document(dateID)
                .set(newDate)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully written!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));

    }

    //FindStudyActivity ---------------------------------------------------------------------------!
    //in findStudyActivity

    //studyActivity -------------------------------------------------------------------------------!
    public Map<String, Object> getAllInfoOfOneStudy(String studyId) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> allStudyInfo = new HashMap<>();

        DocumentReference docRef = db.collection("studies").document(studyId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        allStudyInfo.putAll(document.getData()); //getData is ne map
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        return allStudyInfo;
    }
}
