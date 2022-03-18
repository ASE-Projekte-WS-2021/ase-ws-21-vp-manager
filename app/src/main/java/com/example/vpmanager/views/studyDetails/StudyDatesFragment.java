package com.example.vpmanager.views.studyDetails;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.example.vpmanager.R;
import com.example.vpmanager.adapter.StudyDatesAdapter;
import com.example.vpmanager.viewmodels.StudyViewModel;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StudyDatesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudyDatesFragment extends Fragment implements StudyDatesAdapter.OnDateClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private StudyViewModel studyViewModel;
    private StudyDatesAdapter studyDatesAdapter;

    private String currentStudyId;
    private String currentUserId;

    //Show information about the dates of the study
    private RecyclerView datesList;
    private RelativeLayout unselectedDateView, selectedDateView;
    private TextView selectedDate;
    private TextView selectedDateLineTwo;
    private Button cancelDateBtn;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StudyDatesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment studyDates.
     */
    // TODO: Rename and change types and number of parameters
    public static StudyDatesFragment newInstance(String param1, String param2) {
        StudyDatesFragment fragment = new StudyDatesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_study_dates, container, false);
        prepareComponents();
        initViews(view);
        studyViewModel.fetchStudyDates(currentStudyId, currentUserId);
        return view;
    }

    private void prepareComponents() {
        currentStudyId = StudyFragment.currentStudyId;
        currentUserId = StudyFragment.currentUserId;
        studyViewModel = new ViewModelProvider(requireActivity()).get(StudyViewModel.class);
        studyViewModel.studyDatesFragment = this;
        studyViewModel.prepareRepo();
    }

    private void initViews(View view) {
        //View for the datesList
        datesList = view.findViewById(R.id.recyclerViewStudyFragment);

        //Views for a selected date
        unselectedDateView = view.findViewById(R.id.unselectedViewLayout);
        selectedDateView = view.findViewById(R.id.selectedViewLayout);
        selectedDate = view.findViewById(R.id.selectedDateItemDate);
        selectedDateLineTwo = view.findViewById(R.id.selectedDateItemLayoutProposal);
        cancelDateBtn = view.findViewById(R.id.cancelDateBtn);

        cancelDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unSelectDateAlert();
            }
        });
    }

    public void setDatesView() {
        if (studyViewModel.getUserIdsOfDates().contains(currentUserId)) {
            //The current user already selected a date
            //If the userIdList contains the currentUserId, the corresponding date should be saved in the viewModel
            studyViewModel.findSelectedDateObject(currentUserId);
            setMySelectedDate();
            unselectedDateView.setVisibility(View.GONE);
            selectedDateView.setVisibility(View.VISIBLE);
        } else {
            //The current user did not select a date
            connectDatesAdapter();
            selectedDateView.setVisibility(View.GONE);
            unselectedDateView.setVisibility(View.VISIBLE);
        }
    }

    private void setMySelectedDate() {
        //more textViews can be filled here
        selectedDate.setText(studyViewModel.getSelectedDateObject().getDate());
        //selectedDateLineTwo.setText(studyViewModel.getSelectedDateObject().getXYZ());
    }

    public void connectDatesAdapter() {
        //connect recyclerView with data here
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity());
        studyDatesAdapter = new StudyDatesAdapter(requireActivity(), studyViewModel.getStudyDates(), this);
        datesList.setAdapter(studyDatesAdapter);
        datesList.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onDateClick(String dateId) {
        selectDateAlert(dateId);
    }

    private void selectDateAlert(String dateId) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //starting point of selecting a date
                        studyViewModel.selectDate(dateId, currentUserId);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.selectDateQuestion))
                .setPositiveButton(getString(R.string.yes), dialogClickListener)
                .setNegativeButton(getString(R.string.no), dialogClickListener).show();
    }

    private void unSelectDateAlert() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //starting point of unselecting a date
                        studyViewModel.unselectDate();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.dropDateQuestion))
                .setPositiveButton(getString(R.string.yes), dialogClickListener)
                .setNegativeButton(getString(R.string.no), dialogClickListener).show();
    }

    public void reloadDates() {
        studyViewModel.fetchStudyDates(currentStudyId, currentUserId);
    }
}