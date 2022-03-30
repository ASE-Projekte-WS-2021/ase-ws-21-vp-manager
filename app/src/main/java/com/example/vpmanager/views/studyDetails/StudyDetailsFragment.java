package com.example.vpmanager.views.studyDetails;

import android.os.Bundle;

import com.example.vpmanager.R;
import com.example.vpmanager.viewmodels.StudyViewModel;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StudyDetailsFragment#//newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudyDetailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    /*
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
     */

    private StudyViewModel studyViewModel;

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

    // TODO: Rename and change types of parameters
    /*
    private String mParam1;
    private String mParam2;
     */

    public StudyDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param //param1 Parameter 1.
     * @param //param2 Parameter 2.
     * @return A new instance of fragment studyDetails.
     */
    // TODO: Rename and change types and number of parameters
    /*
    public static StudyDetailsFragment newInstance(String param1, String param2) {
        StudyDetailsFragment fragment = new StudyDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
         */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_study_details, container, false);
        prepareComponents();
        initDetailViews(view);
        Log.d("StudyDetails", "viewModelInstance" + studyViewModel.toString());
        studyViewModel.fetchStudyDetails(currentStudyId);
        return view;
    }

    private void prepareComponents() {
        currentStudyId = StudyFragment.currentStudyId;
        studyViewModel = new ViewModelProvider(requireActivity()).get(StudyViewModel.class);
        studyViewModel.studyDetailsFragment = this;
        studyViewModel.prepareRepo();
    }

    private void initDetailViews(View view) {
        headerText = view.findViewById(R.id.studyFragmentHeader);
        description = view.findViewById(R.id.descriptionStudyFragment);
        vpValue = view.findViewById(R.id.vpValueStudyFragment);
        contactInfo = view.findViewById(R.id.contactInformationStudyFragment);
        category = view.findViewById(R.id.categoryStudyFragment);
        studyType = view.findViewById(R.id.studyTypeStudyFragment);
        //Textview for further studyType data (depending on type)
        remoteData = view.findViewById(R.id.remoteStudyStudyFragment);
        localData = view.findViewById(R.id.localStudyStudyFragment);
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