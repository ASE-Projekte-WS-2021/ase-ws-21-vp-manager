package com.example.vpmanager.views;

import static com.example.vpmanager.views.mainActivity.uniqueID;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vpmanager.PA_ExpandableListDataPump;
import com.example.vpmanager.R;
import com.example.vpmanager.adapter.StudyListAdapter;
import com.example.vpmanager.models.StudyMetaInfoModel;
import com.example.vpmanager.viewmodels.FindStudyViewModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Collections;

public class FindStudyFragment extends Fragment implements StudyListAdapter.OnStudyItemClickListener {

    private NavController navController;
    private RecyclerView studyList;
    private LinearLayout categoryExpander, typeExpander, sortVP;
    private ImageView categoryIcon, typeIcon, sortImageIcon;
    private ChipGroup categoryChips, typeChips;
    private Chip fieldStudy, focusGroup, questionnaire, interview, usability, labStudy, gaming, diaryStudy, others, remote, local;
    private StudyListAdapter studyListAdapter;
    private FindStudyViewModel findStudyViewModel;
    private boolean fieldStudyActive, focusGroupActive, questionnaireActive, interviewActive, usabilityActive, labStudyActive, gamingActive, diaryStudyActive, othersActive, remoteActive, localActive;
    private boolean sortVPActive, sortVpInvert;


    //class constructor
    public FindStudyFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find_study, container, false);
        initViewComponents(view);
        setupClickListener();
        //starting point to get the data
        findStudyViewModel.fetchStudyMetaData();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);
        super.onViewCreated(view, savedInstanceState);
    }


    //Parameter: the fragment view
    //Return Values:
    //Initializes the necessary components for the fragment
    private void initViewComponents(View view) {
        studyList = view.findViewById(R.id.recyclerViewFindStudyFragment);
        categoryExpander = view.findViewById(R.id.FindStudyExpandCategory);
        categoryChips = view.findViewById(R.id.findStudyCategories);

        typeExpander = view.findViewById(R.id.FindStudyExpandType);
        typeIcon = view.findViewById(R.id.findStudyTypeIcon);
        typeChips = view.findViewById(R.id.findStudyType);

        remote = view.findViewById(R.id.findStudyFilterRemote);
        local = view.findViewById(R.id.findStudyFilterpresence);


        fieldStudy = view.findViewById(R.id.findStudyFilterFieldStudy);
        focusGroup = view.findViewById(R.id.findStudyFilterFocusGroup);
        questionnaire = view.findViewById(R.id.findStudyFilterQuestionnaire);
        interview = view.findViewById(R.id.findStudyFilterInterview);
        usability = view.findViewById(R.id.findStudyFilterUeUx);
        labStudy = view.findViewById(R.id.findStudyFilterLabStudy);
        gaming = view.findViewById(R.id.findStudyFilterGaming);
        diaryStudy = view.findViewById(R.id.findStudyFilterDiaryStudy);
        others = view.findViewById(R.id.findStudyFilterOthers);

        categoryIcon = view.findViewById(R.id.findStudyCategoryIcon);

        sortImageIcon = view.findViewById(R.id.findStudy_sort_VP_Icon);
        sortVP = view.findViewById(R.id.findStudy_sort_vp);

        //mainActivity is currently viewModel owner
        findStudyViewModel = new ViewModelProvider(requireActivity()).get(FindStudyViewModel.class);
        findStudyViewModel.findStudyFragment = this;
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
    //Return Values:
    //Sets up the ClickListeners for the fragment view
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
                connectStudyListAdapter();
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
    //Return Values:
    //Sorts the list according to vp value
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
        studyList.setAdapter(studyListAdapter);
        studyList.setLayoutManager(linearLayoutManager);
    }


    //Parameter:
    //Return Values:
    //adds the category filters above the ListView
    private void applyCategoryFilterToList() {
        ArrayList<StudyMetaInfoModel> list = new ArrayList<>();
        for (StudyMetaInfoModel info : findStudyViewModel.getStudyMetaInfo()) {
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
        studyList.setAdapter(studyListAdapter);
        studyList.setLayoutManager(linearLayoutManager);
    }


    //Parameter: list
    //Return Values: ArrayList<StudyMetaInfoModel>
    //applies the filters to the study ArrayList and returns the new list
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
    //Return Values:
    //Sets the adapter to connect the recyclerview with the data
    public void connectStudyListAdapter() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity());
        studyListAdapter = new StudyListAdapter(requireActivity(), findStudyViewModel.getStudyMetaInfo(), this);
        studyList.setAdapter(studyListAdapter);
        studyList.setLayoutManager(linearLayoutManager);
    }

    //Parameter: the id of the study that was clicked on
    //Return Values:
    //Navigates to the more detailed view of a study
    @Override
    public void onStudyClick(String studyId) {
        PA_ExpandableListDataPump.navigateToStudyCreatorFragment(uniqueID, studyId, "FindStudyFragment", navController);
    }
}
