package com.example.vpmanager.views.ownStudies;

import android.content.res.ColorStateList;
import android.os.Bundle;
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
import com.example.vpmanager.adapter.CustomStudyListAdapter;
import com.example.vpmanager.viewmodels.PersonalAccountViewModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

public class PersonalAccountFragment extends Fragment {

    private PersonalAccountViewModel personalAccountViewModel;
    private NavController navController;

    private LinearLayout customProgressBar, sortVpCount, sortAlphabetically, sortAppointments, sortExpanderButton, sortExpanderLayout, stateExpanderButton;
    private View completedView, plannedView, participatedView, restView;
    private TextView completed, participated, planned, remaining;

    //Images and one text view (VP) for sorting
    private ImageView statusExpanderIcon, sortExpanderIcon, sortNameIcon, sortDateIcon, sortVpsIcon;
    //ToggleButtons for filtering
    private Chip removeCompleted, removePlanned, removeParticipated;
    private ChipGroup stateExpanderLayout;

    private boolean removeCompletedActive, removePlannedActive, removeParticipatedActive;

    //ListView that contains the sorted and filtered list of a date x study combination
    private RecyclerView listView;

    public PersonalAccountFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_account, container, false);
        prepareViewModel();
        setupView(view);
        personalAccountViewModel.getDatesStudiesVpsAndMatrikelNumberFromDb();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);
        super.onViewCreated(view, savedInstanceState);
    }

    private void prepareViewModel() {
        //ParentFragment (OwnStudyFragment) should be viewModelStoreOwner
        personalAccountViewModel = new ViewModelProvider(getParentFragment()).get(PersonalAccountViewModel.class);
        personalAccountViewModel.personalAccountFragment = this;
        personalAccountViewModel.prepareRepo();
    }

    //Parameter:
    //Return values:
    //Setting the view components and reading the content Lists for the chart values
    private void setupView(View view) {
        customProgressBar = view.findViewById(R.id.customProgressBar);
        completedView = view.findViewById(R.id.progress_section_completed);
        plannedView = view.findViewById(R.id.progress_section_planned);
        participatedView = view.findViewById(R.id.progress_section_participated);
        restView = view.findViewById(R.id.progress_section_rest);

        completed = view.findViewById(R.id.pa_completed_progressBar);
        participated = view.findViewById(R.id.pa_participated_progressBar);
        planned = view.findViewById(R.id.pa_planned_progressBar);
        remaining = view.findViewById(R.id.pa_remaining_progressBar);

        sortAlphabetically = view.findViewById(R.id.pa_sort_alphabetical);
        sortAppointments = view.findViewById(R.id.pa_sort_date);
        sortVpCount = view.findViewById(R.id.pa_sort_vp);

        removeCompleted = view.findViewById(R.id.pa_remove_completed);
        removePlanned = view.findViewById(R.id.pa_remove_planned);
        removeParticipated = view.findViewById(R.id.pa_remove_participation);

        listView = view.findViewById(R.id.pa_fragment_listView);


        sortExpanderButton = view.findViewById(R.id.pa_sort_expanderButton);
        sortExpanderLayout = view.findViewById(R.id.pa_sort_expanderLayout);
        stateExpanderButton = view.findViewById(R.id.pa_status_expanderButton);
        stateExpanderLayout = view.findViewById(R.id.pa_status_expanderLayout);

        statusExpanderIcon = view.findViewById(R.id.pa_status_expanderIcon);
        sortExpanderIcon = view.findViewById(R.id.pa_sort_expanderIcon);

        sortNameIcon = view.findViewById(R.id.pa_sort_alphabetical_Icon);
        sortDateIcon = view.findViewById(R.id.pa_sort_date_Icon);
        sortVpsIcon = view.findViewById(R.id.pa_sort_vp_Icon);

        removeCompleted.setSelected(true);
        removePlanned.setSelected(true);
        removeParticipated.setSelected(true);
        removeParticipatedActive = true;
        removeCompletedActive = true;
        removePlannedActive = true;
    }


    //Parameter: completedString, participatedString, plannedString, remainingString,  weightSum
    //Return values:
    //Sets the progress bar texts
    public void setProgressBarStrings(String completedString, String participatedString, String plannedString,
                                      String remainingString, float weightSum) {
        completed.setText(completedString);
        participated.setText(participatedString);
        planned.setText(plannedString);
        remaining.setText(remainingString);
        customProgressBar.setWeightSum(weightSum);
    }


    //Parameter: all Layoutparams
    //Return values:
    //Sets the progress bar views
    public void setProgressBarParts(LinearLayout.LayoutParams completedParams, LinearLayout.LayoutParams participatedParams,
                                    LinearLayout.LayoutParams plannedParams, LinearLayout.LayoutParams restParams) {
        completedView.setLayoutParams(completedParams);
        participatedView.setLayoutParams(participatedParams);
        plannedView.setLayoutParams(plannedParams);
        restView.setLayoutParams(restParams);

        setupClickListener();
    }


    //Parameter:
    //Return values:
    //Setup clickListeners for all clickable objects
    private void setupClickListener() {

        sortExpanderButton.setOnClickListener(v -> {
            if (sortExpanderLayout.getVisibility() == View.GONE) {
                sortExpanderLayout.setVisibility(View.VISIBLE);
                sortExpanderIcon.setImageResource(R.drawable.ic_baseline_expand_less_24);
            } else if (sortExpanderLayout.getVisibility() == View.VISIBLE) {
                sortExpanderLayout.setVisibility(View.GONE);
                sortExpanderIcon.setImageResource(R.drawable.ic_baseline_expand_more_24);
            }
        });
        stateExpanderButton.setOnClickListener(v -> {
            if (stateExpanderLayout.getVisibility() == View.GONE) {
                stateExpanderLayout.setVisibility(View.VISIBLE);
                statusExpanderIcon.setImageResource(R.drawable.ic_baseline_expand_less_24);
            } else if (stateExpanderLayout.getVisibility() == View.VISIBLE) {
                stateExpanderLayout.setVisibility(View.GONE);
                statusExpanderIcon.setImageResource(R.drawable.ic_baseline_expand_more_24);
            }
        });

        sortAlphabetically.setOnClickListener(v -> personalAccountViewModel.filterListViewTextTags("names")); //sortAlphabeticallyActive,
        sortAppointments.setOnClickListener(v -> personalAccountViewModel.filterListViewTextTags("dates")); //sortAppointmentsActive,
        sortVpCount.setOnClickListener(v -> personalAccountViewModel.filterListViewTextTags("vps")); //sortVpCountActive,

        removeCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeCompletedActive = !removeCompleted.isSelected();
                removeCompleted.setSelected(removeCompletedActive);
                if (!removeCompletedActive)
                    removeCompleted.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
                else
                    removeCompleted.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.pieChartSafe)));
                personalAccountViewModel.filterListViewColorTags(removeCompletedActive, removePlannedActive, removeParticipatedActive);
            }
        });

        removePlanned.setOnClickListener(v -> {
            removePlannedActive = !removePlanned.isSelected();
            removePlanned.setSelected(removePlannedActive);
            if (!removePlannedActive)
                removePlanned.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
            else
                removePlanned.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.pieChartPlanned)));
            personalAccountViewModel.filterListViewColorTags(removeCompletedActive, removePlannedActive, removeParticipatedActive);
        });
        removeParticipated.setOnClickListener(v -> {
            removeParticipatedActive = !removeParticipated.isSelected();
            removeParticipated.setSelected(removeParticipatedActive);
            if (!removeParticipatedActive)
                removeParticipated.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
            else
                removeParticipated.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.pieChartParticipation)));
            personalAccountViewModel.filterListViewColorTags(removeCompletedActive, removePlannedActive, removeParticipatedActive);
        });
    }


    //Parameter: identifier, alphaActive, invert
    //Return values:
    //Sets toggle view for sorting list alphabetically
    public void setAlphabeticallyToggle(String identifier, Boolean alphaActive, Boolean invert) {
        if (identifier.equals("names")) {
            if (alphaActive) {
                if (invert) {
                    sortNameIcon.setVisibility(View.VISIBLE);
                    sortNameIcon.setImageResource(R.drawable.ic_baseline_north_24);
                } else {
                    sortNameIcon.setVisibility(View.VISIBLE);
                    sortNameIcon.setImageResource(R.drawable.ic_baseline_south_24);
                }
            } else {
                sortNameIcon.setVisibility(View.GONE);
            }
        } else if (identifier.equals("dates")) {
            if (alphaActive) {
                sortNameIcon.setVisibility(View.GONE);
            }
        } else if (identifier.equals("vps")) {
            if (alphaActive) {
                sortNameIcon.setVisibility(View.GONE);
            }
        }
    }


    //Parameter: identifier, appointActive, invert
    //Return values:
    //Sets toggle view for sorting list by date
    public void setAppointmentsToggle(String identifier, Boolean appointActive, Boolean invert) {
        if (identifier.equals("names")) {
            if (appointActive) {
                sortDateIcon.setVisibility(View.GONE);
            }
        } else if (identifier.equals("dates")) {
            if (appointActive) {
                if (invert) {
                    sortDateIcon.setVisibility(View.VISIBLE);
                    sortDateIcon.setImageResource(R.drawable.ic_baseline_north_24);
                } else {
                    sortDateIcon.setVisibility(View.VISIBLE);
                    sortDateIcon.setImageResource(R.drawable.ic_baseline_south_24);
                }
            } else {
                sortDateIcon.setVisibility(View.GONE);
            }
        } else if (identifier.equals("vps")) {
            if (appointActive) {
                sortDateIcon.setVisibility(View.GONE);
            }
        }
    }

    //Parameter: identifier, vpActive, invert
    //Return values:
    //Sets toggle view for sorting list by vp value
    public void setVpToggle(String identifier, Boolean vpActive, Boolean invert) {
        if (identifier.equals("names")) {
            if (vpActive) {
                sortVpsIcon.setVisibility(View.GONE);
            }
        } else if (identifier.equals("dates")) {
            if (vpActive) {
                sortVpsIcon.setVisibility(View.GONE);
            }
        } else if (identifier.equals("vps")) {
            if (vpActive) {
                if (invert) {
                    sortVpsIcon.setVisibility(View.VISIBLE);
                    sortVpsIcon.setImageResource(R.drawable.ic_baseline_north_24);
                } else {
                    sortVpsIcon.setVisibility(View.VISIBLE);
                    sortVpsIcon.setImageResource(R.drawable.ic_baseline_south_24);
                }
            } else {
                sortVpsIcon.setVisibility(View.GONE);
            }
        }
    }

    public NavController getNavController() {
        return navController;
    }

    //returns the current set adapter of the list
    public CustomStudyListAdapter getCurrentAdapter() {
        return (CustomStudyListAdapter) listView.getAdapter();
    }


    //Parameter: adapter
    //Return values:
    //Sets the new Adapter
    public void setNewListViewAdapter(CustomStudyListAdapter adapter) {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity());
        listView.setAdapter(adapter);
        listView.setLayoutManager(linearLayoutManager);
        adapter.notifyDataSetChanged();
    }


    //Parameter:
    //Return values:
    //Applies color filter for finished, planned and participated studies
    public void applyColorFilter() {
        personalAccountViewModel.filterListViewColorTags(removeCompletedActive, removePlannedActive, removeParticipatedActive);
    }
}