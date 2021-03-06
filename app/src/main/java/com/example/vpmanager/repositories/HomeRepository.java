package com.example.vpmanager.repositories;

import static com.example.vpmanager.views.MainActivity.uniqueID;

import com.example.vpmanager.Config;
import com.example.vpmanager.interfaces.GetAllDatesListener;
import com.example.vpmanager.interfaces.GetAllStudiesListener;
import com.example.vpmanager.interfaces.GetVpAndMatNrListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

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


    //Parameter:
    //Return values: HomeRepository
    //Returns instance of the HomeRepository
    public static HomeRepository getInstance() {
        if (instance == null) {
            instance = new HomeRepository();
        }
        return instance;
    }

    //Parameter: associated Listeners
    //Return values:
    //Firestore callback; sets Listeners
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

    //Parameter: getAllDatesListener, getAllStudiesListener
    //Return values:
    //Callback for the upcoming appointments fragment
    public void setFirestoreCallbackUpAppoint(GetAllDatesListener getAllDatesListener, GetAllStudiesListener getAllStudiesListener) {
        this.getAllDatesListener = getAllDatesListener;
        this.getAllStudiesListener = getAllStudiesListener;
    }


    //Parameter:
    //Return values:
    //Loads all date elements from the database
    public void getAllDatesFromDb() {
        db = FirebaseFirestore.getInstance();
        CollectionReference datesRef = db.collection("dates");
        dbDatesListHomeRepo.clear();

        datesRef.get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            dbDatesListHomeRepo.add(document.getData());
                        }
                        getAllDatesListener.onAllDatesReady(true);
                    }
                })
                .addOnFailureListener(e -> getAllDatesListener.onAllDatesReady(false));
    }


    //Parameter:
    //Return values:
    //Loads all studies from the database
    public void getAllStudiesFromDb() {
        db = FirebaseFirestore.getInstance();
        CollectionReference studiesRef = db.collection("studies");
        dbStudiesListHomeRepo.clear();

        studiesRef.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            dbStudiesListHomeRepo.add(document.getData());
                            //createListEntries(); can be skipped here!
                        }
                        getAllStudiesListener.onAllStudiesReady(true);
                    }
                })
                .addOnFailureListener(e -> getAllStudiesListener.onAllStudiesReady(false));
    }


    //Parameter:
    //Return values:
    //Receives VP values and matr. number from the database
    public void getVpAndMatrikelNumber() {
        db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("users");
        //uniqueId from mainActivity can be used
        usersRef.whereEqualTo("deviceId", uniqueID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String vps = "", matrikelNumber = "";
                for (QueryDocumentSnapshot document : task.getResult()) {
                    vps = document.getString("vps");
                    matrikelNumber = document.getString("matrikelNumber");
                }
                getVpAndMatNrListener.onAllDataReady(vps, matrikelNumber, dbDatesListHomeRepo, dbStudiesListHomeRepo);
            }
        }).addOnFailureListener(e -> getVpAndMatNrListener.onAllDataReady("", "", dbDatesListHomeRepo, dbStudiesListHomeRepo));
    }


    //Parameter: vp, matrikelnumber
    //Return values:
    //Saves the received database values in hashmap
    public void saveVpAndMatrikelNumber(String vp, String matrikelnumber) {
        Map<String, Object> updateData = new TreeMap<>();
        updateData.put("deviceId", uniqueID);
        updateData.put("vps", vp);
        updateData.put("matrikelNumber", matrikelnumber);

        db.collection("users").document(uniqueID)
                .update(updateData)
                .addOnSuccessListener(aVoid -> System.out.println("DocumentSnapshot successfully updated!"))
                .addOnFailureListener(e -> System.out.println("Error updating document"));
    }


    //Parameter: matrikelnumber
    //Return values:
    //Creates the request to receive data from database
    public String createGetRequest(String matrikelNumber) throws IOException {
        HttpURLConnection urlConnection = null;
        URL url = new URL("https://vp.software-engineering.education/" + matrikelNumber + "/vps");
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(Config.timeout_Request /* milliseconds */);
        urlConnection.setConnectTimeout(Config.timeout_Request /* milliseconds */);
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
