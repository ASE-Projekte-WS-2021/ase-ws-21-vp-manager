package com.example.vpmanager.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.vpmanager.models.StudyMetaInfoModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

//Singleton pattern
public class StudyListRepository {

    private static StudyListRepository instance;
    private FirebaseFirestore db;
    private CollectionReference studiesRef;

    private ArrayList<StudyMetaInfoModel> studyMetaInfosArrayList = new ArrayList<>();

    //This array gets filled first after the db call. Contains four infos about a study. Can be dropped in the future
    private ArrayList<ArrayList<String>> studyIdNameVpCat;

    private StudyMetaDataListener studyMetaDataListener;

    //Sets the viewModel as the listener for the callback
    public void setFirestoreCallback(StudyMetaDataListener studyMetaDataListener) {
        this.studyMetaDataListener = studyMetaDataListener;
    }

    //Parameter:
    //Return Values: instance of the repository class
    //Creates an instance of the repo and returns always the same one
    public static StudyListRepository getInstance() {
        if (instance == null) {
            instance = new StudyListRepository();
            Log.d("StudyListRepository", "instance was null and a new one was created");
        }
        return instance;
    }

    //Parameter:
    //Return Values:
    //This method gets called when new data about all studies is needed (every time the fragment is opened)
    public void getStudyMetaInfo() { //ArrayList<StudyMetaInfoModel>
        getStudyInfosFromDB();
        //return studyMetaInfosArrayList;
    }

    //Parameter:
    //Return Values:
    //This method has access to the already filled nested arrayList.
    //Stores the study meta infos in studyMetaInfoModel objects and adds them to another ArrayList
    private void setStudyMetaInfo() {
        Log.d("StudyListRepository", "setStudyMetaInfo start");
        //THE LIST NEEDS TO BE CLEARED BEFORE, BECAUSE THE REPO-INSTANCE IS THE SAME AND IT ISN'T CREATED NEW!
        studyMetaInfosArrayList.clear();
        //new data is stored in the list
        for (int i = 0; i < studyIdNameVpCat.size(); i++) {
            studyMetaInfosArrayList.add(
                    new StudyMetaInfoModel(
                            studyIdNameVpCat.get(i).get(0), //add Id
                            studyIdNameVpCat.get(i).get(1), //add Name
                            studyIdNameVpCat.get(i).get(2) + " " + "VP-Stunden", //add vps
                            studyIdNameVpCat.get(i).get(3)) //add category
            );
        }
        Log.d("StudyListRepository", "setStudyMetaInfo end");
    }

    public interface StudyMetaDataListener {
        void onStudyMetaDataReady(ArrayList<StudyMetaInfoModel> studyMetaInfosArrayList);
    }

    //Parameter:
    //Return Values:
    //method with the actual db call
    private void getStudyInfosFromDB() {
        Log.d("StudyListRepository", "getStudyInfosFromDB start");
        db = FirebaseFirestore.getInstance();
        studiesRef = db.collection("studies");
        studyIdNameVpCat = new ArrayList<>();

        //all studies are retrieved, sorted alphabetically by their names
        studiesRef.orderBy("name", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //local ArrayList is stored in the big ArrayList for each existing study
                                ArrayList<String> idNameVphCat = new ArrayList<>();
                                idNameVphCat.add(0, document.getString("id"));
                                idNameVphCat.add(1, document.getString("name"));
                                idNameVphCat.add(2, document.getString("vps"));
                                idNameVphCat.add(3, document.getString("category"));
                                studyIdNameVpCat.add(idNameVphCat);
                            }
                            setStudyMetaInfo();
                            studyMetaDataListener.onStudyMetaDataReady(studyMetaInfosArrayList);
                        } else {
                            Log.d("getStudyInfosFromDB", "Error:" + task.getException());
                        }
                    }
                });
    }
}
