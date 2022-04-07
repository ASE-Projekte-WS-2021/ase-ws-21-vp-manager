package com.example.vpmanager.helper;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;

import com.example.vpmanager.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class AccessDatabaseHelper extends Activity {

    public static FirebaseFirestore db;
    public static CollectionReference studiesRef;


    //Parameters: identifier of user, identifier of study
    //Return Values
    //checks if a given user is the creator of a given study
    public static void navigateToStudyCreatorFragment(String currentUserId, String currentStudyId, String source, NavController navController) {

        db = FirebaseFirestore.getInstance();
        studiesRef = db.collection("studies");
        studiesRef.whereEqualTo("id", currentStudyId).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Bundle args = new Bundle();
                            args.putString("studyId", currentStudyId);

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (Objects.requireNonNull(document.getString("creator")).equals(currentUserId)) {

                                    if (source.equals("HomeFragment"))
                                        navController.navigate(R.id.action_homeFragment_to_studyCreatorFragment, args);
                                    if (source.equals("FindStudyFragment"))
                                        navController.navigate(R.id.action_findStudyFragment_to_studyCreatorFragment, args);
                                    if (source.equals("UpcomingAppointmentsFragment"))
                                        navController.navigate(R.id.action_upcomingAppointmentsFragment_to_studyCreatorFragment, args);
                                    if (source.equals("OwnStudyFragment"))
                                        navController.navigate(R.id.action_ownStudyFragment_to_studyCreatorFragment, args);

                                } else {

                                    if (source.equals("HomeFragment"))
                                        navController.navigate(R.id.action_homeFragment_to_studyFragment, args);
                                    if (source.equals("FindStudyFragment"))
                                        navController.navigate(R.id.action_findStudyFragment_to_studyFragment, args);
                                    if (source.equals("UpcomingAppointmentsFragment"))
                                        navController.navigate(R.id.action_upcomingAppointmentsFragment_to_studyFragment, args);
                                    if (source.equals("OwnStudyFragment"))
                                        navController.navigate(R.id.action_ownStudyFragment_to_studyFragment, args);

                                }
                            }
                        }
                    }
                });
    }


    //Parameters: count of the vp, matrikelnumber of user
    //Return Values
    //updates the date Object with the boolean if the user participated
    public static void setDateState(String dateId, boolean participated) {
        Map<String, Object> updateData = new TreeMap<>();
        updateData.put("id", dateId);
        updateData.put("participated", participated);

        db.collection("dates").document(dateId)
                .update(updateData)
                .addOnSuccessListener(aVoid -> System.out.println("DocumentSnapshot successfully updated!"))
                .addOnFailureListener(e -> System.out.println("Error updating document"));

    }

    public static void getDateState(String dateId, FirestoreCallbackDateState firestoreCallbackDateState) {
        db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("dates");
        usersRef.whereEqualTo("id", dateId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                boolean participated = false;

                for (QueryDocumentSnapshot document : task.getResult()) {
                    participated = document.getBoolean("participated");
                }
                firestoreCallbackDateState.onCallback(participated);
            }
        }).addOnFailureListener(e -> firestoreCallbackDateState.onCallback(false));
    }


    //Parameters: studyId, boolean
    //Return Values
    //updates the study Object with the boolean if the study has been closed
    public static void setStudyState(String studyId, boolean closed) {
        Map<String, Object> updateData = new TreeMap<>();
        updateData.put("id", studyId);
        updateData.put("studyStateClosed", closed);

        db.collection("studies").document(studyId)
                .update(updateData)
                .addOnSuccessListener(aVoid -> System.out.println("DocumentSnapshot successfully updated!"))
                .addOnFailureListener(e -> System.out.println("Error updating document"));

    }


    public interface FirestoreCallbackDateState {
        void onCallback(boolean participated);
    }

}