package com.example.vpmanager.views.createStudy;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.airbnb.lottie.LottieAnimationView;
import com.example.vpmanager.R;
import com.example.vpmanager.viewmodels.CreateStudyViewModel;
import com.kofigyan.stateprogressbar.StateProgressBar;

public class CreateStudyFragment extends Fragment {

    private NavController navController;
    public static CreateStudyViewModel createStudyViewModel;

    //maybe replaced with a navigation graph later
    private FragmentManager fragmentManager;

    private Button backBtn;
    private Button nextBtn;
    public static int currentFragment = 0;

    private StateProgressBar stateProgressBar;
    private String[] progressBarDescriptionData = {"Basis", "Beschreibung", "Ort", "Termine", "Bestätigen"};
    private LottieAnimationView doneAnimation;

    //NEW
    //private static final int NUM_PAGES = 9;
    //private ViewPager2 viewPager;
    //private FragmentStateAdapter pagerAdapter;
    //NEW

    /*
    String studyTitle = "";
    String VP = "";
    String studyDesc = "";

    String category = "";
    String execution = "";

    //remote
    String platform = "";
    String optionalPlatform = "";
    //local
    String location = "";
    String street = "";
    String room = "";

    String contactMail = "";
    String contactPhone = "";
    String contactSkype = "";
    String contactDiscord = "";
    String contactOthers = "";
     */

    /*
    TextInputEditText textInputEditTextTitle;
    TextInputEditText textInputEditTextVP;
    Spinner categoriesSpinner;
    Spinner executionTypeSpinner;


    TextInputEditText textInputEditTextContactMail;
    TextInputEditText textInputEditTextContactZoom;
    TextInputEditText textInputEditTextContactSkype;
    TextInputEditText textInputEditTextContactDiscord;
    TextInputEditText textInputEditTextContactOthers;

    TextInputEditText textInputEditTextDesc;

    TextInputEditText textInputEditTextPlatform;
    TextInputEditText textInputEditTextOptionalPlatform;

    TextInputEditText textInputEditTextLocation;
    TextInputEditText textInputEditTextStreet;
    TextInputEditText textInputEditTextRoom;

     */

    public CreateStudyFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_study, container, false);
        setupView(view);
        setupListeners();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);
        super.onViewCreated(view, savedInstanceState);
    }

    private void setupView(View view) {

        //Fragment needs to be owner. If not the data in the input fields will stay!!!!!!
        createStudyViewModel = new ViewModelProvider(this).get(CreateStudyViewModel.class);
        createStudyViewModel.prepareRepo();
        createStudyViewModel.createStudyFragment = this;

        //NEW
        //viewPager = view.findViewById(R.id.viewPager_create_study);
        //pagerAdapter = new ScreenSlidePagerAdapter(this);
        //viewPager.setAdapter(pagerAdapter);
        //NEW
        fragmentManager = getParentFragmentManager(); //instead of getSupportFragmentManager
        backBtn = view.findViewById(R.id.backButton);
        nextBtn = view.findViewById(R.id.nextButton);

        stateProgressBar = (StateProgressBar) view.findViewById(R.id.your_state_progress_bar_id);
        stateProgressBar.setStateDescriptionData(progressBarDescriptionData);
        stateProgressBar.setVisibility(View.INVISIBLE);

        doneAnimation = view.findViewById(R.id.animationView);
    }

    /*
    private static class ScreenSlidePagerAdapter extends FragmentStateAdapter {

        public ScreenSlidePagerAdapter(Fragment fragment) {
            super(fragment);
        }

        private String type = "local";
        @NonNull
        @Override
        public Fragment createFragment(int position) {
            //return new createStudyFragment_Base();
            switch (position) {
                case 0:
                    return new createStudyFragment_Base();
                case 1:
                    return new createStudyFragment_StepOne();
                case 2:
                    return new createStudyFragment_StepTwo();
                case 3:
                    return new createStudyFragment_StepThree();
                case 4:
                    if (type.equals("remote")){
                        return new createStudyFragment_StepFour_Remote();
                    } else {
                        return new createStudyFragment_StepFour_Presence();
                    }
                case 5:
                    return new createStudyFragment_StepFive();
                case 6:
                    return new createStudyFragment_finalStep();
                case 7:
                    return new createStudyFragment_finalStep_two();
                default:
                    return new createStudyFragment_finalStep_three();
            }
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }
     */

    private void setupListeners() {
        doneAnimation.addAnimatorListener(
                new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        reNavigateToCreateStudyFragment();
                    }
                }
        );
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextButton();
                //viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backButton();
            }
        });
    }

    /*
    private void createDBEntry() {

        String studyID = getNewId();
        Map<String, Object> newStudy = new HashMap<>();

        newStudy.put("id", studyID); //New id for the study
        newStudy.put("creator", mainActivity.uniqueID);

        newStudy.put("name", studyTitle);
        newStudy.put("vps", VP);
        newStudy.put("description", studyDesc);

        newStudy.put("contact", contactMail);
        newStudy.put("contact2", contactPhone);
        newStudy.put("contact3", contactSkype);
        newStudy.put("contact4", contactDiscord);
        newStudy.put("contact5", contactOthers);


        newStudy.put("category", category);
        newStudy.put("executionType", execution);
        if (executionTypeSpinner.getSelectedItem().toString().equals(getString(R.string.remoteString))) {
            newStudy.put("platform", platform);
            newStudy.put("platform2", optionalPlatform);
        } else if (executionTypeSpinner.getSelectedItem().toString().equals(getString(R.string.presenceString))) {
            newStudy.put("location", location);
            newStudy.put("street", street);
            newStudy.put("room", room);
        }
        if (!dates.isEmpty()) {
            ArrayList<String> dateIds = new ArrayList<>();

            for (int i = 0; i < dates.size(); i++) {
                Map<String, Object> newDate = new HashMap<>();
                String dateID = getNewId();

                newDate.put("id", dateID); //New id for every date
                newDate.put("studyId", studyID);
                newDate.put("userId", null);
                newDate.put("date", dates.get(i));
                newDate.put("selected", false);
                dateIds.add(dateID);
                accessDatabase.addNewDate(newDate, dateID);
            }
            newStudy.put("dates", dateIds);
        }
        System.out.println("Here");
        accessDatabase.addNewStudy(newStudy, studyID);

        reNavigateToCreateStudyFragment();
    }

 */

    private void nextButton() {
        switch (currentFragment) {
            //done
            case 0:
                System.out.println("currentFragment case0 " + currentFragment);
                //bundle = createBundle(1);
                createStudyFragment_StepOne createStudyFragment_stepOne = new createStudyFragment_StepOne();
                //createStudyFragment_stepOne.setArguments(bundle);
                stateProgressBar.setVisibility(View.VISIBLE);
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                                R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.fragment_container_create_study, createStudyFragment_stepOne, null)
                        //.addToBackStack(null)
                        .setReorderingAllowed(true)
                        .commit();
                break;
            //done
            case 1:
                System.out.println("currentFragment case1 " + currentFragment);
                //getInput(currentFragment);
                System.out.println("case1 before saveData" + createStudyViewModel.studyCreationProcessData.toString());
                saveDataInViewModelStepOne();
                System.out.println("case1 after saveData" + createStudyViewModel.studyCreationProcessData.toString());
                if (mandatoryCheck(currentFragment)) {
                    //bundle = createBundle(2);
                    createStudyFragment_StepTwo createStudyFragment_stepTwo = new createStudyFragment_StepTwo();
                    //createStudyFragment_stepTwo.setArguments(bundle);
                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
                    fragmentManager
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                                    R.anim.enter_from_left, R.anim.exit_to_right)
                            .replace(R.id.fragment_container_create_study, createStudyFragment_stepTwo, null)
                            //.addToBackStack(null)
                            .setReorderingAllowed(true)
                            .commit();
                }
                break;
            //done
            case 2:
                System.out.println("currentFragment case2" + currentFragment);
                //getInput(currentFragment);
                System.out.println("case2 before saveData" + createStudyViewModel.studyCreationProcessData.toString());
                saveDataInViewModelStepTwo();
                System.out.println("case2 after saveData" + createStudyViewModel.studyCreationProcessData.toString());
                if (mandatoryCheck(currentFragment)) {
                    //bundle = createBundle(3);
                    createStudyFragment_StepThree createStudyFragment_stepThree = new createStudyFragment_StepThree();
                    //createStudyFragment_stepThree.setArguments(bundle);
                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
                    fragmentManager
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                                    R.anim.enter_from_left, R.anim.exit_to_right)
                            .replace(R.id.fragment_container_create_study, createStudyFragment_stepThree, null)
                            //.addToBackStack(null)
                            .setReorderingAllowed(true)
                            .commit();
                }
                break;
            //done
            case 3:
                System.out.println("currentFragment case3" + currentFragment);
                //getInput(currentFragment);
                System.out.println("case3 before saveData" + createStudyViewModel.studyCreationProcessData.toString());
                saveDataInViewModelStepThree();
                System.out.println("case3 after saveData" + createStudyViewModel.studyCreationProcessData.toString());
                if (mandatoryCheck(currentFragment)) {
                    //bundle = createBundle(4);
                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                    String executionType = createStudyViewModel.studyCreationProcessData.get("executionType").toString();
                    Log.d("executionType", "is: " + executionType);
                    if (executionType.equals(getString(R.string.remoteString))) {
                        createStudyFragment_StepFour_Remote createStudyFragment_stepFour_remote = new createStudyFragment_StepFour_Remote();
                        //createStudyFragment_stepFour_remote.setArguments(bundle);
                        fragmentManager
                                .beginTransaction()
                                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                                        R.anim.enter_from_left, R.anim.exit_to_right)
                                .replace(R.id.fragment_container_create_study, createStudyFragment_stepFour_remote, null)
                                //.addToBackStack(null)
                                .setReorderingAllowed(true)
                                .commit();
                    } else {
                        createStudyFragment_StepFour_Presence createStudyFragment_stepFour_presence = new createStudyFragment_StepFour_Presence();
                        //createStudyFragment_stepFour_presence.setArguments(bundle);
                        fragmentManager
                                .beginTransaction()
                                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                                        R.anim.enter_from_left, R.anim.exit_to_right)
                                .replace(R.id.fragment_container_create_study, createStudyFragment_stepFour_presence, null)
                                //.addToBackStack(null)
                                .setReorderingAllowed(true)
                                .commit();
                    }
                }
                break;
            //done
            case 4:
                System.out.println("currentFragment case4" + currentFragment);
                //getInput(currentFragment);
                System.out.println("case4 before saveData" + createStudyViewModel.studyCreationProcessData.toString());
                saveDataInViewModelStepFour();
                System.out.println("case4 after saveData" + createStudyViewModel.studyCreationProcessData.toString());
                if (mandatoryCheck(currentFragment)) {
                    //bundle = createBundle(5);
                    createStudyFragment_StepFive createStudyFragment_stepFive = new createStudyFragment_StepFive();
                    //createStudyFragment_stepFive.setArguments(bundle);
                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR);
                    fragmentManager
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                                    R.anim.enter_from_left, R.anim.exit_to_right)
                            .replace(R.id.fragment_container_create_study, createStudyFragment_stepFive, null)
                            //.addToBackStack(null)
                            .setReorderingAllowed(true)
                            .commit();
                }
                break;
            //done
            case 5:
                System.out.println("currentFragment case5" + currentFragment);
                //getInput(currentFragment);
                System.out.println("case5 before saveData" + createStudyViewModel.datesCreationProcessData.toString());
                saveDataInViewModelStepFive();
                System.out.println("case5 after saveData" + createStudyViewModel.datesCreationProcessData.toString());
                //bundle = createBundle(6);
                createStudyFragment_finalStep createStudyFragment_finalStep = new createStudyFragment_finalStep();
                //createStudyFragment_finalStep.setArguments(bundle);
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FIVE);
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                                R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.fragment_container_create_study, createStudyFragment_finalStep, null)
                        //.addToBackStack(null)
                        .setReorderingAllowed(true)
                        .commit();
                break;
            //done
            case 6:
                System.out.println("currentFragment case6" + currentFragment);
                //bundle = createBundle(7);
                createStudyFragment_finalStep_two createStudyFragment_finalStep_two = new createStudyFragment_finalStep_two();
                //createStudyFragment_finalStep_two.setArguments(bundle);
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FIVE);
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                                R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.fragment_container_create_study, createStudyFragment_finalStep_two, null)
                        //.addToBackStack(null)
                        .setReorderingAllowed(true)
                        .commit();
                break;
            //done
            case 7:
                System.out.println("currentFragment case7" + currentFragment);
                //bundle = createBundle(8);
                createStudyFragment_finalStep_three createStudyFragment_finalStep_three = new createStudyFragment_finalStep_three();
                //createStudyFragment_finalStep_three.setArguments(bundle);
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FIVE);
                nextBtn.setText(getString(R.string.fragment_create_study_base_create));
                nextBtn.setBackgroundColor(getResources().getColor(R.color.green_Main));
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                                R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.fragment_container_create_study, createStudyFragment_finalStep_three, null)
                        //.addToBackStack(null)
                        .setReorderingAllowed(true)
                        .commit();
                break;
            //done
            case 8:
                System.out.println("currentFragment case8" + currentFragment);
                createStudyViewModel.saveStudyInDb();
                playAnimation();
                break;
            default:
                break;
        }
    }

    private void backButton() {
        switch (currentFragment) {
            case 0:
                navController.navigate(R.id.action_global_homeFragment);
                break;
            case 1:
                //getInput(currentFragment);
                stateProgressBar.setVisibility(View.INVISIBLE);
                createStudyFragment_Base createStudyFragment_base = new createStudyFragment_Base();
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right,
                                R.anim.enter_from_right, R.anim.exit_to_left)
                        .replace(R.id.fragment_container_create_study, createStudyFragment_base, null)
                        //.addToBackStack(null)
                        .setReorderingAllowed(true)
                        .commit();
                break;
            case 2:
                //getInput(currentFragment);
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
                //bundle = createBundle(1);
                createStudyFragment_StepOne createStudyFragment_stepOne = new createStudyFragment_StepOne();
                //createStudyFragment_stepOne.setArguments(bundle);
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right,
                                R.anim.enter_from_right, R.anim.exit_to_left)
                        .replace(R.id.fragment_container_create_study, createStudyFragment_stepOne, null)
                        //.addToBackStack(null)
                        .setReorderingAllowed(true)
                        .commit();
                break;
            case 3:
                //getInput(currentFragment);
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
                //bundle = createBundle(2);
                createStudyFragment_StepTwo createStudyFragment_stepTwo = new createStudyFragment_StepTwo();
                //createStudyFragment_stepTwo.setArguments(bundle);
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right,
                                R.anim.enter_from_right, R.anim.exit_to_left)
                        .replace(R.id.fragment_container_create_study, createStudyFragment_stepTwo, null)
                        //.addToBackStack(null)
                        .setReorderingAllowed(true)
                        .commit();
                break;
            case 4:
                //getInput(currentFragment);
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
                //bundle = createBundle(3);
                createStudyFragment_StepThree createStudyFragment_stepThree = new createStudyFragment_StepThree();
                //createStudyFragment_stepThree.setArguments(bundle);
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right,
                                R.anim.enter_from_right, R.anim.exit_to_left)
                        .replace(R.id.fragment_container_create_study, createStudyFragment_stepThree, null)
                        //.addToBackStack(null)
                        .setReorderingAllowed(true)
                        .commit();
                break;
            case 5:
                //getInput(currentFragment);
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                //bundle = createBundle(4);
                String executionType = createStudyViewModel.studyCreationProcessData.get("executionType").toString();
                Log.d("executionType", "is: " + executionType);
                if (executionType.equals(getString(R.string.remoteString))) {
                    createStudyFragment_StepFour_Remote createStudyFragment_stepFour_remote = new createStudyFragment_StepFour_Remote();
                    //createStudyFragment_stepFour_remote.setArguments(bundle);
                    fragmentManager
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right,
                                    R.anim.enter_from_right, R.anim.exit_to_left)
                            .replace(R.id.fragment_container_create_study, createStudyFragment_stepFour_remote, null)
                            //.addToBackStack(null)
                            .setReorderingAllowed(true)
                            .commit();
                } else {
                    createStudyFragment_StepFour_Presence createStudyFragment_stepFour_presence = new createStudyFragment_StepFour_Presence();
                    //createStudyFragment_stepFour_presence.setArguments(bundle);
                    fragmentManager
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right,
                                    R.anim.enter_from_right, R.anim.exit_to_left)
                            .replace(R.id.fragment_container_create_study, createStudyFragment_stepFour_presence, null)
                            //.addToBackStack(null)
                            .setReorderingAllowed(true)
                            .commit();
                }
                break;
            case 6:
                //getInput(currentFragment);
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR);
                //bundle = createBundle(5);
                createStudyFragment_StepFive createStudyFragment_stepFive = new createStudyFragment_StepFive();
                //createStudyFragment_stepFive.setArguments(bundle);
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right,
                                R.anim.enter_from_right, R.anim.exit_to_left)
                        .replace(R.id.fragment_container_create_study, createStudyFragment_stepFive, null)
                        //.addToBackStack(null)
                        .setReorderingAllowed(true)
                        .commit();
                break;
            case 7:
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR);
                //bundle = createBundle(6);
                createStudyFragment_finalStep createStudyFragment_finalStep = new createStudyFragment_finalStep();
                //createStudyFragment_finalStep.setArguments(bundle);
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right,
                                R.anim.enter_from_right, R.anim.exit_to_left)
                        .replace(R.id.fragment_container_create_study, createStudyFragment_finalStep, null)
                        //.addToBackStack(null)
                        .setReorderingAllowed(true)
                        .commit();
                break;
            case 8:
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FIVE);
                //bundle = createBundle(7);
                createStudyFragment_finalStep_two createStudyFragment_finalStep_two = new createStudyFragment_finalStep_two();
                //createStudyFragment_finalStep_two.setArguments(bundle);
                nextBtn.setText(getString(R.string.fragment_create_study_base_next));
                nextBtn.setBackgroundColor(getResources().getColor(R.color.heatherred_dark));
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right,
                                R.anim.enter_from_right, R.anim.exit_to_left)
                        .replace(R.id.fragment_container_create_study, createStudyFragment_finalStep_two, null)
                        //.addToBackStack(null)
                        .setReorderingAllowed(true)
                        .commit();
                break;
            default:
                break;
        }
    }

    private void saveDataInViewModelStepOne() {
        String name = createStudyFragment_StepOne.textInputEditTextTitle.getText().toString();
        if (!name.isEmpty()) {
            createStudyViewModel.studyCreationProcessData.put("name", name);
        }
        String vps = createStudyFragment_StepOne.textInputEditTextVP.getText().toString();
        if (!vps.isEmpty()) {
            createStudyViewModel.studyCreationProcessData.put("vps", vps);
        }
        String category = createStudyFragment_StepOne.categories.getSelectedItem().toString();
        if (!category.equals("Studienkategorie*")) {
            createStudyViewModel.studyCreationProcessData.put("category", category);
        }
        String executionType = createStudyFragment_StepOne.executionType.getSelectedItem().toString();
        if (!executionType.equals("Durchführungsart*")) {
            createStudyViewModel.studyCreationProcessData.put("executionType", executionType);
        }
        //if user changes type in the process
        if (executionType.equals("Remote")) {
            createStudyViewModel.studyCreationProcessData.remove("location");
            createStudyViewModel.studyCreationProcessData.remove("street");
            createStudyViewModel.studyCreationProcessData.remove("room");
        }
        if (executionType.equals("Präsenz")) {
            createStudyViewModel.studyCreationProcessData.remove("platform");
            createStudyViewModel.studyCreationProcessData.remove("platform2");
        }
    }

    private void saveDataInViewModelStepTwo() {
        String mail = createStudyFragment_StepTwo.textInputEditTextContactMail.getText().toString();
        if (!mail.isEmpty()) {
            createStudyViewModel.studyCreationProcessData.put("contact", mail);
        }
        String phone = createStudyFragment_StepTwo.textInputEditTextContactPhone.getText().toString();
        if (!phone.isEmpty()) {
            createStudyViewModel.studyCreationProcessData.put("contact2", phone);
        }
        String skype = createStudyFragment_StepTwo.textInputEditTextContactSkype.getText().toString();
        if (!skype.isEmpty()) {
            createStudyViewModel.studyCreationProcessData.put("contact3", skype);
        }
        String discord = createStudyFragment_StepTwo.textInputEditTextContactDiscord.getText().toString();
        if (!discord.isEmpty()) {
            createStudyViewModel.studyCreationProcessData.put("contact4", discord);
        }
        String other = createStudyFragment_StepTwo.textInputEditTextContactOthers.getText().toString();
        if (!other.isEmpty()) {
            createStudyViewModel.studyCreationProcessData.put("contact5", other);
        }
    }

    private void saveDataInViewModelStepThree() {
        String description = createStudyFragment_StepThree.textInputEditTextDesc.getText().toString();
        if (!description.isEmpty()) {
            createStudyViewModel.studyCreationProcessData.put("description", description);
        }
    }

    private void saveDataInViewModelStepFour() {
        String executionType = createStudyViewModel.studyCreationProcessData.get("executionType").toString();
        if (executionType.equals(getString(R.string.remoteString))) {
            String platform = createStudyFragment_StepFour_Remote.textInputEditTextPlatform.getText().toString();
            if (!platform.isEmpty()) {
                createStudyViewModel.studyCreationProcessData.put("platform", platform);
            }
            String platform2 = createStudyFragment_StepFour_Remote.textInputEditTextOptionalPlatform.getText().toString();
            if (!platform2.isEmpty()) {
                createStudyViewModel.studyCreationProcessData.put("platform2", platform2);
            }
        } else {
            String location = createStudyFragment_StepFour_Presence.textInputEditTextLocation.getText().toString();
            if (!location.isEmpty()) {
                createStudyViewModel.studyCreationProcessData.put("location", location);
            }
            String street = createStudyFragment_StepFour_Presence.textInputEditTextStreet.getText().toString();
            if (!street.isEmpty()) {
                createStudyViewModel.studyCreationProcessData.put("street", street);
            }
            String room = createStudyFragment_StepFour_Presence.textInputEditTextRoom.getText().toString();
            if (!room.isEmpty()) {
                createStudyViewModel.studyCreationProcessData.put("room", room);
            }
        }
    }

    private void saveDataInViewModelStepFive() {
        if (!createStudyFragment_StepFive.dates.isEmpty()) {
            createStudyViewModel.datesCreationProcessData = createStudyFragment_StepFive.dates;
        }
    }

    /*
    private void getInput(int page) {
        int currentFragmentIndex = fragmentManager.getFragments().size() - 1;
        switch (page) {
            case 1:
                textInputEditTextTitle = fragmentManager.getFragments().get(currentFragmentIndex).getView().findViewById(R.id.inputFieldTitle);
                textInputEditTextVP = fragmentManager.getFragments().get(currentFragmentIndex).getView().findViewById(R.id.inputFieldVP);
                studyTitle = Objects.requireNonNull(textInputEditTextTitle.getText()).toString();
                VP = Objects.requireNonNull(textInputEditTextVP.getText()).toString();
                System.out.println("getInput" + textInputEditTextTitle + " " + textInputEditTextVP);
                System.out.println(VP + " " + studyTitle);

                categoriesSpinner = fragmentManager.getFragments().get(currentFragmentIndex).getView().findViewById(R.id.createCategories);
                executionTypeSpinner = fragmentManager.getFragments().get(currentFragmentIndex).getView().findViewById(R.id.createExecutionType);
                category = categoriesSpinner.getSelectedItem().toString();
                execution = executionTypeSpinner.getSelectedItem().toString();
                System.out.println("Kategorie: " + category);
                System.out.println("Durchführung: " + execution);
                break;

            case 2:
                textInputEditTextContactMail = fragmentManager.getFragments().get(currentFragmentIndex).getView().findViewById(R.id.inputContact1);
                textInputEditTextContactZoom = fragmentManager.getFragments().get(currentFragmentIndex).getView().findViewById(R.id.inputContact2);
                textInputEditTextContactSkype = fragmentManager.getFragments().get(currentFragmentIndex).getView().findViewById(R.id.inputContact3);
                textInputEditTextContactDiscord = fragmentManager.getFragments().get(currentFragmentIndex).getView().findViewById(R.id.inputContact4);
                textInputEditTextContactOthers = fragmentManager.getFragments().get(currentFragmentIndex).getView().findViewById(R.id.inputContact5);

                contactMail = Objects.requireNonNull(textInputEditTextContactMail.getText()).toString();
                contactPhone = Objects.requireNonNull(textInputEditTextContactZoom.getText()).toString();
                contactSkype = Objects.requireNonNull(textInputEditTextContactSkype.getText()).toString();
                contactDiscord = Objects.requireNonNull(textInputEditTextContactDiscord.getText()).toString();
                contactOthers = Objects.requireNonNull(textInputEditTextContactOthers.getText()).toString();
                break;

            case 3:
                textInputEditTextDesc = fragmentManager.getFragments().get(currentFragmentIndex).getView().findViewById(R.id.inputFieldStudyDesc);
                studyDesc = Objects.requireNonNull(textInputEditTextDesc.getText()).toString();
                System.out.println(studyDesc);
                break;

            case 4:
                if (execution.equals(getString(R.string.remoteString))) {
                    // GET REMOTE STUFF HERE
                    location = "";
                    street = "";
                    room = "";
                    textInputEditTextPlatform = fragmentManager.getFragments().get(currentFragmentIndex).getView().findViewById(R.id.inputFieldPlatform);
                    textInputEditTextOptionalPlatform = fragmentManager.getFragments().get(currentFragmentIndex).getView().findViewById(R.id.inputFieldPlatformOptional);
                    platform = Objects.requireNonNull(textInputEditTextPlatform.getText()).toString();
                    optionalPlatform = Objects.requireNonNull(textInputEditTextOptionalPlatform.getText()).toString();
                }
                if (execution.equals((getString(R.string.presenceString)))) {
                    // GET PRESENCE STUFF HERE
                    platform = "";
                    optionalPlatform = "";

                    textInputEditTextLocation = fragmentManager.getFragments().get(currentFragmentIndex).getView().findViewById(R.id.inputFieldLocation);
                    textInputEditTextStreet = fragmentManager.getFragments().get(currentFragmentIndex).getView().findViewById(R.id.inputFieldStreet);
                    textInputEditTextRoom = fragmentManager.getFragments().get(currentFragmentIndex).getView().findViewById(R.id.inputFieldRoom);

                    location = Objects.requireNonNull(textInputEditTextLocation.getText()).toString();
                    street = Objects.requireNonNull(textInputEditTextStreet.getText()).toString();
                    room = Objects.requireNonNull(textInputEditTextRoom.getText()).toString();
                }
                break;
            case 5:
                listViewDates = fragmentManager.getFragments().get(currentFragmentIndex).getView().findViewById(R.id.createDatelist);
                for (int i = 0; i < listViewDates.getAdapter().getCount(); i++) {
                    System.out.println(listViewDates.getAdapter().getItem(i));
                    System.out.println(listViewDates.getAdapter().getCount());
                    dates.add(listViewDates.getAdapter().getItem(i).toString());
                }
                break;
        }
    }

     */

    /*
    private Bundle createBundle(int page) {
        Bundle bundle = new Bundle();
        switch (page) {
            case 1:
                bundle.putString("title", studyTitle);
                bundle.putString("vp", VP);
                bundle.putString("category", category);
                bundle.putString("exe", execution);
                break;
            case 2:
                bundle.putString("contact", mainActivity.uniqueID); //mainActivity.createUserId(this);
                bundle.putString("contact2", contactPhone);
                bundle.putString("contact3", contactSkype);
                bundle.putString("contact4", contactDiscord);
                bundle.putString("contact5", contactOthers);
                break;
            case 3:
                bundle.putString("desc", studyDesc);
                break;
            case 4:
                if (execution.equals(getString(R.string.remoteString))) {
                    bundle.putString("platform", platform);
                    bundle.putString("platform2", optionalPlatform);

                }
                if (execution.equals((getString(R.string.presenceString)))) {
                    bundle.putString("location", location);
                    bundle.putString("street", street);
                    bundle.putString("room", room);
                }
                break;
            case 5:
                bundle.putStringArrayList("dates", dates);
                break;
            case 6:
                prepareData();
                bundle.putString("title", studyTitle);
                bundle.putString("vp", VP);
                bundle.putString("category", category);
                bundle.putString("exe", execution);
                bundle.putString("location", locationViewString);
                break;
            case 7:
                bundle.putString("desc", studyDesc);
                bundle.putString("contact", contactViewString);
                break;
            case 8:
                bundle.putStringArrayList("dates", dates);
                break;
            default:
                break;
        }
        return bundle;
    }
    */

    /*
    private void prepareData() {
        contactViewString = "\n";
        contactViewString += "Email-adresese: " + contactMail;
        if (!contactPhone.isEmpty()) {
            contactViewString += "\nHandynummer: " + contactPhone;
        }
        if (!contactSkype.isEmpty()) {
            contactViewString += "\nSkype: " + contactSkype;
        }
        if (!contactDiscord.isEmpty()) {
            contactViewString += "\nDiscord: " + contactDiscord;
        }
        if (!contactOthers.isEmpty()) {
            contactViewString += "\nSonstige: " + contactOthers;
        }

        locationViewString = "\n";
        if (!location.isEmpty()) {
            locationViewString = location + "\n" + street + "\n" + room;
        } else {
            locationViewString += platform;
            if (!optionalPlatform.isEmpty()) {
                locationViewString += " & " + optionalPlatform;
            }
        }
    }

     */

    private boolean mandatoryCheck(int page) {
        switch (page) {
            case 1:
                if (createStudyViewModel.studyCreationProcessData.get("name") != null &&
                        createStudyViewModel.studyCreationProcessData.get("category") != null &&
                        createStudyViewModel.studyCreationProcessData.get("executionType") != null) {
                    return true;
                }
                if (createStudyViewModel.studyCreationProcessData.get("name") == null) {
                    createStudyFragment_StepOne.textInputEditTextTitle.setError("Titel darf nicht leer sein");
                    createStudyFragment_StepOne.textInputEditTextTitle.requestFocus();
                    break;
                }
                if (createStudyViewModel.studyCreationProcessData.get("category") == null) {
                    createStudyFragment_StepOne.categories.setFocusable(true);
                    createStudyFragment_StepOne.categories.setFocusableInTouchMode(true);
                    createStudyFragment_StepOne.categories.requestFocus();
                    createStudyFragment_StepOne.categories.performClick();
                    break;
                }
                if (createStudyViewModel.studyCreationProcessData.get("executionType") == null) {
                    createStudyFragment_StepOne.executionType.setFocusable(true);
                    createStudyFragment_StepOne.executionType.setFocusableInTouchMode(true);
                    createStudyFragment_StepOne.executionType.requestFocus();
                    createStudyFragment_StepOne.executionType.performClick();
                    break;
                }
                /*
                if (!studyTitle.isEmpty() && !category.equals("Studienkategorie*") && !execution.equals("Durchführungsart*")) {
                    System.out.println(studyTitle);
                    return true;
                }
                if (studyTitle.isEmpty()) {
                    textInputEditTextTitle.setError("Titel darf nicht leer sein");
                    textInputEditTextTitle.requestFocus();
                    break;
                }
                if (category.equals("Studienkategorie*")) {
                    categoriesSpinner.setFocusable(true);
                    categoriesSpinner.setFocusableInTouchMode(true);
                    categoriesSpinner.requestFocus();
                    categoriesSpinner.performClick();
                    break;
                }
                if (execution.equals("Durchführungsart*")) {
                    executionTypeSpinner.setFocusable(true);
                    executionTypeSpinner.setFocusableInTouchMode(true);
                    executionTypeSpinner.requestFocus();
                    executionTypeSpinner.performClick();
                    break;
                }
                 */
                break;
            case 2:
                if (createStudyViewModel.studyCreationProcessData.get("contact") != null) {
                    return true;
                }
                createStudyFragment_StepTwo.textInputEditTextContactMail.setError("Email-Kontakt muss angegeben werden");
                createStudyFragment_StepTwo.textInputEditTextContactMail.requestFocus();
                /*
                textInputEditTextContactMail.setError("Email-Kontakt muss angegeben werden");
                textInputEditTextContactMail.requestFocus();
                 */
                break;
            case 3:
                if (createStudyViewModel.studyCreationProcessData.get("description") != null) {
                    return true;
                }
                createStudyFragment_StepThree.textInputEditTextDesc.setError("Studienbeschreibung darf nicht leer sein");
                createStudyFragment_StepThree.textInputEditTextDesc.requestFocus();
                break;
            case 4:
                if (createStudyViewModel.studyCreationProcessData.get("location") != null) {
                    return true;
                }
                String executionType = createStudyViewModel.studyCreationProcessData.get("executionType").toString();
                if (executionType.equals(getString(R.string.presenceString))) {
                    createStudyFragment_StepFour_Presence.textInputEditTextLocation
                            .setError("Ort der Studie muss angegeben werden");
                    createStudyFragment_StepFour_Presence.textInputEditTextLocation.requestFocus();
                }
                if (createStudyViewModel.studyCreationProcessData.get("platform") != null) {
                    return true;
                }
                if (executionType.equals(getString(R.string.remoteString))) {
                    createStudyFragment_StepFour_Remote.textInputEditTextPlatform
                            .setError("Primärplattform muss angegeben werden");
                    createStudyFragment_StepFour_Remote.textInputEditTextPlatform.requestFocus();
                }
                /*
                if (!location.isEmpty()) {
                    return true;
                }
                if (execution.equals("Präsenz")) {
                    textInputEditTextLocation.setError("Ort der Studie muss angegeben werden");
                    textInputEditTextLocation.requestFocus();
                }
                if (!platform.isEmpty()) {
                    return true;
                }
                if (execution.equals("Remote")) {
                    textInputEditTextPlatform.setError("Primärplattform muss angegeben werden");
                    textInputEditTextPlatform.requestFocus();
                }
                 */
                break;
            default:
                return false;
        }
        return false;
    }

    private void reNavigateToCreateStudyFragment() {
        navController.navigate(R.id.action_global_createStudyFragment);
    }

    public void playAnimation() {
        doneAnimation.setVisibility(LottieAnimationView.VISIBLE);
        doneAnimation.setProgress(0);
        doneAnimation.pauseAnimation();
        doneAnimation.playAnimation();
    }

    public void showSnackBar() {
        //show snackBar
    }

}
