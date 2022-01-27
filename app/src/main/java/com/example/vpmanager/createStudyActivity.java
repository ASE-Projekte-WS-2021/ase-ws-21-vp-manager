package com.example.vpmanager;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class createStudyActivity extends AppCompatActivity {

    Button addButton;
    ListView dateList;
    Spinner categories;
    Spinner executionType;
    ArrayList<String> dates = new ArrayList<>();
    EditText studyTitle;
    EditText VP;
    EditText studyDesc;
    LinearLayout locationLinearLayout;
    EditText programEditText;
    EditText locationEditText;
    EditText streetEditText;
    EditText roomEditText;
    Button createButton;

    ArrayAdapter<String> datePickerAdapter;

    String date_time = "";
    int mYear;
    int mMonth;
    int mDay;
    int mHour;
    int mMinute;

    //Checker for Inputs
    boolean checkListDate = false;
    boolean checkListExecutionType = false;
    boolean checkListLocation = false;
    boolean checkListCategory = false;
    boolean checkListStudyDesc = false;
    boolean checkListStreet = false;
    boolean checkListRoom = false;
    boolean checkListProgram = false;
    boolean checkListTitle = false;
    boolean checkListVP = false;
    boolean remoteActive = false;
    boolean presenceActive = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_study);
        setupView();
        setupSpinner();
        setupDatePicker();
        setupClickListener();
    }

    //Parameter:
    //Return values:
    //Connects the View elements with the code
    private void setupView() {
        addButton = findViewById(R.id.createAddDateButton);
        dateList = findViewById(R.id.createDatelist);
        categories = findViewById(R.id.createCategories);
        executionType = findViewById(R.id.createExecutionType);
        studyTitle = findViewById(R.id.createStudyTitleInput);
        VP = findViewById(R.id.createVPInput);
        studyDesc = findViewById(R.id.createStudyDescInput);
        locationLinearLayout = findViewById(R.id.locationLayout);
        createButton = findViewById(R.id.createCreateButton);
    }

    //Parameter:
    //Return values:
    //Sets click listener on buttons
    private void setupClickListener() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();
            }
        });

        createButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                checkInput();
            }
        });
    }

    //Parameter:
    //Return values:
    //Setting up an adapter for the dates
    private void setupDatePicker() {
        datePickerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                dates);
        dateList.setAdapter(datePickerAdapter);
    }

    //Parameter: executionType
    //Return values:
    //Depending on the executionType another set of Edittexts are dynamically loaded
    private void loadLocation(String executionType) {
        if (executionType.equals("Remote")) {
            programEditText = new EditText(this);
            programEditText.setHint("Welche Plattform");
            programEditText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            if (locationLinearLayout != null) {
                locationLinearLayout.addView(programEditText);
            }
        }

        if (executionType.equals("Präsenz")) {
            locationEditText = new EditText(this);
            locationEditText.setHint("Ort");
            locationEditText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            streetEditText = new EditText(this);
            streetEditText.setHint("Straße");
            streetEditText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            roomEditText = new EditText(this);
            roomEditText.setHint("Raum");
            roomEditText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            if (locationLinearLayout != null) {
                locationLinearLayout.addView(locationEditText);
                locationLinearLayout.addView(streetEditText);
                locationLinearLayout.addView(roomEditText);
            }
        }
    }

    //Parameter:
    //Return values:
    //OnChange Listener on the Spinner Items so the App knows when it has to load the dynamically created Edittexts
    private void spinnerOnChange() {
        executionType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (id == 0) {
                    locationLinearLayout.removeAllViews();
                    remoteActive = false;
                    presenceActive = false;
                }
                if (id == 1) {
                    locationLinearLayout.removeAllViews();
                    loadLocation("Remote");
                    remoteActive = true;
                    presenceActive = false;
                }
                if (id == 2) {
                    locationLinearLayout.removeAllViews();
                    loadLocation("Präsenz");
                    remoteActive = false;
                    presenceActive = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    //Parameter:
    //Return values:
    //Setting up Spinners for executiontype and category
    private void setupSpinner() {
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.createCategoryList, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categories.setAdapter(categoryAdapter);

        ArrayAdapter<CharSequence> executionTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.createExecutionTypeList, android.R.layout.simple_spinner_item);
        executionTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        executionType.setAdapter(executionTypeAdapter);
        spinnerOnChange();
    }

    //Parameter: hourOfDay, minute
    //Return values:
    //Adds the selected date to the ListView
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
    //Creates the Datepicker for the dates
    private void datePicker() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
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
    //Creates the TimePicker for the dates
    private void timePicker() {
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        mHour = hourOfDay;
                        mMinute = minute;
                        addDateToList(hourOfDay, minute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    //Parameter:
    //Return values:
    //Will be the last method called to transfer the data to the database
    private void createDBEntry() {
        //DBCODE
        System.out.println("DB");
    }

    //Parameter:
    //Return values:
    //Checks the input of the EditTexts, Spinners and ListViews and gives an Error if something mandatory is missing or a notification if its something optional
    private void checkInput() {
        getDeviceID();
        checkTitle();
        checkVP();
        checkStudyDesc();
        checkCategory();
        checkExecutionType();
        checkDate();
        if (remoteActive) {
            checkProgram();
        }
        if (presenceActive) {
            checkLocation();
            checkRoom();
            checkStreet();
        }

        boolean mandatoryCheck = false;
        String alertMessage = "";
        //Mandatory Checks
        if (checkListTitle && checkListStudyDesc && checkListExecutionType && checkListCategory) {
            if(presenceActive){
                if(checkListLocation){
                    mandatoryCheck = true;
                }
            } else {
                mandatoryCheck = true;
            }
        }

        if (!mandatoryCheck) {
            alertMessage += "Bitten tragen Sie die fehlenden Angaben ein: \n";

            if(!checkListTitle){
                alertMessage += "Studientitel \n";
            }

            if(!checkListStudyDesc){
                alertMessage += "Studienbeschreibung \n";

            }

            if(!checkListExecutionType){
                alertMessage += "Durchführungsart \n";
            }

            if(!checkListCategory){
                alertMessage += "Kategorie der Studie \n";

            }

            if (!checkListLocation && presenceActive) {
                alertMessage += "Durchführungsort \n";
            }

        } else {
            //Optional
            alertMessage += "Sind Sie sicher das Sie ohne folgende Angaben fortfahren wollen: \n";
            if (!checkListVP) {
                alertMessage += "VP-Stunden \n";
            }

            if (!checkListRoom && presenceActive) {
                alertMessage += "Raum \n";
            }

            if (!checkListStreet && presenceActive) {
                alertMessage += "Straße \n";
            }

            if (!checkListProgram && remoteActive) {
                alertMessage += "Programm \n";
            }

            if (!checkListDate) {
                alertMessage += "Daten \n";
            }
        }
        if(alertMessage.isEmpty()){
            createDBEntry();
        } else{
            alertPopup(alertMessage, mandatoryCheck);
        }

    }

    //Parameter: alertMessage, mandatoryCheck
    //Return values:
    //Creates the AlertDialog based of the result of the mandatoryCheck and displays the alertMessage previously build
    private void alertPopup(String alertMessage, boolean mandatoryCheck){
        if(!mandatoryCheck) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(alertMessage)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        } else{
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            createDBEntry();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            break;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(alertMessage).setPositiveButton("Ja", dialogClickListener)
                    .setNegativeButton("Nein", dialogClickListener).show();
        }
    }

    //Parameter:
    //Return values:
    //Checks if the Program EditText is empty
    private void checkProgram() {
        checkListProgram = !programEditText.getText().toString().equals("");
    }

    //Parameter:
    //Return values:
    //Checks if the Street EditText is empty
    private void checkStreet() {
        checkListStreet = !streetEditText.getText().toString().equals("");
    }

    //Parameter:
    //Return values:
    //Checks if the Room EditText is empty
    private void checkRoom() {
        checkListRoom = !roomEditText.getText().toString().equals("");
    }

    //Parameter:
    //Return values:
    //Checks if there are any dates added to the ListView
    private void checkDate() {
        checkListDate = dates.size() != 0;
    }

    //Parameter:
    //Return values:
    //Checks if the Location EditText is empty
    private void checkLocation() {
        checkListLocation = !locationEditText.getText().toString().equals("");
    }

    //Parameter:
    //Return values:
    //Checks if a executiontype was picked at the spinner
    private void checkExecutionType() {
        checkListExecutionType = !executionType.getSelectedItem().toString().equals("Durchführungsart");
    }

    //Parameter:
    //Return values:
    //Checks if a category was picked at the spinner
    private void checkCategory() {
        checkListCategory = !categories.getSelectedItem().toString().equals("Studienkategorie");
    }

    //Parameter:
    //Return values:
    //Checks if the study description EditText is empty
    private void checkStudyDesc() {
        checkListStudyDesc = !studyDesc.getText().toString().equals("");
    }

    //Parameter:
    //Return values:
    //Checks if the VP-hours EditText is empty
    private void checkVP() {
        checkListVP = !VP.getText().toString().equals("");
    }

    //Parameter:
    //Return values:
    //Checks if the study title EditText is empty
    private void checkTitle() {
        checkListTitle = !studyTitle.getText().toString().equals("");
    }

    //Parameter:
    //Return values: UUID
    //Returns the users Device ID to enable an assignment of the study to the specific user
    private String getDeviceID() {
        return UUID.randomUUID().toString();
    }
}
