package com.example.vpmanager.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.vpmanager.R;
import com.example.vpmanager.adapter.CustomListViewAdapterAppointments;
import com.example.vpmanager.viewmodels.UpcomingAppointViewModel;

public class UpcomingAppointmentsFragment extends Fragment {

    private UpcomingAppointViewModel mViewModel;
    private NavController navController;
    private ListView arrivingDatesList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upcoming_appointments, container, false);
        prepareViewModel();
        initHomeFragmentComponents(view);
        mViewModel.getAllDatesAndStudies();
        mainActivity.currentFragment = "upcomingAppointments";

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);
        super.onViewCreated(view, savedInstanceState);
    }

    private void prepareViewModel(){
        //the fragment is viewModelStoreOwner
        mViewModel = new ViewModelProvider(this).get(UpcomingAppointViewModel.class);
        mViewModel.upcomingAppointmentsFragment = this;
        mViewModel.prepareRepo();
    }

    //Parameter: the fragment view
    //Return Values:
    //connects the view components with their ids
    private void initHomeFragmentComponents(View view) {
        arrivingDatesList = view.findViewById(R.id.listViewOwnArrivingStudyFragment);
    }



    public void setListViewAdapter(CustomListViewAdapterAppointments adapter) {
        arrivingDatesList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public NavController getNavController() {
        return navController;
    }


}
