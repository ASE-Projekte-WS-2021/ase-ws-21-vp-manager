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

        //childFragmentManager is needed to load tabs this way. Instead of parentFragmentManager()
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), 0);

        viewPagerAdapter.addFragment(personalAccountFragment, "Mein VP-Status"); //Teilnahmen
        viewPagerAdapter.addFragment(selfcreatedStudiesFragment, "Meine Studien"); //Veranstaltungen
        viewPager.setAdapter(viewPagerAdapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        //createTabs(view);
    }

    /*
    @Override
    public void onStart()
    {
        super.onStart();
    }
     */

    //Parameter: current view to find components
    //Return values:
    //sets up the tablayout and adds and adapter for the content
    /*
    private void createTabs(View view) {

        viewPager = view.findViewById(R.id.view_pager_ownStudies);
        tabLayout = view.findViewById(R.id.tab_layout_ownStudies);

        PersonalAccountFragment personalAccountFragment = new PersonalAccountFragment();
        SelfcreatedStudiesFragment studies = new SelfcreatedStudiesFragment();

        tabLayout.addTab(tabLayout.newTab().setText("Teilnahmen"));
        tabLayout.addTab(tabLayout.newTab().setText("Veranstaltungen"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                tabLayout.selectTab(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), 0);//tabLayout.getTabCount());
        viewPagerAdapter.addFragment(personalAccountFragment, "Teilnahmen");
        viewPagerAdapter.addFragment(studies, "Veranstaltungen");

        viewPager.setAdapter(viewPagerAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        System.out.println("Tab Count: " + tabLayout.getTabCount());
    }

     */

    //Parameter:
    //Return values:
    //defining adapter class for the tabLayout
    /*
    private static class ViewPagerAdapter extends FragmentStatePagerAdapter
    {
        private final List<Fragment> fragments = new ArrayList<>();
        private final List<String> fragmentNames = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment, String title){
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

     */
}
