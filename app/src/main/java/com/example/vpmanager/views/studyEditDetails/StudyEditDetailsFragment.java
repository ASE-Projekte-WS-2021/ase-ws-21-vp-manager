package com.example.vpmanager.views.studyEditDetails;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.airbnb.lottie.LottieAnimationView;
import com.example.vpmanager.R;
import com.example.vpmanager.viewmodels.StudyEditViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class StudyEditDetailsFragment extends Fragment {

    private String currentStudyIdEdit;
    private StudyEditViewModel studyEditViewModel;
    private NavController navController;

    public TextInputEditText title;
    public TextInputEditText vph;
    @SuppressLint("StaticFieldLeak")
    public AutoCompleteTextView categories;
    @SuppressLint("StaticFieldLeak")
    public AutoCompleteTextView executionType;

    public TextInputEditText contactMail;
    public TextInputEditText contactPhone;
    public TextInputEditText contactSkype;
    public TextInputEditText contactDiscord;
    public TextInputEditText contactOther;

    public TextInputEditText description;

    public RelativeLayout remoteLayout;
    public RelativeLayout presenceLayout;

    public TextInputEditText platformOne;
    public TextInputEditText platformTwo;

    public TextInputEditText location;
    public TextInputEditText street;
    public TextInputEditText room;

    private Button saveButton;
    private LottieAnimationView doneAnimation;



    public StudyEditDetailsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_study_details, container, false);
        prepareComponents();
        setupView(view);
        //dates also need to be loaded here
        studyEditViewModel.fetchEditStudyDetailsAndDates(currentStudyIdEdit);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);
        super.onViewCreated(view, savedInstanceState);
    }


    //Parameter:
    //Return values:
    //Sets study ID and the View Model
    private void prepareComponents() {
        currentStudyIdEdit = StudyEditFragment.currentStudyIdEdit;
        studyEditViewModel = new ViewModelProvider(getParentFragment()).get(StudyEditViewModel.class);
        studyEditViewModel.studyEditDetailsFragment = this;
        studyEditViewModel.prepareRepo();
    }

    //Parameter: view
    //Return values:
    //Connects the code with the view and sets Listener for Saving
    private void setupView(View view) {

        title = view.findViewById(R.id.edit_study_title);
        vph = view.findViewById(R.id.edit_study_vph);

        ArrayList<String> dropdownListCategories, dropdownListExecutionType;
        ArrayAdapter<String> arrayAdapterCategories, arrayAdapterExecutionType;

        categories = view.findViewById(R.id.edit_study_category);
        dropdownListCategories = new ArrayList<>();

        for(int i = 1; i < getResources().getStringArray(R.array.createCategoryList).length; i++){
            dropdownListCategories.add(getResources().getStringArray(R.array.createCategoryList)[i]);
        }
        arrayAdapterCategories = new ArrayAdapter<>(getActivity(), R.layout.material_dropdown_item, dropdownListCategories);
        categories.setAdapter(arrayAdapterCategories);

        executionType = view.findViewById(R.id.edit_study_executionType);
        dropdownListExecutionType = new ArrayList<>();
        dropdownListExecutionType.add("Remote");
        dropdownListExecutionType.add("Präsenz");
        arrayAdapterExecutionType = new ArrayAdapter<>(getActivity(), R.layout.material_dropdown_item, dropdownListExecutionType);
        executionType.setAdapter(arrayAdapterExecutionType);

        contactMail = view.findViewById(R.id.edit_study_contact_mail);
        contactPhone = view.findViewById(R.id.edit_study_contact_phone);
        contactSkype = view.findViewById(R.id.edit_study_contact_skype);
        contactDiscord = view.findViewById(R.id.edit_study_contact_discord);
        contactOther = view.findViewById(R.id.edit_study_contact_others);

        description = view.findViewById(R.id.edit_study_description);

        remoteLayout = view.findViewById(R.id.edit_study_remote_layout);
        presenceLayout = view.findViewById(R.id.edit_study_presence_layout);

        location = view.findViewById(R.id.edit_study_location);
        street = view.findViewById(R.id.edit_study_street);
        room = view.findViewById(R.id.edit_study_room);

        platformOne = view.findViewById(R.id.edit_study_platform_one);
        platformTwo = view.findViewById(R.id.edit_study_platform_two);

        saveButton = view.findViewById(R.id.edit_study_saving_button);

        executionType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equals("Remote")){

                    presenceLayout.setVisibility(View.GONE);
                    remoteLayout.setVisibility(View.VISIBLE);
                } else {
                    remoteLayout.setVisibility(View.GONE);
                    presenceLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkMandatoryFields()){
                    playAnimation();
                    studyEditViewModel.updateStudyAndDates();
                }
            }
        });

        doneAnimation = view.findViewById(R.id.animationViewEdit);
        doneAnimation.addAnimatorListener(
                new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        navigateToCreatorDetailsView();
                    }
                }
        );
    }


    //Parameter:
    //Return values: boolean
    //Checks for mandatory input and sets error messages
    private Boolean checkMandatoryFields() {

        if (title.getText().toString().isEmpty()){
            title.setError("Titel darf nicht leer sein!");
            return false;
        }
        if (contactMail.getText().toString().isEmpty()){
            contactMail.setError("Meil darf nicht leer sein!");
            return false;
        }
        if (description.getText().toString().isEmpty()){
            description.setError("Beschreibung darf nicht leer sein!");
            return false;
        }
        if (executionType.getText().toString().equals("Remote")) {
            if (platformOne.getText().toString().isEmpty()){
                platformOne.setError("Es muss eine Platform angegeben werden!");
                return false;
            }
        }else {
            if (location.getText().toString().isEmpty()){
                location.setError("Es muss ein Ort angegeben werden!");
                return false;
            }
        }
        return true;
    }


    //Parameter:
    //Return values:
    //Sets animation properties
    private void playAnimation() {
        doneAnimation.setVisibility(LottieAnimationView.VISIBLE);
        doneAnimation.setProgress(0);
        doneAnimation.pauseAnimation();
        doneAnimation.playAnimation();
    }

    //Parameter:
    //Return values:
    //Navigates back to the study details view
    private void navigateToCreatorDetailsView(){
        Bundle args = new Bundle();
        args.putString("studyId", currentStudyIdEdit);
        args.putBoolean("fromEditFragment", true);
        navController.navigate(R.id.action_editStudyFragment_to_studyCreatorFragment, args);
    }


    //Parameter:
    //Return values:
    //Set the existing study details in the edit view
    public void setDetails() {

        //title, vph, category, executionType
        if (studyEditViewModel.studyEditProcessData.get("name") != null) {
            title.setText(studyEditViewModel.studyEditProcessData.get("name").toString());
        }
        if (studyEditViewModel.studyEditProcessData.get("vps") != null) {
            vph.setText(studyEditViewModel.studyEditProcessData.get("vps").toString());
        }
        if (studyEditViewModel.studyEditProcessData.get("category") != null) {
            String cat = studyEditViewModel.studyEditProcessData.get("category").toString();
            if (cat.equals("Feldstudie")) {
                categories.setText(getString(R.string.fieldStudyText), false);
            }
            if (cat.equals("Fokusgruppe")) {
                categories.setText(getString(R.string.focusGroupText), false);
            }
            if (cat.equals("Fragebogen")) {
                categories.setText(getString(R.string.questionnaireText), false);
            }
            if (cat.equals("Gamingstudie")) {
                categories.setText(getString(R.string.gamingText), false);
            }
            if (cat.equals("Interview")) {
                categories.setText(getString(R.string.interviewText), false);
            }
            if (cat.equals("Laborstudien")) {
                categories.setText(getString(R.string.labStudyText), false);
            }
            if (cat.equals("Tagebuchstudie")) {
                categories.setText(getString(R.string.diaryStudyText), false);
            }
            if (cat.equals("Usability/UXstudie")) {
                categories.setText(getString(R.string.usabilityText), false);
            }
            if (cat.equals("Sonstige")) {
                categories.setText(getString(R.string.othersText), false);
            }
        }
        if (studyEditViewModel.studyEditProcessData.get("executionType") != null) {
            String exe = studyEditViewModel.studyEditProcessData.get("executionType").toString();
            if (exe.equals("Remote")) {
                executionType.setText("Remote", false);
            }
            if (exe.equals("Präsenz")) {
                executionType.setText("Präsenz", false);
            }
        }

        //contacts
        //mail is never null (mandatory mail)
        if (studyEditViewModel.studyEditProcessData.get("contact") != null) {
            contactMail.setText(studyEditViewModel.studyEditProcessData.get("contact").toString());
            contactMail.setFocusable(false);
            contactMail.setTextColor(ContextCompat.getColor(getActivity(), R.color.pieChartRemaining));
        }
        if (studyEditViewModel.studyEditProcessData.get("contact2") != null) {
            contactPhone.setText(studyEditViewModel.studyEditProcessData.get("contact2").toString());
        }
        if (studyEditViewModel.studyEditProcessData.get("contact3") != null) {
            contactSkype.setText(studyEditViewModel.studyEditProcessData.get("contact3").toString());
        }
        if (studyEditViewModel.studyEditProcessData.get("contact4") != null) {
            contactDiscord.setText(studyEditViewModel.studyEditProcessData.get("contact4").toString());
        }
        if (studyEditViewModel.studyEditProcessData.get("contact5") != null) {
            contactOther.setText(studyEditViewModel.studyEditProcessData.get("contact5").toString());
        }

        //description
        if (studyEditViewModel.studyEditProcessData.get("description") != null) {
            description.setText(studyEditViewModel.studyEditProcessData.get("description").toString());
        }

        if (studyEditViewModel.studyEditProcessData.get("executionType").equals("Remote")) {

            presenceLayout.setVisibility(View.GONE);
            remoteLayout.setVisibility(View.VISIBLE);

        } else if (studyEditViewModel.studyEditProcessData.get("executionType").equals("Präsenz")) {

            remoteLayout.setVisibility(View.GONE);
            presenceLayout.setVisibility(View.VISIBLE);
        }

        //platformOne, platformTwo
        if (studyEditViewModel.studyEditProcessData.get("platform") != null) {
            platformOne.setText(studyEditViewModel.studyEditProcessData.get("platform").toString());
        }
        if (studyEditViewModel.studyEditProcessData.get("platform2") != null) {
            platformTwo.setText(studyEditViewModel.studyEditProcessData.get("platform2").toString());
        }

        //location, street, room
        if (studyEditViewModel.studyEditProcessData.get("location") != null) {
            location.setText(studyEditViewModel.studyEditProcessData.get("location").toString());
        }
        if (studyEditViewModel.studyEditProcessData.get("street") != null) {
            street.setText(studyEditViewModel.studyEditProcessData.get("street").toString());
        }
        if (studyEditViewModel.studyEditProcessData.get("room") != null) {
            room.setText(studyEditViewModel.studyEditProcessData.get("room").toString());
        }
    }
}
