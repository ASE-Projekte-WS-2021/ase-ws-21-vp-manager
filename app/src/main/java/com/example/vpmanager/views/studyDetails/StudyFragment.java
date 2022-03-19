package com.example.vpmanager.views.studyDetails;

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
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

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

        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);

        detailsFragment = new StudyDetailsFragment();
        datesFragment = new StudyDatesFragment();

        tabLayout.setupWithViewPager(viewPager);

        viewPagerAdapter viewPagerAdapter = new viewPagerAdapter(getParentFragmentManager(), 0);
        viewPagerAdapter.addFragment(detailsFragment, "Details");
        viewPagerAdapter.addFragment(datesFragment, "Termine");
        viewPager.setAdapter(viewPagerAdapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);
        staticnavcontroller = navController;
        super.onViewCreated(view, savedInstanceState);
    }

    private void getRequiredInfos() {
        currentStudyId = getArguments().getString("studyId");
        currentUserId = mainActivity.createUserId(getActivity());
    }

    public static void creatorDetailViewCall(){
        Bundle args = new Bundle();
        args.putString("studyId", currentStudyId);
        staticnavcontroller.navigate(R.id.action_studyFragment_to_editStudyFragment, args);
    }

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
