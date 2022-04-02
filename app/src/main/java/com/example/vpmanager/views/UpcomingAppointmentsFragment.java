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

import com.example.vpmanager.PA_ExpandableListDataPump;
import com.example.vpmanager.R;
import com.example.vpmanager.adapter.CustomListViewAdapterAppointments;
import com.example.vpmanager.viewmodels.UpcomingAppointViewModel;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class UpcomingAppointmentsFragment extends Fragment {

    private UpcomingAppointViewModel mViewModel;
    private NavController navController;
    private ListView arrivingDatesList;
    //private HashMap<String, String> getStudyIdByName = new HashMap<>();

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
        //setUpDateList();
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

    //Parameter:
    //Return Values:
    //gets necessary data from database converts it into a list
    //setUpDateList
    /*
    private void setUpDateList() {
        final List<String[]>[] arrivingDates = new List[]{null};

        PA_ExpandableListDataPump.DB_DATES_LIST.clear();
        PA_ExpandableListDataPump.DB_STUDIES_LIST.clear();

        PA_ExpandableListDataPump.getAllDates(new PA_ExpandableListDataPump.FirestoreCallbackDates() {
            @Override
            public void onCallback(boolean finished) {
                if (finished)
                    PA_ExpandableListDataPump.getAllStudies(new PA_ExpandableListDataPump.FirestoreCallbackStudy() {
                        @Override
                        public void onCallback() {
                            arrivingDates[0] = PA_ExpandableListDataPump.getAllArrivingDates();
                            finishSetupList(arrivingDates[0]);
                        }
                    });
            }
        });
    }
     */

    public void setListViewAdapter(CustomListViewAdapterAppointments adapter) {
        arrivingDatesList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public NavController getNavController() {
        return navController;
    }

    //Parameter: data list from database call
    //Return Values:
    //converts and sorts the data. calls listview adapter to set up list entries
    //finishSetupList
    /*
    private void finishSetupList(List <String[]> dates){

        if (dates != null) {

            ArrayList<String> listEntries = new ArrayList<>();

            HashMap<String, String> sortingMap = new HashMap<>();

            for (String[] listEntry : dates) {
                String name = listEntry[0];
                String date = listEntry[1];
                String studyID = listEntry[2];

                if(date != null && name != null)
                {
                    sortingMap.put(date, name);
                    getStudyIdByName.put(name, studyID);
                }
            }

            for (String key : sortingMap.keySet()) {
                listEntries.add(sortingMap.get(key) + "\t\t" + key);
            }
            Collections.reverse(listEntries);
            arrivingDatesList.setAdapter(new CustomListViewAdapterAppointments(this.getContext(), navController, listEntries, getStudyIdByName, "UpcomingAppointmentsFragment"));
        }
    }
     */

}
