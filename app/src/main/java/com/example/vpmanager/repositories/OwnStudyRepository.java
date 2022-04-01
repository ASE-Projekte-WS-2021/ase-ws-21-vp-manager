package com.example.vpmanager.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.vpmanager.interfaces.StudyMetaDataListener;
import com.example.vpmanager.models.StudyMetaInfoModel;
import com.example.vpmanager.views.mainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class OwnStudyRepository {

    private static OwnStudyRepository instance;
    private FirebaseFirestore db;

    private ArrayList<StudyMetaInfoModel> ownStudiesMetaInfoArrayList = new ArrayList<>();

    private StudyMetaDataListener studyMetaDataListener;

    public void setFirestoreCallback(StudyMetaDataListener studyMetaDataListener) {
        this.studyMetaDataListener = studyMetaDataListener;
    }

    public static OwnStudyRepository getInstance() {
        if (instance == null) {
            instance = new OwnStudyRepository();
        }
        return instance;
    }

    public void getOwnStudyMetaInfo() {
        String currentUserId = mainActivity.uniqueID;
        db = FirebaseFirestore.getInstance();
        CollectionReference studiesRef = db.collection("studies");

        studiesRef.whereEqualTo("creator", currentUserId).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ownStudiesMetaInfoArrayList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ownStudiesMetaInfoArrayList.add(new StudyMetaInfoModel(
                                        document.getString("id"),
                                        document.getString("name"),
                                        document.getString("vps"),
                                        document.getString("category"),
                                        document.getString("executionType"))
                                );
                            }
                            Log.d("getOwnStudyMetaInfo", "all Studies: " + ownStudiesMetaInfoArrayList);
                            studyMetaDataListener.onStudyMetaDataReady(ownStudiesMetaInfoArrayList);
                        } else {
                            Log.d("getOwnStudyMetaInfo", "Error:" + task.getException());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Error", "onFailure: " + e);
                    }
                });
    }
}
