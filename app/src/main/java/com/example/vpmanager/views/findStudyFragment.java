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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find_study, container, false);
        initFindStudyFragmentComponents(view);

        //data is first changed in the viewModel...
        findStudyViewModel.init();
        //...and passed to the adapter later.
        connectStudyListAdapter();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d("findStudyFragment", "onViewCreated start + end");
        navController = Navigation.findNavController(view);
        super.onViewCreated(view, savedInstanceState);
    }

    private void initFindStudyFragmentComponents(View view){
        studyList = view.findViewById(R.id.recyclerViewFindStudyFragment);
        //findStudyFragment is now viewModel owner. maybe the activity should be? --> "requireActivity"
        findStudyViewModel = new ViewModelProvider(this).get(FindStudyViewModel.class);
    }

    private void connectStudyListAdapter() {
        Log.d("findStudyFragment",
                "connectStudyListAdapter start. Size of Array:" + findStudyViewModel.getStudyMetaInfo().size());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        studyListAdapter = new StudyListAdapter(requireActivity(), findStudyViewModel.getStudyMetaInfo(), this);
        studyList.setAdapter(studyListAdapter);
        studyList.setLayoutManager(linearLayoutManager);
        Log.d("findStudyFragment", "connectStudyListAdapter end");
    }

    //Navigate to a detailed view of a study
    @Override
    public void onStudyClick(String studyId) {
        Log.d("findStudyFragment", "onStudyClick start + end");
        Bundle args = new Bundle();
        Log.d("findStudyFragment", "onStudyClick übergebene studyId:" + studyId);
        //studyList.getAdapter().getItemId(übergebene position); //man könnte die position auch hier holen!?
        args.putString("studyId", studyId);
        navController.navigate(R.id.action_findStudyFragment_to_studyFragment, args);
    }
}
