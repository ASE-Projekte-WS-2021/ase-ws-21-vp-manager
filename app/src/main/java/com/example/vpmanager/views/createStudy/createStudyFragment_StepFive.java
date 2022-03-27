package com.example.vpmanager.views.createStudy;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.vpmanager.R;
import com.example.vpmanager.adapter.SwipeableDatesAdapter;
import com.example.vpmanager.helper.SwipeToDeleteCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class createStudyFragment_StepFive extends Fragment {

    private int mYear;
    private int mMonth;
    private int mDay;
    private String weekDay = "";
    private int mHour;
    private int mMinute;
    private String date_time = "";

    //private ListView dateList;
    //private ArrayAdapter<String> datePickerAdapter;
    private RecyclerView dateListRecycler;
    private SwipeableDatesAdapter swipeableDatesAdapter;

    //the complete date+time strings are stored here and passed to the adapter
    public static ArrayList<String> dates = new ArrayList<>();


    //Parameter:
    //Return values:
    //Sets the current fragment for the activity
    public createStudyFragment_StepFive() {
        CreateStudyFragment.currentFragment = 5;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        //dateList = view.findViewById(R.id.createDatelist);
        dateListRecycler = view.findViewById(R.id.createDateRecyclerView);
        //loadData();
        setupDatePicker(view);
        FloatingActionButton fab = view.findViewById(R.id.fab);
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
    //Sets an adapter for the Listview
    private void setupDatePicker(View view) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity());
        //datePickerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, dates);
        swipeableDatesAdapter = new SwipeableDatesAdapter(requireActivity(), dates, view);
        //dateList.setAdapter(datePickerAdapter);
        dateListRecycler.setAdapter(swipeableDatesAdapter);
        dateListRecycler.setLayoutManager(linearLayoutManager);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(swipeableDatesAdapter));
        itemTouchHelper.attachToRecyclerView(dateListRecycler);
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
                        Date date = new Date(year, monthOfYear, dayOfMonth - 1);
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
        Log.d("addDatetoList", "before add new" + dates);
        dates.add(date_time + hours + ":" + minutes + " Uhr");
        //datePickerAdapter.notifyDataSetChanged();
        swipeableDatesAdapter.notifyDataSetChanged();
        Log.d("addDatetoList", "after notity" + dates);
    }
}
