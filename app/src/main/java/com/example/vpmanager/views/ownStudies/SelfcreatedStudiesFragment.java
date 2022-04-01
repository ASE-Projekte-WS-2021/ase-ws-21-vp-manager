package com.example.vpmanager.views.ownStudies;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vpmanager.R;
import com.example.vpmanager.adapter.StudyListAdapter;
import com.example.vpmanager.models.StudyMetaInfoModel;
import com.example.vpmanager.views.mainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SelfcreatedStudiesFragment extends Fragment implements StudyListAdapter.OnStudyItemClickListener {

    private NavController navController;
    private RecyclerView ownStudiesList;
    private StudyListAdapter ownStudyListAdapter;
    private ArrayList<StudyMetaInfoModel> ownStudiesModelArray;

    private TextView noOwnStudies;
    private ArrayList<ArrayList<String>> ownStudyIdNameVpCat;

    private FirebaseFirestore db;
    private CollectionReference studiesRef;

    //private ArrayList<String> studyNamesAndVps;
    //private ArrayList<String> studyIds;
    //private ListView studyList;

    public SelfcreatedStudiesFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selfcreated_study, container, false);
        setupListView(new FirestoreCallback() {
            @Override
            public void onCallback() { //ArrayList<ArrayList<String>> arrayList
                loadOwnStudiesData(view);
                Log.d("OwnStudies", ownStudyIdNameVpCat.toString());
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);
        super.onViewCreated(view, savedInstanceState);
    }

    //Parameter: the fragment view
    //Return Values:
    //loads all retrieved studies in the list view
    private void loadOwnStudiesData(View view) {
        //studyList = view.findViewById(R.id.listViewOwnStudyFragment);
        noOwnStudies = view.findViewById(R.id.ownStudiesInfoText);

        ownStudiesList = view.findViewById(R.id.recyclerViewOwnStudyFragment);
        ownStudiesModelArray = new ArrayList<>();
        //studyNamesAndVps = new ArrayList<>();
        //studyIds = new ArrayList<>();

        //Store the names and the vps in an ArrayList
        //Store the ids in the same order in another ArrayList
        for (int i = 0; i < ownStudyIdNameVpCat.size(); i++) {
            //studyNamesAndVps.add(ownStudyIdNameVpCat.get(i).get(1) + ownStudyIdNameVpCat.get(i).get(2) + getString(R.string.vpHours));
            //studyIds.add(ownStudyIdNameVpCat.get(i).get(0));
            ownStudiesModelArray.add(
                    new StudyMetaInfoModel(
                            ownStudyIdNameVpCat.get(i).get(0), //add Id
                            ownStudyIdNameVpCat.get(i).get(1), //add Name
                            ownStudyIdNameVpCat.get(i).get(2) + " " + "VP-Stunden", //add vps
                            ownStudyIdNameVpCat.get(i).get(3), //add category
                            ownStudyIdNameVpCat.get(i).get(4)) //add study type
            );
        }
        if (!ownStudyIdNameVpCat.isEmpty()) {
            noOwnStudies.setVisibility(View.GONE);
        } else {
            noOwnStudies.setVisibility(View.VISIBLE);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        ownStudyListAdapter = new StudyListAdapter(requireActivity(), ownStudiesModelArray, this);
        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.studylist_cart, studyNamesAndVps); //simple_list_item_1
        //studyList.setAdapter(arrayAdapter);
        ownStudiesList.setAdapter(ownStudyListAdapter);
        ownStudiesList.setLayoutManager(linearLayoutManager);
    }

    public interface FirestoreCallback {
        void onCallback(); //ArrayList<ArrayList<String>> arrayList
    }

    //Parameter: callback
    //Return Values:
    //gets all studies the user created
    private void setupListView(FirestoreCallback firestoreCallback) {

        String currentUserId = mainActivity.uniqueID;
        db = FirebaseFirestore.getInstance();
        studiesRef = db.collection(getString(R.string.collectionPathStudies));
        ownStudyIdNameVpCat = new ArrayList<>();

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
                                idNameVph.add(3, document.getString("category"));
                                idNameVph.add(3, document.getString("executionType"));
                                ownStudyIdNameVpCat.add(idNameVph);
                            }
                            firestoreCallback.onCallback(); //ownStudyIdNameVpCat
                        }
                    }
                });
    }

    //Parameter: the id of the ownStudy that was clicked on
    //Return Values:
    //navigates to the more detailed and changeable view of a study
    @Override
    public void onStudyClick(String studyId) {
        Bundle args = new Bundle();
        Log.d("OwnStudyFragment", "onStudyClick - studyId:" + studyId);
        args.putString("studyId", studyId);
        navController.navigate(R.id.action_ownStudyFragment_to_studyCreatorFragment, args);
    }
}
