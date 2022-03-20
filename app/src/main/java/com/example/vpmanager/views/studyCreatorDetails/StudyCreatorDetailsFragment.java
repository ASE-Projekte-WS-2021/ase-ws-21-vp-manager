package com.example.vpmanager.views.studyCreatorDetails;

import android.os.Bundle;

import com.example.vpmanager.R;
import com.example.vpmanager.viewmodels.StudyCreatorViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class StudyCreatorDetailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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

    ImageView editButton;

    NavController navController;


    private String mParam1;
    private String mParam2;

    public StudyCreatorDetailsFragment() {
        // Required empty public constructor
    }



    public static StudyCreatorDetailsFragment newInstance(String param1, String param2) {
        StudyCreatorDetailsFragment fragment = new StudyCreatorDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

    private void prepareComponents() {
        currentStudyId = studyCreatorFragment.currentStudyId;
        studyViewModel = new ViewModelProvider(requireActivity()).get(StudyCreatorViewModel.class);
        studyViewModel.studyCreatorDetailsFragment = this;
        studyViewModel.prepareRepo();

    }

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

        editButton.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putString("studyId", currentStudyId);
            navController.navigate(R.id.action_studyCreatorFragment_to_editStudyFragment, args);
        });
    }

    public void setStudyDetails() {
        //id could be accessed
        headerText.setText(studyViewModel.getStudyDetails().getName());
        description.setText(studyViewModel.getStudyDetails().getDescription());
        vpValue.setText(studyViewModel.getStudyDetails().getVps() + " VP");
        contactInfo.setText(studyViewModel.getStudyDetails().getContactOne());
        //contactTwo could be accessed
        //contactThree could be accessed
        category.setText(studyViewModel.getStudyDetails().getCategory());
        studyType.setText(studyViewModel.getStudyDetails().getExecutionType());
        if (studyViewModel.getStudyDetails().getExecutionType().equals("Remote")) {
            remoteData.setText(studyViewModel.getStudyDetails().getRemotePlatformOne());
            //remotePlatformTwo could be accessed
        } else {
            String locationString = studyViewModel.getStudyDetails().getLocation() + "\t\t" +
                    studyViewModel.getStudyDetails().getStreet() + "\t\t" + studyViewModel.getStudyDetails().getRoom();
            localData.setText(locationString);
        }

    }
}
