package com.example.vpmanager.views;

import android.os.Bundle;
import android.util.Log;
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

import com.example.vpmanager.AccessDatabase;
import com.example.vpmanager.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class homeFragment extends Fragment {

    private NavController navController;
    FirebaseAuth firebaseAuth;
    private AccessDatabase accessDatabase;


    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    public homeFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        createTabs(view);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        userLoggedIn();
    }

    private void registerNewUser() {
        accessDatabase = new AccessDatabase();
        String deviceID = mainActivity.createUserId(getActivity());
        Log.d("deviceId", "deviceId" + deviceID);
        accessDatabase.createNewUser(deviceID);
    }

    //Parameter: current view to find components
    //Return values:
    //sets up the tablayout and adds and adapter for the content
    private void createTabs(View view) {

        viewPager = view.findViewById(R.id.view_pager_home);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout_home);

        com.example.vpmanager.views.personalAccountFragment personalAccountFragment = new personalAccountFragment();
        UpcomingAppointments appointments = new UpcomingAppointments();

        tabLayout.addTab(tabLayout.newTab().setText("Persönliche Übersicht"));
        tabLayout.addTab(tabLayout.newTab().setText("Termine"));

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
        viewPagerAdapter.addFragment(personalAccountFragment, "Persönliche Übersicht");
        viewPagerAdapter.addFragment(appointments, "Termine");

        viewPager.setAdapter(viewPagerAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        System.out.println("Tab Count: " + tabLayout.getTabCount());
    }

    //Parameter:
    //Return values:
    //checks if the user is currently logged in. If not sends him to login screen
    private void userLoggedIn(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user == null) {
            navController.navigate(R.id.action_global_nestedGraphLoginRegistration);
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        userLoggedIn();

    }

    //Parameter:
    //Return values:
    //defining adapter class for the tabLayout
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
}
