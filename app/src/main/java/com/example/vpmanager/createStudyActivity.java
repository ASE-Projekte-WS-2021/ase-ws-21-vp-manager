package com.example.vpmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.kofigyan.stateprogressbar.StateProgressBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class createStudyActivity extends AppCompatActivity {
    MaterialToolbar topAppBarCreate;
    DrawerLayout drawerLayoutCreate;
    NavigationView navigationViewCreate;

    String studyTitle = "";
    String VP = "0";
    String studyDesc = "";
    String category = "";
    String execution = "";
    String platform = "";
    String optionalPlatform = "";
    String location = "";
    String street = "";
    String room = "";
    String contact = "";
    String contact2 = "";
    String contact3 = "";


    String firstSpinnerItemExecution = "Durchführungsart";
    String firstSpinnerItemCategory = "Studienkategorie";

    String contactViewString = "";
    String locationViewString = "";
    ArrayList<String> dates = new ArrayList<>();

    accessDatabase accessDatabase = new accessDatabase();

    StateProgressBar stateProgressBar;
    String[] descriptionData = {"Basis", "Info", "Kategorie", "Termine", "Bestätigen"};

    public static int currentFragment = 0;

    Bundle bundle;

    Button back;
    Button next;
    TextInputEditText textInputEditTextTitle;
    TextInputEditText textInputEditTextVP;
    TextInputEditText textInputEditTextDesc;
    TextInputEditText textInputEditTextPlatform;
    TextInputEditText textInputEditTextOptionalPlatform;
    TextInputEditText textInputEditTextLocation;
    TextInputEditText textInputEditTextStreet;
    TextInputEditText textInputEditTextRoom;
    TextInputEditText textInputEditTextContact;
    TextInputEditText textInputEditTextContact2;
    TextInputEditText textInputEditTextContact3;
    ListView listViewDates;
    Spinner categories;
    Spinner executionType;


    Fragment fragment_container;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_study);
        setupView();
        setupListeners();
        //Checks if a new user needs to be registered

    }

    //Parameter:
    //Return values:
    //Connects the code with the view
    private void setupView() {
        topAppBarCreate = findViewById(R.id.topAppBarCreate);
        setSupportActionBar(topAppBarCreate);
        drawerLayoutCreate = findViewById(R.id.drawerLayoutCreate);
        navigationViewCreate = findViewById(R.id.navigationViewCreate);
        navigationViewCreate.getMenu().getItem(2).setChecked(true);

        fragmentManager = getSupportFragmentManager();
        fragment_container = fragmentManager.findFragmentById(R.id.fragment_container);
        back = findViewById(R.id.backButton);
        next = findViewById(R.id.nextButton);
        stateProgressBar = (StateProgressBar) findViewById(R.id.your_state_progress_bar_id);
        stateProgressBar.setStateDescriptionData(descriptionData);
    }

    //Parameter:
    //Return values:
    //Sets clickListener on navigation items
    private void setupListeners() {
        //For NavigationDrawer to open
        topAppBarCreate.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View V) {
                drawerLayoutCreate.open();
            }
        });

        //Handle click on single item in drawer here
        navigationViewCreate.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Intent homeIntent = new Intent(createStudyActivity.this, homeActivity.class);
                        startActivity(homeIntent);
                        break;
                    case R.id.nav_search:
                        Intent searchIntent = new Intent(createStudyActivity.this, findStudyActivity.class);
                        startActivity(searchIntent);
                        break;
                    case R.id.nav_create:
                        break;
                    case R.id.nav_overview:
                        Intent overviewIntent = new Intent(createStudyActivity.this, personalAccountActivity.class);
                        startActivity(overviewIntent);
                        break;
                    case R.id.nav_own:
                        //Added later
                        break;

                }
                drawerLayoutCreate.close();
                return true;
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextButton();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backButton();
            }
        });
    }

    //Parameter:
    //Return values:
    //Will be the last method called to transfer the data to the database
    private void createDBEntry() {

        String studyID = getNewId();
        Map<String, Object> newStudy = new HashMap<>();

        newStudy.put("id", studyID); //New id for the study
        newStudy.put("creator", homeActivity.uniqueID);
        newStudy.put("name", studyTitle);
        newStudy.put("vps", VP);
        newStudy.put("contact", contact);
        newStudy.put("contact2", contact2);
        newStudy.put("contact3", contact3);
        newStudy.put("description", studyDesc);
        newStudy.put("category", category);
        newStudy.put("executionType", execution);
        if (executionType.getSelectedItem().toString().equals(getString(R.string.remoteString))) {
            newStudy.put("platform", platform);
            newStudy.put("platform2", optionalPlatform);
        } else if (executionType.getSelectedItem().toString().equals(getString(R.string.presenceString))) {
            newStudy.put("location", location);
            newStudy.put("street", street);
            newStudy.put("room", room);
        }
        if (!dates.isEmpty()) {
            ArrayList<String> dateIds = new ArrayList<>();

            for (int i = 0; i < dates.size(); i++) {
                Map<String, Object> newDate = new HashMap<>();
                String dateID = getNewId();

                newDate.put("id", dateID); //New id for every date
                newDate.put("studyId", studyID);
                newDate.put("userId", null);
                newDate.put("date", dates.get(i));
                newDate.put("selected", false);
                dateIds.add(dateID);
                accessDatabase.addNewDate(newDate, dateID);
            }
            newStudy.put("dates", dateIds);
        }
        System.out.println("Here");
        accessDatabase.addNewStudy(newStudy, studyID);

        //reload Activity
        reloadActivity();
    }


    private void reloadActivity() {
        finish();
        startActivity(getIntent());
    }

    //Parameter:
    //Return values:
    //Handles the logic and the loading of fragments while moving forwards through the creation process
    private void nextButton() {
        switch (currentFragment) {
            case 0:
                back.setText(getString(R.string.fragment_create_study_base_back));
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                                R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.fragment_container, new createStudyFragment_StepOne(), null)
                        .addToBackStack(null)
                        .commit();
                break;
            case 1:
                getInput(currentFragment);
                if (mandatoryCheck(currentFragment)) {
                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
                    fragmentManager
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                                    R.anim.enter_from_left, R.anim.exit_to_right)
                            .replace(R.id.fragment_container, new createStudyFragment_StepOne_Contact(), null)
                            .addToBackStack(null)
                            .commit();
                }
                break;
            case 2:
                getInput(currentFragment);
                if (mandatoryCheck(currentFragment)) {
                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
                    fragmentManager
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                                    R.anim.enter_from_left, R.anim.exit_to_right)
                            .replace(R.id.fragment_container, new createStudyFragment_StepTwo(), null)
                            .addToBackStack(null)
                            .commit();
                }
                break;
            case 3:
                getInput(currentFragment);
                if (mandatoryCheck(currentFragment)) {
                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                    fragmentManager
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                                    R.anim.enter_from_left, R.anim.exit_to_right)
                            .replace(R.id.fragment_container, new createStudyFragment_StepThree(), null)
                            .addToBackStack(null)
                            .commit();
                }
                break;
            case 4:
                getInput(currentFragment);
                if (mandatoryCheck(currentFragment)) {
                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                    if (execution.equals(getString(R.string.remoteString))) {
                        fragmentManager
                                .beginTransaction()
                                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                                        R.anim.enter_from_left, R.anim.exit_to_right)
                                .replace(R.id.fragment_container, new createStudyFragment_StepFour_Remote(), null)
                                .addToBackStack(null)
                                .commit();
                    } else {
                        fragmentManager
                                .beginTransaction()
                                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                                        R.anim.enter_from_left, R.anim.exit_to_right)
                                .replace(R.id.fragment_container, new createStudyFragment_StepFour_Presence(), null)
                                .addToBackStack(null)
                                .commit();
                    }
                    break;
                }
            case 5:
                getInput(currentFragment);
                if (mandatoryCheck(currentFragment)) {
                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR);
                    fragmentManager
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                                    R.anim.enter_from_left, R.anim.exit_to_right)
                            .replace(R.id.fragment_container, new createStudyFragment_StepFive(), null)
                            .addToBackStack(null)
                            .commit();
                }
                break;
            case 6:
                getInput(currentFragment);
                bundle = createBundle(1);
                createStudyFragment_finalStep createStudyFragment_finalStep = new createStudyFragment_finalStep();
                createStudyFragment_finalStep.setArguments(bundle);
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FIVE);
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                                R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.fragment_container, createStudyFragment_finalStep, null)
                        .addToBackStack(null)
                        .commit();
                break;
            case 7:
                bundle = createBundle(2);
                createStudyFragment_finalStep_two createStudyFragment_finalStep_two = new createStudyFragment_finalStep_two();
                createStudyFragment_finalStep_two.setArguments(bundle);
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FIVE);
                next.setText(getString(R.string.fragment_create_study_base_create));
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                                R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.fragment_container, createStudyFragment_finalStep_two, null)
                        .addToBackStack(null)
                        .commit();
                break;

            case 8:
                bundle = createBundle(3);
                createStudyFragment_finalStep_three createStudyFragment_finalStep_three = new createStudyFragment_finalStep_three();
                createStudyFragment_finalStep_three.setArguments(bundle);
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FIVE);
                next.setText(getString(R.string.fragment_create_study_base_create));
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                                R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.fragment_container, createStudyFragment_finalStep_three, null)
                        .addToBackStack(null)
                        .commit();
                break;
            case 9:
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FIVE);
                createDBEntry();
                Intent homeIntent = new Intent(createStudyActivity.this, homeActivity.class);
                startActivity(homeIntent);
                break;
            default:
                break;
        }
    }

    //Parameter:
    //Return values:
    //Handles the logic and the loading of fragments while moving backwards through the creation process
    private void backButton() {
        switch (currentFragment) {
            case 0:
                Intent homeIntent = new Intent(createStudyActivity.this, homeActivity.class);
                startActivity(homeIntent);
                break;
            case 1:
                getInput(currentFragment);
                back.setText(getString(R.string.fragment_create_study_base_main));
                //MAKE PROGRESSBAR INVIS
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right,
                                R.anim.enter_from_right, R.anim.exit_to_left)
                        .replace(R.id.fragment_container, new createStudyFragment_Base(), null)
                        .addToBackStack(null)
                        .commit();
                break;
            case 2:
                getInput(currentFragment);
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right,
                                R.anim.enter_from_right, R.anim.exit_to_left)
                        .replace(R.id.fragment_container, new createStudyFragment_StepOne(), null)
                        .addToBackStack(null)
                        .commit();
                break;
            case 3:
                getInput(currentFragment);
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right,
                                R.anim.enter_from_right, R.anim.exit_to_left)
                        .replace(R.id.fragment_container, new createStudyFragment_StepOne_Contact(), null)
                        .addToBackStack(null)
                        .commit();
                break;
            case 4:
                getInput(currentFragment);
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right,
                                R.anim.enter_from_right, R.anim.exit_to_left)
                        .replace(R.id.fragment_container, new createStudyFragment_StepTwo(), null)
                        .addToBackStack(null)
                        .commit();
                break;

            case 5:
                getInput(currentFragment);
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right,
                                R.anim.enter_from_right, R.anim.exit_to_left)
                        .replace(R.id.fragment_container, new createStudyFragment_StepThree(), null)
                        .addToBackStack(null)
                        .commit();
                break;
            case 6:
                getInput(currentFragment);
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right,
                                R.anim.enter_from_right, R.anim.exit_to_left)
                        .replace(R.id.fragment_container, new createStudyFragment_StepFour_Remote(), null)
                        .addToBackStack(null)
                        .commit();
                break;
            case 7:
                getInput(currentFragment);
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR);
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right,
                                R.anim.enter_from_right, R.anim.exit_to_left)
                        .replace(R.id.fragment_container, new createStudyFragment_StepFive(), null)
                        .addToBackStack(null)
                        .commit();
                break;

            case 8:
                bundle = createBundle(1);
                createStudyFragment_finalStep createStudyFragment_finalStep = new createStudyFragment_finalStep();
                createStudyFragment_finalStep.setArguments(bundle);
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR);
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right,
                                R.anim.enter_from_right, R.anim.exit_to_left)
                        .replace(R.id.fragment_container, createStudyFragment_finalStep, null)
                        .addToBackStack(null)
                        .commit();
                break;
            case 9:
                bundle = createBundle(2);
                createStudyFragment_finalStep_two createStudyFragment_finalStep_two = new createStudyFragment_finalStep_two();
                createStudyFragment_finalStep_two.setArguments(bundle);
                next.setText(getString(R.string.fragment_create_study_base_next));
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FIVE);
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right,
                                R.anim.enter_from_right, R.anim.exit_to_left)
                        .replace(R.id.fragment_container, createStudyFragment_finalStep_two, null)
                        .addToBackStack(null)
                        .commit();
                break;
            default:
                break;
        }
    }

    //Parameter:
    //Return values:
    //Saves the input made in the fragments and in variables in this activity
    private void getInput(int page) {
        switch (page) {
            case 1:
                textInputEditTextTitle = fragmentManager.getFragments().get(0).getView().findViewById(R.id.inputFieldTitle);
                textInputEditTextVP = fragmentManager.getFragments().get(0).getView().findViewById(R.id.inputFieldVP);
                studyTitle = Objects.requireNonNull(textInputEditTextTitle.getText()).toString();
                VP = Objects.requireNonNull(textInputEditTextVP.getText()).toString();
                System.out.println(VP + " " + studyTitle);
                break;

            case 2:
                textInputEditTextContact = fragmentManager.getFragments().get(0).getView().findViewById(R.id.inputContact1);
                textInputEditTextContact2 = fragmentManager.getFragments().get(0).getView().findViewById(R.id.inputContact2);
                textInputEditTextContact3 = fragmentManager.getFragments().get(0).getView().findViewById(R.id.inputContact3);

                contact = Objects.requireNonNull(textInputEditTextContact.getText()).toString();
                contact2 = Objects.requireNonNull(textInputEditTextContact2.getText()).toString();
                contact3 = Objects.requireNonNull(textInputEditTextContact3.getText()).toString();
                break;

            case 3:
                textInputEditTextDesc = fragmentManager.getFragments().get(0).getView().findViewById(R.id.inputFieldStudyDesc);
                studyDesc = Objects.requireNonNull(textInputEditTextDesc.getText()).toString();
                System.out.println(studyDesc);
                break;


            case 4:
                categories = fragmentManager.getFragments().get(0).getView().findViewById(R.id.createCategories);
                executionType = fragmentManager.getFragments().get(0).getView().findViewById(R.id.createExecutionType);
                category = categories.getSelectedItem().toString();
                execution = executionType.getSelectedItem().toString();
                System.out.println("Kategorie: " + category);
                System.out.println("Durchführung: " + execution);
                break;

            case 5:
                if (execution.equals(getString(R.string.remoteString))) {
                    // GET REMOTE STUFF HERE
                    location = "";
                    street = "";
                    room = "";
                    textInputEditTextPlatform = fragmentManager.getFragments().get(0).getView().findViewById(R.id.inputFieldPlatform);
                    textInputEditTextOptionalPlatform = fragmentManager.getFragments().get(0).getView().findViewById(R.id.inputFieldPlatformOptional);
                    platform = Objects.requireNonNull(textInputEditTextPlatform.getText()).toString();
                    optionalPlatform = Objects.requireNonNull(textInputEditTextOptionalPlatform.getText()).toString();
                }
                if (execution.equals((getString(R.string.presenceString)))) {
                    // GET PRESENCE STUFF HERE
                    platform = "";
                    optionalPlatform = "";

                    textInputEditTextLocation = fragmentManager.getFragments().get(0).getView().findViewById(R.id.inputFieldLocation);
                    textInputEditTextStreet = fragmentManager.getFragments().get(0).getView().findViewById(R.id.inputFieldStreet);
                    textInputEditTextRoom = fragmentManager.getFragments().get(0).getView().findViewById(R.id.inputFieldRoom);

                    location = Objects.requireNonNull(textInputEditTextLocation.getText()).toString();
                    street = Objects.requireNonNull(textInputEditTextStreet.getText()).toString();
                    room = Objects.requireNonNull(textInputEditTextRoom.getText()).toString();
                }
                break;
            case 6:

                listViewDates = fragmentManager.getFragments().get(0).getView().findViewById(R.id.createDatelist);
                for (int i = 0; i < listViewDates.getAdapter().getCount(); i++) {
                    System.out.println(listViewDates.getAdapter().getItem(i));
                    dates.add(listViewDates.getAdapter().getItem(i).toString());
                }
                break;
        }
    }

    //Parameter:
    //Return values:
    //Creates a bundle which will be send to the fragments to display the data for the confirmation process
    private Bundle createBundle(int finalPage) {
        Bundle bundle = new Bundle();
        if (finalPage == 1) {
            prepareData();
            bundle.putString("title", studyTitle);
            bundle.putString("vp", VP);
            bundle.putString("category", category);
            bundle.putString("exe", execution);
            bundle.putString("location", locationViewString);
        }
        if (finalPage == 2) {
            bundle.putString("desc", studyDesc);
            bundle.putString("contact", contactViewString);
        }
        if (finalPage == 3) {
            bundle.putStringArrayList("dates", dates);
        }
        return bundle;
    }

    //Parameter:
    //Return values:
    //Prepares the data send in the bundle so the strings are ready to be displayed in the confirmation fragments
    private void prepareData() {
        contactViewString = "";
        locationViewString = "";
        contactViewString += contact;
        if (!contact2.isEmpty()) {
            contactViewString += "\n" + contact2;
        }
        if (contact3.isEmpty()) {
            contactViewString += "\n" + contact3;
        }

        if (location.isEmpty()) {
            locationViewString = location + " \n " + street + "\n" + room;
        } else {
            locationViewString += platform;
            if (optionalPlatform.isEmpty()) {
                locationViewString += " & " + optionalPlatform;
            }
        }
    }

    //Parameter:
    //Return values:
    //Checks for mandatory information in the current fragment is missing and returns a boolean
    private boolean mandatoryCheck(int page) {
        switch (page) {
            case 1:
                if (!studyTitle.isEmpty()) {
                    System.out.println(studyTitle);
                    return true;
                }
                break;
            case 2:
                if (!contact.isEmpty()) {
                    return true;
                }
                break;
            case 3:
                if (!studyDesc.isEmpty()) {
                    return true;
                }
                break;
            case 4:
                if (!category.equals(firstSpinnerItemCategory) && !execution.equals(firstSpinnerItemExecution)) {
                    return true;
                }
                break;
            case 5:
                if (!location.isEmpty()) {
                    return true;
                }
                if (!platform.isEmpty()) {
                    return true;
                }
                break;
            default:
                return false;

        }
        return false;
    }

    //Parameter:
    //Return values:
    //Creates a unique id for the study in the database entry
    private String getNewId() {
        return UUID.randomUUID().toString();
    }

    //Parameter:
    //Return values:
    //Overrides the return button from the android device to act like the backbutton
    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
        backButton();
    }
}
