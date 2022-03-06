package com.example.vpmanager.views;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vpmanager.R;
import com.example.vpmanager.adapter.StudyListAdapter;
import com.example.vpmanager.models.StudyMetaInfoModel;
import com.example.vpmanager.viewmodels.FindStudyViewModel;

import java.util.List;

public class findStudyFragment extends Fragment implements StudyListAdapter.OnStudyItemClickListener {

    private NavController navController;

    private RecyclerView studyList;

    private StudyListAdapter studyListAdapter;
    private FindStudyViewModel findStudyViewModel;

    public findStudyFragment() {
        //Empty public constructor required?
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        Log.d("findStudyFragment", "onCreateView start + end");
        return inflater.inflate(R.layout.fragment_find_study, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d("findStudyFragment", "onViewCreated start");
        super.onViewCreated(view, savedInstanceState);
        studyList = view.findViewById(R.id.recyclerViewFindStudyFragment);

        navController = Navigation.findNavController(view);
        Log.d("findStudyFragment", "navController:" + navController);

        //findStudyFragment is now viewModel owner. maybe the activity should be? --> "requireActivity"
        findStudyViewModel = new ViewModelProvider(this).get(FindStudyViewModel.class);

        //wird nur "neu initiert" wenn es noch keine studyInfoListe gibt (wird sonst frühzeitig returnt).
        findStudyViewModel.init();

        /*
        Log.d("findStudyFragment:", "(in onViewCreated) observer will be set to detect changes");
        findStudyViewModel.getStudyMetaInfo().observe(getViewLifecycleOwner(), new Observer<List<StudyMetaInfoModel>>() {
            @Override
            public void onChanged(List<StudyMetaInfoModel> studyMetaInfos) {
                //triggers when changes are made to the livedata object
                studyListAdapter.notifyDataSetChanged();
            }
        });
         */

        connectStudyListAdapter(view);
        //setupClickListener();
        Log.d("findStudyFragment", "onViewCreated end");
    }

    private void connectStudyListAdapter(View view) {
        Log.d("findStudyFragment", "connectStudyListAdapter start");

        studyListAdapter = new StudyListAdapter(requireActivity(), findStudyViewModel.getStudyMetaInfo().getValue(),
                this);
        Log.d("findStudyFragment",
                "(in connectStudyListAdapter) Studies:" + findStudyViewModel.getStudyMetaInfo().getValue());
        studyList.setLayoutManager(new LinearLayoutManager(getActivity()));
        studyList.setAdapter(studyListAdapter);

        Log.d("findStudyFragment", "connectStudyListAdapter end");
    }

    //Navigate here!!!
    @Override
    public void onStudyClick(String studyId) {
        Log.d("findStudyFragment", "onStudyClick start + end");
        Bundle args = new Bundle();
        Log.d("findStudyFragment", "onStudyClick übergebene studyId:" + studyId);
        //studyList.getAdapter().getItemId(übergebene position); //können position auch hier holen!!
        args.putString("studyId", studyId);
        navController.navigate(R.id.action_findStudyFragment_to_studyFragment, args);
    }

    /*
    public void navigateToStudyDetail(String studyId) {
        Bundle args = new Bundle();
        args.putString("studyId", studyId);
        Log.d("studyId", studyId);
        Log.d("The NavController", navController.toString());
        navController.navigate(R.id.action_findStudyFragment_to_studyFragment, args);
    }
     */

    /*
    private void setupClickListener() {

        studyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //open the detailed view of a study
                //the id of the study is passed to the next fragment
                Bundle args = new Bundle();
                String studyId = studyIds.get(position);
                args.putString("studyId", studyId);
                navController.navigate(R.id.action_findStudyFragment_to_studyFragment, args);
            }
        });
    }
     */
}
