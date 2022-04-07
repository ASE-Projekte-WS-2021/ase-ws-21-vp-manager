package com.example.vpmanager.views.studyDetails;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.example.vpmanager.R;
import com.example.vpmanager.adapter.StudyDatesAdapter;
import com.example.vpmanager.viewmodels.StudyViewModel;
import com.google.android.material.snackbar.Snackbar;

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


public class StudyDatesFragment extends Fragment implements StudyDatesAdapter.OnDateClickListener {


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


    public StudyDatesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_study_appointments, container, false);
        prepareComponents();
        initViews(view);
        studyViewModel.fetchStudyDates(currentStudyId, currentUserId);
        return view;
    }


    //Parameter:
    //Return values:
    //Sets current study ID, user ID and the ViewModel
    private void prepareComponents() {
        currentStudyId = StudyFragment.currentStudyId;
        currentUserId = StudyFragment.currentUserId;
        studyViewModel = new ViewModelProvider(requireActivity()).get(StudyViewModel.class);
        studyViewModel.studyDatesFragment = this;
        studyViewModel.prepareRepo();
    }


    //Parameter: view
    //Return values:
    //Sets the view for the dateslist and selected dates
    private void initViews(View view) {
        datesList = view.findViewById(R.id.recyclerViewStudyFragment);

        //views for a selected date
        unselectedDateView = view.findViewById(R.id.unselectedViewLayout);
        selectedDateView = view.findViewById(R.id.selectedViewLayout);
        selectedDate = view.findViewById(R.id.selectedDateItemDate);
        selectedDateLineTwo = view.findViewById(R.id.selectedDateItemLayoutProposal);
        cancelDateBtn = view.findViewById(R.id.cancelDateBtn);

        cancelDateBtn.setOnClickListener(view1 -> unSelectDateAlert());
    }


    //Parameter:
    //Return values:
    //Sets the views for selected dates and the unselected date state
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


    //Parameter:
    //Return values:
    //Sets the text for selected dates
    private void setMySelectedDate() {
        //more textViews can be filled here
        selectedDate.setText(studyViewModel.getSelectedDateObject().getDate());
    }


    //Parameter:
    //Return values:
    //Connects the recyclerView with dat
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


    //Parameter: dateId
    //Return values:
    //Sets alertdialogs for selecting dates
    private void selectDateAlert(String dateId) {
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    //starting point of selecting a date
                    studyViewModel.selectDate(dateId, currentUserId);
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.selectDateQuestion))
                .setPositiveButton(getString(R.string.yes), dialogClickListener)
                .setNegativeButton(getString(R.string.no), dialogClickListener).show();
    }


    //Parameter: dateId
    //Return values:
    //Sets alertdialogs for unselecting dates
    private void unSelectDateAlert() {
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    //starting point of unselecting a date
                    studyViewModel.unselectDate();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.dropDateQuestion))
                .setPositiveButton(getString(R.string.yes), dialogClickListener)
                .setNegativeButton(getString(R.string.no), dialogClickListener).show();
    }

    public void disAbleSignOutButton() {
        cancelDateBtn.setVisibility(View.GONE);
    }


    //Parameter:
    //Return values:
    //Gets current study id and user id
    public void reloadDates() {
        studyViewModel.fetchStudyDates(currentStudyId, currentUserId);
    }

    //Parameter:
    //Return values:
    //Shows text when successfully signed up for study
    public void showSnackBarSelectionSuccessful() {
        Snackbar.make(getView(), getString(R.string.logInSuccessful), Snackbar.LENGTH_LONG)
                .show();
    }

    //Parameter:
    //Return values:
    //Shows text when successfully signed out
    public void showSnackBarDeselectionSuccessful() {
        Snackbar.make(getView(), getString(R.string.logOutSuccessful), Snackbar.LENGTH_LONG)
                .show();
    }

    @SuppressLint("ResourceAsColor")
    public void showSnackBarSelectionUnsuccessful() {
        Snackbar.make(getView(), getString(R.string.elementExpired), Snackbar.LENGTH_LONG)
                .show();
    }
}