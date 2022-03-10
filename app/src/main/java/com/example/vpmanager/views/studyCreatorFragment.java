package com.example.vpmanager.views;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

public class studyCreatorFragment extends Fragment {

    ListView dateList;
    String currentStudyId;
    String currentUserId;

    ArrayList<String> studyDetails;

    ArrayList<ArrayList<String>> studyDatesInfo;
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
        for (int i = 0; i < studyDatesInfo.size(); i++) {
            dateIds.add(studyDatesInfo.get(i).get(0));
            allDates.add(studyDatesInfo.get(i).get(1));
            userIdsOfDates.add(studyDatesInfo.get(i).get(2));
        }

        setAllDatesAdapter();
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
        studyDatesInfo = new ArrayList<>();

        //only the unselected dates should be retrieved here!
        datesRef.whereEqualTo("studyId", currentStudyId).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ArrayList<String> idDateUser = new ArrayList<>();

                                idDateUser.add(0, document.getString("id"));
                                idDateUser.add(1, document.getString("date"));
                                idDateUser.add(2, document.getString("userId"));
                                idDateUser.add(3, document.getString("selected"));

                                studyDatesInfo.add(idDateUser);
                            }
                            firestoreCallbackDates.onCallback(studyDatesInfo);
                        }
                    }
                });
    }

    private void setAllDatesAdapter() {
        availableDatesAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, allDates);
        dateList.setAdapter(availableDatesAdapter);

        for(int i = 0; i< dateList.getCount(); i++) {
            String date = (String) dateList.getItemAtPosition(i);

            for(int k = 0; k < allDates.size(); k++)
            {
                if(allDates.get(k).equals(date))
                {
                    String seleceted = studyDatesInfo.get(k).get(3);
                    if(Boolean.parseBoolean(seleceted))
                    {
                        dateList.getChildAt(i).setBackgroundColor(Color.GREEN);
                    }
                }
            }
        }
    }
}
