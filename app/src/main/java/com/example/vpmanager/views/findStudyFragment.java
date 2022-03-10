package com.example.vpmanager.views;

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

import com.example.vpmanager.R;
import com.example.vpmanager.adapter.StudyListAdapter;
import com.example.vpmanager.viewmodels.FindStudyViewModel;

public class findStudyFragment extends Fragment implements StudyListAdapter.OnStudyItemClickListener {

    private NavController navController;
    private RecyclerView studyList;
    private StudyListAdapter studyListAdapter;
    private FindStudyViewModel findStudyViewModel;

    public findStudyFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find_study, container, false);
        initFindStudyFragmentComponents(view);
        //data is first retrieved from the viewModel...
        findStudyViewModel.init();
        //...and passed to the adapter later.
        connectStudyListAdapter();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);
        super.onViewCreated(view, savedInstanceState);
    }

    //Parameter: the fragment view
    //Return Values:
    //connects the recycler view with its id and provides the viewModel
    private void initFindStudyFragmentComponents(View view) {
        studyList = view.findViewById(R.id.recyclerViewFindStudyFragment);
        //findStudyFragment is now viewModel owner. maybe the activity should be? --> "requireActivity"
        findStudyViewModel = new ViewModelProvider(this).get(FindStudyViewModel.class);
    }

    //Parameter:
    //Return Values:
    //sets the adapter to connect the recyclerview with the data
    private void connectStudyListAdapter() {
        Log.d("findStudyFragment", "Size of Array:" + findStudyViewModel.getStudyMetaInfo().size());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        studyListAdapter = new StudyListAdapter(requireActivity(),
                findStudyViewModel.getStudyMetaInfo(), this);
        studyList.setAdapter(studyListAdapter);
        studyList.setLayoutManager(linearLayoutManager);
    }

    //Parameter: the id of the study that was clicked on
    //Return Values:
    //navigates to the more detailed view of a study
    @Override
    public void onStudyClick(String studyId) {
        Bundle args = new Bundle();
        Log.d("findStudyFragment", "onStudyClick - studyId:" + studyId);
        args.putString("studyId", studyId);
        navController.navigate(R.id.action_findStudyFragment_to_studyFragment, args);
                                  //action_findStudyFragment_to_studyFragment
    }
}
