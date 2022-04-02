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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.vpmanager.R;
import com.example.vpmanager.adapter.CustomListViewAdapterAppointments;
import com.example.vpmanager.viewmodels.HomeViewModel;
import com.google.android.flexbox.FlexboxLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private NavController navController;
    private HomeViewModel homeViewModel;

    /*
    private String jsonString; im viewModel
    private ArrayList<String> upComingAppointments;
    private String matrikelNumber; im viewModel
    private float maxVpCount; im viewModel (sumVpsHome)
     */

    private ListView nextDatesList;
    private ProgressBar progressBar;
    private TextView progressHint, missingVpsText, plannedCompletionDate, collectedVPText;
    private Button appointmentsButton, findStudyButton, registerMatrikelnummer;

    private LinearLayout progressBarLayout, defaultLayout, appointmentLayout, registerMatrikelnumberLayout;
    private ScrollView appointmentScrollView;
    private FlexboxLayout flexLayout;

    public HomeFragment() {
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
        prepareViewModel();
        setupView(view);
        homeViewModel.getDatesStudiesVpsAndMatrikelNumberFromDb();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        userLoggedIn();
    }

    //Parameter:
    //Return values:
    //checks if the user is currently logged in. If not sends him to login screen
    private void userLoggedIn() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        });
        if (user == null) {
            navController.navigate(R.id.action_global_nestedGraphLoginRegistration);
        }
    }

    private void prepareViewModel() {
        //mainActivity is viewModelStoreOwner
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.homeFragment = this;
        homeViewModel.prepareRepo();
    }

    private void setupView(View view) {
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
        setClickListener();
    }

    private void setClickListener() {
        progressBar.setOnClickListener(v -> {
            CustomAlertDialog dialog = new CustomAlertDialog(HomeFragment.this, "", "");
            dialog.show();
        });

        registerMatrikelnummer.setOnClickListener(v -> {
            CustomAlertDialog dialog = new CustomAlertDialog(HomeFragment.this, "", "");
            dialog.show();
        });
        findStudyButton.setOnClickListener(v -> navController.navigate(R.id.action_homeFragment_to_findStudyFragment));
        appointmentsButton.setOnClickListener(v -> navController.navigate(R.id.action_homeFragment_to_upcomingAppointmentsFragment));
    }

    //Parameter: view is passed to setup view
    //Return values:
    //calls to database to get all the relevant data for this fragment
    //loadData
    /*
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
                    //upComingAppointments = new ArrayList<>();
                    //PA_ExpandableListDataPump.createListEntries(); done in viewModel
                    //setupViewElements(view); done
                    //setupProgressBar(); done in viewModel
                    //setUpDateList(); done in viewModel
                    //setTextsForOverView(); done in viewModel
                    //setLastAppointmentText(); done in viewModel
                }));
        });
    }
     */

    //setupViewElements
    /*
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
            CustomAlertDialog dialog = new CustomAlertDialog(HomeFragment.this, "", "");
            dialog.show();
        });

        registerMatrikelnummer.setOnClickListener(v -> {
            CustomAlertDialog dialog = new CustomAlertDialog(HomeFragment.this, "", "");
            dialog.show();
        });
        findStudyButton.setOnClickListener(v -> navController.navigate(R.id.action_homeFragment_to_findStudyFragment));
        appointmentsButton.setOnClickListener(v -> navController.navigate(R.id.action_homeFragment_to_upcomingAppointmentsFragment));
    }
     */

    //setTextsForOverView
    /*
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
     */

    //setupProgressBar
    /*
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
     */

    public void setProgressBarMaximum(int maximum) {
        progressBar.setMax(maximum);
    }

    public void setProgressBarProgress(int progress, String text) {
        progressBar.setProgress(progress);
        progressHint.setText(text);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.showContextMenu();
    }

    public void hideProgressBar() {
        progressBarLayout.setVisibility(View.GONE);
        registerMatrikelnumberLayout.setVisibility(View.VISIBLE);
    }

    //Parameter:
    //Return values:
    //Creates and calls a getRequest to get the saved number of vps from the universities website. saves the count in a string
    //createGetRequest
    /*
    private void createGetRequest() throws IOException {
        HttpURLConnection urlConnection = null;
        URL url = new URL("https://vp.software-engineering.education/" + matrikelNumber + "/vps");
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(10000); //milliseconds
        urlConnection.setConnectTimeout(15000); //milliseconds
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
     */

    //setUpDateList
    /*
    private void setUpDateList() {
        final List<String[]>[] arrivingDates = new List[]{null};
        arrivingDates[0] = PA_ExpandableListDataPump.getAllArrivingDates();
        finishSetupList(arrivingDates[0]);
    }
     */

    //Parameter: data list from database call
    //Return Values:
    //converts and sorts the data. calls listview adapter to set up list entries
    //finishSetupList
    /*
    private  void finishSetupList(List <String[]> dates){
        HashMap<String, String> getStudyIdByName = new HashMap<>();

        ArrayList<String> upComingAppointments = new ArrayList<>();

        if (dates != null) {

            HashMap<String, String> dateNameList = new HashMap<>();

            for (String[] listEntry : dates) {
                String name = listEntry[0];
                String date = listEntry[1];
                String studyID = listEntry[2];

                if(date != null && name != null)
                {
                    dateNameList.put(date, name);
                    getStudyIdByName.put(name, studyID);
                }
            }

            dateNameList = sortList(dateNameList);

            int studyDisplayCount = 2;

            if(dateNameList.size() < 3)
            {
                disableAllAppointmentsButton();
                studyDisplayCount = 3;
            }

            for (String key : dateNameList.keySet()) {
                if (upComingAppointments.size() < studyDisplayCount) {
                    upComingAppointments.add(dateNameList.get(key) + "\t\t" + key);
                }
            }
            //Collections.reverse(upComingAppointments);
            nextDatesList.setAdapter(new CustomListViewAdapterAppointments(this.getContext(), navController, upComingAppointments, getStudyIdByName, "HomeFragment"));
        }
    }
     */

    public void setListViewAdapter(CustomListViewAdapterAppointments adapter) {
        nextDatesList.setAdapter(adapter);
    }

    public NavController getNavController() {
        return navController;
    }

    public void setMissingAndCollectedVpText(String missing, String collected) {
        missingVpsText.setText(missing);
        collectedVPText.setText(collected);
    }

    public void setPlannedCompletionDate(String completionDate){
        plannedCompletionDate.setText(completionDate);
    }

    public void disableAllAppointmentsButton() {
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

    public void loadDefaultorEmptyVersion() {
        defaultLayout.setVisibility(View.VISIBLE);
        flexLayout.setVisibility(View.GONE);
    }

    //setLastAppointmentText
    /*
    private void setLastAppointmentText() {

        String date = PA_ExpandableListDataPump.getLastParticipationDate();
        if (date == null) {
            loadDefaultorEmptyVersion();
        } else {
            plannedCompletionDate.setText(date);
        }
        //String date = PA_ExpandableListDataPump.getLastParticipationDate();
    }
     */

    public void closeDialog(String vps, String mNumber) {
        //maxVpCount = Float.parseFloat(vps);
        //matrikelNumber = mNumber;
        homeViewModel.saveVpAndMatrikelNumber(vps, mNumber);
        navController.navigate(R.id.action_global_homeFragment);
        //PA_ExpandableListDataPump.saveVPandMatrikelnumber(vps, mNumber);
    }

    //sortList
    /*
    private HashMap<String, String> sortList(HashMap<String, String> toSort) {
        HashMap<String, String> list = new HashMap<>();

        String[][] dateList = new String[toSort.size()][2];
        int position = 0;
        for(String key: toSort.keySet()) {
            dateList[position][0] = key;
            dateList[position][1] = toSort.get(key);
            position++;
        }

        for (int i = 0; i < dateList.length; i++) {
            for (int k = 0; k < dateList.length - 1; k++) {

                String date1 = dateList[k][0].substring(dateList[k][0].indexOf(",") + 2);
                String date2 = dateList[k + 1][0].substring(dateList[k + 1][0].indexOf(",") + 2);

                date1 = date1.replaceAll("um", "");
                date1 = date1.replaceAll("Uhr", "");
                date2 = date2.replaceAll("um", "");
                date2 = date2.replaceAll("Uhr", "");

                Format format = new SimpleDateFormat("dd.MM.yyyy hh:mm");
                try {

                    Date d1_Date = (Date) format.parseObject(date1);
                    Date d2_Date = (Date) format.parseObject(date2);

                    if (d2_Date.before(d1_Date)) {
                        String[] tempDate = dateList[k];
                        dateList[k] = dateList[k + 1];
                        dateList[k + 1] = tempDate;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        list.clear();
        for(String[] date: dateList) {
            list.put(date[0], date[1]);
        }
        return list;
    }
     */
}
