package com.example.vpmanager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.textfield.TextInputEditText;

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

    int currentFragment = 1;

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
        textInputEditTextTitle = fragmentManager.getFragments().get(0).getView().findViewById(R.id.inputFieldTitle);
        textInputEditTextVP = fragmentManager.getFragments().get(0).getView().findViewById(R.id.inputFieldVP);
        back = findViewById(R.id.backButton);
        next = findViewById(R.id.nextButton);
    }


    private void setupListeners() {
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (currentFragment){
                    case 1:
                        getInput(currentFragment);
                        fragmentManager
                                .beginTransaction()
                                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                                        R.anim.enter_from_left, R.anim.exit_to_right)
                                .replace(R.id.fragment_container, new createStudyFragment_StepOne(), null)
                                .addToBackStack(null)
                                .commit();
                        System.out.println("Frag 1 , Actually: " + currentFragment);
                        currentFragment++;
                        break;
                    case 2:
                        getInput(currentFragment-1);
                        fragmentManager
                                .beginTransaction()
                                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                                        R.anim.enter_from_left, R.anim.exit_to_right)
                                .replace(R.id.fragment_container, new createStudyFragment_StepTwo(), null)
                                .addToBackStack(null)
                                .commit();
                        System.out.println("Frag 2 , Actually: " + currentFragment);
                        currentFragment++;
                        break;
                    case 3:
                        getInput(currentFragment-1);
                        fragmentManager
                                .beginTransaction()
                                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                                        R.anim.enter_from_left, R.anim.exit_to_right)
                                .replace(R.id.fragment_container, new createStudyFragment_StepThree(), null)
                                .addToBackStack(null)
                                .commit();
                        System.out.println("Frag 3 , Actually: " + currentFragment);
                        currentFragment++;
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void pageLoad(){

    }

    private void getInput(int page){
        if(page == 1) {
            studyTitle = Objects.requireNonNull(textInputEditTextTitle.getText()).toString();
            VP = Objects.requireNonNull(textInputEditTextVP.getText()).toString();
        }
        System.out.println(page == 2);
        if(page == 2){
            textInputEditTextDesc = fragmentManager.getFragments().get(0).getView().findViewById(R.id.inputFieldStudyDesc);
            studyDesc = Objects.requireNonNull(textInputEditTextDesc.getText()).toString();
        }

        if(page == 3){

        }
    }


}
