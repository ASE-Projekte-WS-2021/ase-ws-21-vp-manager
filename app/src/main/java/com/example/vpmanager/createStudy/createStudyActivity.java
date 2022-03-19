package com.example.vpmanager.createStudy;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.airbnb.lottie.LottieAnimationView;
import com.example.vpmanager.R;
import com.example.vpmanager.AccessDatabase;
import com.example.vpmanager.views.mainActivity;
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

    LottieAnimationView doneAnimation;

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
    String contactMail = "";
    String contactPhone = "";
    String contactSkype = "";
    String contactDiscord = "";
    String contactOthers = "";


    String firstSpinnerItemExecution = "Durchführungsart*";
    String firstSpinnerItemCategory = "Studienkategorie*";

    String contactViewString = "";
    String locationViewString = "";
    ArrayList<String> dates = new ArrayList<>();

    AccessDatabase accessDatabase = new AccessDatabase();

    StateProgressBar stateProgressBar;
    String[] descriptionData = {"Basis", "Beschreibung", "Ort", "Termine", "Bestätigen"};

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
    TextInputEditText textInputEditTextContactMail;
    TextInputEditText textInputEditTextContactZoom;
    TextInputEditText textInputEditTextContactSkype;
    TextInputEditText textInputEditTextContactDiscord;
    TextInputEditText textInputEditTextContactOthers;
    ListView listViewDates;
    Spinner categoriesSpinner;
    Spinner executionTypeSpinner;


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
        doneAnimation = findViewById(R.id.animationView);
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
        stateProgressBar.setVisibility(View.INVISIBLE);
    }

    //Parameter:
    //Return values:
    //Sets clickListener on navigation items
    private void setupListeners() {
        doneAnimation.addAnimatorListener(new AnimatorListenerAdapter() {
                                              @Override
                                              public void onAnimationEnd(Animator animation) {
                                                  super.onAnimationEnd(animation);
                                                  createDBEntry();
                                                  Intent homeIntent = new Intent(createStudyActivity.this, mainActivity.class);
                                                  startActivity(homeIntent);
                                              }
                                          }
        );

        topAppBarCreate.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(createStudyActivity.this, mainActivity.class);
                startActivity(homeIntent);
            }
        });



        /*
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
         */

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
        newStudy.put("creator", mainActivity.uniqueID);
        newStudy.put("name", studyTitle);
        newStudy.put("vps", VP);
        newStudy.put("contact", contactMail);
        newStudy.put("contact2", contactPhone);
        newStudy.put("contact3", contactSkype);
        newStudy.put("contact4", contactDiscord);
        newStudy.put("contact5", contactOthers);
        newStudy.put("description", studyDesc);
        newStudy.put("category", category);
        newStudy.put("executionType", execution);
        if (executionTypeSpinner.getSelectedItem().toString().equals(getString(R.string.remoteString))) {
            newStudy.put("platform", platform);
            newStudy.put("platform2", optionalPlatform);
        } else if (executionTypeSpinner.getSelectedItem().toString().equals(getString(R.string.presenceString))) {
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
                bundle = createBundle(1);
                createStudyFragment_StepOne createStudyFragment_stepOne = new createStudyFragment_StepOne();
                createStudyFragment_stepOne.setArguments(bundle);
                stateProgressBar.setVisibility(View.VISIBLE);
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                                R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.fragment_container, createStudyFragment_stepOne, null)
                        .addToBackStack(null)
                        .commit();
                break;
            case 1:
                getInput(currentFragment);
                if (mandatoryCheck(currentFragment)) {
                    bundle = createBundle(2);
                    createStudyFragment_StepTwo createStudyFragment_stepTwo = new createStudyFragment_StepTwo();
                    createStudyFragment_stepTwo.setArguments(bundle);
                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
                    fragmentManager
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                                    R.anim.enter_from_left, R.anim.exit_to_right)
                            .replace(R.id.fragment_container, createStudyFragment_stepTwo, null)
                            .addToBackStack(null)
                            .commit();
                }
                break;
            case 2:
                getInput(currentFragment);
                if (mandatoryCheck(currentFragment)) {
                    bundle = createBundle(3);
                    createStudyFragment_StepThree createStudyFragment_stepThree = new createStudyFragment_StepThree();
                    createStudyFragment_stepThree.setArguments(bundle);
                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
                    fragmentManager
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                                    R.anim.enter_from_left, R.anim.exit_to_right)
                            .replace(R.id.fragment_container, createStudyFragment_stepThree, null)
                            .addToBackStack(null)
                            .commit();
                }
                break;
            case 3:
                getInput(currentFragment);
                if (mandatoryCheck(currentFragment)) {
                    bundle = createBundle(4);
                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                    if (execution.equals(getString(R.string.remoteString))) {
                        createStudyFragment_StepFour_Remote createStudyFragment_stepFour_remote = new createStudyFragment_StepFour_Remote();
                        createStudyFragment_stepFour_remote.setArguments(bundle);
                        fragmentManager
                                .beginTransaction()
                                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                                        R.anim.enter_from_left, R.anim.exit_to_right)
                                .replace(R.id.fragment_container, createStudyFragment_stepFour_remote, null)
                                .addToBackStack(null)
                                .commit();
                    } else {
                        createStudyFragment_StepFour_Presence createStudyFragment_stepFour_presence = new createStudyFragment_StepFour_Presence();
                        createStudyFragment_stepFour_presence.setArguments(bundle);
                        fragmentManager
                                .beginTransaction()
                                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                                        R.anim.enter_from_left, R.anim.exit_to_right)
                                .replace(R.id.fragment_container, createStudyFragment_stepFour_presence, null)
                                .addToBackStack(null)
                                .commit();
                    }
                }
                break;
            case 4:
                getInput(currentFragment);
                if (mandatoryCheck(currentFragment)) {
                    bundle = createBundle(5);
                    createStudyFragment_StepFive createStudyFragment_stepFive = new createStudyFragment_StepFive();
                    createStudyFragment_stepFive.setArguments(bundle);
                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR);
                    fragmentManager
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                                    R.anim.enter_from_left, R.anim.exit_to_right)
                            .replace(R.id.fragment_container, createStudyFragment_stepFive, null)
                            .addToBackStack(null)
                            .commit();
                }
                break;
            case 5:
                getInput(currentFragment);
                bundle = createBundle(6);
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
            case 6:
                bundle = createBundle(7);
                createStudyFragment_finalStep_two createStudyFragment_finalStep_two = new createStudyFragment_finalStep_two();
                createStudyFragment_finalStep_two.setArguments(bundle);
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FIVE);
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                                R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.fragment_container, createStudyFragment_finalStep_two, null)
                        .addToBackStack(null)
                        .commit();
                break;

            case 7:
                bundle = createBundle(8);
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
            case 8:
                doneAnimation.setVisibility(LottieAnimationView.VISIBLE);
                doneAnimation.setProgress(0);
                doneAnimation.pauseAnimation();
                doneAnimation.playAnimation();
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
                Intent homeIntent = new Intent(createStudyActivity.this, mainActivity.class);
                startActivity(homeIntent);
                break;
            case 1:
                getInput(currentFragment);
                stateProgressBar.setVisibility(View.INVISIBLE);
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
                bundle = createBundle(1);
                createStudyFragment_StepOne createStudyFragment_stepOne = new createStudyFragment_StepOne();
                createStudyFragment_stepOne.setArguments(bundle);
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right,
                                R.anim.enter_from_right, R.anim.exit_to_left)
                        .replace(R.id.fragment_container, createStudyFragment_stepOne, null)
                        .addToBackStack(null)
                        .commit();
                break;
            case 3:
                getInput(currentFragment);
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
                bundle = createBundle(2);
                createStudyFragment_StepTwo createStudyFragment_stepTwo = new createStudyFragment_StepTwo();
                createStudyFragment_stepTwo.setArguments(bundle);
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right,
                                R.anim.enter_from_right, R.anim.exit_to_left)
                        .replace(R.id.fragment_container, createStudyFragment_stepTwo, null)
                        .addToBackStack(null)
                        .commit();
                break;
            case 4:
                getInput(currentFragment);
                bundle = createBundle(3);
                createStudyFragment_StepThree createStudyFragment_stepThree = new createStudyFragment_StepThree();
                createStudyFragment_stepThree.setArguments(bundle);
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right,
                                R.anim.enter_from_right, R.anim.exit_to_left)
                        .replace(R.id.fragment_container, createStudyFragment_stepThree, null)
                        .addToBackStack(null)
                        .commit();
                break;

            case 5:
                getInput(currentFragment);
                bundle = createBundle(4);
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                if (execution.equals(getString(R.string.remoteString))) {
                    createStudyFragment_StepFour_Remote createStudyFragment_stepFour_remote = new createStudyFragment_StepFour_Remote();
                    createStudyFragment_stepFour_remote.setArguments(bundle);
                    fragmentManager
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right,
                                    R.anim.enter_from_right, R.anim.exit_to_left)
                            .replace(R.id.fragment_container, createStudyFragment_stepFour_remote, null)
                            .addToBackStack(null)
                            .commit();
                } else {
                    createStudyFragment_StepFour_Presence createStudyFragment_stepFour_presence = new createStudyFragment_StepFour_Presence();
                    createStudyFragment_stepFour_presence.setArguments(bundle);
                    fragmentManager
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right,
                                    R.anim.enter_from_right, R.anim.exit_to_left)
                            .replace(R.id.fragment_container, createStudyFragment_stepFour_presence, null)
                            .addToBackStack(null)
                            .commit();
                }
                break;
            case 6:
                getInput(currentFragment);
                bundle = createBundle(5);
                createStudyFragment_StepFive createStudyFragment_stepFive = new createStudyFragment_StepFive();
                createStudyFragment_stepFive.setArguments(bundle);
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR);
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right,
                                R.anim.enter_from_right, R.anim.exit_to_left)
                        .replace(R.id.fragment_container, createStudyFragment_stepFive, null)
                        .addToBackStack(null)
                        .commit();
                break;

            case 7:
                bundle = createBundle(6);
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
            case 8:
                bundle = createBundle(7);
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

                categoriesSpinner = fragmentManager.getFragments().get(0).getView().findViewById(R.id.createCategories);
                executionTypeSpinner = fragmentManager.getFragments().get(0).getView().findViewById(R.id.createExecutionType);
                category = categoriesSpinner.getSelectedItem().toString();
                execution = executionTypeSpinner.getSelectedItem().toString();
                System.out.println("Kategorie: " + category);
                System.out.println("Durchführung: " + execution);
                break;

            case 2:
                textInputEditTextContactMail = fragmentManager.getFragments().get(0).getView().findViewById(R.id.inputContact1);
                textInputEditTextContactZoom = fragmentManager.getFragments().get(0).getView().findViewById(R.id.inputContact2);
                textInputEditTextContactSkype = fragmentManager.getFragments().get(0).getView().findViewById(R.id.inputContact3);
                textInputEditTextContactDiscord = fragmentManager.getFragments().get(0).getView().findViewById(R.id.inputContact4);
                textInputEditTextContactOthers = fragmentManager.getFragments().get(0).getView().findViewById(R.id.inputContact5);

                contactMail = Objects.requireNonNull(textInputEditTextContactMail.getText()).toString();
                contactPhone = Objects.requireNonNull(textInputEditTextContactZoom.getText()).toString();
                contactSkype = Objects.requireNonNull(textInputEditTextContactSkype.getText()).toString();
                contactDiscord = Objects.requireNonNull(textInputEditTextContactDiscord.getText()).toString();
                contactOthers = Objects.requireNonNull(textInputEditTextContactOthers.getText()).toString();
                break;

            case 3:
                textInputEditTextDesc = fragmentManager.getFragments().get(0).getView().findViewById(R.id.inputFieldStudyDesc);
                studyDesc = Objects.requireNonNull(textInputEditTextDesc.getText()).toString();
                System.out.println(studyDesc);
                break;

            case 4:
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
            case 5:
                listViewDates = fragmentManager.getFragments().get(0).getView().findViewById(R.id.createDatelist);
                for (int i = 0; i < listViewDates.getAdapter().getCount(); i++) {
                    System.out.println(listViewDates.getAdapter().getItem(i));
                    System.out.println(listViewDates.getAdapter().getCount());
                    dates.add(listViewDates.getAdapter().getItem(i).toString());
                }
                break;
        }
    }

    //Parameter:
    //Return values:
    //Creates a bundle which will be send to the fragments to display the data for the confirmation process
    private Bundle createBundle(int page) {
        Bundle bundle = new Bundle();
        switch (page) {
            case 1:
                bundle.putString("title", studyTitle);
                bundle.putString("vp", VP);
                bundle.putString("category", category);
                bundle.putString("exe", execution);
                break;
            case 2:
                bundle.putString("contact", contactMail);
                bundle.putString("contact2", contactPhone);
                bundle.putString("contact3", contactSkype);
                bundle.putString("contact4", contactDiscord);
                bundle.putString("contact5", contactOthers);
                break;
            case 3:
                bundle.putString("desc", studyDesc);
                break;
            case 4:
                if (execution.equals(getString(R.string.remoteString))) {
                    bundle.putString("platform", platform);
                    bundle.putString("platform2", optionalPlatform);

                }
                if (execution.equals((getString(R.string.presenceString)))) {
                    bundle.putString("location", location);
                    bundle.putString("street", street);
                    bundle.putString("room", room);
                }
                break;
            case 5:
                bundle.putStringArrayList("dates", dates);
                break;
            case 6:
                prepareData();
                bundle.putString("title", studyTitle);
                bundle.putString("vp", VP);
                bundle.putString("category", category);
                bundle.putString("exe", execution);
                bundle.putString("location", locationViewString);
                break;
            case 7:
                bundle.putString("desc", studyDesc);
                bundle.putString("contact", contactViewString);
                break;
            case 8:
                bundle.putStringArrayList("dates", dates);
                break;
            default:
                break;
        }
        return bundle;
    }

    //Parameter:
    //Return values:
    //Prepares the data send in the bundle so the strings are ready to be displayed in the confirmation fragments
    private void prepareData() {
        contactViewString = "\n";
        locationViewString = "\n";
        contactViewString += "Email-adresese: " + contactMail;
        if (!contactPhone.isEmpty()) {
            contactViewString += "\nHandynummer: " + contactPhone;
        }
        if (!contactSkype.isEmpty()) {
            contactViewString += "\nSkype: " + contactSkype;
        }
        if (!contactDiscord.isEmpty()) {
            contactViewString += "\nDiscord: " + contactDiscord;
        }
        if (!contactOthers.isEmpty()) {
            contactViewString += "\nSonstige: " + contactOthers;
        }

        if (!location.isEmpty()) {
            locationViewString = location + " \n" + street + "\n" + room;
        } else {
            locationViewString += platform;
            if (!optionalPlatform.isEmpty()) {
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
                if (!studyTitle.isEmpty()&& !category.equals(firstSpinnerItemCategory) && !execution.equals(firstSpinnerItemExecution)) {
                    System.out.println(studyTitle);
                    return true;
                }
                if(studyTitle.isEmpty()){
                    textInputEditTextTitle.setError("Titel darf nicht leer sein");
                    textInputEditTextTitle.requestFocus();
                    break;
                }
                if(category.equals(firstSpinnerItemCategory)){
                    categoriesSpinner.setFocusable(true);
                    categoriesSpinner.setFocusableInTouchMode(true);
                    categoriesSpinner.requestFocus();
                    categoriesSpinner.performClick();
                    break;
                }
                if(execution.equals(firstSpinnerItemExecution)){
                    executionTypeSpinner.setFocusable(true);
                    executionTypeSpinner.setFocusableInTouchMode(true);
                    executionTypeSpinner.requestFocus();
                    executionTypeSpinner.performClick();
                    break;
                }
                break;
            case 2:
                if (!contactMail.isEmpty()) {
                    return true;
                }
                textInputEditTextContactMail.setError("Email-Kontakt muss angegeben werden");
                textInputEditTextContactMail.requestFocus();
                break;
            case 3:
                if (!studyDesc.isEmpty()) {
                    return true;
                }
                textInputEditTextDesc.setError("Studienbeschreibung darf nicht leer sein");
                textInputEditTextDesc.requestFocus();
                break;
            case 4:
                if (!location.isEmpty()) {
                    return true;
                }
                if(execution.equals("Präsenz")) {
                    textInputEditTextLocation.setError("Ort der Studie muss angegeben werden");
                    textInputEditTextLocation.requestFocus();
                }
                if (!platform.isEmpty()) {
                    return true;
                }
                if(execution.equals("Remote")) {
                    textInputEditTextPlatform.setError("Primärplattform muss angegeben werden");
                    textInputEditTextPlatform.requestFocus();
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
