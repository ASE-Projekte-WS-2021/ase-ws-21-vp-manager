package com.example.vpmanager.repositories;

import static com.example.vpmanager.views.mainActivity.uniqueID;

import androidx.annotation.NonNull;

import com.example.vpmanager.interfaces.GetAllDatesListener;
import com.example.vpmanager.interfaces.GetAllStudiesListener;
import com.example.vpmanager.interfaces.GetVpAndMatNrListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OwnStudyRepository {

    private static OwnStudyRepository instance;
    private FirebaseFirestore db;

    private List<Map<String, Object>> dbDatesList = new ArrayList<>();
    private List<Map<String, Object>> dbStudiesList = new ArrayList<>();

    private GetAllDatesListener getAllDatesListener;
    private GetAllStudiesListener getAllStudiesListener;
    private GetVpAndMatNrListener getVpAndMatNrListener;

    public static OwnStudyRepository getInstance(){
        if (instance == null) {
            instance = new OwnStudyRepository();
        }
        return instance;
    }

    public void setFirestoreCallback(GetAllDatesListener getAllDatesListener,
                                     GetAllStudiesListener getAllStudiesListener, GetVpAndMatNrListener getVpAndMatNrListener){
        this.getAllDatesListener = getAllDatesListener;
        this.getAllStudiesListener = getAllStudiesListener;
        this.getVpAndMatNrListener = getVpAndMatNrListener;
    }

    public void getAllDatesFromDb(){
        db = FirebaseFirestore.getInstance();
        CollectionReference datesRef = db.collection("dates");
        dbDatesList.clear();

        datesRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                dbDatesList.add(document.getData());
                            }
                            getAllDatesListener.onAllDatesReady(true);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        getAllDatesListener.onAllDatesReady(false);
                    }
                });
    }

    public void getAllStudiesFromDb(){
        db = FirebaseFirestore.getInstance();
        CollectionReference studiesRef = db.collection("studies");
        dbStudiesList.clear();

        studiesRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                dbStudiesList.add(document.getData());
                                //createListEntries(); can be skipped here!
                            }
                            getAllStudiesListener.onAllStudiesReady(true);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        getAllStudiesListener.onAllStudiesReady(false);
                    }
                });
    }

    public void getVpAndMatrikelNumber(){
        db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");
        //uniqueId from mainActivity can be used
        usersRef.whereEqualTo("deviceId", uniqueID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    String vps= "", matrikelNumber = "";
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        vps = document.getString("vps");
                        matrikelNumber = document.getString("matrikelNumber");
                    }
                    getVpAndMatNrListener.onAllDataReady(vps, matrikelNumber, dbDatesList, dbStudiesList);
                }
            }
        }).addOnFailureListener(e -> getVpAndMatNrListener.onAllDataReady("", "", dbDatesList, dbStudiesList));
    }

}
