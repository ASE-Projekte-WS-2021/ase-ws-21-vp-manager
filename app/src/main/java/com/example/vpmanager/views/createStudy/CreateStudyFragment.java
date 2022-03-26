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
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.airbnb.lottie.LottieAnimationView;
import com.example.vpmanager.R;
import com.example.vpmanager.viewmodels.CreateStudyViewModel;
import com.kofigyan.stateprogressbar.StateProgressBar;

public class CreateStudyFragment extends Fragment {

    private NavController navControllerMain;
    public static CreateStudyViewModel createStudyViewModel;

    //private FragmentManager fragmentManager;
    private NavHostFragment navHostFragmentCreate;
    private NavController navControllerCreate;

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
        setupNavigation();
        setupView(view);
        setupListeners();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navControllerMain = Navigation.findNavController(view);
        super.onViewCreated(view, savedInstanceState);
    }

    private void setupNavigation(){
        //getChildFragmentManager is important!
        navHostFragmentCreate = (NavHostFragment) getChildFragmentManager().findFragmentById(R.id.nav_host_fragment_create);
        assert navHostFragmentCreate != null;
        navControllerCreate = navHostFragmentCreate.getNavController();
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

        //fragmentManager = getParentFragmentManager(); //instead of getSupportFragmentManager
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

    private void nextButton() {
        switch (currentFragment) {
            case 0:
                System.out.println("currentFragment case0 " + currentFragment);
                createStudyFragment_StepOne createStudyFragment_stepOne = new createStudyFragment_StepOne();
                stateProgressBar.setVisibility(View.VISIBLE);
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);

                navControllerCreate.navigate(R.id.action_createStudyFragment_Base_to_createStudyFragment_StepOne);
                /*
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                                R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.nav_host_fragment_create, createStudyFragment_stepOne, null)
                        //.addToBackStack(null)
                        .setReorderingAllowed(true)
                        .commit();
                 */
                break;
            case 1:
                System.out.println("currentFragment case1 " + currentFragment);
                System.out.println("case1 before saveData" + createStudyViewModel.studyCreationProcessData.toString());
                saveDataInViewModelStepOne();
                System.out.println("case1 after saveData" + createStudyViewModel.studyCreationProcessData.toString());
                if (mandatoryCheck(currentFragment)) {
                    createStudyFragment_StepTwo createStudyFragment_stepTwo = new createStudyFragment_StepTwo();
                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);

                    navControllerCreate.navigate(R.id.action_createStudyFragment_StepOne_to_createStudyFragment_StepTwo);
                    /*
                    fragmentManager
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                                    R.anim.enter_from_left, R.anim.exit_to_right)
                            .replace(R.id.nav_host_fragment_create, createStudyFragment_stepTwo, null)
                            //.addToBackStack(null)
                            .setReorderingAllowed(true)
                            .commit();
                     */
                }
                break;
            case 2:
                System.out.println("currentFragment case2" + currentFragment);
                System.out.println("case2 before saveData" + createStudyViewModel.studyCreationProcessData.toString());
                saveDataInViewModelStepTwo();
                System.out.println("case2 after saveData" + createStudyViewModel.studyCreationProcessData.toString());
                if (mandatoryCheck(currentFragment)) {
                    createStudyFragment_StepThree createStudyFragment_stepThree = new createStudyFragment_StepThree();
                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);

                    navControllerCreate.navigate(R.id.action_createStudyFragment_StepTwo_to_createStudyFragment_StepThree);
                    /*
                    fragmentManager
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                                    R.anim.enter_from_left, R.anim.exit_to_right)
                            .replace(R.id.nav_host_fragment_create, createStudyFragment_stepThree, null)
                            //.addToBackStack(null)
                            .setReorderingAllowed(true)
                            .commit();
                     */
                }
                break;
            case 3:
                System.out.println("currentFragment case3" + currentFragment);
                System.out.println("case3 before saveData" + createStudyViewModel.studyCreationProcessData.toString());
                saveDataInViewModelStepThree();
                System.out.println("case3 after saveData" + createStudyViewModel.studyCreationProcessData.toString());
                if (mandatoryCheck(currentFragment)) {
                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                    String executionType = createStudyViewModel.studyCreationProcessData.get("executionType").toString();
                    Log.d("executionType", "is: " + executionType);
                    if (executionType.equals(getString(R.string.remoteString))) {
                        createStudyFragment_StepFour_Remote createStudyFragment_stepFour_remote = new createStudyFragment_StepFour_Remote();

                        navControllerCreate.navigate(R.id.action_createStudyFragment_StepThree_to_createStudyFragment_StepFour_Remote);
                        /*
                        fragmentManager
                                .beginTransaction()
                                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                                        R.anim.enter_from_left, R.anim.exit_to_right)
                                .replace(R.id.nav_host_fragment_create, createStudyFragment_stepFour_remote, null)
                                //.addToBackStack(null)
                                .setReorderingAllowed(true)
                                .commit();
                         */
                    } else {
                        createStudyFragment_StepFour_Presence createStudyFragment_stepFour_presence = new createStudyFragment_StepFour_Presence();

                        navControllerCreate.navigate(R.id.action_createStudyFragment_StepThree_to_createStudyFragment_StepFour_Presence);
                        /*
                        fragmentManager
                                .beginTransaction()
                                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                                        R.anim.enter_from_left, R.anim.exit_to_right)
                                .replace(R.id.nav_host_fragment_create, createStudyFragment_stepFour_presence, null)
                                //.addToBackStack(null)
                                .setReorderingAllowed(true)
                                .commit();
                         */
                    }
                }
                break;
            case 4:
                System.out.println("currentFragment case4" + currentFragment);
                System.out.println("case4 before saveData" + createStudyViewModel.studyCreationProcessData.toString());
                saveDataInViewModelStepFour();
                System.out.println("case4 after saveData" + createStudyViewModel.studyCreationProcessData.toString());
                if (mandatoryCheck(currentFragment)) {
                    createStudyFragment_StepFive createStudyFragment_stepFive = new createStudyFragment_StepFive();
                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR);

                    String executionType = createStudyViewModel.studyCreationProcessData.get("executionType").toString();
                    if(executionType.equals(getString(R.string.remoteString))){
                        navControllerCreate.navigate(R.id.action_createStudyFragment_StepFour_Remote_to_createStudyFragment_StepFive);
                    }else {
                        navControllerCreate.navigate(R.id.action_createStudyFragment_StepFour_Presence_to_createStudyFragment_StepFive);
                    }
                    /*
                    fragmentManager
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                                    R.anim.enter_from_left, R.anim.exit_to_right)
                            .replace(R.id.nav_host_fragment_create, createStudyFragment_stepFive, null)
                            //.addToBackStack(null)
                            .setReorderingAllowed(true)
                            .commit();
                     */
                }
                break;
            case 5:
                System.out.println("currentFragment case5" + currentFragment);
                System.out.println("case5 before saveData" + createStudyViewModel.datesCreationProcessData.toString());
                saveDataInViewModelStepFive();
                System.out.println("case5 after saveData" + createStudyViewModel.datesCreationProcessData.toString());
                createStudyFragment_finalStep createStudyFragment_finalStep = new createStudyFragment_finalStep();
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FIVE);

                navControllerCreate.navigate(R.id.action_createStudyFragment_StepFive_to_createStudyFragment_finalStep);
                /*
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                                R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.nav_host_fragment_create, createStudyFragment_finalStep, null)
                        //.addToBackStack(null)
                        .setReorderingAllowed(true)
                        .commit();
                 */
                break;
            case 6:
                System.out.println("currentFragment case6" + currentFragment);
                createStudyFragment_finalStep_two createStudyFragment_finalStep_two = new createStudyFragment_finalStep_two();
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FIVE);

                navControllerCreate.navigate(R.id.action_createStudyFragment_finalStep_to_createStudyFragment_finalStep_two);
                /*
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                                R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.nav_host_fragment_create, createStudyFragment_finalStep_two, null)
                        //.addToBackStack(null)
                        .setReorderingAllowed(true)
                        .commit();
                 */
                break;
            case 7:
                System.out.println("currentFragment case7" + currentFragment);
                createStudyFragment_finalStep_three createStudyFragment_finalStep_three = new createStudyFragment_finalStep_three();
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FIVE);
                nextBtn.setText(getString(R.string.fragment_create_study_base_create));
                nextBtn.setBackgroundColor(getResources().getColor(R.color.green_Main));

                navControllerCreate.navigate(R.id.action_createStudyFragment_finalStep_two_to_createStudyFragment_finalStep_three);
                /*
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                                R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.nav_host_fragment_create, createStudyFragment_finalStep_three, null)
                        //.addToBackStack(null)
                        .setReorderingAllowed(true)
                        .commit();
                 */
                break;
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
                navControllerMain.navigate(R.id.action_global_homeFragment);
                break;
            case 1:
                stateProgressBar.setVisibility(View.INVISIBLE);
                createStudyFragment_Base createStudyFragment_base = new createStudyFragment_Base();

                navControllerCreate.navigate(R.id.action_createStudyFragment_StepOne_to_createStudyFragment_Base);
                /*
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right,
                                R.anim.enter_from_right, R.anim.exit_to_left)
                        .replace(R.id.nav_host_fragment_create, createStudyFragment_base, null)
                        //.addToBackStack(null)
                        .setReorderingAllowed(true)
                        .commit();
                 */
                break;
            case 2:
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
                createStudyFragment_StepOne createStudyFragment_stepOne = new createStudyFragment_StepOne();

                navControllerCreate.navigate(R.id.action_createStudyFragment_StepTwo_to_createStudyFragment_StepOne);
                /*
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right,
                                R.anim.enter_from_right, R.anim.exit_to_left)
                        .replace(R.id.nav_host_fragment_create, createStudyFragment_stepOne, null)
                        //.addToBackStack(null)
                        .setReorderingAllowed(true)
                        .commit();
                 */
                break;
            case 3:
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
                createStudyFragment_StepTwo createStudyFragment_stepTwo = new createStudyFragment_StepTwo();

                navControllerCreate.navigate(R.id.action_createStudyFragment_StepThree_to_createStudyFragment_StepTwo);
                /*
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right,
                                R.anim.enter_from_right, R.anim.exit_to_left)
                        .replace(R.id.nav_host_fragment_create, createStudyFragment_stepTwo, null)
                        //.addToBackStack(null)
                        .setReorderingAllowed(true)
                        .commit();
                 */
                break;
            case 4:
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
                createStudyFragment_StepThree createStudyFragment_stepThree = new createStudyFragment_StepThree();

                String executionTypeCase4 = createStudyViewModel.studyCreationProcessData.get("executionType").toString();
                if (executionTypeCase4.equals(getString(R.string.remoteString))){
                    navControllerCreate.navigate(R.id.action_createStudyFragment_StepFour_Remote_to_createStudyFragment_StepThree);
                } else {
                    navControllerCreate.navigate(R.id.action_createStudyFragment_StepFour_Presence_to_createStudyFragment_StepThree);
                }
                /*
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right,
                                R.anim.enter_from_right, R.anim.exit_to_left)
                        .replace(R.id.nav_host_fragment_create, createStudyFragment_stepThree, null)
                        //.addToBackStack(null)
                        .setReorderingAllowed(true)
                        .commit();
                 */
                break;
            case 5:
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                String executionTypeCase5 = createStudyViewModel.studyCreationProcessData.get("executionType").toString();
                Log.d("executionType", "is: " + executionTypeCase5);
                if (executionTypeCase5.equals(getString(R.string.remoteString))) {
                    createStudyFragment_StepFour_Remote createStudyFragment_stepFour_remote = new createStudyFragment_StepFour_Remote();

                    navControllerCreate.navigate(R.id.action_createStudyFragment_StepFive_to_createStudyFragment_StepFour_Remote);
                    /*
                    fragmentManager
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right,
                                    R.anim.enter_from_right, R.anim.exit_to_left)
                            .replace(R.id.nav_host_fragment_create, createStudyFragment_stepFour_remote, null)
                            //.addToBackStack(null)
                            .setReorderingAllowed(true)
                            .commit();
                     */
                } else {
                    createStudyFragment_StepFour_Presence createStudyFragment_stepFour_presence = new createStudyFragment_StepFour_Presence();

                    navControllerCreate.navigate(R.id.action_createStudyFragment_StepFive_to_createStudyFragment_StepFour_Presence);
                    /*
                    fragmentManager
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right,
                                    R.anim.enter_from_right, R.anim.exit_to_left)
                            .replace(R.id.nav_host_fragment_create, createStudyFragment_stepFour_presence, null)
                            //.addToBackStack(null)
                            .setReorderingAllowed(true)
                            .commit();
                     */
                }
                break;
            case 6:
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR);
                createStudyFragment_StepFive createStudyFragment_stepFive = new createStudyFragment_StepFive();

                navControllerCreate.navigate(R.id.action_createStudyFragment_finalStep_to_createStudyFragment_StepFive);
                /*
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right,
                                R.anim.enter_from_right, R.anim.exit_to_left)
                        .replace(R.id.nav_host_fragment_create, createStudyFragment_stepFive, null)
                        //.addToBackStack(null)
                        .setReorderingAllowed(true)
                        .commit();
                 */
                break;
            case 7:
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR);
                createStudyFragment_finalStep createStudyFragment_finalStep = new createStudyFragment_finalStep();

                navControllerCreate.navigate(R.id.action_createStudyFragment_finalStep_two_to_createStudyFragment_finalStep);
                /*
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right,
                                R.anim.enter_from_right, R.anim.exit_to_left)
                        .replace(R.id.nav_host_fragment_create, createStudyFragment_finalStep, null)
                        //.addToBackStack(null)
                        .setReorderingAllowed(true)
                        .commit();
                 */
                break;
            case 8:
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FIVE);
                createStudyFragment_finalStep_two createStudyFragment_finalStep_two = new createStudyFragment_finalStep_two();
                nextBtn.setText(getString(R.string.fragment_create_study_base_next));
                nextBtn.setBackgroundColor(getResources().getColor(R.color.heatherred_dark));

                navControllerCreate.navigate(R.id.action_createStudyFragment_finalStep_three_to_createStudyFragment_finalStep_two);
                /*
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right,
                                R.anim.enter_from_right, R.anim.exit_to_left)
                        .replace(R.id.nav_host_fragment_create, createStudyFragment_finalStep_two, null)
                        //.addToBackStack(null)
                        .setReorderingAllowed(true)
                        .commit();
                 */
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
                break;
            case 2:
                if (createStudyViewModel.studyCreationProcessData.get("contact") != null) {
                    return true;
                }
                createStudyFragment_StepTwo.textInputEditTextContactMail.setError("Email-Kontakt muss angegeben werden");
                createStudyFragment_StepTwo.textInputEditTextContactMail.requestFocus();
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
                break;
            default:
                return false;
        }
        return false;
    }

    private void reNavigateToCreateStudyFragment() {
        navControllerMain.navigate(R.id.action_global_createStudyFragment);
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
