package com.example.vpmanager.repositories;

import android.util.Log;

import com.example.vpmanager.interfaces.StudyMetaDataListener;
import com.example.vpmanager.models.StudyMetaInfoModel;
import com.example.vpmanager.views.MainActivity;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class OwnStudyRepository {

    private static OwnStudyRepository instance;
    private FirebaseFirestore db;
    private ArrayList<StudyMetaInfoModel> ownStudiesMetaInfoArrayList = new ArrayList<>();
    private StudyMetaDataListener studyMetaDataListener;


    //Parameter: studyMetaDataListener
    //Return values:
    //Firestore callback; set studyMetaDataListener
    public void setFirestoreCallback(StudyMetaDataListener studyMetaDataListener) {
        this.studyMetaDataListener = studyMetaDataListener;
    }

    //Parameter:
    //Return values: OwnStudyRepository
    //Creates an instance of the repo and returns always the same one
    public static OwnStudyRepository getInstance() {
        if (instance == null) {
            instance = new OwnStudyRepository();
        }
        return instance;
    }


    //Parameter:
    //Return values:
    //Retrieves study meta infos from database
    public void getOwnStudyMetaInfo() {
        String currentUserId = MainActivity.uniqueID;
        db = FirebaseFirestore.getInstance();
        CollectionReference studiesRef = db.collection("studies");

        studiesRef.whereEqualTo("creator", currentUserId).get()
                .addOnCompleteListener(task -> {
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
                        studyMetaDataListener.onStudyMetaDataReady(ownStudiesMetaInfoArrayList);
                    } else {
                        Log.d("getOwnStudyMetaInfo", "Error:" + task.getException());
                    }
                })
                .addOnFailureListener(e -> {
                });
    }
}
