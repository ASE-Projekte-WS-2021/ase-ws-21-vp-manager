package com.example.vpmanager.views.ownStudies;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vpmanager.R;
import com.example.vpmanager.adapter.StudyListAdapter;
import com.example.vpmanager.models.StudyMetaInfoModel;
import com.example.vpmanager.viewmodels.OwnStudyViewModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Collections;

public class SelfcreatedStudiesFragment extends Fragment implements StudyListAdapter.OnStudyItemClickListener {

    private NavController navController;
    private RecyclerView ownStudiesList;
    private TextView noOwnStudies;
    private OwnStudyViewModel ownStudyViewModel;

    private LinearLayout categoryExpander, typeExpander, sortVP;
    private ImageView categoryIcon, typeIcon, sortImageIcon;
    private ChipGroup categoryChips, typeChips;
    private Chip fieldStudy, focusGroup, questionnaire, interview, usability, labStudy, gaming, diaryStudy, others, remote, local;
    private StudyListAdapter studyListAdapter;

    private boolean fieldStudyActive, focusGroupActive, questionnaireActive, interviewActive, usabilityActive, labStudyActive, gamingActive, diaryStudyActive, othersActive, remoteActive, localActive;
    private boolean sortVPActive, sortVpInvert;

    public SelfcreatedStudiesFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selfcreated_study, container, false);
        prepareViewModelAndView(view);
        setupClickListener();
        ownStudyViewModel.prepareRepo();
        ownStudyViewModel.fetchOwnStudyMetaData();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);
        super.onViewCreated(view, savedInstanceState);
    }


    //Parameter: view
    //Return values:
    //Sorts the list by date
    private void prepareViewModelAndView(View view) {
        ownStudiesList = view.findViewById(R.id.recyclerViewOwnStudies);
        noOwnStudies = view.findViewById(R.id.ownStudiesInfoText);
        ownStudyViewModel = new ViewModelProvider(getParentFragment()).get(OwnStudyViewModel.class);
        ownStudyViewModel.selfcreatedStudiesFragment = this;

        categoryExpander = view.findViewById(R.id.ownStudyExpandCategory);
        categoryChips = view.findViewById(R.id.ownStudyCategories);

        typeExpander = view.findViewById(R.id.ownStudyExpandType);
        typeIcon = view.findViewById(R.id.ownStudyTypeIcon);
        typeChips = view.findViewById(R.id.ownStudyType);

        remote = view.findViewById(R.id.ownStudyFilterRemote);
        local = view.findViewById(R.id.ownStudyFilterpresence);


        fieldStudy = view.findViewById(R.id.ownStudyFilterFieldStudy);
        focusGroup = view.findViewById(R.id.ownStudyFilterFocusGroup);
        questionnaire = view.findViewById(R.id.ownStudyFilterQuestionnaire);
        interview = view.findViewById(R.id.ownStudyFilterInterview);
        usability = view.findViewById(R.id.ownStudyFilterUeUx);
        labStudy = view.findViewById(R.id.ownStudyFilterLabStudy);
        gaming = view.findViewById(R.id.ownStudyFilterGaming);
        diaryStudy = view.findViewById(R.id.ownStudyFilterDiaryStudy);
        others = view.findViewById(R.id.ownStudyFilterOthers);

        categoryIcon = view.findViewById(R.id.ownStudyCategoryIcon);

        sortImageIcon = view.findViewById(R.id.ownStudy_sort_VP_Icon);
        sortVP = view.findViewById(R.id.ownStudy_sort_vp);

        //mainActivity is currently viewModel owner
        fieldStudyActive = false;
        focusGroupActive = false;
        questionnaireActive = false;
        interviewActive = false;
        usabilityActive = false;
        labStudyActive = false;
        gamingActive = false;
        diaryStudyActive = false;
        othersActive = false;
        localActive = false;
        remoteActive = false;


        sortVPActive = false;
        sortVpInvert = false;
    }


    //Parameter:
    //Return values:
    //Sets the ClickListeners and updates the study list filters
    private void setupClickListener() {
        categoryExpander.setOnClickListener(v -> {
            if (categoryChips.getVisibility() == View.GONE) {
                categoryIcon.setImageResource(R.drawable.ic_baseline_expand_less_24);
                categoryChips.setVisibility(View.VISIBLE);
                typeIcon.setImageResource(R.drawable.ic_baseline_expand_more_24);
                typeChips.setVisibility(View.GONE);
            } else if (categoryChips.getVisibility() == View.VISIBLE) {
                categoryIcon.setImageResource(R.drawable.ic_baseline_expand_more_24);
                categoryChips.setVisibility(View.GONE);
            }
        });

        typeExpander.setOnClickListener(v -> {
            if (typeChips.getVisibility() == View.GONE) {
                typeIcon.setImageResource(R.drawable.ic_baseline_expand_less_24);
                typeChips.setVisibility(View.VISIBLE);
                categoryIcon.setImageResource(R.drawable.ic_baseline_expand_more_24);
                categoryChips.setVisibility(View.GONE);
            } else if (typeChips.getVisibility() == View.VISIBLE) {
                typeIcon.setImageResource(R.drawable.ic_baseline_expand_more_24);
                typeChips.setVisibility(View.GONE);
            }
        });
        fieldStudy.setOnClickListener(v -> {
            fieldStudyActive = !fieldStudyActive;
            fieldStudy.setChecked(fieldStudyActive);
            if (fieldStudyActive) {
                fieldStudy.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.green_Main)));
            } else {
                fieldStudy.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.heatherred_Main)));
            }
            applyCategoryFilterToList();
        });
        focusGroup.setOnClickListener(v -> {
            focusGroupActive = !focusGroupActive;
            focusGroup.setChecked(focusGroupActive);
            if (focusGroupActive) {
                focusGroup.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.green_Main)));
            } else {
                focusGroup.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.heatherred_Main)));
            }
            applyCategoryFilterToList();
        });
        questionnaire.setOnClickListener(v -> {
            questionnaireActive = !questionnaireActive;
            questionnaire.setChecked(questionnaireActive);
            if (questionnaireActive) {
                questionnaire.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.green_Main)));
            } else {
                questionnaire.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.heatherred_Main)));
            }
            applyCategoryFilterToList();
        });
        interview.setOnClickListener(v -> {
            interviewActive = !interviewActive;
            interview.setChecked(interviewActive);
            if (interviewActive) {
                interview.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.green_Main)));
            } else {
                interview.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.heatherred_Main)));
            }
            applyCategoryFilterToList();
        });
        usability.setOnClickListener(v -> {
            usabilityActive = !usabilityActive;
            usability.setChecked(usabilityActive);
            if (usabilityActive) {
                usability.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.green_Main)));
            } else {
                usability.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.heatherred_Main)));
            }
            applyCategoryFilterToList();
        });
        labStudy.setOnClickListener(v -> {
            labStudyActive = !labStudyActive;
            labStudy.setChecked(labStudyActive);
            if (labStudyActive) {
                labStudy.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.green_Main)));
            } else {
                labStudy.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.heatherred_Main)));
            }
            applyCategoryFilterToList();
        });
        gaming.setOnClickListener(v -> {
            gamingActive = !gamingActive;
            gaming.setChecked(gamingActive);
            if (gamingActive) {
                gaming.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.green_Main)));
            } else {
                gaming.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.heatherred_Main)));
            }
            applyCategoryFilterToList();
        });
        diaryStudy.setOnClickListener(v -> {
            diaryStudyActive = !diaryStudyActive;
            diaryStudy.setChecked(diaryStudyActive);
            if (diaryStudyActive) {
                diaryStudy.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.green_Main)));
            } else {
                diaryStudy.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.heatherred_Main)));
            }
            applyCategoryFilterToList();
        });
        others.setOnClickListener(v -> {
            othersActive = !othersActive;
            others.setChecked(othersActive);
            if (othersActive) {
                others.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.green_Main)));
            } else {
                others.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.heatherred_Main)));
            }
            applyCategoryFilterToList();
        });

        local.setOnClickListener(v -> {
            localActive = !localActive;
            local.setChecked(localActive);
            if (localActive) {
                local.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.green_Main)));
            } else {
                local.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.heatherred_Main)));
            }
            applyCategoryFilterToList();
        });
        remote.setOnClickListener(v -> {
            remoteActive = !remoteActive;
            remote.setChecked(remoteActive);
            if (remoteActive) {
                remote.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.green_Main)));
            } else {
                remote.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.heatherred_Main)));
            }
            applyCategoryFilterToList();
        });


        sortVP.setOnClickListener(v -> {
            if (sortVPActive && sortVpInvert) {
                sortVPActive = false;
                sortVpInvert = false;
                connectOwnStudyListAdapter();
                applyCategoryFilterToList();
                sortImageIcon.setVisibility(View.GONE);
            } else if (sortVPActive && !sortVpInvert) {
                sortVpInvert = true;
                sortByVPS(sortVpInvert);
                sortImageIcon.setVisibility(View.VISIBLE);
                sortImageIcon.setImageResource(R.drawable.ic_baseline_south_24);
            } else {
                sortVPActive = true;
                sortByVPS(sortVpInvert);
                sortImageIcon.setVisibility(View.VISIBLE);
                sortImageIcon.setImageResource(R.drawable.ic_baseline_north_24);
            }
        });
    }


    //Parameter: invert
    //Return values: List<String[]>
    //Sorts the list by vp values
    private void sortByVPS(boolean invert) {
        ArrayList<StudyMetaInfoModel> currentList = studyListAdapter.mStudyMetaInfos;
        ArrayList<StudyMetaInfoModel> list = new ArrayList<>();

        StudyMetaInfoModel[] studyMetaList = new StudyMetaInfoModel[currentList.size()];
        for (int i = 0; i < currentList.size(); i++) {
            studyMetaList[i] = currentList.get(i);
        }

        for (int i = 0; i < studyMetaList.length; i++) {
            for (int k = 0; k < studyMetaList.length - 1; k++) {
                String vps1 = studyMetaList[k].getVps().replace(" VP-Stunden", "");
                String vps2 = studyMetaList[k + 1].getVps().replace(" VP-Stunden", "");
                if (vps1.trim().equals("null") || vps1.trim().equals("")) {
                    vps1 = "0";
                }
                if (vps2.trim().equals("null") || vps2.trim().equals("")) {
                    vps2 = "0";
                }
                if (Float.parseFloat(vps1) < Float.parseFloat(vps2)) {
                    StudyMetaInfoModel tempStudy = studyMetaList[k];
                    studyMetaList[k] = studyMetaList[k + 1];
                    studyMetaList[k + 1] = tempStudy;
                }
            }
        }
        for (StudyMetaInfoModel ob : studyMetaList) {
            list.add(ob);
        }

        if (!invert) {
            Collections.reverse(list);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity());
        studyListAdapter = new StudyListAdapter(requireActivity(), list, this);
        ownStudiesList.setAdapter(studyListAdapter);
        ownStudiesList.setLayoutManager(linearLayoutManager);
    }


    //Parameter:
    //Return values:
    //Adds the filters for different study categories
    private void applyCategoryFilterToList() {
        ArrayList<StudyMetaInfoModel> list = new ArrayList<>();
        for (StudyMetaInfoModel info : ownStudyViewModel.getStudyMetaInfo()) {
            if (!fieldStudyActive && !focusGroupActive && !questionnaireActive && !interviewActive &&
                    !usabilityActive && !labStudyActive && !gamingActive && !diaryStudyActive && !othersActive) {
                list.add(info);
            } else {
                switch (info.getCategory()) {
                    case "Feldstudie":
                        if (fieldStudyActive)
                            list.add(info);
                        break;
                    case "Fokusgruppe":
                        if (focusGroupActive)
                            list.add(info);
                        break;
                    case "Fragebogen":
                        if (questionnaireActive)
                            list.add(info);
                        break;
                    case "Gamingstudie":
                        if (gamingActive)
                            list.add(info);
                        break;
                    case "Interview":
                        if (interviewActive)
                            list.add(info);
                        break;
                    case "Laborstudie":
                        if (labStudyActive)
                            list.add(info);
                        break;
                    case "Tagebuchstudie":
                        if (diaryStudyActive)
                            list.add(info);
                        break;
                    case "Usability/UX":
                        if (usabilityActive)
                            list.add(info);
                        break;
                    case "Sonstige":
                        if (othersActive)
                            list.add(info);
                        break;
                    default:
                        if (othersActive)
                            list.add(info);
                        break;
                }
            }
        }
        list = applyTypeFilterToList(list);

        if (sortVPActive)
            sortByVPS(sortVpInvert);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity());
        studyListAdapter = new StudyListAdapter(requireActivity(), list, this);
        ownStudiesList.setAdapter(studyListAdapter);
        ownStudiesList.setLayoutManager(linearLayoutManager);
    }


    //Parameter: list
    //Return values: ArrayList<StudyMetaInfoModel>
    //Fills ArrayList with items depending on the chosen study type, returns list
    private ArrayList<StudyMetaInfoModel> applyTypeFilterToList(ArrayList<StudyMetaInfoModel> list) {

        ArrayList<StudyMetaInfoModel> newList = new ArrayList<>();
        for (StudyMetaInfoModel info : list) {
            if (!remoteActive && !localActive) {
                newList.add(info);
            } else {
                if (remoteActive) {
                    if (info.getType().equals("Remote")) {
                        newList.add(info);
                    }
                }
                if (localActive) {
                    if (info.getType().equals("Präsenz")) {
                        newList.add(info);
                    }
                }
            }
        }
        return newList;
    }


    //Parameter:
    //Return values:
    //Sets the adapter for the own studies list
    public void connectOwnStudyListAdapter() {
        if (!ownStudyViewModel.getOwnStudyMetaInfo().isEmpty()) {
            noOwnStudies.setVisibility(View.GONE);
        } else {
            noOwnStudies.setVisibility(View.VISIBLE);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity());
        StudyListAdapter studyListAdapter = new StudyListAdapter(getContext(), ownStudyViewModel.getOwnStudyMetaInfo(),
                this);
        ownStudiesList.setAdapter(studyListAdapter);
        ownStudiesList.setLayoutManager(linearLayoutManager);
    }

    //Parameter: the id of the ownStudy that was clicked on
    //Return Values:
    //navigates to the more detailed and changeable view of a study
    @Override
    public void onStudyClick(String studyId) {
        Bundle args = new Bundle();
        Log.d("OwnStudyFragment", "onStudyClick - studyId:" + studyId);
        args.putString("studyId", studyId);
        navController.navigate(R.id.action_ownStudyFragment_to_studyCreatorFragment, args);
    }
}
