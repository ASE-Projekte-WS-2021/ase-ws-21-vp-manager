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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class HomeRepository {

    private static HomeRepository instance;
    private FirebaseFirestore db;

    private List<Map<String, Object>> dbDatesListHomeRepo = new ArrayList<>();
    private List<Map<String, Object>> dbStudiesListHomeRepo = new ArrayList<>();

    private GetAllDatesListener getAllDatesListener;
    private GetAllStudiesListener getAllStudiesListener;
    private GetVpAndMatNrListener getVpAndMatNrListener;

    public static HomeRepository getInstance() {
        if (instance == null) {
            instance = new HomeRepository();
        }
        return instance;
    }

    public void setFirestoreCallback(GetAllDatesListener getAllDatesListener,
                                     GetAllStudiesListener getAllStudiesListener, GetVpAndMatNrListener getVpAndMatNrListener) {
        this.getAllDatesListener = getAllDatesListener;
        this.getAllStudiesListener = getAllStudiesListener;
        this.getVpAndMatNrListener = getVpAndMatNrListener;
    }

    //use these methods or create another allStudiesReadyCallback that have dates and studies as parameter
    public List<Map<String, Object>> getDbDatesList() {
        return dbDatesListHomeRepo;
    }

    public List<Map<String, Object>> getDbStudiesList() {
        return dbStudiesListHomeRepo;
    }

    //for the upcoming appointments fragment
    public void setFirestoreCallbackUpAppoint(GetAllDatesListener getAllDatesListener, GetAllStudiesListener getAllStudiesListener) {
        this.getAllDatesListener = getAllDatesListener;
        this.getAllStudiesListener = getAllStudiesListener;
    }

    public void getAllDatesFromDb() {
        db = FirebaseFirestore.getInstance();
        CollectionReference datesRef = db.collection("dates");
        dbDatesListHomeRepo.clear();

        datesRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                dbDatesListHomeRepo.add(document.getData());
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

    public void getAllStudiesFromDb() {
        db = FirebaseFirestore.getInstance();
        CollectionReference studiesRef = db.collection("studies");
        dbStudiesListHomeRepo.clear();

        studiesRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                dbStudiesListHomeRepo.add(document.getData());
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

    public void getVpAndMatrikelNumber() {
        db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");
        //uniqueId from mainActivity can be used
        usersRef.whereEqualTo("deviceId", uniqueID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    String vps = "", matrikelNumber = "";
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        vps = document.getString("vps");
                        matrikelNumber = document.getString("matrikelNumber");
                    }
                    getVpAndMatNrListener.onAllDataReady(vps, matrikelNumber, dbDatesListHomeRepo, dbStudiesListHomeRepo);
                }
            }
        }).addOnFailureListener(e -> getVpAndMatNrListener.onAllDataReady("", "", dbDatesListHomeRepo, dbStudiesListHomeRepo));
    }

    public void saveVpAndMatrikelNumber(String vp, String matrikelnumber) {  //static?
        Map<String, Object> updateData = new TreeMap<>();
        updateData.put("deviceId", uniqueID);
        updateData.put("vps", vp);
        updateData.put("matrikelNumber", matrikelnumber);

        db.collection("users").document(uniqueID)
                .update(updateData)
                .addOnSuccessListener(aVoid -> System.out.println("DocumentSnapshot successfully updated!"))
                .addOnFailureListener(e -> System.out.println("Error updating document"));
    }

    public String createGetRequest(String matrikelNumber) throws IOException {
        HttpURLConnection urlConnection = null;
        URL url = new URL("https://vp.software-engineering.education/" + matrikelNumber + "/vps");
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(10000 /* milliseconds */);
        urlConnection.setConnectTimeout(15000 /* milliseconds */);
        urlConnection.setDoOutput(true);
        urlConnection.connect();

        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        br.close();

        return sb.toString();
    }

}
