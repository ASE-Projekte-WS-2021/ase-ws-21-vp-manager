package com.example.vpmanager.views.createStudy;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.example.vpmanager.Config;
import com.example.vpmanager.R;
import com.example.vpmanager.adapter.SwipeableDatesAdapter;
import com.example.vpmanager.helper.SwipeToDeleteCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.example.vpmanager.views.MainActivity;


public class CreateStudyFragment_StepFive extends Fragment {

    private int mYear;
    private int mMonth;
    private int mDay;
    private String weekDay = "";
    private String date_time = "";
    private int mHour;
    private int mMinute;
    private boolean doNotShowDuplicateWarning = false;
    private RecyclerView dateListRecycler;
    private SwipeableDatesAdapter swipeableDatesAdapter;

    //the complete date+time strings are stored here and passed to the adapter
    public static ArrayList<String> dates = new ArrayList<>();


    //Parameter:
    //Return values:
    //Sets the current fragment for the activity
    public CreateStudyFragment_StepFive() {
        CreateStudyFragment.currentFragment = Config.createFragmentFive;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MainActivity.currentFragment = "createStepFive";
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
        dateListRecycler = view.findViewById(R.id.createDateRecyclerView);
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
    //Sets an adapter for the Listview
    private void setupDatePicker(View view) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity());
        swipeableDatesAdapter = new SwipeableDatesAdapter(requireActivity(), dates, view);
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
    //Creates a timepicker, which will be displayed as a popup in the fragment after the datepicker
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
        String currentDate = date_time + hours + ":" + minutes + " Uhr";
        if (!isDuplicate(currentDate)) {
            dates.add(currentDate);
            swipeableDatesAdapter.notifyDataSetChanged();
        } else {
            if (!doNotShowDuplicateWarning) {
                showToolTip(currentDate);
            } else {
                dates.add(currentDate);
                swipeableDatesAdapter.notifyDataSetChanged();
            }
        }
    }


    //Parameter: currentDate
    //Return values: boolean
    //Check for duplicates in date ArrayList
    private boolean isDuplicate(String currentDate) {
        for (int i = 0; i < dates.size(); i++) {
            if (dates.get(i).equals(currentDate)) {
                return true;
            }
        }
        return false;
    }


    //Parameter: currentDate
    //Return values: boolean
    //Sets tooltip view and dialog for current fragment
    private void showToolTip(String currentDate) {
        Dialog dialog = new Dialog(getActivity(), R.style.DialogStyle);
        dialog.setContentView(R.layout.dialog_warning);

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_window);

        ImageView btnClose = dialog.findViewById(R.id.btn_close);
        Button addAnywaysButton = dialog.findViewById(R.id.addAnyways);
        Button abortButton = dialog.findViewById(R.id.abort);
        CheckBox doNotShowAgainCheck = dialog.findViewById(R.id.doNotShowAgainCheckBox);

        btnClose.setOnClickListener(view -> {
            if (doNotShowAgainCheck.isChecked()) {
                doNotShowDuplicateWarning = true;
            }
            dialog.dismiss();
        });

        addAnywaysButton.setOnClickListener(v -> {
            if (doNotShowAgainCheck.isChecked()) {
                doNotShowDuplicateWarning = true;
            }
            dates.add(currentDate);
            swipeableDatesAdapter.notifyDataSetChanged();
            dialog.dismiss();

        });

        abortButton.setOnClickListener(v -> {
            if (doNotShowAgainCheck.isChecked()) {
                doNotShowDuplicateWarning = true;
            }
            dialog.dismiss();
        });

        dialog.show();
    }


}
