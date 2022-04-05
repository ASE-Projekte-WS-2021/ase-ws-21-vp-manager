package com.example.vpmanager.views.studyCreatorDetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import com.example.vpmanager.R;
import com.example.vpmanager.views.mainActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class studyCreatorFragment extends Fragment {

    public static String currentStudyId;
    public static String currentUserId;

    private NavController navController;

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private StudyCreatorDetailsFragment detailsFragment;
    private StudyCreatorDatesFragment datesFragment;


    public studyCreatorFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_creator_study, container, false);
        getRequiredInfos();

        viewPager = (ViewPager) view.findViewById(R.id.creator_view_pager);
        tabLayout = (TabLayout) view.findViewById(R.id.creator_tab_layout);

        detailsFragment = new StudyCreatorDetailsFragment();
        datesFragment = new StudyCreatorDatesFragment();

        tabLayout.setupWithViewPager(viewPager);

        viewPagerAdapter viewPagerAdapter = new viewPagerAdapter(getChildFragmentManager(), 0);
        viewPagerAdapter.addFragment(detailsFragment, "Details");
        viewPagerAdapter.addFragment(datesFragment, "Termine");
        viewPager.setAdapter(viewPagerAdapter);
        mainActivity.currentFragment = "studyCreatorFragment";

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);
        super.onViewCreated(view, savedInstanceState);
        Boolean fromEditFragment = getArguments().getBoolean("fromEditFragment");
        if (fromEditFragment) {
            Snackbar snackbar = Snackbar.make(view, "Studie geupdated", Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
    }


    //Parameter:
    //Return values:
    //Sets the current user ID and study ID
    private void getRequiredInfos() {
        currentUserId = mainActivity.createUserId(getActivity());
        currentStudyId = getArguments().getString("studyId");

    }


    //viewPagerAdapter class for swiping between tab fragments
    private class viewPagerAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();
        private List<String> fragmentNames = new ArrayList<>();

        public viewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            fragmentNames.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentNames.get(position);
        }
    }


}
