package com.example.vpmanager.views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.vpmanager.R;
import com.example.vpmanager.accessDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class studyCreatorFragment extends Fragment {

    ListView dateList;
    String currentStudyId;
    String currentUserId;

    ArrayList<String> studyDetails;

    ArrayList<ArrayList<String>> freeAndOwnDatesInfo;
    ArrayList<String> allDates;
    ArrayList<String> dateIds;
    ArrayList<String> userIdsOfDates;

    ArrayList<String> savedDateItem;
    ArrayAdapter availableDatesAdapter;
    ArrayAdapter savedDateAdapter;

    FirebaseFirestore db;
    DocumentReference studyRef;
    CollectionReference datesRef;
    com.example.vpmanager.accessDatabase accessDatabase = new accessDatabase();

    TextView headerText;
    TextView description;
    TextView vpValue;
    TextView category;
    TextView studyType;
    TextView remoteData;
    TextView localData;
    TextView contactInfo;

    ImageView editButton;

    NavController navController;

    public studyCreatorFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_creator_study, container, false);
        getRequiredInfos();
        navController = Navigation.findNavController(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupStudyDetails(new studyCreatorFragment.FirestoreCallbackStudy() {
            @Override
            public void onCallback(ArrayList<String> arrayList) {
                loadStudyData(view);
            }
        });
        setupDateListView(new studyCreatorFragment.FirestoreCallbackDates() {
            @Override
            public void onCallback(ArrayList<ArrayList<String>> arrayList) {
                loadDatesData(view);
            }
        });
    }

    private void getRequiredInfos() {
        //Get the studyId early
        currentStudyId = getArguments().getString("studyId");
        currentUserId = mainActivity.createUserId(getActivity());
        savedDateItem = new ArrayList<>();
        savedDateItem.add(getString(R.string.dropDateView));
    }

    public interface FirestoreCallbackStudy {
        void onCallback(ArrayList<String> arrayList);
    }

    public interface FirestoreCallbackDates {
        void onCallback(ArrayList<ArrayList<String>> arrayList);
    }

    private void loadStudyData(View view) {

        headerText = view.findViewById(R.id.studyFragmentHeader);
        description = view.findViewById(R.id.descriptionStudyFragment);
        vpValue = view.findViewById(R.id.vpValueStudyFragment);
        category = view.findViewById(R.id.categoryStudyFragment);
        studyType = view.findViewById(R.id.studyTypeStudyFragment);
        editButton = view.findViewById(R.id.editOwnStudyButton);
        //Textview for further studyType data (depending on type)
        remoteData = view.findViewById(R.id.remoteStudyStudyFragment);
        localData = view.findViewById(R.id.localStudyStudyFragment);
        contactInfo = view.findViewById(R.id.contactInformationStudyFragment);

        //store DB Data Strings in textViews
        headerText.setText(studyDetails.get(0));
        description.setText(studyDetails.get(1));
        String vpHours = studyDetails.get(2) + " VP";
        vpValue.setText(vpHours);
        contactInfo.setText(studyDetails.get(9));
        category.setText(studyDetails.get(3));
        studyType.setText(studyDetails.get(4));

        // set further studyType data
        if (studyDetails.get(4).equals(getString(R.string.remoteString))) {
            remoteData.setText(studyDetails.get(5));
        } else {
            String locationString = studyDetails.get(6) + "\t\t" + studyDetails.get(7) + "\t\t" + studyDetails.get(8);
            localData.setText(locationString);
        }
    }

    private void loadDatesData(View view) {
        dateList = view.findViewById(R.id.listViewDatesStudyFragment);
        allDates = new ArrayList<>();
        dateIds = new ArrayList<>();
        userIdsOfDates = new ArrayList<>();

        //store ids and dates in different ArrayLists
        for (int i = 0; i < freeAndOwnDatesInfo.size(); i++) {
            dateIds.add(freeAndOwnDatesInfo.get(i).get(0));
            allDates.add(freeAndOwnDatesInfo.get(i).get(1));
            userIdsOfDates.add(freeAndOwnDatesInfo.get(i).get(2));
        }

        //makes date selection unavailable if user already picked a date from this study
        if (userIdsOfDates.contains(mainActivity.createUserId(getActivity()))) { //before: homeActivity.createUserId(this);
            setSavedDateAdapter();
        } else {
            setAllDatesAdapter();
            setupClickListener();
        }
    }

    private void setupStudyDetails(studyCreatorFragment.FirestoreCallbackStudy firestoreCallbackStudy) {

        db = FirebaseFirestore.getInstance();
        studyRef = db.collection(getString(R.string.collectionPathStudies)).document(currentStudyId);
        studyDetails = new ArrayList<>();

        studyRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        studyDetails.add(0, document.getString("name"));
                        studyDetails.add(1, document.getString("description"));
                        studyDetails.add(2, document.getString("vps"));
                        studyDetails.add(3, document.getString("category"));
                        studyDetails.add(4, document.getString("executionType"));
                        studyDetails.add(5, document.getString("platform"));
                        studyDetails.add(6, document.getString("location"));
                        studyDetails.add(7, document.getString("street"));
                        studyDetails.add(8, document.getString("room"));
                        studyDetails.add(9, document.getString("contact"));

                        firestoreCallbackStudy.onCallback(studyDetails);
                    }
                }
            }
        });
    }

    private void setupDateListView(studyCreatorFragment.FirestoreCallbackDates firestoreCallbackDates) {

        db = FirebaseFirestore.getInstance();
        datesRef = db.collection(getString(R.string.collectionPathDates));
        freeAndOwnDatesInfo = new ArrayList<>();

        //only the unselected dates should be retrieved here!
        datesRef.whereEqualTo("studyId", currentStudyId).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ArrayList<String> idDateUser = new ArrayList<>();
                                if (Objects.equals(document.getBoolean("selected"), true) &&
                                        !Objects.equals(document.getString("userId"), currentUserId)) {
                                    continue;
                                }
                                idDateUser.add(0, document.getString("id"));
                                idDateUser.add(1, document.getString("date"));
                                idDateUser.add(2, document.getString("userId"));

                                freeAndOwnDatesInfo.add(idDateUser);
                            }
                            firestoreCallbackDates.onCallback(freeAndOwnDatesInfo);
                        }
                    }
                });
    }

    private void setupClickListener() {
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("studyId", currentStudyId);
                navController.navigate(R.id.action_studyFragment_to_studyCreatorFragment, args);
            }
        });
    }


    private void setSavedDateAdapter() {
        savedDateAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, savedDateItem);
        dateList.setAdapter(savedDateAdapter);
    }

    private void setAllDatesAdapter() {
        availableDatesAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, allDates);
        dateList.setAdapter(availableDatesAdapter);
    }

    private void selectDate(String dateId) {

        String userId = mainActivity.createUserId(getActivity()); //before: homeActivity.createUserId(this);

        Map<String, Object> updateDataMap = new HashMap<>();
        updateDataMap.put("selected", true);
        updateDataMap.put("userId", userId);

        accessDatabase.selectDate(updateDataMap, dateId);
        reloadFragment();
    }

    private void unSelectDate() {
        int datePosition = userIdsOfDates.indexOf(mainActivity.createUserId(getActivity())); //before: homeActivity.createUserId(this);
        String dateId = dateIds.get(datePosition);
        accessDatabase.unselectDate(dateId);
        reloadFragment();
    }

    private void reloadFragment() {
        Bundle args = new Bundle();
        args.putString("studyId", currentStudyId);

        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setReorderingAllowed(true);
        transaction.replace(R.id.nav_host_fragment_main, studyCreatorFragment.class, args);
        transaction.commit();
    }

    public ArrayList<String> getStudyDetails() {
        return studyDetails;
    }
}
