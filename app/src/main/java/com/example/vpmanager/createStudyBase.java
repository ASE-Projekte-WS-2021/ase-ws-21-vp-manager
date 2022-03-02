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
    String VP = "";
    String studyDesc = "";
    String category = "";
    String execution = "";
    String platform = "";
    String optionalPlatform = "";
    String location = "";
    String street = "";
    String room = "";
    String contact = "";
    ArrayList<String> dates = new ArrayList<>();

    StateProgressBar stateProgressBar;
    String[] descriptionData = {"Basis", "Beschreibung", "Art", "Termine", "Bestätigen"};

    int currentFragment = 0;

    Button back;
    Button next;
    TextView tv;
    TextInputEditText textInputEditTextTitle;
    TextInputEditText textInputEditTextVP;
    TextInputEditText textInputEditTextDesc;
    TextInputEditText textInputEditTextPlatform;
    TextInputEditText textInputEditTextOptionalPlatform;
    TextInputEditText textInputEditTextLocation;
    TextInputEditText textInputEditTextStreet;
    TextInputEditText textInputEditTextRoom;
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
                break;
            case 2:
                getInput(currentFragment);
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
                break;
            case 3:
                getInput(currentFragment);
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
            case 4:
                getInput(currentFragment);
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
                break;
            case 5:
                getInput(currentFragment);
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FIVE);
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                                R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.fragment_container, new createStudyFragment_finalStep(), null)
                        .addToBackStack(null)
                        .commit();
                System.out.println("Before Currentfragment = " + currentFragment);
                currentFragment++;
                System.out.println("Currentfragment = " + currentFragment);
                break;
            case 6:
                getInput(currentFragment);
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FIVE);
                System.out.println("Before Currentfragment = " + currentFragment);
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

            case 4:
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
            case 5:
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
            case 6:
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
                textInputEditTextDesc = fragmentManager.getFragments().get(0).getView().findViewById(R.id.inputFieldStudyDesc);
                studyDesc = Objects.requireNonNull(textInputEditTextDesc.getText()).toString();
                System.out.println(studyDesc);
                break;


            case 3:
                categories = fragmentManager.getFragments().get(0).getView().findViewById(R.id.createCategories);
                executionType = fragmentManager.getFragments().get(0).getView().findViewById(R.id.createExecutionType);
                category = categories.getSelectedItem().toString();
                execution = executionType.getSelectedItem().toString();
                System.out.println("Kategorie: " + category);
                System.out.println("Durchführung: " + execution);
                break;

            case 4:
                if (execution.equals(getString(R.string.remoteString))) {
                    // GET REMOTE STUFF HERE
                    textInputEditTextPlatform = fragmentManager.getFragments().get(0).getView().findViewById(R.id.inputFieldPlatform);
                    textInputEditTextOptionalPlatform = fragmentManager.getFragments().get(0).getView().findViewById(R.id.inputFieldPlatformOptional);
                    platform = Objects.requireNonNull(textInputEditTextPlatform.getText()).toString();
                    optionalPlatform = Objects.requireNonNull(textInputEditTextOptionalPlatform.getText()).toString();
                } else {
                    // GET PRESENCE STUFF HERE
                    textInputEditTextLocation = fragmentManager.getFragments().get(0).getView().findViewById(R.id.inputFieldLocation);
                    textInputEditTextStreet = fragmentManager.getFragments().get(0).getView().findViewById(R.id.inputFieldStreet);
                    textInputEditTextRoom = fragmentManager.getFragments().get(0).getView().findViewById(R.id.inputFieldRoom);

                    location = Objects.requireNonNull(textInputEditTextLocation.getText()).toString();
                    street = Objects.requireNonNull(textInputEditTextStreet.getText()).toString();
                    room = Objects.requireNonNull(textInputEditTextRoom.getText()).toString();
                }
                break;
            case 5:
                ArrayAdapter adapter = new ArrayAdapter(this,
                        android.R.layout.simple_list_item_1);
                listViewDates = fragmentManager.getFragments().get(0).getView().findViewById(R.id.createDatelist);
                listViewDates.setAdapter(adapter);
                for (int i = 0; i < adapter.getCount(); i++) {
                    dates.add((String) adapter.getItem(i));
                    System.out.println(adapter.getItem(i));
                    System.out.println((String) adapter.getItem(i));
                }
                break;
        }
    }
}
