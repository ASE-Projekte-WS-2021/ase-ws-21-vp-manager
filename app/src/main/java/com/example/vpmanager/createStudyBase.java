package com.example.vpmanager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.textfield.TextInputEditText;
import com.kofigyan.stateprogressbar.StateProgressBar;

import java.util.Objects;

public class createStudyBase extends AppCompatActivity {
    String studyTitle = "";
    String VP = "";
    String studyDesc = "";
    String platform = "";
    String location = "";
    String street = "";
    String room = "";
    String contact = "";

    StateProgressBar stateProgressBar;
    String[] descriptionData = {"Basis", "Beschreibung", "Art", "Termine", "Bestätigen"};

    int currentFragment = 0;

    Button back;
    Button next;
    TextView tv;
    TextInputEditText textInputEditTextTitle;
    TextInputEditText textInputEditTextVP;
    TextInputEditText textInputEditTextDesc;

    Fragment fragment_container;
    createStudyFragment_StepOne fragment;
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
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
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
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
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
            default:
                break;
        }
    }

    private void backButton() {
        switch (currentFragment) {
            case 1:
                getInput(currentFragment);
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
            default:
                break;
        }
    }


    private void getInput(int page) {
        if (page == 1) {
            textInputEditTextTitle = fragmentManager.getFragments().get(0).getView().findViewById(R.id.inputFieldTitle);
            textInputEditTextVP = fragmentManager.getFragments().get(0).getView().findViewById(R.id.inputFieldVP);
            studyTitle = Objects.requireNonNull(textInputEditTextTitle.getText()).toString();
            VP = Objects.requireNonNull(textInputEditTextVP.getText()).toString();
            System.out.println(VP + " " + studyTitle);
        }
        if (page == 2) {
            textInputEditTextDesc = fragmentManager.getFragments().get(0).getView().findViewById(R.id.inputFieldStudyDesc);
            studyDesc = Objects.requireNonNull(textInputEditTextDesc.getText()).toString();
            System.out.println(studyDesc);
        }

        if (page == 3) {
            System.out.println("Poggers");
        }
    }


}
