package com.example.vpmanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class findStudyFragment extends Fragment {

    private ListView studyList;
    private ArrayList<ArrayList<String>> studyIdNameVp;
    private ArrayList<String> studyNamesAndVps;
    private ArrayList<String> studyIds;
    private FirebaseFirestore db;
    private CollectionReference studiesRef;

    private NavController navController;

    public findStudyFragment() {
        //Empty public constructor required?
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_find_study, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupListView(new findStudyFragment.FirestoreCallback() {
            @Override
            public void onCallback(ArrayList<ArrayList<String>> arrayList) {
                loadAllStudiesData(view);
            }
        });
    }

    //unterschied zur activity: das view wurde übergeben
    private void loadAllStudiesData(View view) {
        navController = Navigation.findNavController(view);

        studyList = view.findViewById(R.id.listViewFindStudyFragment);
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
        setupClickListener();
    }

    public interface FirestoreCallback {
        void onCallback(ArrayList<ArrayList<String>> arrayList);
    }

    private void setupListView(findStudyFragment.FirestoreCallback firestoreCallback) {

        db = FirebaseFirestore.getInstance();
        studiesRef = db.collection(getString(R.string.collectionPathStudies));
        studyIdNameVp = new ArrayList<>();

        studiesRef.orderBy("name", Query.Direction.DESCENDING)
                .get()
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

    private void setupClickListener() {

        studyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //code startet studyFragment
                Bundle args = new Bundle();
                String studyId = studyIds.get(position);
                args.putString("studyId", studyId);
                navController.navigate(R.id.action_findStudyFragment_to_studyFragment, args);
                /*
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setReorderingAllowed(true);
                transaction.replace(R.id.nav_host_fragment_main, studyFragment.class, args);
                transaction.commit();
                 */
            }
        });

    }
}
