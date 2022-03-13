package com.example.vpmanager.views;

import static com.example.vpmanager.views.mainActivity.uniqueID;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.vpmanager.PA_ExpandableListDataPump;
import com.example.vpmanager.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class homeFragment extends Fragment {

    private NavController navController;
    private Button findStudyNavBtn, createStudyNavBtn, tempLoginBtn;
    FirebaseAuth firebaseAuth;

    private ListView arrivingDatesList;

    private HashMap<String, String> getStudyIdByName = new HashMap<>();

    public homeFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initHomeFragmentComponents(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        setNavCardClickListeners();
        setUpDateList();
        userLoggedIn();
    }

    private void userLoggedIn(){
        super.onStart();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        System.out.println("USERRRRRRRR: " + user);
        if(user == null) {
            navController.navigate(R.id.action_homeFragment_to_loginFragment);
        }
    }

    //Parameter: the fragment view
    //Return Values:
    //connects the view components with their ids
    private void initHomeFragmentComponents(View view) {
        arrivingDatesList = view.findViewById(R.id.listViewOwnArrivingStudyFragment);
        findStudyNavBtn = view.findViewById(R.id.findStudyBtn);
        createStudyNavBtn = view.findViewById(R.id.createStudyBtn);
        tempLoginBtn = view.findViewById(R.id.tempLogin);
    }

    //Parameter:
    //Return Values:
    //sets click listeners on the four buttons to open their corresponding fragments
    private void setNavCardClickListeners() {
        findStudyNavBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("homeFragment", "Navigate to: findStudyFragment");
                navController.navigate(R.id.action_homeFragment_to_findStudyFragment);
            }
        });
        createStudyNavBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("homeFragment", "Navigate to: createStudyActivity");
                navController.navigate(R.id.action_homeFragment_to_createStudyActivity);
            }
        });

        tempLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("homeFragment", "Navigate to: createStudyActivity");
                navController.navigate(R.id.action_homeFragment_to_loginFragment);
            }
        });
    }

    private void setUpDateList() {
        final List<String[]>[] arrivingDates = new List[]{null};

        PA_ExpandableListDataPump.getAllDates(new PA_ExpandableListDataPump.FirestoreCallbackDates() {
            @Override
            public void onCallback(boolean finished) {
                if (finished)
                    PA_ExpandableListDataPump.getAllStudies(new PA_ExpandableListDataPump.FirestoreCallbackStudy() {
                        @Override
                        public void onCallback() {
                            arrivingDates[0] = PA_ExpandableListDataPump.getAllArrivingDates();
                            finishSetupList(arrivingDates[0]);
                        }
                    });
            }
        });
    }

    private  void finishSetupList(List <String[]> dates){


        if (dates != null) {
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");

//            List<String[]> dates = arrivingDates[0];
            ArrayList<String> listEntries = new ArrayList<>();

            Map<Date, String> sortingMap = new TreeMap<>();

            for (String[] listEntry : dates) {
                String name = listEntry[0];
                String date = listEntry[1];
                String studyID = listEntry[2];

                Date studyDate = null;
                try {
                    studyDate = format.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                sortingMap.put(studyDate, name);
                //listEntries.add(name + "\t\t" + date);
                getStudyIdByName.put(name, studyID);
            }

            for (Date key : sortingMap.keySet()) {
                listEntries.add(sortingMap.get(key) + "\t\t" + key);
            }

            ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, listEntries);
            arrivingDatesList.setAdapter(arrayAdapter);
            arrivingDatesList.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String text = (String) arrivingDatesList.getItemAtPosition(position);
                    String name = text.split("\t\t")[0];

                    if (name != null) {
                        String studyId = getStudyIdByName.get(name);
                        //go to Study Detail View
                        Bundle args = new Bundle();
                        args.putString("studyId", studyId);
                        if(PA_ExpandableListDataPump.navigateToStudyCreatorFragment(uniqueID, studyId)) {
                            navController.navigate(R.id.action_homeFragment_to_studyCreatorFragment, args);
                        }
                        else
                        {
                            navController.navigate(R.id.action_homeFragment_to_studyFragment, args);
                        }
                    }
                }
            });
        }
    }
}
