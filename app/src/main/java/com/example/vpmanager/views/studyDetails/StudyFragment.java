package com.example.vpmanager.views.studyDetails;

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

public class StudyFragment extends Fragment {

    public static String currentStudyId;
    public static String currentUserId;

    private NavController navController;
    private static NavController staticnavcontroller;

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private StudyDetailsFragment detailsFragment;
    private StudyDatesFragment datesFragment;

    public StudyFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_study, container, false);
        getRequiredInfos();

        viewPager = (ViewPager) view.findViewById(R.id.view_pager_study_details);
        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout_study_details);

        detailsFragment = new StudyDetailsFragment();
        datesFragment = new StudyDatesFragment();

        tabLayout.setupWithViewPager(viewPager);
        mainActivity.currentFragment = "studyFragment";

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getParentFragmentManager(), 0);
        viewPagerAdapter.addFragment(detailsFragment, getString(R.string.studyViewTabOne));
        viewPagerAdapter.addFragment(datesFragment, getString(R.string.studyViewTabTwo));
        viewPager.setAdapter(viewPagerAdapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);
        staticnavcontroller = navController;
        super.onViewCreated(view, savedInstanceState);
    }


    //Parameter:
    //Return values:
    //Sets the current user ID and study ID
    private void getRequiredInfos() {
        currentStudyId = getArguments().getString("studyId");
        currentUserId = mainActivity.createUserId(getActivity());
    }
}
