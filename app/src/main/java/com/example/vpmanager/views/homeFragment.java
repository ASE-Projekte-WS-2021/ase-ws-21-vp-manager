package com.example.vpmanager.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.vpmanager.PA_ExpandableListDataPump;
import com.example.vpmanager.R;
import com.example.vpmanager.adapter.CustomListViewAdapterAppointments;
import com.google.android.flexbox.FlexboxLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class homeFragment extends Fragment {

    private NavController navController;

    FirebaseAuth firebaseAuth;

    private String jsonString, matrikelNumber;
    private ArrayList<String> upComingAppointments; 
    private float maxVpCount;

    private ListView nextDatesList;

    private ProgressBar progressBar;
    private TextView progressHint, missingVpsText, plannedCompletionDate, collectedVPText;
    private Button appointmentsButton, findStudyButton, registerMatrikelnummer;

    private LinearLayout progressBarLayout, appointmentLayout, defaultLayout, registerMatrikelnumberLayout;
    private ScrollView appointmentScrollView;

    private FlexboxLayout flexLayout;

    private double plannedVP, completedVP, participatedVP;

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
        loadData(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        userLoggedIn();
    }

    //Parameter: view is passed to setup view
    //Return values:
    //calls to database to get all the relevant data for this fragment
    private void loadData(View view) {

        PA_ExpandableListDataPump.getAllDates(finished -> {
            if (finished)
                PA_ExpandableListDataPump.getAllStudies(() -> PA_ExpandableListDataPump.getVPandMatrikelnumber((vps, mNumber) -> {
                    if (vps != null && mNumber != null && !vps.equals("") && !mNumber.equals("")) {
                        maxVpCount = Float.parseFloat(vps);
                        matrikelNumber = mNumber;
                    } else {
                        maxVpCount = 15;
                        matrikelNumber = "";
                    }
                    upComingAppointments = new ArrayList<>();
                    PA_ExpandableListDataPump.createListEntries();
                    setupViewElements(view);
                    setupProgressBar();
                    setUpDateList();
                    setTextsForOverView();
                    setLastAppointmentText();
                }));
        });
    }



    //Parameter:
    //Return values:
    //checks if the user is currently logged in. If not sends him to login screen
    private void userLoggedIn(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        });
        if(user == null) {
            navController.navigate(R.id.action_global_nestedGraphLoginRegistration);
        }
    }

    private void setupViewElements(View view) {
        progressBar = view.findViewById(R.id.homeOverViewProgressBar);
        progressHint = view.findViewById(R.id.homeProgressHint);
        nextDatesList = view.findViewById(R.id.homeAppointmentList);
        appointmentsButton = view.findViewById(R.id.homeAllAppointmetsButton);
        progressBarLayout = view.findViewById(R.id.homeBookedVpLayout);
        appointmentScrollView = view.findViewById(R.id.homeScrollview);
        appointmentLayout = view.findViewById(R.id.homeAppointmentLayout);
        missingVpsText = view.findViewById(R.id.homeMissingVpsText);
        findStudyButton = view.findViewById(R.id.homeFindStudyButton);
        collectedVPText = view.findViewById(R.id.homePlannedCompletionVps);
        plannedCompletionDate = view.findViewById(R.id.homePlannedCompletionDate);
        defaultLayout = view.findViewById(R.id.defaultLayoutForNewUsers);
        flexLayout = view.findViewById(R.id.homeFlexLayout);
        findStudyButton = view.findViewById(R.id.homeFindStudyButton);
        registerMatrikelnummer = view.findViewById(R.id.homeRegisterMatrikelnummer);
        registerMatrikelnumberLayout = view.findViewById(R.id.homeRegisterVPLayout);


        progressBar.setOnClickListener(v -> {
            CustomAlertDialog dialog = new CustomAlertDialog(homeFragment.this, "", "");
            dialog.show();
        });

        registerMatrikelnummer.setOnClickListener(v -> {
            CustomAlertDialog dialog = new CustomAlertDialog(homeFragment.this, "", "");
            dialog.show();
        });
        findStudyButton.setOnClickListener(v -> navController.navigate(R.id.action_homeFragment_to_findStudyFragment));
        appointmentsButton.setOnClickListener(v -> navController.navigate(R.id.action_homeFragment_to_upcomingAppointmentsFragment));
    }

    private void setTextsForOverView() {

        completedVP = 0;
        participatedVP = 0;
        plannedVP = 0;

        List<String> savedList = PA_ExpandableListDataPump.EXPANDABLE_LIST_DETAIL.get("Abgeschlossene Studien");
        if (savedList != null) {
            for (int i = 0; i < savedList.size(); i++) {
                String vps = savedList.get(i).split(";")[1];
                double studyVPS = 0;
                if (vps != null && !vps.equals(""))
                    studyVPS = Double.parseDouble(vps);
                completedVP += studyVPS;
            }
        }

        List<String> vpList = PA_ExpandableListDataPump.EXPANDABLE_LIST_DETAIL.get("Geplante Studien");
        if (vpList != null) {
            for (int i = 0; i < vpList.size(); i++) {
                String vps = vpList.get(i).split(";")[1];
                double studyVPS = 0;
                if (vps != null && !vps.equals(""))
                    studyVPS = Double.parseDouble(vps);
                plannedVP += studyVPS;
            }
        }
        List<String> passedVpList = PA_ExpandableListDataPump.EXPANDABLE_LIST_DETAIL.get("Teilgenommene Studien");
        if (passedVpList != null) {
            for (int i = 0; i < passedVpList.size(); i++) {
                String vps = passedVpList.get(i).split(";")[1];
                double studyVPS = 0;
                if (vps != null && !vps.equals(""))
                    studyVPS = Double.parseDouble(vps);
                participatedVP += studyVPS;
            }
        }

        float collectedVPS = (float) (completedVP + participatedVP + plannedVP);
        collectedVPText.setText(Float.toString(collectedVPS));
        float missingVPS = (float) maxVpCount - (float) (completedVP + participatedVP + plannedVP);
        if(missingVPS < 0)
        {
            missingVpsText.setText("0");
        }
        else {
            missingVpsText.setText("" + missingVPS);
        }
    }

    private void setupProgressBar() {

        progressBar.setMax((int) maxVpCount *100);

        if (!matrikelNumber.isEmpty()) {
            Thread getRequest = new Thread(() -> {
                try {
                    createGetRequest();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            getRequest.start();

            try {
                getRequest.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (jsonString != null && !jsonString.isEmpty() && jsonString.contains("matriculationNumber")) {
                System.out.println("JSON: " + jsonString);
                String[] entries = jsonString.split(",");
                if (entries.length == 3) {
                    String VPS = entries[1].split(":")[1];
                    float vpCount = Float.parseFloat(VPS);
                    if (vpCount > maxVpCount) {
                        progressBar.setProgress((int) maxVpCount * 100);
                    } else {
                        progressBar.setProgress((int) vpCount * 100);
                    }
                    progressHint.setText(vpCount + "/" + (int) maxVpCount);
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.showContextMenu();
                }
            }
        }
        else
        {
            hideProgressBar();
        }
    }

    private void hideProgressBar() {
        progressBarLayout.setVisibility(View.GONE);
        registerMatrikelnumberLayout.setVisibility(View.VISIBLE);
    }

    //Parameter:
    //Return values:
    //Creates and calls a getRequest to get the saved number of vps from the universities website. saves the count in a string
    private void createGetRequest() throws IOException {
        HttpURLConnection urlConnection = null;
        URL url = new URL("https://vp.software-engineering.education/" + matrikelNumber + "/vps");
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(10000 /* milliseconds */);
        urlConnection.setConnectTimeout(15000 /* milliseconds */);
        urlConnection.setDoOutput(true);
        urlConnection.connect();

        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        br.close();

        jsonString = sb.toString();
    }


    private void setUpDateList() {
        final List<String[]>[] arrivingDates = new List[]{null};
        arrivingDates[0] = PA_ExpandableListDataPump.getAllArrivingDates();
        finishSetupList(arrivingDates[0]);
    }

    //Parameter: data list from database call
    //Return Values:
    //converts and sorts the data. calls listview adapter to set up list entries
    private  void finishSetupList(List <String[]> dates){
        HashMap<String, String> getStudyIdByName = new HashMap<>();
        if (dates != null) {

            HashMap<String, String> sortingMap = new HashMap<>();

            for (String[] listEntry : dates) {
                String name = listEntry[0];
                String date = listEntry[1];
                String studyID = listEntry[2];

                if(date != null && name != null)
                {
                    sortingMap.put(date, name);
                    getStudyIdByName.put(name, studyID);
                }
            }

            int studyDisplayCount = 2;

             if(sortingMap.size() < 3)
            {
                disableAllAppointmentsButton();
                studyDisplayCount = 3;
            }
             /** Liste erst invertieren, dann kürzen => letztes Ergebnis in textView anzeigen  **/
            for (String key : sortingMap.keySet()) {
                if (upComingAppointments.size() < studyDisplayCount) {
                    upComingAppointments.add(sortingMap.get(key) + "\t\t" + key);
                }
            }
            Collections.reverse(upComingAppointments);
            nextDatesList.setAdapter(new CustomListViewAdapterAppointments(this.getContext(), this.getActivity(), navController, upComingAppointments, getStudyIdByName, "homeFragment"));
        }
    }

    private void disableAllAppointmentsButton() {
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                0f
        );
        LinearLayout.LayoutParams scrollViewParam = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                5f
        );
        appointmentScrollView.setLayoutParams(scrollViewParam);
        appointmentsButton.setLayoutParams(param);
        appointmentLayout.removeView(appointmentsButton);
        appointmentsButton.setVisibility(View.INVISIBLE);
        appointmentsButton.setClickable(false);
    }


    private void setLastAppointmentText()
    {
        String date = PA_ExpandableListDataPump.getLastParticipationDate();
        if(date == null)
        {
            loadDefaultorEmptyVersion();
        }
        else
            plannedCompletionDate.setText(date);
    }

    private void loadDefaultorEmptyVersion() {
        defaultLayout.setVisibility(View.VISIBLE);
        flexLayout.setVisibility(View.GONE);
    }

    public void closeDialog(String vps, String mNumber) {
        matrikelNumber = mNumber;
        maxVpCount = Float.parseFloat(vps);

        navController.navigate(R.id.action_global_homeFragment);
        PA_ExpandableListDataPump.saveVPandMatrikelnumber(vps, mNumber);
    }
}
