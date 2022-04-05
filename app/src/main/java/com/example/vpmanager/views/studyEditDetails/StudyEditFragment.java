package com.example.vpmanager.views.studyEditDetails;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import com.example.vpmanager.R;
import com.example.vpmanager.adapter.ViewPagerAdapter;
import com.example.vpmanager.views.mainActivity;
import com.google.android.material.tabs.TabLayout;

public class StudyEditFragment extends Fragment {

    public static String currentStudyIdEdit;
    public static String currentUserIdEdit;

    private NavController navController;

    private ViewPager viewPagerEdit;
    private TabLayout tabLayoutEdit;

    private StudyEditDetailsFragment studyEditDetailsFragment;
    private StudyEditDatesFragment studyEditDatesFragment;

    public StudyEditFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_study, container, false);
        getRequiredInfos();

        viewPagerEdit = (ViewPager) view.findViewById(R.id.view_pager_edit_study);
        tabLayoutEdit = (TabLayout) view.findViewById(R.id.tab_layout_edit_study);

        studyEditDetailsFragment = new StudyEditDetailsFragment();
        studyEditDatesFragment = new StudyEditDatesFragment();

        tabLayoutEdit.setupWithViewPager(viewPagerEdit);
        mainActivity.currentFragment = "editFragment";

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getParentFragmentManager(), 0);
        viewPagerAdapter.addFragment(studyEditDetailsFragment, getString(R.string.studyViewTabOne));
        viewPagerAdapter.addFragment(studyEditDatesFragment, getString(R.string.studyViewTabTwo));
        viewPagerEdit.setAdapter(viewPagerAdapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);
        super.onViewCreated(view, savedInstanceState);
    }

    //Parameter:
    //Return values:
    //Sets the current user ID and study ID
    private void getRequiredInfos() {
        currentStudyIdEdit = getArguments().getString("studyId");
        currentUserIdEdit = mainActivity.createUserId(getActivity());
    }

}