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
    private ArrayList<StudyMetaInfoModel> studyMetaInfosArrayListTest = new ArrayList<>();
    //this array gets filled first after the db call. Contains the three infos about a study
    private ArrayList<ArrayList<String>> studyIdNameVp;

    public static StudyListRepository getInstance(){
        Log.d("StudyListRepository","getInstance start");

        if (instance == null){
            instance = new StudyListRepository();
            Log.d("StudyListRepository","instance was null and a new one was created");
        }

        Log.d("StudyListRepository","getInstance end");
        return instance;
    }

    //This method gets called when the ViewModel is initiated in the fragment (every time the fragment is opened)!
    public ArrayList<StudyMetaInfoModel> getStudyMetaInfo(){
        Log.d("StudyListRepository","getStudyMetaInfo start");

        //This method is called first, but needs to long to finish!
        getStudyInfosFromDB(new FirestoreCallback() { //StudyListRepository.
            @Override
            public void onCallback() { //ArrayList<ArrayList<String>> arrayList
                Log.d("StudyListRepository", "The ArrayList of StudyInfoModels will be set now");
                setStudyMetaInfo();
                //send signal, that the db call is finished, here!
            }
        });

        //fillTestArrayList(); //hardcoded data is used, for getting example studies (but ids must match to go in detail!)

        Log.d("StudyListRepository","getStudyMetaInfo end");
        return studyMetaInfosArrayList;
    }

    //Stores the hardcoded study infos in another arrayList, which is further used in this app
    private void fillTestArrayList(){
        Log.d("StudyListRepository","fillTestArrayList start");
        studyMetaInfosArrayListTest.add(
                new StudyMetaInfoModel(
                        "e5b44b34-1f91-484f-ada4-018bc99004c",
                        "Studie 1",
                        "1 VP-Stunde")
        );
        studyMetaInfosArrayListTest.add(
                new StudyMetaInfoModel(
                        "90e16b34-efbc-4c77-89a7-ea49a61d6f9",
                        "Studie 2",
                        "2 VP-Stunden")
        );
        studyMetaInfosArrayListTest.add(
                new StudyMetaInfoModel(
                        "121cbc7e-7487-4311-9c2c-e6ac58e090a",
                        "test für evtl zu langen Studiennamen ABCDEFG",
                        "8 VP-Stunden")
        );
        Log.d("StudyListRepository","fillTestArrayList end");
    }

    //Method has access to the already filled nested ArrayList.
    //Stores the study meta infos in studyMetaInfoModel Objects and adds them to another ArrayList
    private void setStudyMetaInfo() {
        Log.d("StudyListRepository","setStudyMetaInfo start");
        //THE LIST NEEDS TO BE CLEARED BEFORE, BECAUSE THE REPO-INSTANCE IS THE SAME AND IT ISN'T CREATED NEW!
        studyMetaInfosArrayList.clear();
        //New data is stored in the list
        for (int i = 0; i < studyIdNameVp.size(); i++) {
            studyMetaInfosArrayList.add(
                    new StudyMetaInfoModel(
                            studyIdNameVp.get(i).get(0), //add Id
                            studyIdNameVp.get(i).get(1), //add Name
                            studyIdNameVp.get(i).get(2) + " " + "VP-Stunden") //add Vps
            );
        }
        Log.d("StudyListRepository","setStudyMetaInfo end");
    }

    private interface FirestoreCallback {
        void onCallback(); //ArrayList<ArrayList<String>> arrayList
    }

    //method with the actual db call
    private void getStudyInfosFromDB(FirestoreCallback firestoreCallback) {
        Log.d("StudyListRepository","getStudyInfosFromDB start");
        db = FirebaseFirestore.getInstance();
        studiesRef = db.collection("studies");
        studyIdNameVp = new ArrayList<>();

        //all studies are retrieved, sorted alphabetically by their names
        studiesRef.orderBy("name", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //local ArrayList is stored in the big ArrayList for each existing study
                                ArrayList<String> idNameVph = new ArrayList<>();
                                idNameVph.add(0, document.getString("id"));
                                idNameVph.add(1, document.getString("name"));
                                idNameVph.add(2, document.getString("vps"));
                                studyIdNameVp.add(idNameVph);
                            }
                            firestoreCallback.onCallback(); //studyIdNameVp
                        }else {
                            Log.d("getStudyInfosFromDB", "Error:" + task.getException());
                        }
                    }
                });
        Log.d("StudyListRepository","getStudyInfosFromDB end");
    }

}
