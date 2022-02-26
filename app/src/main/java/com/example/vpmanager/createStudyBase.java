package com.example.vpmanager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class createStudyBase extends AppCompatActivity {
    String studyTitle;
    double VP;
    String studyDesc;
    String platform;
    String location;
    String street;
    String room;
    String contact;

    Button back;
    Button next;
    TextView tv;

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
        tv = fragmentManager.getFragments().get(0).getView().findViewById(R.id.textView1);



        /*
        fragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, new createStudyFragment_StepOne(), null)
                .addToBackStack(null)
                .commit();

         */

        back = findViewById(R.id.backButton);
        next = findViewById(R.id.nextButton);
    }


    private void setupListeners() {
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(tv.getText().toString());
            }
        });
    }

}
