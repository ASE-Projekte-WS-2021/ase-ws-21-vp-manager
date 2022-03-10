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

    //this is the actual list that should be used
    private ArrayList<StudyMetaInfoModel> studyMetaInfosArrayList = new ArrayList<>();

    //this is a test list with hard coded studies that are added below
    //private ArrayList<StudyMetaInfoModel> studyMetaInfosArrayListTest = new ArrayList<>();

    //this array gets filled first after the db call. Contains the three infos about a study
    private ArrayList<ArrayList<String>> studyIdNameVpCat;

    //Parameter:
    //Return Values: instance of the repository class
    //creates an instance of the repo and returns always the same one
    public static StudyListRepository getInstance() {

        if (instance == null) {
            instance = new StudyListRepository();
            Log.d("StudyListRepository", "instance was null and a new one was created");
        }
        return instance;
    }

    //Parameter:
    //Return Values: arrayList of studyMetaInfoModels
    //This method gets called when the ViewModel is initiated in the fragment (every time the fragment is opened)!
    public ArrayList<StudyMetaInfoModel> getStudyMetaInfo() {
        Log.d("StudyListRepository", "getStudyMetaInfo start");

        //This method is called first, but needs to long to finish!
        getStudyInfosFromDB(new FirestoreCallback() {
            @Override
            public void onCallback() {
                Log.d("StudyListRepository", "The ArrayList of StudyInfoModels will be set now");
                setStudyMetaInfo();
                //send signal, that the db call is finished, here!
            }
        });

        //hardcoded data is used, for getting example studies (but ids must match to go in detail!)
        //fillTestArrayList();

        Log.d("StudyListRepository", "getStudyMetaInfo end");
        return studyMetaInfosArrayList;
    }

    //Stores the hardcoded study infos in another arrayList, which is further used in this app
    /*
    private void fillTestArrayList(){
        Log.d("StudyListRepository","fillTestArrayList start");
        studyMetaInfosArrayListTest.add(
                new StudyMetaInfoModel(
                        "e5b44b34-1f91-484f-ada4-018bc99004c",
                        "Studie 1",
                        "1 VP-Stunde",
                        "cat2")
        );
        studyMetaInfosArrayListTest.add(
                new StudyMetaInfoModel(
                        "90e16b34-efbc-4c77-89a7-ea49a61d6f9",
                        "Studie 2",
                        "2 VP-Stunden",
                        "cat2")
        );
        studyMetaInfosArrayListTest.add(
                new StudyMetaInfoModel(
                        "121cbc7e-7487-4311-9c2c-e6ac58e090a",
                        "test für evtl zu langen Studiennamen ABCDEFG",
                        "8 VP-Stunden",
                        "cat3")
        );
        Log.d("StudyListRepository","fillTestArrayList end");
    }
     */

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

    private interface FirestoreCallback {
        void onCallback();
    }

    //Parameter: FirestoreCallback
    //Return Values:
    //method with the actual db call
    private void getStudyInfosFromDB(FirestoreCallback firestoreCallback) {
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
                            firestoreCallback.onCallback();
                        } else {
                            Log.d("getStudyInfosFromDB", "Error:" + task.getException());
                        }
                    }
                });
        Log.d("StudyListRepository", "getStudyInfosFromDB end");
    }
}
