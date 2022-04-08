package com.example.vpmanager.views.studyEditDetails;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vpmanager.R;
import com.example.vpmanager.adapter.EditSwipeableDatesAdapter;
import com.example.vpmanager.helper.EditSwipeToDeleteCallback;
import com.example.vpmanager.viewmodels.StudyEditViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class StudyEditDatesFragment extends Fragment {

    private StudyEditViewModel studyEditViewModel;
    private String currentStudyIdEdit;

    private int mYear;
    private int mMonth;
    private int mDay;
    private String weekDay = "";
    private int mHour;
    private int mMinute;
    private String date_time = "";

    private RecyclerView studyEditDatesRecyclerView;
    private EditSwipeableDatesAdapter editSwipeableDatesAdapter;
    private View currentView;


    public StudyEditDatesFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_study_appointments, container, false);
        prepareComponents();
        setupView(view);
        currentView = view;
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    //Parameter:
    //Return values:
    //Sets study edit ID and the View Model
    private void prepareComponents() {
        currentStudyIdEdit = StudyEditFragment.currentStudyIdEdit;
        //should get the same viewModel as the Details fragment
        studyEditViewModel = new ViewModelProvider(getParentFragment()).get(StudyEditViewModel.class);
        studyEditViewModel.studyEditDatesFragment = this;
    }


    //Parameter:
    //Return values:
    //no arrayList of strings needs to be filled. The list of dateObjects in the viewModel is used directly
    public void notifyDatesObjectListChanged() {
        editSwipeableDatesAdapter.notifyDataSetChanged();
    }


    //Parameter: view
    //Return values:
    //Initializes the Recycler View and sets the edit dates button
    private void setupView(View view) {
        studyEditDatesRecyclerView = view.findViewById(R.id.editStudyDatesRecyclerView);
        FloatingActionButton editFab = view.findViewById(R.id.fab_edit_dates);
        editFab.setOnClickListener(view1 -> datePicker());
    }


    //Parameter:
    //Return values:
    //Sets up the Recycler View elements
    public void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        editSwipeableDatesAdapter = new EditSwipeableDatesAdapter(getContext(), studyEditViewModel.datesEditProcessDataObjects, currentView);
        studyEditDatesRecyclerView.setAdapter(editSwipeableDatesAdapter);
        studyEditDatesRecyclerView.setLayoutManager(linearLayoutManager);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new EditSwipeToDeleteCallback(editSwipeableDatesAdapter));
        itemTouchHelper.attachToRecyclerView(studyEditDatesRecyclerView);
    }


    //Parameter:
    //Return values:
    //Sets up the date picker view and dialog
    private void datePicker() {
        final Calendar c = Calendar.getInstance();
        Locale.setDefault(Locale.GERMANY);
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.my_dialog_theme,
                (view, year, monthOfYear, dayOfMonth) -> {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
                    Date date = new Date(year, monthOfYear, dayOfMonth - 1);
                    weekDay = simpleDateFormat.format(date);
                    date_time = weekDay + ", " + dayOfMonth + "." + (monthOfYear + 1) + "." + year + " um ";
                    timePicker();
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }


    //Parameter:
    //Return values:
    //Sets up the time picker view and dialog
    private void timePicker() {
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), R.style.my_timepicker_theme,
                (view, hourOfDay, minute) -> {

                    mHour = hourOfDay;
                    mMinute = minute;
                    addDateToList(hourOfDay, minute);
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }


    //Parameter: hourOfDay, minute
    //Return values:
    //Adds new date item to the list
    private void addDateToList(int hourOfDay, int minute) {
        String minutes = Integer.toString(minute);
        String hours = Integer.toString(hourOfDay);
        if (minute < 10) {
            minutes = "0" + minute;
        }
        if (hourOfDay < 10) {
            hours = "0" + hourOfDay;
        }

        String newDate = date_time + hours + ":" + minutes + " Uhr";

        //The new date is immediately added to the dateObjectList in the viewModel
        studyEditViewModel.addNewDateToList(newDate, currentStudyIdEdit);
    }

}
