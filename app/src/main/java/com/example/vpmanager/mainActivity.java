package com.example.vpmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import java.util.UUID;

public class mainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayoutMain;
    private NavController navController;
    private NavigationView navigationViewMain;
    private NavHostFragment navHostFragment;

    private MaterialToolbar topAppBarMain;
    private AppBarConfiguration appBarConfiguration;

    //logic to register a new user (app installation) if necessary
    accessDatabase accessDatabase;
    public static String uniqueID = null;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUserId();
        setupNavigationView();
    }

    private void setupUserId() {
        uniqueID = createUserId(this);
        accessDatabase = new accessDatabase();
        registerNewUser();
    }

    private void setupNavigationView() {

        drawerLayoutMain = findViewById(R.id.drawerLayoutMain);
        topAppBarMain = findViewById(R.id.topAppBarMain);

        // ActionBar assumes complete ownership of the (Material) Toolbar after the following call.
        // Instead of the Toolbar, the ActionBar needs to be connected with the NavController!
        setSupportActionBar(topAppBarMain);
        navigationViewMain = findViewById(R.id.navigationViewMain);

        //get the navController like this because the code line below isn't working
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_main);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();
        //navController = Navigation.findNavController(this, R.id.nav_host_fragment_main);

        //Top level destinations are configured here. createStudyActivity and studyFragment should not be included!
        appBarConfiguration = new AppBarConfiguration.Builder(R.id.homeFragment, R.id.findStudyFragment,
                R.id.personalAccountFragment, R.id.ownStudyFragment).setDrawerLayout(drawerLayoutMain).build();

        // Handle Navigation item clicks
        // This works with no further action, if the menu and destination id’s match.
        NavigationUI.setupWithNavController(navigationViewMain, navController);

        // setupActionBarWithNavController needs to be called to connect the Action Bar.
        // The line below can't be used as the (Material) Toolbar is already set as the ActionBar!
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        //NavigationUI.setupWithNavController(topAppBarMain, navController, appBarConfiguration);
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Allows NavigationUI to support proper up navigation or the drawer layout
        // drawer menu, depending on the situation.
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
        //return NavigationUI.navigateUp(navController, drawerLayoutMain);
    }

    //Parameter:
    //Return values:
    //Registers a new user (installation of the app)  in the DB, if the user doesn't already exist
    private void registerNewUser() {
        String deviceID = createUserId(this);
        accessDatabase.createNewUser(deviceID);
    }

    //Parameter: context
    //Return values: uniqueID
    //Generates an unique id for every installation of the app.
    //Source: https://ssaurel.medium.com/how-to-retrieve-an-unique-id-to-identify-android-devices-6f99fd5369eb
    public synchronized static String createUserId(Context context) {
        if (uniqueID == null) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(
                    PREF_UNIQUE_ID, Context.MODE_PRIVATE);
            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString();
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString(PREF_UNIQUE_ID, uniqueID);
                editor.apply();
            }
        }
        return uniqueID;
    }
}
