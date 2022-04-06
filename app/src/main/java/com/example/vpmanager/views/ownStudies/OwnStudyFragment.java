package com.example.vpmanager.views.ownStudies;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import com.example.vpmanager.R;
import com.example.vpmanager.adapter.ViewPagerAdapter;
import com.example.vpmanager.views.mainActivity;
import com.google.android.material.tabs.TabLayout;

public class OwnStudyFragment extends Fragment {

    private NavController navController;

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private PersonalAccountFragment personalAccountFragment;
    private SelfcreatedStudiesFragment selfcreatedStudiesFragment;


    public OwnStudyFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_own_study, container, false);

        viewPager = view.findViewById(R.id.view_pager_ownStudies);
        tabLayout = view.findViewById(R.id.tab_layout_ownStudies);
        personalAccountFragment = new PersonalAccountFragment();
        selfcreatedStudiesFragment = new SelfcreatedStudiesFragment();
        tabLayout.setupWithViewPager(viewPager);
        mainActivity.currentFragment = "ownStudyFragment";

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), 0);
        viewPagerAdapter.addFragment(personalAccountFragment, getString(R.string.ownStudyStatusTab)); //Teilnahmen
        viewPagerAdapter.addFragment(selfcreatedStudiesFragment, getString(R.string.ownStudyTab)); //Veranstaltungen
        viewPager.setAdapter(viewPagerAdapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

}
