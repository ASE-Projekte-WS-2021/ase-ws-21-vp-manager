package com.example.vpmanager;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.util.HashMap;
import java.util.Map;

public class accessDatabase {

    //Adds a new device (user) to the database, if it doesn't already exist
    public static void createNewUser(String deviceId){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //A DocumentReference refers to a document location
        final DocumentReference userDocRef = db.collection("users").document(deviceId);

        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
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
}
