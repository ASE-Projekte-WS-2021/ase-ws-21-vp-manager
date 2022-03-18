package com.example.vpmanager.createStudy;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TimePicker;

import com.example.vpmanager.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class createStudyFragment_StepFive extends Fragment {

    int mYear;
    int mMonth;
    int mDay;
    String weekDay = "";
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
        createStudyActivity.currentFragment = 5;
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
        loadData();
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
    //Loads data received from the activity into the listview
    private void loadData() {
        Bundle bundle = getArguments();
        System.out.println(bundle);
        if (bundle != null) {
            //dates = bundle.getStringArrayList("dates");
        }

    }

    //Parameter:
    //Return values:
    //Creates a datepicker, which will be displayed as a popup in the fragment
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
                        Date date = new Date(year, monthOfYear, dayOfMonth-1);
                        weekDay = simpleDateFormat.format(date);
                        date_time = weekDay + ", " + dayOfMonth + "." + (monthOfYear + 1) + "." + year + " um ";
                        timePicker();
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }



    //Parameter:
    //Return values:
    //Creates a timepicker, which will be displayed as a popup in the fragment after the datepicker
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
        dates.add(date_time + hours + ":" + minutes + " Uhr");
        datePickerAdapter.notifyDataSetChanged();
    }

    //Parameter:
    //Return values:
    //Sets an adapter for the Listview
    private void setupDatePicker() {
        datePickerAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1,
                dates);
        dateList.setAdapter(datePickerAdapter);
    }

}
