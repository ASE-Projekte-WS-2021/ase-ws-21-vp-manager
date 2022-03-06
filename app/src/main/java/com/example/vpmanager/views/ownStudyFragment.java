package com.example.vpmanager.views;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.vpmanager.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ownStudyFragment extends Fragment {

    ListView studyList;
    ArrayList<ArrayList<String>> studyIdNameVp;
    ArrayList<String> studyNamesAndVps;
    ArrayList<String> studyIds;
    FirebaseFirestore db;
    CollectionReference studiesRef;

    public ownStudyFragment() {
        //Empty public constructor required?
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_own_study, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupListView(new ownStudyFragment.FirestoreCallback() {
            @Override
            public void onCallback(ArrayList<ArrayList<String>> arrayList) {
                loadOwnStudiesData(view);
                Log.d("OwnStudies", studyNamesAndVps.toString());
            }
        });
    }

    private void loadOwnStudiesData(View view) {
        studyList = view.findViewById(R.id.listViewOwnStudyFragment);
        studyNamesAndVps = new ArrayList<>();
        studyIds = new ArrayList<>();
        //Store the names and the vps in an ArrayList
        //Store the ids in the same order in another ArrayList
        for (int i = 0; i < studyIdNameVp.size(); i++) {
            studyNamesAndVps.add(studyIdNameVp.get(i).get(1) + "\t\t(" + studyIdNameVp.get(i).get(2) + getString(R.string.vpHours));
            studyIds.add(studyIdNameVp.get(i).get(0));
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, studyNamesAndVps);
        studyList.setAdapter(arrayAdapter);
        //setupClickListener();
    }

    public interface FirestoreCallback {
        void onCallback(ArrayList<ArrayList<String>> arrayList);
    }

    //gets all studies the user created and adds them in a list like the one with all studies
    private void setupListView(ownStudyFragment.FirestoreCallback firestoreCallback) {

        String currentUserId = mainActivity.uniqueID;
        db = FirebaseFirestore.getInstance();
        studiesRef = db.collection(getString(R.string.collectionPathStudies));
        studyIdNameVp = new ArrayList<>();

        studiesRef.whereEqualTo("creator", currentUserId).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //local ArrayList
                                ArrayList<String> idNameVph = new ArrayList<>();
                                idNameVph.add(0, document.getString("id"));
                                idNameVph.add(1, document.getString("name"));
                                idNameVph.add(2, document.getString("vps"));
                                studyIdNameVp.add(idNameVph);
                            }
                            firestoreCallback.onCallback(studyIdNameVp);
                        }
                    }
                });
    }
}
