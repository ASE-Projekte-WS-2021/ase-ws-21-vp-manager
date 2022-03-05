package com.example.vpmanager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class createStudyFragment_finalStep extends Fragment {

    TextView title;
    TextView vp;
    TextView desc;
    TextView category;
    TextView execution;
    TextView location;
    TextView contact;

    public createStudyFragment_finalStep() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_study_final_step, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setupView(view);
        loadData();
    }

    private void setupView(View view){
        title = view.findViewById(R.id.confirmTitle);
        vp = view.findViewById(R.id.confirmVP);
        category = view.findViewById(R.id.confirmCategory);
        desc = view.findViewById(R.id.confirmDesc);
        execution = view.findViewById(R.id.confirmExecution);
        contact = view.findViewById(R.id.confirmContact);
        location = view.findViewById(R.id.confirmLocation);
    }

    private void loadData() {
        Bundle bundle = getArguments();
        System.out.println(bundle);
        if(bundle != null){
            title.setText(bundle.getString("title"));
            vp.setText(bundle.getString("vp"));
            category.setText(bundle.getString("category"));
            desc.setText(bundle.getString("desc"));
            execution.setText(bundle.getString("exe"));
            contact.setText(bundle.getString("contact"));
            String locationString;
            if(!bundle.getString("location").isEmpty()) {
                locationString = bundle.getString("location") + " \n " + bundle.getString("street") + "\n" + bundle.getString("room") ;
            } else{
                locationString = bundle.getString("platform") + " & " + bundle.getString("platform2");
            }
            location.setText(locationString);
        }
    }
}