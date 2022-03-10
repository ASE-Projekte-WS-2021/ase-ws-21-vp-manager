package com.example.vpmanager.views;

import static com.example.vpmanager.views.mainActivity.uniqueID;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vpmanager.PA_ExpandableListDataPump;
import com.example.vpmanager.R;
import com.example.vpmanager.adapter.StudyListAdapter;
import com.example.vpmanager.models.StudyMetaInfoModel;
import com.example.vpmanager.viewmodels.FindStudyViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class findStudyFragment extends Fragment implements StudyListAdapter.OnStudyItemClickListener {

    private NavController navController;
    private RecyclerView studyList;
    private StudyListAdapter studyListAdapter;
    private FindStudyViewModel findStudyViewModel;


    private FirebaseFirestore db;
    private CollectionReference studiesRef;

    private ArrayList<ArrayList<String>> studyIdNameVpCat;

    private ArrayList<StudyMetaInfoModel> studyMetaInfosArrayList = new ArrayList<>();

    public findStudyFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find_study, container, false);
        initFindStudyFragmentComponents(view);
        //data is first retrieved from the viewModel...

        getData();
        //...and passed to the
        //
        // later.

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);
        super.onViewCreated(view, savedInstanceState);


    }


    private void getData() {
        getStudyInfosFromDB(new FirestoreCallback() {
            @Override
            public void onCallback() {
                setStudyMetaInfo();
                connectStudyListAdapter();
            }
        });
    }

    //Parameter: the fragment view
    //Return Values:
    //connects the recycler view with its id and provides the viewModel
    private void initFindStudyFragmentComponents(View view) {
        studyList = view.findViewById(R.id.recyclerViewFindStudyFragment);
        //findStudyFragment is now viewModel owner. maybe the activity should be? --> "requireActivity"
        findStudyViewModel = new ViewModelProvider(this).get(FindStudyViewModel.class);
    }

    //Parameter:
    //Return Values:
    //sets the adapter to connect the recyclerview with the data
    private void connectStudyListAdapter() {
        // Log.d("findStudyFragment", "Size of Array:" + findStudyViewModel.getStudyMetaInfo().size());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        studyListAdapter = new StudyListAdapter(requireActivity(),
                studyMetaInfosArrayList, this);
        studyList.setAdapter(studyListAdapter);
        studyList.setLayoutManager(linearLayoutManager);
    }

    //Parameter: the id of the study that was clicked on
    //Return Values:
    //navigates to the more detailed view of a study
    @Override
    public void onStudyClick(String studyId) {
        Bundle args = new Bundle();
        Log.d("findStudyFragment", "onStudyClick - studyId:" + studyId);
        args.putString("studyId", studyId);
        if (PA_ExpandableListDataPump.navigateToStudyCreatorFragment(uniqueID, studyId)) {
            navController.navigate(R.id.action_findStudyFragment_to_studyCreatorFragment, args);
        } else {
            navController.navigate(R.id.action_findStudyFragment_to_studyFragment, args);
        }                         //action_findStudyFragment_to_studyFragment
    }


    private interface FirestoreCallback {
        void onCallback();
    }

    private void getStudyInfosFromDB(FirestoreCallback firestoreCallback) {
        Log.d("StudyListRepository", "getStudyInfosFromDB start");
        db = FirebaseFirestore.getInstance();
        studiesRef = db.collection("studies");
        studyIdNameVpCat = new ArrayList<>();

        //all studies are retrieved, sorted alphabetically by their names
        studiesRef.orderBy("name", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //local ArrayList is stored in the big ArrayList for each existing study
                                ArrayList<String> idNameVphCat = new ArrayList<>();
                                idNameVphCat.add(0, document.getString("id"));
                                idNameVphCat.add(1, document.getString("name"));
                                idNameVphCat.add(2, document.getString("vps"));
                                idNameVphCat.add(3, document.getString("category"));
                                studyIdNameVpCat.add(idNameVphCat);
                            }
                            firestoreCallback.onCallback();
                        } else {
                            Log.d("getStudyInfosFromDB", "Error:" + task.getException());
                        }
                    }
                });
        Log.d("StudyListRepository", "getStudyInfosFromDB end");
    }

    private void setStudyMetaInfo() {
        Log.d("StudyListRepository", "setStudyMetaInfo start");
        //THE LIST NEEDS TO BE CLEARED BEFORE, BECAUSE THE REPO-INSTANCE IS THE SAME AND IT ISN'T CREATED NEW!
        studyMetaInfosArrayList.clear();
        //new data is stored in the list
        for (int i = 0; i < studyIdNameVpCat.size(); i++) {
            studyMetaInfosArrayList.add(
                    new StudyMetaInfoModel(
                            studyIdNameVpCat.get(i).get(0), //add Id
                            studyIdNameVpCat.get(i).get(1), //add Name
                            studyIdNameVpCat.get(i).get(2) + " " + "VP-Stunden", //add vps
                            studyIdNameVpCat.get(i).get(3)) //add category
            );
        }
        Log.d("StudyListRepository", "setStudyMetaInfo end");
    }

}
