package com.example.vpmanager.views.studyCreatorDetails;

import android.app.Dialog;
import android.content.res.ColorStateList;
import android.os.Bundle;

import com.example.vpmanager.helper.AccessDatabaseHelper;
import com.example.vpmanager.R;
import com.example.vpmanager.viewmodels.StudyCreatorViewModel;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;


public class StudyCreatorDetailsFragment extends Fragment {


    private StudyCreatorViewModel studyViewModel;

    private String currentStudyId;

    //Show detailed information of a study
    private TextView headerText;
    private TextView description;
    private TextView vpValue;
    private TextView contactInfo;
    private TextView category;
    private TextView studyType;
    private TextView remoteData;
    private TextView localData;

    private boolean studyIsClosed;

    ExtendedFloatingActionButton editButton, changeStudyStateButton;

    NavController navController;


    public StudyCreatorDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_study_creator_details, container, false);
        prepareComponents();
        initDetailViews(view);
        studyViewModel.fetchStudyDetails(currentStudyId);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }


    //Parameter:
    //Return values:
    //Sets the current study ID and ViewModel
    private void prepareComponents() {
        currentStudyId = StudyCreatorFragment.currentStudyId;
        studyViewModel = new ViewModelProvider(requireActivity()).get(StudyCreatorViewModel.class);
        studyViewModel.studyCreatorDetailsFragment = this;
        studyViewModel.prepareRepo();

    }


    //Parameter: view
    //Return values:
    //Initializes and sets the views for the study details; sets ClickListeners for Buttons
    private void initDetailViews(View view) {
        headerText = view.findViewById(R.id.creator_studyFragmentHeader);
        description = view.findViewById(R.id.creator_descriptionStudyFragment);
        vpValue = view.findViewById(R.id.creator_vpValueStudyFragment);
        editButton = view.findViewById(R.id.creator_editOwnStudyButton);
        contactInfo = view.findViewById(R.id.creator_contactInformationStudyFragment);
        category = view.findViewById(R.id.creator_categoryStudyFragment);
        studyType = view.findViewById(R.id.creator_studyTypeStudyFragment);

        //Textview for further studyType data (depending on type)
        remoteData = view.findViewById(R.id.creator_remoteStudyStudyFragment);
        localData = view.findViewById(R.id.creator_localStudyStudyFragment);
        changeStudyStateButton = view.findViewById(R.id.creator_changeStudyStateButton);

        editButton.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putString("studyId", currentStudyId);
            navController.navigate(R.id.action_studyCreatorFragment_to_editStudyFragment, args);
        });

        changeStudyStateButton.setOnClickListener(v -> showCloseStudyWarning());
    }


    //Parameter:
    //Return values:
    //Initializes button for closing finished studies; sets up alert messages
    private void showCloseStudyWarning() {
        Dialog dialog = new Dialog(getActivity(), R.style.DialogStyle);
        dialog.setContentView(R.layout.dialog_warning);

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_window);

        ImageView btnClose = dialog.findViewById(R.id.btn_close);
        TextView warningTitle = dialog.findViewById(R.id.info_title);
        Button addAnywaysButton = dialog.findViewById(R.id.addAnyways);
        Button abortButton = dialog.findViewById(R.id.abort);
        TextView warningText = dialog.findViewById(R.id.duplicate_desc);
        CheckBox doNotShowAgainCheck = dialog.findViewById(R.id.doNotShowAgainCheckBox);
        doNotShowAgainCheck.setVisibility(View.GONE);
        addAnywaysButton.setText(R.string.changeStudyStateContinue);
        abortButton.setText(R.string.changeStudyStateAbort);
        if (!studyIsClosed) {
            warningTitle.setText(R.string.changeStudyStateTitleClose);
            warningText.setText(R.string.studyCloseWarning);

        } else {
            warningTitle.setText(getString(R.string.changeStudyStateTitleOpen));
            warningText.setText(getString(R.string.studyOpenWarning));
        }

        btnClose.setOnClickListener(view -> dialog.dismiss());

        addAnywaysButton.setOnClickListener(v -> {
            studyIsClosed = !studyIsClosed;
            if (!studyIsClosed) {
                changeStudyStateButton.setText(R.string.changeStudyStateTitleClose);
                changeStudyStateButton.setStrokeColor(ColorStateList.valueOf(getContext().getResources().getColor(R.color.heatherred_dark)));
                changeStudyStateButton.setBackgroundTintList(ColorStateList.valueOf(getContext().getResources().getColor(R.color.heatherred_Main)));
                changeStudyStateButton.setIcon(getResources().getDrawable(R.drawable.ic_baseline_close_24));
            } else {
                changeStudyStateButton.setText(R.string.changeStudyStateTitleOpen);
                changeStudyStateButton.setStrokeColor(ColorStateList.valueOf(getContext().getResources().getColor(R.color.green_dark)));
                changeStudyStateButton.setBackgroundTintList(ColorStateList.valueOf(getContext().getResources().getColor(R.color.green_Main)));
                changeStudyStateButton.setIcon(getResources().getDrawable(R.drawable.ic_baseline_refresh_24));
            }
            AccessDatabaseHelper.setStudyState(currentStudyId, studyIsClosed);
            dialog.dismiss();

        });

        abortButton.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }


    //Parameter:
    //Return values:
    //Sets up the study detail textviews
    public void setStudyDetails() {
        //id could be accessed
        headerText.setText(studyViewModel.getStudyDetails().getName());
        description.setText(studyViewModel.getStudyDetails().getDescription());
        vpValue.setText(studyViewModel.getStudyDetails().getVps() + " VP");
        String contactOne, contactTwo, contactThree, contactFour, contactFive, contactString = "";
        //mail
        contactOne = studyViewModel.getStudyDetails().getContactOne();
        //handy
        contactTwo = studyViewModel.getStudyDetails().getContactTwo();
        //Skype
        contactThree = studyViewModel.getStudyDetails().getContactThree();
        //Discord
        contactFour = studyViewModel.getStudyDetails().getContactFour();
        //Sonstiges
        contactFive = studyViewModel.getStudyDetails().getContactFive();

        if (contactOne != null && !contactOne.isEmpty()) {
            contactString += "Mail: " + contactOne + "\n";
        }
        if (contactTwo != null && !contactTwo.isEmpty()) {
            contactString += "Telefon: " + contactTwo + "\n";
        }
        if (contactThree != null && !contactThree.isEmpty()) {
            contactString += "Skype: " + contactThree + "\n";
        }
        if (contactFour != null && !contactFour.isEmpty()) {
            contactString += "Discord: " + contactFour + "\n";
        }
        if (contactFive != null && !contactFive.isEmpty()) {
            contactString += "Andere: " + contactFive + "\n";
        }
        contactInfo.setText(contactString);

        category.setText(studyViewModel.getStudyDetails().getCategory());
        studyType.setText(studyViewModel.getStudyDetails().getExecutionType());
        if (studyViewModel.getStudyDetails().getExecutionType().equals(getString(R.string.remoteString))) {
            remoteData.setText(studyViewModel.getStudyDetails().getRemotePlatformOne());
            //remotePlatformTwo could be accessed
            if (studyViewModel.getStudyDetails().getRemotePlatformTwo() != null) {
                String platforms = remoteData.getText().toString() + " & " + studyViewModel.getStudyDetails().getRemotePlatformTwo();
                remoteData.setText(platforms);
            }
        } else {
            String locationString = studyViewModel.getStudyDetails().getLocation() + "\t\t" +
                    studyViewModel.getStudyDetails().getStreet() + "\t\t" + studyViewModel.getStudyDetails().getRoom();
            localData.setText(locationString);
        }

        studyIsClosed = studyViewModel.getStudyDetails().getStudyState();
        setStudyStateButton();
    }

    //Parameter:
    //Return values:
    //Sets the states for the close study button; study can be continued if chosen
    private void setStudyStateButton() {
        if (!studyIsClosed) {
            changeStudyStateButton.setText(R.string.changeStudyStateTitleClose);
            changeStudyStateButton.setStrokeColor(ColorStateList.valueOf(getContext().getResources().getColor(R.color.heatherred_dark)));
            changeStudyStateButton.setBackgroundTintList(ColorStateList.valueOf(getContext().getResources().getColor(R.color.heatherred_Main)));
            changeStudyStateButton.setIcon(getContext().getResources().getDrawable(R.drawable.ic_baseline_close_24));
        } else {
            changeStudyStateButton.setText(R.string.changeStudyStateTitleOpen);
            changeStudyStateButton.setStrokeColor(ColorStateList.valueOf(getContext().getResources().getColor(R.color.green_dark)));
            changeStudyStateButton.setBackgroundTintList(ColorStateList.valueOf(getContext().getResources().getColor(R.color.green_Main)));
            changeStudyStateButton.setIcon(getContext().getResources().getDrawable(R.drawable.ic_baseline_refresh_24));
        }
    }
}

