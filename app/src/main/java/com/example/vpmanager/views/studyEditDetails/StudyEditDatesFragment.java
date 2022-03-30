package com.example.vpmanager.views.studyEditDetails;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vpmanager.R;
import com.example.vpmanager.adapter.EditSwipeableDatesAdapter;
import com.example.vpmanager.adapter.SwipeableDatesAdapter;
import com.example.vpmanager.helper.EditSwipeToDeleteCallback;
import com.example.vpmanager.helper.SwipeToDeleteCallback;
import com.example.vpmanager.models.DateModel;
import com.example.vpmanager.viewmodels.StudyEditViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    //private SwipeableDatesAdapter studyEditSwipeableDatesAdapter;
    private EditSwipeableDatesAdapter editSwipeableDatesAdapter;
    private View currentView;

    public StudyEditDatesFragment(){
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_study_dates, container, false);
        prepareComponents();
        setupView(view);
        currentView = view;
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void prepareComponents(){
        currentStudyIdEdit = StudyEditFragment.currentStudyIdEdit;
        //should get the same instance as the Details fragment...
        studyEditViewModel = new ViewModelProvider(getParentFragment()).get(StudyEditViewModel.class);
        studyEditViewModel.studyEditDatesFragment = this;
        Log.d("DatesFragment", "viewModelStore: " + getParentFragment().toString());
        //studyEditViewModel.prepareRepo();
        //setDatesFromViewModel();
    }

    //no arrayList of strings needs to be filled. the list of dateObjects in the viewModel is used directly!!
    public void notifyDatesObjectListChanged(){
        Log.d("StudyEditDatesFragment", "notifyDatesObjectListChanged: "
                + studyEditViewModel.datesEditProcessDataObjects.toString());
        /*
        Log.d("DatesFragment 2", "setDatesFromViewModel: " + studyEditViewModel.datesEditProcessDataObjects.toString());
        for (int i = 0; i < studyEditViewModel.datesEditProcessDataObjects.size(); i++){
            datesArrayList.add(studyEditViewModel.datesEditProcessDataObjects.get(i).getDate());
        }
         */
        editSwipeableDatesAdapter.notifyDataSetChanged();
        //studyEditDatesRecyclerView.setLayoutManager(linearLayoutManager);
    }

    private void setupView(View view){
        studyEditDatesRecyclerView = view.findViewById(R.id.editStudyDatesRecyclerView);
        //setupRecyclerView(view);
        FloatingActionButton editFab = view.findViewById(R.id.fab_edit_dates);
        editFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker();
            }
        });
    }

    public void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        Log.d("StudyEditDatesFragment", "datesEditProcessDataObjects: " + studyEditViewModel.datesEditProcessDataObjects);
        editSwipeableDatesAdapter = new EditSwipeableDatesAdapter(getContext(), studyEditViewModel.datesEditProcessDataObjects, currentView);
        studyEditDatesRecyclerView.setAdapter(editSwipeableDatesAdapter);
        studyEditDatesRecyclerView.setLayoutManager(linearLayoutManager);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new EditSwipeToDeleteCallback(editSwipeableDatesAdapter));
        itemTouchHelper.attachToRecyclerView(studyEditDatesRecyclerView);
        Log.d("StudyEditDatesFragment", "setupRecyclerView: done");
    }

    private void datePicker() {
        final Calendar c = Calendar.getInstance();
        Locale.setDefault(Locale.GERMANY);
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.my_dialog_theme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
                        Date date = new Date(year, monthOfYear, dayOfMonth - 1);
                        weekDay = simpleDateFormat.format(date);
                        date_time = weekDay + ", " + dayOfMonth + "." + (monthOfYear + 1) + "." + year + " um ";
                        timePicker();
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void timePicker() {
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), R.style.my_timepicker_theme,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        mHour = hourOfDay;
                        mMinute = minute;
                        addDateToList(hourOfDay, minute);
                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }

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
        //datesArrayList.add(newDate);

        //The new date is immediately added to the dateObjectList in the viewModel!
        studyEditViewModel.addNewDateToList(newDate, currentStudyIdEdit);

        //editSwipeableDatesAdapter.notifyDataSetChanged();
    }

}
