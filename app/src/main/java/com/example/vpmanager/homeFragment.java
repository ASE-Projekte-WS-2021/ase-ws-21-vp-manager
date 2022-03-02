package com.example.vpmanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class homeFragment extends Fragment {

    private NavController navController;
    private Button findStudyNavBtn, createStudyNavBtn, overviewNavBtn, ownStudyNavBtn;

    public homeFragment() {
        //Empty public constructor required?
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setClickListeners(view);
    }

    private void setClickListeners(View view) {
        navController = Navigation.findNavController(view);

        findStudyNavBtn = view.findViewById(R.id.findStudyCard);
        findStudyNavBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_homeFragment_to_findStudyFragment);
                /*
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setReorderingAllowed(true);
                transaction.replace(R.id.nav_host_fragment_main, findStudyFragment.class, null);
                transaction.commit();
                 */
            }
        });
        createStudyNavBtn = view.findViewById(R.id.createStudyCard);
        createStudyNavBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_homeFragment_to_createStudyActivity);
                /*
                Intent createIntent = new Intent(getContext(), createStudyActivity.class);
                startActivity(createIntent);
                 */
            }
        });
        overviewNavBtn = view.findViewById(R.id.overviewCard);
        overviewNavBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_homeFragment_to_personalAccountFragment);
                /*
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setReorderingAllowed(true);
                transaction.replace(R.id.nav_host_fragment_main, personalAccountFragment.class, null);
                transaction.commit();
                 */
            }
        });
        ownStudyNavBtn = view.findViewById(R.id.ownStudyCard);
        ownStudyNavBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_homeFragment_to_ownStudyFragment);
                /*
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setReorderingAllowed(true);
                transaction.replace(R.id.nav_host_fragment_main, ownStudyFragment.class, null);
                transaction.commit();
                 */
            }
        });
    }
}
