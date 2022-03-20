package com.example.vpmanager.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.vpmanager.DrawerController;
import com.example.vpmanager.R;
import com.example.vpmanager.AccessDatabase;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class mainActivity extends AppCompatActivity implements DrawerController {

    public DrawerLayout drawerLayoutMain;
    private NavController navController;
    private NavigationView navigationViewMain;
    private NavHostFragment navHostFragment;
    FirebaseAuth firebaseAuth;

    private MaterialToolbar topAppBarMain;
    private AppBarConfiguration appBarConfiguration;

    //logic to register a new user (app installation) if necessary
    AccessDatabase accessDatabase;
    public static String uniqueID = null;
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUserId();
        setupNavigationView();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    //closes the navigation drawer when returning to mainActivity
    @Override
    protected void onResume() {
        super.onResume();
        drawerLayoutMain.close();
    }

    private void setupUserId() {
        //uniqueID = createUserId(this);
        uniqueID = "default";
        accessDatabase = new AccessDatabase();
        registerNewUser();
    }

    //Parameter:
    //Return Values:
    //sets up all navigation components of the app and connects the navigation graph
    private void setupNavigationView() {
        Log.d("mainActivity", "setupNavigationView start");
        drawerLayoutMain = findViewById(R.id.drawerLayoutMain);
        topAppBarMain = findViewById(R.id.topAppBarMain);

        //ActionBar assumes complete ownership of the (Material) Toolbar after the following call.
        //Instead of the Toolbar, the ActionBar needs to be connected with the NavController!
        setSupportActionBar(topAppBarMain);

        //this is the view with the drawer
        navigationViewMain = findViewById(R.id.navigationViewMain);

        //get the navController like this because the code line below isn't working
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_main);
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();
        //navController = Navigation.findNavController(this, R.id.nav_host_fragment_main);

        //Top level destinations are configured here. createStudyActivity and studyFragment should not be included!
        appBarConfiguration = new AppBarConfiguration.Builder(R.id.homeFragment, R.id.findStudyFragment, R.id.ownStudyFragment).setDrawerLayout(drawerLayoutMain).build();

        //handle Navigation item clicks
        //this works with no further action, if the menu and destination id’s match.
        NavigationUI.setupWithNavController(navigationViewMain, navController);

        //setupActionBarWithNavController needs to be called to connect the Action Bar.
        //the line below can't be used as the (Material) Toolbar is already set as the ActionBar!
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        //NavigationUI.setupWithNavController(topAppBarMain, navController, appBarConfiguration);

        //Workaround because the drawer isn't working properly
        setWorkAround();
        Log.d("mainActivity", "setupNavigationView end");
    }

    //Parameter:
    //Return Values:
    //sets an additional listener on the drawer menu items because it didn't work properly (only the first is needed)
    private void setWorkAround() {
        navigationViewMain.getMenu().getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                navController.navigate(R.id.action_global_homeFragment);
                Log.d("mainActivity", "menuItem" + navigationViewMain.getMenu().getItem(0).toString());
                Log.d("mainActivity", "additional listener on homeMenuItem was active!");
                return false;
            }
        });
        navigationViewMain.getMenu().getItem(4).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                firebaseAuth.signOut();

                navController.navigate(R.id.action_global_nestedGraphLoginRegistration);

                Log.d("mainActivity", "menuItem" + navigationViewMain.getMenu().getItem(4).toString());
                Log.d("mainActivity", "additional listener on logout was active!");
                return false;
            }
        });

        /*
        navigationViewMain.getMenu().getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                navController.navigate(R.id.action_global_findStudyFragment);
                Log.d("menuItem", navigationViewMain.getMenu().getItem(1).toString());
                Log.d("additionalListener", "find was active!");
                return false;
            }
        });
        navigationViewMain.getMenu().getItem(3).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                navController.navigate(R.id.action_global_personalAccountFragment);
                Log.d("menuItem", navigationViewMain.getMenu().getItem(3).toString());
                Log.d("additionalListener", "personal was active!");
                return false;
            }
        });
        navigationViewMain.getMenu().getItem(4).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                navController.navigate(R.id.action_global_ownStudyFragment);
                Log.d("menuItem", navigationViewMain.getMenu().getItem(4).toString());
                Log.d("additionalListener", "own was active!");
                return false;
            }
        });
         */
    }

    //Parameter:
    //Return Values: boolean (if the navigation was successful or not)
    //allows NavigationUI to support proper up navigation (in the Action Bar)
    @Override
    public boolean onSupportNavigateUp() {
/*
        System.out.println(navController.getCurrentDestination().toString());
        if (!Objects.requireNonNull(navController.getCurrentDestination()).toString()
                .equals("Destination(com.example.vpmanager:id/homeFragment) label=VP Manager class=com.example.vpmanager.views.homeFragment")) {
            boolean navResult = NavigationUI.navigateUp(navController, appBarConfiguration);
            if (navResult) {
                if (Objects.requireNonNull(navController.getCurrentDestination()).toString()
                        .equals("Destination(com.example.vpmanager:id/homeFragment) label=VP Manager class=com.example.vpmanager.views.homeFragment")) {
                    System.out.println(navController.getCurrentDestination().toString());
                    navController.navigate(R.id.action_global_homeFragment);
                    return true;
                }
            } else
                return navResult || super.onSupportNavigateUp();
        }*/
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
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
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            if (uniqueID == null || !uniqueID.equals(user.getEmail())) {
                SharedPreferences sharedPrefs = context.getSharedPreferences(
                        PREF_UNIQUE_ID, Context.MODE_PRIVATE);
                uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
                if (uniqueID == null || !uniqueID.equals(user.getEmail())) {
                    uniqueID = user.getEmail();
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putString(PREF_UNIQUE_ID, uniqueID);
                    editor.apply();
                }
            }
        }
        return uniqueID;
    }

    @Override
    public void setDrawerLocked() {
        drawerLayoutMain.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        topAppBarMain.setNavigationIcon(null);
    }

    @Override
    public void setDrawerUnlocked() {
        drawerLayoutMain.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        topAppBarMain.setNavigationIcon(getDrawable(R.drawable.ic_baseline_menu_24));
    }

    @Override
    public void onBackPressed() {

        if (drawerLayoutMain.isOpen())
        {
            drawerLayoutMain.close();
        }
        else {
                super.onBackPressed();
        }
    }
}