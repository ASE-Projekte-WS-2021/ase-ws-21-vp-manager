package com.example.vpmanager.views;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.vpmanager.DrawerController;
import com.example.vpmanager.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity implements DrawerController {

    public DrawerLayout drawerLayoutMain;
    private NavController navController;
    public static NavController staticNavController;
    public static String currentFragment = "";
    private NavigationView navigationViewMain;
    private NavHostFragment navHostFragment;
    private FirebaseAuth firebaseAuth;
    private boolean userStay = true;
    private TextView drawerMail;


    private MaterialToolbar topAppBarMain;
    private AppBarConfiguration appBarConfiguration;

    //logic to register a new user (app installation) if necessary
    public static String uniqueID = "";
    private static final String PREF_UNIQUE_ID = "PREF_UNIQUE_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupNavigationView();
        setupUserId();
        firebaseAuth = FirebaseAuth.getInstance();
    }


    //closes the navigation drawer when returning to mainActivity
    @Override
    protected void onResume() {
        super.onResume();
        drawerLayoutMain.close();
    }

    private void setupUserId() {
        uniqueID = createUserId(this);

        View view = navigationViewMain.getHeaderView(0);
        drawerMail = view.findViewById(R.id.drawer_header_mail);
        drawerMail.setText(uniqueID);

    }

    //Parameter:
    //Return Values:
    //sets up all navigation components of the app and connects the navigation graph
    private void setupNavigationView() {
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
        staticNavController = navController;
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

        setDrawerListener();
    }

    //Parameter:
    //Return Values:
    //sets an additional listener on the drawer menu items because it didn't work properly (only the first is needed)
    private void setDrawerListener() {
        navigationViewMain.getMenu().getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                navController.navigate(R.id.action_global_homeFragment);
                return false;
            }
        });
        navigationViewMain.getMenu().getItem(4).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                firebaseAuth.signOut();
                navController.navigate(R.id.action_global_loginFragment);

                return false;
            }
        });
    }

    //Parameter:
    //Return Values: boolean (if the navigation was successful or not)
    //allows NavigationUI to support proper up navigation (in the Action Bar)
    @Override
    public boolean onSupportNavigateUp() {
        if (userStay) {
            if (currentFragment.equals("createBase") || currentFragment.equals("createStepOne") || currentFragment.equals("createStepTwo")
                    || currentFragment.equals("createStepThree") || currentFragment.equals("createStepFourRemote") || currentFragment.equals("createStepFourPresence")
                    || currentFragment.equals("createStepFive") || currentFragment.equals("createFinalStep") || currentFragment.equals("createFinalStepTwo") ||
                    currentFragment.equals("createFinalStepThree") || currentFragment.equals("editFragment")) {
                Dialog dialog = new Dialog(this, R.style.DialogStyle);
                dialog.setContentView(R.layout.dialog_warning);
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_window);
                ImageView btnClose = dialog.findViewById(R.id.btn_close);
                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                TextView title = dialog.findViewById(R.id.info_title);
                TextView desc = dialog.findViewById(R.id.duplicate_desc);
                CheckBox box = dialog.findViewById(R.id.doNotShowAgainCheckBox);
                box.setVisibility(View.GONE);
                Button stay = dialog.findViewById(R.id.abort);
                Button leave = dialog.findViewById(R.id.addAnyways);
                ImageView icon = dialog.findViewById(R.id.img_icon);
                icon.setImageDrawable(getDrawable(R.drawable.ic_baseline_warning_24));

                title.setText(R.string.warning);
                desc.setText(R.string.leavePageWarning);
                stay.setText(R.string.cancelNavigation);
                leave.setText(R.string.leavePage);
                dialog.show();

                stay.setOnClickListener(v -> dialog.dismiss());

                leave.setOnClickListener(v -> {
                    userStay = false;
                    dialog.dismiss();
                    onSupportNavigateUp();
                });
                return false;
            }
        }
        userStay = true;
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
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
        } else {
            staticNavController.navigate(R.id.action_global_loginFragment);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.app_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.pageInfoIcon) {
            showInfoText();
        }
        return super.onOptionsItemSelected(item);
    }


    //Parameter:
    //Return values:
    //Sets all info dialogs for the application; buttons are set on the top action bar of every page
    private void showInfoText() {
        Dialog dialog = new Dialog(this, R.style.DialogStyle);
        dialog.setContentView(R.layout.dialog_info);

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_window);

        ImageView btnClose = dialog.findViewById(R.id.btn_close);

        btnClose.setOnClickListener(view -> dialog.dismiss());

        TextView title = dialog.findViewById(R.id.info_title);
        TextView desc = dialog.findViewById(R.id.info_desc);

        switch (currentFragment) {
            case "home":
                title.setText(getString(R.string.infoPaMainTitle));
                desc.setText(getString(R.string.infoPaMain));
                break;
            case "findStudy":
                title.setText(getString(R.string.infoFindStudyTitle));
                desc.setText(getString(R.string.infoFindStudies));
                break;
            case "login":
                title.setText(getString(R.string.infoLoginTitle));
                desc.setText(getString(R.string.infoLogin));
                break;
            case "register":
                title.setText(getString(R.string.register_info_title));
                desc.setText(getString(R.string.register_info_desc));
                break;
            case "upcomingAppointments":
                title.setText(getString(R.string.infoPAUpcomingTitle));
                desc.setText(getString(R.string.infoPaUpcoming));
                break;
            case "editFragment":
                title.setText(getString(R.string.infoEditFragmentTitle));
                desc.setText(getString(R.string.infoEditStudy));
                break;
            case "studyFragment":
                title.setText(getString(R.string.infoStudyDetailsTitle));
                desc.setText(getString(R.string.infoStudyDetails));
                break;
            case "studyCreatorFragment":
                title.setText(getString(R.string.infoCreatorStudyDetailsTitle));
                desc.setText(getString(R.string.infoStudyCreatorDetails));
                break;
            case "ownStudyFragment":
                title.setText(getString(R.string.infoOwnStudiesTitle));
                desc.setText(getString(R.string.infoOwnStudies));
                break;
            case "createBase":
                title.setText(getString(R.string.infoCreateStartTitle));
                desc.setText(getString(R.string.infoCreateStart));
                break;
            case "createStepOne":
                title.setText(getString(R.string.infoCreateBaseTitle));
                desc.setText(getString(R.string.infoCreateBase));
                break;
            case "createStepTwo":
                title.setText(getString(R.string.infoCreateContactTitle));
                desc.setText(getString(R.string.infoCreateContact));
                break;
            case "createStepThree":
                title.setText(getString(R.string.infoCreateDescTitle));
                desc.setText(getString(R.string.infoCreateDescription));
                break;
            case "createStepFourRemote":
                title.setText(getString(R.string.infoCreatePlatformTitle));
                desc.setText(getString(R.string.infoCreatePlatform));
                break;
            case "createStepFourPresence":
                title.setText(getString(R.string.infoCreateLocationTitle));
                desc.setText(getString(R.string.infoCreateLocation));
                break;
            case "createStepFive":
                title.setText(getString(R.string.infoCreateDatesTitle));
                desc.setText(getString(R.string.infoCreateDates));
                break;
            case "createFinalStep":
                title.setText(getString(R.string.infoCreateConfirmDetailsTitle));
                desc.setText(getString(R.string.infoCreateConfirmDetails));
                break;
            case "createFinalStepTwo":
                title.setText(getString(R.string.infoCreateConfirmDetailsTitle));
                desc.setText(getString(R.string.infoCreateConfirmDetails));
                break;
            case "createFinalStepThree":
                title.setText(getString(R.string.infoCreateConfirmDatesTitle));
                desc.setText(getString(R.string.infoCreateConfirmDates));
                break;
            default:
                break;
        }
        dialog.show();

    }

    @Override
    public void onBackPressed() {
        if (drawerLayoutMain.isOpen()) {
            drawerLayoutMain.close();
        } else {
            if (currentFragment.equals("login")) {
                System.exit(0);

            } else {
                if (currentFragment.equals("createBase") || currentFragment.equals("createStepOne") || currentFragment.equals("createStepTwo")
                        || currentFragment.equals("createStepThree") || currentFragment.equals("createStepFourRemote") || currentFragment.equals("createStepFourPresence")
                        || currentFragment.equals("createStepFive") || currentFragment.equals("createFinalStep") || currentFragment.equals("createFinalStepTwo") ||
                        currentFragment.equals("createFinalStepThree") || currentFragment.equals("editFragment")) {
                    if (userStay) {
                        Dialog dialog = new Dialog(this, R.style.DialogStyle);
                        dialog.setContentView(R.layout.dialog_warning);
                        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_window);
                        ImageView btnClose = dialog.findViewById(R.id.btn_close);
                        btnClose.setOnClickListener(view -> dialog.dismiss());
                        TextView title = dialog.findViewById(R.id.info_title);
                        TextView desc = dialog.findViewById(R.id.duplicate_desc);
                        CheckBox box = dialog.findViewById(R.id.doNotShowAgainCheckBox);
                        box.setVisibility(View.GONE);
                        Button stay = dialog.findViewById(R.id.abort);
                        Button leave = dialog.findViewById(R.id.addAnyways);
                        ImageView icon = dialog.findViewById(R.id.img_icon);
                        icon.setImageDrawable(getDrawable(R.drawable.ic_baseline_warning_24));

                        title.setText(getString(R.string.warning));
                        desc.setText(getString(R.string.leavePageWarning));
                        stay.setText(getString(R.string.cancelNavigation));
                        leave.setText(getString(R.string.leavePage));
                        dialog.show();

                        stay.setOnClickListener(v -> dialog.dismiss());

                        leave.setOnClickListener(v -> {
                            userStay = false;
                            dialog.dismiss();
                            onBackPressed();
                        });

                    } else {
                        userStay = true;
                        super.onBackPressed();
                    }
                } else {
                    super.onBackPressed();
                }
            }
        }
    }
}