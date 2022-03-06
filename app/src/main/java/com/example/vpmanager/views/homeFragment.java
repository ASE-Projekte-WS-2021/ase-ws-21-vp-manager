package com.example.vpmanager.views;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.vpmanager.R;

public class homeFragment extends Fragment {

    private NavController navController;
    private Button findStudyNavBtn;
    private Button createStudyNavBtn;
    private Button overviewNavBtn;
    private Button ownStudyNavBtn;

    public homeFragment() {
        //Empty public constructor required?
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        Log.d("homeFragment", "onCreateView start + end");
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d("homeFragment", "onViewCreated start");
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        Log.d("homeFragment", "navController:" + navController);

        setClickListeners(view);
        Log.d("homeFragment", "onViewCreated end");
    }

    //sets click listeners on the four buttons to open their corresponding fragments
    private void setClickListeners(View view) {
        Log.d("homeFragment", "setClickListeners start");
        findStudyNavBtn = view.findViewById(R.id.findStudyCard);
        findStudyNavBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("homeFragment", "Navigate to: findStudyFragment");
                navController.navigate(R.id.action_homeFragment_to_findStudyFragment);
            }
        });
        createStudyNavBtn = view.findViewById(R.id.createStudyCard);
        createStudyNavBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("homeFragment", "Navigate to: createStudyActivity");
                navController.navigate(R.id.action_homeFragment_to_createStudyActivity);
            }
        });
        overviewNavBtn = view.findViewById(R.id.overviewCard);
        overviewNavBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("homeFragment", "Navigate to: personalAccountFragment");
                navController.navigate(R.id.action_homeFragment_to_personalAccountFragment);
            }
        });
        ownStudyNavBtn = view.findViewById(R.id.ownStudyCard);
        ownStudyNavBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("homeFragment", "Navigate to: ownStudyFragment");
                navController.navigate(R.id.action_homeFragment_to_ownStudyFragment);
            }
        });
        Log.d("homeFragment", "setClickListeners end");
    }

    //TODO: For now the "Anstehende Termine" are hardcoded. These need to be retrieved from the database later!
}
