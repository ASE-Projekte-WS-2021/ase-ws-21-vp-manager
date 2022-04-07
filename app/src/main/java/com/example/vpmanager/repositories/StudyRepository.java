package com.example.vpmanager.repositories;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.example.vpmanager.helper.AccessDatabaseHelper;
import com.example.vpmanager.interfaces.SelectDateListener;
import com.example.vpmanager.interfaces.UnselectDateListener;
import com.example.vpmanager.interfaces.StudyDatesListener;
import com.example.vpmanager.interfaces.StudyDetailsListener;
import com.example.vpmanager.models.DateModel;
import com.example.vpmanager.models.StudyDetailModel;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class StudyRepository {

    private static StudyRepository instance;
    private FirebaseFirestore db;

    private StudyDetailModel studyDetailObject;
    private ArrayList<DateModel> datesArrayList = new ArrayList<>();

    private StudyDetailsListener studyDetailsListener;
    private StudyDatesListener studyDatesListener;
    private UnselectDateListener unselectDateListener;
    private SelectDateListener selectDateListener;


    //Parameter:
    //Return Values: instance of the repository class
    //Creates an instance of the repo and returns always the same one
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

    public void getAllStudyDates(String currentStudyId) {
        loadAllStudyDates(currentStudyId);
    }


    //Parameter: all listeners in fragment
    //Return values:
    //Sets the viewModel as the listener for the callbacks
    public void setFirestoreCallback(StudyDetailsListener studyDetailsListener, StudyDatesListener studyDatesListener,
                                     UnselectDateListener unselectDateListener, SelectDateListener selectDateListener) {
        this.studyDetailsListener = studyDetailsListener;
        this.studyDatesListener = studyDatesListener;
        this.unselectDateListener = unselectDateListener;
        this.selectDateListener = selectDateListener;
    }

    //Parameter: listener for study details, listener for date objects
    //Return values:
    //Firestore callback; set studyDates and studyDetails Listener
    public void setFirestoreCallback(StudyDetailsListener studyDetailsListener, StudyDatesListener studyDatesListener) {
        this.studyDetailsListener = studyDetailsListener;
        this.studyDatesListener = studyDatesListener;
    }


    //Parameter: id of current study object
    //Return values:
    //Sets values from database
    private void loadStudyDetails(String currentStudyId) {
        db = FirebaseFirestore.getInstance();
        CollectionReference studyDocRef = db.collection("studies");
        studyDetailObject = new StudyDetailModel();

        studyDocRef.whereEqualTo("id", currentStudyId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    if (document != null) {
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
                        studyDetailObject.setStudyState(document.getBoolean("studyStateClosed"));
                    } else {
                        Log.d("loadStudyDetails", "Error: the document does not exist!");
                    }
                    studyDetailsListener.onStudyDetailsReady(studyDetailObject);

                }
            } else {
                Log.d("loadStudyDetails", "Error:" + task.getException());
            }
        });
    }


    //Parameter: id of current study object, id of user
    //Return values:
    //Sets selected date values from database
    private void loadStudyDates(String currentStudyId, String currentUserId) {
        db = FirebaseFirestore.getInstance();
        CollectionReference datesRef = db.collection("dates");
        datesArrayList.clear();


        datesRef.whereEqualTo("studyId", currentStudyId).get()
                .addOnCompleteListener(task -> {
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
                                            document.getString("userId"),  //id of user who selected the date
                                            document.getBoolean("selected"), //boolean if selected or not
                                            document.getBoolean("participated"))

                            );
                        }
                        studyDatesListener.onStudyDatesReady(datesArrayList);
                    } else {
                        Log.d("loadStudyDates", "Error:" + task.getException());
                    }
                });
    }


    //Parameter: id of current study object
    //Return values:
    //Sets all date values from database
    private void loadAllStudyDates(String currentStudyId) {
        db = FirebaseFirestore.getInstance();
        CollectionReference datesRef = db.collection("dates");
        datesArrayList.clear();

        //All dates of one study are retrieved with this db call. Then, the results are filtered.
        datesRef.whereEqualTo("studyId", currentStudyId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            datesArrayList.add(
                                    new DateModel(
                                            document.getString("id"),      //id of the date
                                            document.getString("date"),    //the date itself
                                            document.getString("studyId"), //id of corresponding study
                                            document.getString("userId"),  //id of user who selected the date
                                            document.getBoolean("selected"),
                                            document.getBoolean("participated"))
                            );
                        }
                        studyDatesListener.onStudyDatesReady(datesArrayList);
                    } else {
                        Log.d("loadStudyDates", "Error:" + task.getException());
                    }
                });
    }


    //Parameter: id of date object, id of current study object
    //Return values:
    //Method for selecting dates from list; checks availability
    public void selectDate(String dateId, String currentUserId) {
        db = FirebaseFirestore.getInstance();
        final DocumentReference specificDate = db.collection("dates").document(dateId);

        db.runTransaction(transaction -> {
            DocumentSnapshot snapshot = transaction.get(specificDate);
            boolean selected = snapshot.getBoolean("selected");
            boolean updated = false;
            if (!selected) {
                transaction.update(specificDate, "selected", true, "userId", currentUserId);
                updated = true;
            } else {
                try {
                    throw new Exception("Dieser Termin ist nicht mehr verfügbar!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return updated;
        }).addOnSuccessListener(updated -> {
            //Transaction is always successful. Check for updated or not is made later
            selectDateListener.onDateSelected(updated);
        }).addOnFailureListener(e -> selectDateListener.onDateSelected(false));
    }


    //Parameter: id of date object
    //Return values:
    //Method for cancelling selected appointments
    public void unselectDate(String dateId) {
        AccessDatabaseHelper.getDateState(dateId, participated -> {
            if (!participated) {
                db = FirebaseFirestore.getInstance();
                db.collection("dates").document(dateId)
                        .update("selected", false, "userId", null, "participated", false)
                        .addOnSuccessListener(aVoid -> unselectDateListener.onDateUnselected(),
                                aVoid -> Log.d(TAG, "DocumentSnapshot successfully updated!"))
                        .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
            }
        });
    }
}