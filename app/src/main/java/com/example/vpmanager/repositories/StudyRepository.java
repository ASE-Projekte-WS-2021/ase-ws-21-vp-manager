package com.example.vpmanager.repositories;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.vpmanager.interfaces.SelectUnselectDateListener;
import com.example.vpmanager.interfaces.StudyDatesListener;
import com.example.vpmanager.interfaces.StudyDetailsListener;
import com.example.vpmanager.models.DateModel;
import com.example.vpmanager.models.StudyDetailModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class StudyRepository {

    private static StudyRepository instance;
    private FirebaseFirestore db;

    private StudyDetailModel studyDetailObject;
    private ArrayList<DateModel> datesArrayList = new ArrayList<>();

    private StudyDetailsListener studyDetailsListener;
    private StudyDatesListener studyDatesListener;
    private SelectUnselectDateListener selectUnselectDateListener;

    public static StudyRepository getInstance() {
        if (instance == null) {
            instance = new StudyRepository();
        }
        return instance;
    }

    public void getStudyDetails(String currentStudyId) {
        loadStudyDetails(currentStudyId);
    }

    public void getStudyDates(String currentStudyId, String currentUserId) {
        loadStudyDates(currentStudyId, currentUserId);
    }

    //Sets the viewModel as the listener for the callbacks
    public void setFirestoreCallback(StudyDetailsListener studyDetailsListener, StudyDatesListener studyDatesListener,
                                     SelectUnselectDateListener selectUnselectDateListener) {
        this.studyDetailsListener = studyDetailsListener;
        this.studyDatesListener = studyDatesListener;
        this.selectUnselectDateListener = selectUnselectDateListener;
    }

    private void loadStudyDetails(String currentStudyId) {
        db = FirebaseFirestore.getInstance();
        DocumentReference studyDocRef = db.collection("studies").document(currentStudyId);
        studyDetailObject = new StudyDetailModel();

        studyDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        studyDetailObject.setId(document.getString("id"));
                        studyDetailObject.setName(document.getString("name"));
                        studyDetailObject.setDescription(document.getString("description"));
                        studyDetailObject.setVps(document.getString("vps"));
                        studyDetailObject.setContactOne(document.getString("contact"));
                        studyDetailObject.setContactTwo(document.getString("contact2"));
                        studyDetailObject.setContactThree(document.getString("contact3"));
                        studyDetailObject.setCategory(document.getString("category"));
                        studyDetailObject.setExecutionType(document.getString("executionType"));
                        studyDetailObject.setRemotePlatformOne(document.getString("platform"));
                        studyDetailObject.setRemotePlatformTwo(document.getString("platform2"));
                        studyDetailObject.setLocation(document.getString("location"));
                        studyDetailObject.setStreet(document.getString("street"));
                        studyDetailObject.setRoom(document.getString("room"));
                    } else {
                        Log.d("loadStudyDetails", "Error: the document does not exist!");
                    }
                    studyDetailsListener.onStudyDetailsReady(studyDetailObject);
                } else {
                    Log.d("loadStudyDetails", "Error:" + task.getException());
                }
            }
        });
    }

    private void loadStudyDates(String currentStudyId, String currentUserId) {
        db = FirebaseFirestore.getInstance();
        CollectionReference datesRef = db.collection("dates");
        datesArrayList.clear();

        //All dates of one study are retrieved with this db call. Then, the results are filtered.
        datesRef.whereEqualTo("studyId", currentStudyId).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (Objects.equals(document.getBoolean("selected"), true) &&
                                        !Objects.equals(document.getString("userId"), currentUserId)) {
                                    continue;
                                }
                                datesArrayList.add(
                                        new DateModel(
                                                document.getString("id"),      //id of the date
                                                document.getString("date"),    //the date itself
                                                document.getString("studyId"), //id of corresponding study
                                                document.getString("userId"))  //id of user who selected the date
                                );
                            }
                            studyDatesListener.onStudyDatesReady(datesArrayList);
                        } else {
                            Log.d("loadStudyDates", "Error:" + task.getException());
                        }
                    }
                });
    }

    public void selectDate(String dateId, String currentUserId) {
        db = FirebaseFirestore.getInstance();
        db.collection("dates").document(dateId)
                .update("selected", true, "userId", currentUserId)
                .addOnSuccessListener(aVoid -> selectUnselectDateListener.onDateActionFinished(),
                        aVoid -> Log.d(TAG, "DocumentSnapshot successfully updated!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
    }

    public void unselectDate(String dateId) {
        db = FirebaseFirestore.getInstance();
        db.collection("dates").document(dateId)
                .update("selected", false, "userId", null)
                .addOnSuccessListener(aVoid -> selectUnselectDateListener.onDateActionFinished(),
                        aVoid -> Log.d(TAG, "DocumentSnapshot successfully updated!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
    }
}