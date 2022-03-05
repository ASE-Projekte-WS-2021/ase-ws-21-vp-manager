package com.example.vpmanager;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.textfield.TextInputEditText;
import com.kofigyan.stateprogressbar.StateProgressBar;

import java.util.ArrayList;
import java.util.Objects;

public class createStudyBase extends AppCompatActivity {
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

    StateProgressBar stateProgressBar;
    String[] descriptionData = {"Basis", "Beschreibung", "Art", "Termine", "Bestätigen"};

    int currentFragment = 0;
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
        setContentView(R.layout.activity_create_study_base);
        setupView();
        setupListeners();
        //Checks if a new user needs to be registered

    }

    private void setupView() {
        fragmentManager = getSupportFragmentManager();
        fragment_container = fragmentManager.findFragmentById(R.id.fragment_container);
        back = findViewById(R.id.backButton);
        next = findViewById(R.id.nextButton);
        stateProgressBar = (StateProgressBar) findViewById(R.id.your_state_progress_bar_id);
        stateProgressBar.setStateDescriptionData(descriptionData);
    }


    private void setupListeners() {
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

    private void nextButton() {
        switch (currentFragment) {
            case 0:
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                                R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.fragment_container, new createStudyFragment_StepOne(), null)
                        .addToBackStack(null)
                        .commit();
                System.out.println("Before Currentfragment = " + currentFragment);
                currentFragment++;
                System.out.println("Currentfragment = " + currentFragment);
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
                    System.out.println("Before Currentfragment = " + currentFragment);
                    currentFragment++;
                    System.out.println("Currentfragment = " + currentFragment);
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
                    System.out.println("Before Currentfragment = " + currentFragment);
                    currentFragment++;
                    System.out.println("Currentfragment = " + currentFragment);
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
                    System.out.println("Before Currentfragment = " + currentFragment);
                    currentFragment++;
                    System.out.println("Currentfragment = " + currentFragment);
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
                    System.out.println("Before Currentfragment = " + currentFragment);
                    currentFragment++;
                    System.out.println("Currentfragment = " + currentFragment);
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
                    System.out.println("Before Currentfragment = " + currentFragment);
                    currentFragment++;
                    System.out.println("Currentfragment = " + currentFragment);
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
                System.out.println("Before Currentfragment = " + currentFragment);
                currentFragment++;
                System.out.println("Currentfragment = " + currentFragment);
                break;
            case 7:
                bundle = createBundle(2);
                createStudyFragment_finalStep_two createStudyFragment_finalStep_two = new createStudyFragment_finalStep_two();
                createStudyFragment_finalStep_two.setArguments(bundle);
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FIVE);
                next.setText("Erstellen");
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                                R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.fragment_container, createStudyFragment_finalStep_two, null)
                        .addToBackStack(null)
                        .commit();
                System.out.println("Before Currentfragment = " + currentFragment);
                currentFragment++;
                System.out.println("Currentfragment = " + currentFragment);
                break;

            case 8:
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FIVE);
                System.out.println("Before Currentfragment = " + currentFragment);
                //DATABASE
                System.out.println("Currentfragment = " + currentFragment);
                break;
            default:
                break;
        }
    }

    private void backButton() {
        switch (currentFragment) {
            case 1:
                getInput(currentFragment);
                //MAKE PROGRESSBAR INVIS
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right,
                                R.anim.enter_from_right, R.anim.exit_to_left)
                        .replace(R.id.fragment_container, new createStudyFragment_Base(), null)
                        .addToBackStack(null)
                        .commit();
                System.out.println("Before Currentfragment = " + currentFragment);
                currentFragment--;
                System.out.println("Currentfragment = " + currentFragment);
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
                System.out.println("Before Currentfragment = " + currentFragment);
                currentFragment--;
                System.out.println("Currentfragment = " + currentFragment);
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
                System.out.println("Before Currentfragment = " + currentFragment);
                currentFragment--;
                System.out.println("Currentfragment = " + currentFragment);
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
                System.out.println("Before Currentfragment = " + currentFragment);
                currentFragment--;
                System.out.println("Currentfragment = " + currentFragment);
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
                System.out.println("Before Currentfragment = " + currentFragment);
                currentFragment--;
                System.out.println("Currentfragment = " + currentFragment);
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
                System.out.println("Before Currentfragment = " + currentFragment);
                currentFragment--;
                System.out.println("Currentfragment = " + currentFragment);
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
                System.out.println("Before Currentfragment = " + currentFragment);
                currentFragment--;
                System.out.println("Currentfragment = " + currentFragment);
                break;
            case 8:
                Bundle bundle = createBundle(1);
                createStudyFragment_finalStep createStudyFragment_finalStep = new createStudyFragment_finalStep();
                createStudyFragment_finalStep.setArguments(bundle);
                next.setText("Weiter");
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR);
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right,
                                R.anim.enter_from_right, R.anim.exit_to_left)
                        .replace(R.id.fragment_container, createStudyFragment_finalStep, null)
                        .addToBackStack(null)
                        .commit();
                System.out.println("Before Currentfragment = " + currentFragment);
                currentFragment--;
                System.out.println("Currentfragment = " + currentFragment);
                break;
            default:
                break;
        }
    }

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

    private Bundle createBundle(int finalPage) {
        Bundle bundle = new Bundle();
        if (finalPage == 1) {
            prepareData();
            bundle.putString("title", studyTitle);
            bundle.putString("vp", VP);
            bundle.putString("desc", studyDesc);
            bundle.putString("category", category);
            bundle.putString("exe", execution);
            bundle.putString("location", locationViewString);
            bundle.putString("contact", contactViewString);
        } else {
            bundle.putStringArrayList("dates", dates);
        }
        return bundle;
    }

    private void prepareData() {
        contactViewString += contact;
        if (!contact2.isEmpty()) {
            contactViewString += " , " + contact2;
        }
        if (contact3.isEmpty()) {
            contactViewString += " , " + contact3;
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
}
