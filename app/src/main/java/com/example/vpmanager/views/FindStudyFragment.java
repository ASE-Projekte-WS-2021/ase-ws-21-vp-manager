package com.example.vpmanager.views;

import static com.example.vpmanager.views.mainActivity.uniqueID;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vpmanager.PA_ExpandableListDataPump;
import com.example.vpmanager.R;
import com.example.vpmanager.adapter.StudyListAdapter;
import com.example.vpmanager.viewmodels.FindStudyViewModel;

public class FindStudyFragment extends Fragment implements StudyListAdapter.OnStudyItemClickListener {

    private NavController navController;
    private RecyclerView studyList;
    private StudyListAdapter studyListAdapter;
    private FindStudyViewModel findStudyViewModel;

    public FindStudyFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find_study, container, false);
        initViewComponents(view);
        //starting point to get the data
        findStudyViewModel.fetchStudyMetaData();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);
        super.onViewCreated(view, savedInstanceState);
    }

    //Parameter: the fragment view
    //Return Values:
    //Initializes the necessary components for the fragment
    private void initViewComponents(View view) {
        studyList = view.findViewById(R.id.recyclerViewFindStudyFragment);
        //mainActivity is currently viewModel owner
        findStudyViewModel = new ViewModelProvider(requireActivity()).get(FindStudyViewModel.class);
        findStudyViewModel.findStudyFragment = this;
    }

    //Parameter:
    //Return Values:
    //Sets the adapter to connect the recyclerview with the data
    public void connectStudyListAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity());
        studyListAdapter = new StudyListAdapter(requireActivity(), findStudyViewModel.getStudyMetaInfo(), this);
        studyList.setAdapter(studyListAdapter);
        studyList.setLayoutManager(linearLayoutManager);
    }

    //Parameter: the id of the study that was clicked on
    //Return Values:
    //Navigates to the more detailed view of a study
    @Override
    public void onStudyClick(String studyId) {
        Bundle args = new Bundle();
        Log.d("FindStudyFragment", "onStudyClick - studyId:" + studyId);
        args.putString("studyId", studyId);
        if (PA_ExpandableListDataPump.navigateToStudyCreatorFragment(uniqueID, studyId)) {
            navController.navigate(R.id.action_findStudyFragment_to_studyCreatorFragment, args);
        } else {
            navController.navigate(R.id.action_findStudyFragment_to_studyFragment, args);
        }
    }
}
