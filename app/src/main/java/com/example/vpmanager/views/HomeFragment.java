package com.example.vpmanager.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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


    private ListView nextDatesList;
    private ProgressBar progressBar;
    private TextView progressHint, missingVpsText, plannedCompletionDate, collectedVPText;
    private Button appointmentsButton, findStudyButton, registerMatrikelnummer;

    private LinearLayout progressBarLayout, defaultLayout, registerMatrikelnumberLayout;
    private ScrollView appointmentScrollView;
    private FlexboxLayout flexLayout;
    private RelativeLayout appointmentLayout;

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
        mainActivity.currentFragment = "home";
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
            navController.navigate(R.id.action_global_loginFragment);
        }
    }


    //Parameter:
    //Return values:
    //Sets ViewModel and prepares Repository
    private void prepareViewModel() {
        //mainActivity is viewModelStoreOwner
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        homeViewModel.homeFragment = this;
        homeViewModel.prepareRepo();
    }


    //Parameter: view
    //Return values:
    //Initialize fragment views
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


    //Parameter: view
    //Return values:
    //Set Listeners and alert dialogs
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


    //Parameter: maximum
    //Return values:
    //Set maximal progress bar value
    public void setProgressBarMaximum(int maximum) {
        progressBar.setMax(maximum);
    }

    //Parameter: view
    //Return values:
    //Set progress bar properties
    public void setProgressBarProgress(int progress, String text) {
        progressBar.setProgress(progress);
        progressHint.setText(text);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.showContextMenu();
    }

    //Parameter: view
    //Return values:
    //Hides progressbar and sets the other view
    public void hideProgressBar() {
        progressBarLayout.setVisibility(View.GONE);
        registerMatrikelnumberLayout.setVisibility(View.VISIBLE);
    }


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

    public void setPlannedCompletionDate(String completionDate) {
        plannedCompletionDate.setText(completionDate);
    }


    public void disableAllAppointmentsButton() {
        appointmentLayout.removeView(appointmentsButton);
        appointmentsButton.setVisibility(View.GONE);
        appointmentsButton.setClickable(false);
    }


    //Parameter:
    //Return values:
    //Sets default Layout
    public void loadDefaultorEmptyVersion() {
        defaultLayout.setVisibility(View.VISIBLE);
        flexLayout.setVisibility(View.GONE);
    }


    //Parameter: vps, mNumber
    //Return values:
    //Navigate from dialog to fragment
    public void closeDialog(String vps, String mNumber) {
        homeViewModel.saveVpAndMatrikelNumber(vps, mNumber);
        navController.navigate(R.id.action_global_homeFragment);
    }

}
