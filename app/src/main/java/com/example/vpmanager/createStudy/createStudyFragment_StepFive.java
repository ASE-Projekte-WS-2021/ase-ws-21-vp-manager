package com.example.vpmanager.createStudy;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TimePicker;

import com.example.vpmanager.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

public class createStudyFragment_StepFive extends Fragment {

    int mYear;
    int mMonth;
    int mDay;
    int mHour;
    int mMinute;
    String date_time = "";

    ListView dateList;
    ArrayList<String> dates = new ArrayList<>();
    ArrayAdapter<String> datePickerAdapter;



    //Parameter:
    //Return values:
    //Sets the current fragment for the activity
    public createStudyFragment_StepFive() {
        createStudyActivity.currentFragment = 6;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_study_step_five, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setupView(view);
    }

    //Parameter:
    //Return values:
    //Connects the code with the view
    private void setupView(View view) {
        dateList = view.findViewById(R.id.createDatelist);
        setupDatePicker();
        ExtendedFloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker();
            }
        });
    }

    //Parameter:
    //Return values:
    //Creates a datepicker, which will be displayed as a popup in the fragment
    private void datePicker() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),R.style.my_dialog_theme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        date_time = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        timePicker();
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    //Parameter:
    //Return values:
    //Creates a timepicker, which will be displayed as a popup in the fragment after the datepicker
    private void timePicker() {
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),R.style.my_timepicker_theme,
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

    //Parameter: hoursOfDay, minute
    //Return values:
    //Adds the created date and time to the list and transforms them into a uniform pattern
    private void addDateToList(int hourOfDay, int minute) {
        String minutes = Integer.toString(minute);
        String hours = Integer.toString(hourOfDay);
        if (minute < 10) {
            minutes = "0" + minute;
        }
        if (hourOfDay < 10) {
            hours = "0" + hourOfDay;
        }
        dates.add(date_time + " " + hours + ":" + minutes);
        datePickerAdapter.notifyDataSetChanged();
    }

    //Parameter:
    //Return values:
    //Sets an adapter for the Listview
    public void setupDatePicker() {
        datePickerAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1,
                dates);
        dateList.setAdapter(datePickerAdapter);
    }
}
