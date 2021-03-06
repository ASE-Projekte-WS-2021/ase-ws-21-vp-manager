package com.example.vpmanager.repositories;

import android.util.Log;

import com.example.vpmanager.models.StudyMetaInfoModel;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;


//Singleton pattern
public class StudyListRepository {

    private static StudyListRepository instance;
    private FirebaseFirestore db;
    private CollectionReference studiesRef;
    private ArrayList<StudyMetaInfoModel> studyMetaInfosArrayList = new ArrayList<>();

    //This array gets filled first after the db call. Contains four infos about a study.
    private ArrayList<ArrayList<String>> studyIdNameVpCat;

    private StudyMetaDataListener studyMetaDataListener;


    //Parameter:
    //Return Values: instance of the repository class
    //Creates an instance of the repo and returns always the same one
    public static StudyListRepository getInstance() {
        if (instance == null) {
            instance = new StudyListRepository();
        }
        return instance;
    }


    //Parameter:
    //Return values:
    //Firestore callback; set studyMetaListener
    public void setFirestoreCallback(StudyMetaDataListener studyMetaDataListener) {
        this.studyMetaDataListener = studyMetaDataListener;
    }


    //Parameter:
    //Return Values:
    //This method gets called when new data about all studies is needed (every time the fragment is opened)
    public void getStudyMetaInfo() {
        getStudyInfosFromDB();
    }


    //Parameter:
    //Return Values:
    //This method has access to the already filled nested arrayList.
    //Stores the study meta infos in studyMetaInfoModel objects and adds them to another ArrayList
    private void setStudyMetaInfo() {

        //list needs to be cleared before, repo-instance is the same and not newly created
        studyMetaInfosArrayList.clear();

        //new data is stored in the list
        for (int i = 0; i < studyIdNameVpCat.size(); i++) {
            studyMetaInfosArrayList.add(
                    new StudyMetaInfoModel(
                            studyIdNameVpCat.get(i).get(0), //add Id
                            studyIdNameVpCat.get(i).get(1), //add Name
                            studyIdNameVpCat.get(i).get(2) + " " + "VP-Stunden", //add vps
                            studyIdNameVpCat.get(i).get(3), //add category
                            studyIdNameVpCat.get(i).get(4)) //add study type

            );
        }
    }


    //Parameter:
    //Return values:
    //interface to get studyMetaInfos Arraylist
    public interface StudyMetaDataListener {
        void onStudyMetaDataReady(ArrayList<StudyMetaInfoModel> studyMetaInfosArrayList);
    }


    //Parameter:
    //Return Values:
    //method with the actual db call
    private void getStudyInfosFromDB() {
        db = FirebaseFirestore.getInstance();
        studiesRef = db.collection("studies");
        studyIdNameVpCat = new ArrayList<>();

        //all studies are retrieved, sorted alphabetically by their names
        studiesRef.orderBy("name", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            //local ArrayList is stored in the big ArrayList for each existing study
                            if (!document.getBoolean("studyStateClosed")) {
                                ArrayList<String> idNameVphCat = new ArrayList<>();
                                idNameVphCat.add(0, document.getString("id"));
                                idNameVphCat.add(1, document.getString("name"));
                                idNameVphCat.add(2, document.getString("vps"));
                                idNameVphCat.add(3, document.getString("category"));
                                idNameVphCat.add(4, document.getString("executionType"));
                                studyIdNameVpCat.add(idNameVphCat);
                            }
                        }
                        setStudyMetaInfo();
                        studyMetaDataListener.onStudyMetaDataReady(studyMetaInfosArrayList);
                    } else {
                        Log.d("getStudyInfosFromDB", "Error:" + task.getException());
                    }
                });
    }
}
