package com.example.vpmanager.views.createStudy;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.airbnb.lottie.LottieAnimationView;
import com.example.vpmanager.Config;
import com.example.vpmanager.R;
import com.example.vpmanager.viewmodels.CreateStudyViewModel;
import com.kofigyan.stateprogressbar.StateProgressBar;


public class CreateStudyFragment extends Fragment {


    public static CreateStudyViewModel createStudyViewModel;
    public static int currentFragment = Config.createFragmentBase;

    private NavController navControllerMain;
    private NavHostFragment navHostFragmentCreate;
    private NavController navControllerCreate;

    private Button backBtn;
    private Button nextBtn;
    private StateProgressBar stateProgressBar;
    private String stepOne, stepTwo, stepThree, stepFour, stepFive;
    private String[] progressBarDescriptionData;
    private LottieAnimationView doneAnimation;


    //Class constructor
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
        navControllerCreate.navigate(R.id.action_global_createStudyFragment_Base);
    }


    //Parameter:
    //Return values:
    //Set up navigation for the the create study activity
    private void setupNavigation() {
        navHostFragmentCreate = (NavHostFragment) getChildFragmentManager().findFragmentById(R.id.nav_host_fragment_create);
        assert navHostFragmentCreate != null;
        navControllerCreate = navHostFragmentCreate.getNavController();
    }


    //Parameter: View
    //Return values:
    //Sets views  for current fragment; loads the  layout elements from associated resource files
    private void setupView(View view) {

        stepOne = getString(R.string.progressbar_one);
        stepTwo = getString(R.string.progressbar_two);
        stepThree = getString(R.string.progressbar_three);
        stepFour = getString(R.string.progressbar_four);
        stepFive = getString(R.string.progressbar_five);
        progressBarDescriptionData = new String[]{stepOne, stepTwo, stepThree, stepFour, stepFive};

        //Fragment needs to be owner. Otherwise the data in the input fields will stay!
        createStudyViewModel = new ViewModelProvider(CreateStudyFragment.this).get(CreateStudyViewModel.class);
        createStudyViewModel.prepareRepo();
        createStudyViewModel.createStudyFragment = this;

        createStudyViewModel.studyCreationProcessData.clear();
        createStudyViewModel.datesCreationProcessData.clear();
        CreateStudyFragment_StepFive.dates.clear();

        backBtn = view.findViewById(R.id.backButton);
        nextBtn = view.findViewById(R.id.nextButton);

        stateProgressBar = (StateProgressBar) view.findViewById(R.id.your_state_progress_bar_id);
        stateProgressBar.setStateDescriptionData(progressBarDescriptionData);
        stateProgressBar.setVisibility(View.INVISIBLE);

        doneAnimation = view.findViewById(R.id.animationView);
    }


    //Parameter:
    //Return values:
    //Sets listeners for animations and buttons
    private void setupListeners() {
        doneAnimation.addAnimatorListener(
                new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        reNavigateToHomeStudyFragment();
                    }
                }
        );
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextButton();
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backButton();
            }
        });
    }


    //Parameter:
    //Return values:
    //Sets navigation for button leading to next fragment
    private void nextButton() {
        switch (currentFragment) {
            case 0:
                CreateStudyFragment_StepOne createStudyFragment_stepOne = new CreateStudyFragment_StepOne();
                stateProgressBar.setVisibility(View.VISIBLE);
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);

                navControllerCreate.navigate(R.id.action_createStudyFragment_Base_to_createStudyFragment_StepOne);

                break;
            case 1:
                saveDataInViewModelStepOne();

                if (mandatoryCheck(currentFragment)) {
                    CreateStudyFragment_StepTwo createStudyFragment_stepTwo = new CreateStudyFragment_StepTwo();
                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);

                    navControllerCreate.navigate(R.id.action_createStudyFragment_StepOne_to_createStudyFragment_StepTwo);

                }
                break;
            case 2:
                saveDataInViewModelStepTwo();
                if (mandatoryCheck(currentFragment)) {
                    CreateStudyFragment_StepThree createStudyFragment_stepThree = new CreateStudyFragment_StepThree();
                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);

                    navControllerCreate.navigate(R.id.action_createStudyFragment_StepTwo_to_createStudyFragment_StepThree);

                }
                break;
            case 3:
                saveDataInViewModelStepThree();
                if (mandatoryCheck(currentFragment)) {
                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                    String executionType = createStudyViewModel.studyCreationProcessData.get("executionType").toString();

                    if (executionType.equals(getString(R.string.remoteString))) {
                        CreateStudyFragment_StepFour_Remote createStudyFragment_stepFour_remote = new CreateStudyFragment_StepFour_Remote();

                        navControllerCreate.navigate(R.id.action_createStudyFragment_StepThree_to_createStudyFragment_StepFour_Remote);

                    } else {
                        CreateStudyFragment_StepFour_Presence createStudyFragment_stepFour_presence = new CreateStudyFragment_StepFour_Presence();

                        navControllerCreate.navigate(R.id.action_createStudyFragment_StepThree_to_createStudyFragment_StepFour_Presence);

                    }
                }
                break;
            case 4:
                saveDataInViewModelStepFour();
                if (mandatoryCheck(currentFragment)) {
                    CreateStudyFragment_StepFive createStudyFragment_stepFive = new CreateStudyFragment_StepFive();
                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR);

                    String executionType = createStudyViewModel.studyCreationProcessData.get("executionType").toString();
                    if (executionType.equals(getString(R.string.remoteString))) {

                        navControllerCreate.navigate(R.id.action_createStudyFragment_StepFour_Remote_to_createStudyFragment_StepFive);
                    } else {

                        navControllerCreate.navigate(R.id.action_createStudyFragment_StepFour_Presence_to_createStudyFragment_StepFive);

                    }
                }
                break;
            case 5:
                saveDataInViewModelStepFive();
                CreateStudyFragment_finalStep createStudyFragment_finalStep = new CreateStudyFragment_finalStep();
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FIVE);

                navControllerCreate.navigate(R.id.action_createStudyFragment_StepFive_to_createStudyFragment_finalStep);

                break;
            case 6:
                CreateStudyFragment_finalStep_two createStudyFragment_finalStep_two = new CreateStudyFragment_finalStep_two();
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FIVE);

                navControllerCreate.navigate(R.id.action_createStudyFragment_finalStep_to_createStudyFragment_finalStep_two);

                break;
            case 7:
                CreateStudyFragment_finalStep_three createStudyFragment_finalStep_three = new CreateStudyFragment_finalStep_three();
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FIVE);
                nextBtn.setText(getString(R.string.fragment_create_study_base_create));
                nextBtn.setBackgroundColor(getResources().getColor(R.color.green_Main));

                navControllerCreate.navigate(R.id.action_createStudyFragment_finalStep_two_to_createStudyFragment_finalStep_three);

                break;
            case 8:
                createStudyViewModel.saveStudyInDb();
                playAnimation();
                break;
            default:
                break;
        }
    }

    //Parameter:
    //Return values:
    //Sets navigation for button leading to previous fragment
    private void backButton() {
        switch (currentFragment) {
            case 0:
                navControllerMain.navigate(R.id.action_global_homeFragment);
                break;
            case 1:
                saveDataInViewModelStepOne();
                stateProgressBar.setVisibility(View.INVISIBLE);
                CreateStudyFragment_Base createStudyFragment_base = new CreateStudyFragment_Base();

                navControllerCreate.navigate(R.id.action_createStudyFragment_StepOne_to_createStudyFragment_Base);

                break;
            case 2:
                saveDataInViewModelStepTwo();
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
                CreateStudyFragment_StepOne createStudyFragment_stepOne = new CreateStudyFragment_StepOne();


                navControllerCreate.navigate(R.id.action_createStudyFragment_StepTwo_to_createStudyFragment_StepOne);

                break;
            case 3:
                saveDataInViewModelStepThree();
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
                CreateStudyFragment_StepTwo createStudyFragment_stepTwo = new CreateStudyFragment_StepTwo();

                navControllerCreate.navigate(R.id.action_createStudyFragment_StepThree_to_createStudyFragment_StepTwo);

                break;
            case 4:
                saveDataInViewModelStepFour();
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
                CreateStudyFragment_StepThree createStudyFragment_stepThree = new CreateStudyFragment_StepThree();

                String executionTypeCase4 = createStudyViewModel.studyCreationProcessData.get("executionType").toString();
                if (executionTypeCase4.equals(getString(R.string.remoteString))) {


                    navControllerCreate.navigate(R.id.action_createStudyFragment_StepFour_Remote_to_createStudyFragment_StepThree);
                } else {


                    navControllerCreate.navigate(R.id.action_createStudyFragment_StepFour_Presence_to_createStudyFragment_StepThree);
                }

                break;
            case 5:
                saveDataInViewModelStepFive();
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                String executionTypeCase5 = createStudyViewModel.studyCreationProcessData.get("executionType").toString();

                if (executionTypeCase5.equals(getString(R.string.remoteString))) {
                    CreateStudyFragment_StepFour_Remote createStudyFragment_stepFour_remote = new CreateStudyFragment_StepFour_Remote();

                    navControllerCreate.navigate(R.id.action_createStudyFragment_StepFive_to_createStudyFragment_StepFour_Remote);

                } else {
                    CreateStudyFragment_StepFour_Presence createStudyFragment_stepFour_presence = new CreateStudyFragment_StepFour_Presence();

                    navControllerCreate.navigate(R.id.action_createStudyFragment_StepFive_to_createStudyFragment_StepFour_Presence);

                }

                break;
            case 6:
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR);
                CreateStudyFragment_StepFive createStudyFragment_stepFive = new CreateStudyFragment_StepFive();

                navControllerCreate.navigate(R.id.action_createStudyFragment_finalStep_to_createStudyFragment_StepFive);

                break;
            case 7:
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR);
                CreateStudyFragment_finalStep createStudyFragment_finalStep = new CreateStudyFragment_finalStep();

                navControllerCreate.navigate(R.id.action_createStudyFragment_finalStep_two_to_createStudyFragment_finalStep);

                break;
            case 8:
                stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FIVE);
                CreateStudyFragment_finalStep_two createStudyFragment_finalStep_two = new CreateStudyFragment_finalStep_two();
                nextBtn.setText(getString(R.string.fragment_create_study_base_next));
                nextBtn.setBackgroundColor(getResources().getColor(R.color.heatherred_Main));

                navControllerCreate.navigate(R.id.action_createStudyFragment_finalStep_three_to_createStudyFragment_finalStep_two);

                break;
            default:
                break;
        }
    }


    //Parameter:
    //Return values:
    //Loads data from firebase and stores associated values in Strings, following methods do the same for the next fragments
    private void saveDataInViewModelStepOne() {
        String name = CreateStudyFragment_StepOne.textInputEditTextTitle.getText().toString().trim();
        if (!name.isEmpty()) {
            createStudyViewModel.studyCreationProcessData.put("name", name);
        }
        String vps = CreateStudyFragment_StepOne.textInputEditTextVP.getText().toString().trim();
        if (!vps.isEmpty()) {
            createStudyViewModel.studyCreationProcessData.put("vps", vps);
        } else {
            createStudyViewModel.studyCreationProcessData.put("vps", "0");
        }

        String category = CreateStudyFragment_StepOne.autoCompleteTextViewCategory.getText().toString().trim();

        if (!category.isEmpty()) {
            createStudyViewModel.studyCreationProcessData.put("category", category);
        }

        String executionType = CreateStudyFragment_StepOne.autoCompleteTextViewExecutionType.getText().toString().trim();

        if (!executionType.isEmpty()) {
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


    //Parameter:
    //Return values:
    //Loads data from firebase and stores associated values in Strings
    private void saveDataInViewModelStepTwo() {
        String mail = CreateStudyFragment_StepTwo.textInputEditTextContactMail.getText().toString().trim();
        if (!mail.isEmpty()) {
            createStudyViewModel.studyCreationProcessData.put("contact", mail);
        }
        String phone = CreateStudyFragment_StepTwo.textInputEditTextContactPhone.getText().toString().trim();
        if (!phone.isEmpty()) {
            createStudyViewModel.studyCreationProcessData.put("contact2", phone);
        }
        String skype = CreateStudyFragment_StepTwo.textInputEditTextContactSkype.getText().toString().trim();
        if (!skype.isEmpty()) {
            createStudyViewModel.studyCreationProcessData.put("contact3", skype);
        }
        String discord = CreateStudyFragment_StepTwo.textInputEditTextContactDiscord.getText().toString().trim();
        if (!discord.isEmpty()) {
            createStudyViewModel.studyCreationProcessData.put("contact4", discord);
        }
        String other = CreateStudyFragment_StepTwo.textInputEditTextContactOthers.getText().toString().trim();
        if (!other.isEmpty()) {
            createStudyViewModel.studyCreationProcessData.put("contact5", other);
        }
    }

    //Parameter:
    //Return values:
    //Loads data from firebase stores associated value in String
    private void saveDataInViewModelStepThree() {
        String description = CreateStudyFragment_StepThree.textInputEditTextDesc.getText().toString().trim();
        if (!description.isEmpty()) {
            createStudyViewModel.studyCreationProcessData.put("description", description);
        }
    }


    //Parameter:
    //Return values:
    //Loads data from firebase and stores associated values in Strings
    private void saveDataInViewModelStepFour() {
        String executionType = createStudyViewModel.studyCreationProcessData.get("executionType").toString();
        if (executionType.equals(getString(R.string.remoteString))) {
            String platform = CreateStudyFragment_StepFour_Remote.textInputEditTextPlatform.getText().toString().trim();
            if (!platform.isEmpty()) {
                createStudyViewModel.studyCreationProcessData.put("platform", platform);
            }
            String platform2 = CreateStudyFragment_StepFour_Remote.textInputEditTextOptionalPlatform.getText().toString().trim();
            if (!platform2.isEmpty()) {
                createStudyViewModel.studyCreationProcessData.put("platform2", platform2);
            }
        } else {
            String location = CreateStudyFragment_StepFour_Presence.textInputEditTextLocation.getText().toString().trim();
            if (!location.isEmpty()) {
                createStudyViewModel.studyCreationProcessData.put("location", location);
            }
            String street = CreateStudyFragment_StepFour_Presence.textInputEditTextStreet.getText().toString().trim();
            if (!street.isEmpty()) {
                createStudyViewModel.studyCreationProcessData.put("street", street);
            }
            String room = CreateStudyFragment_StepFour_Presence.textInputEditTextRoom.getText().toString().trim();
            if (!room.isEmpty()) {
                createStudyViewModel.studyCreationProcessData.put("room", room);
            }
        }
    }


    //Parameter:
    //Return values:
    //Stores date data in the ViewModel Arraylist
    private void saveDataInViewModelStepFive() {
        if (!CreateStudyFragment_StepFive.dates.isEmpty()) {
            createStudyViewModel.datesCreationProcessData = CreateStudyFragment_StepFive.dates;
        }
    }


    //Parameter:
    //Return values:
    //Check if mandatory text fields in fragments are empty; sets error message if null
    private boolean mandatoryCheck(int page) {
        switch (page) {
            case 1:
                if (createStudyViewModel.studyCreationProcessData.get("name") != null &&
                        createStudyViewModel.studyCreationProcessData.get("category") != null &&
                        createStudyViewModel.studyCreationProcessData.get("executionType") != null) {
                    return true;
                }
                if (createStudyViewModel.studyCreationProcessData.get("name") == null) {
                    CreateStudyFragment_StepOne.textInputEditTextTitle.setError(getString(R.string.titleError));
                    CreateStudyFragment_StepOne.textInputEditTextTitle.requestFocus();

                    break;
                }
                if (createStudyViewModel.studyCreationProcessData.get("category") == null) {
                    CreateStudyFragment_StepOne.autoCompleteTextViewCategory.setFocusable(true);
                    CreateStudyFragment_StepOne.autoCompleteTextViewCategory.requestFocus();
                    CreateStudyFragment_StepOne.autoCompleteTextViewCategory.showDropDown();

                    break;
                }
                if (createStudyViewModel.studyCreationProcessData.get("executionType") == null) {
                    CreateStudyFragment_StepOne.autoCompleteTextViewExecutionType.setFocusable(true);
                    CreateStudyFragment_StepOne.autoCompleteTextViewExecutionType.requestFocus();
                    CreateStudyFragment_StepOne.autoCompleteTextViewExecutionType.showDropDown();

                    break;
                }
                break;
            case 2:
                if (createStudyViewModel.studyCreationProcessData.get("contact") != null) {
                    return true;
                }
                CreateStudyFragment_StepTwo.textInputEditTextContactMail.setError(getString(R.string.mailError));
                CreateStudyFragment_StepTwo.textInputEditTextContactMail.requestFocus();
                break;
            case 3:
                if (createStudyViewModel.studyCreationProcessData.get("description") != null) {
                    return true;
                }
                CreateStudyFragment_StepThree.textInputEditTextDesc.setError(getString(R.string.descriptionError));
                CreateStudyFragment_StepThree.textInputEditTextDesc.requestFocus();
                break;
            case 4:
                if (createStudyViewModel.studyCreationProcessData.get("location") != null &&
                        createStudyViewModel.studyCreationProcessData.get("street") != null &&
                        createStudyViewModel.studyCreationProcessData.get("room") != null) {
                    return true;
                }
                String executionType = createStudyViewModel.studyCreationProcessData.get("executionType").toString();
                if (executionType.equals(getString(R.string.presenceString))) {
                    CreateStudyFragment_StepFour_Presence.textInputEditTextLocation
                            .setError(getString(R.string.locationError));
                    CreateStudyFragment_StepFour_Presence.textInputEditTextRoom
                            .setError(getString(R.string.roomError));
                    CreateStudyFragment_StepFour_Presence.textInputEditTextStreet
                            .setError(getString(R.string.streetError));

                    if (createStudyViewModel.studyCreationProcessData.get("location") == null)
                        CreateStudyFragment_StepFour_Presence.textInputEditTextLocation.requestFocus();
                    else if (createStudyViewModel.studyCreationProcessData.get("street") == null)
                        CreateStudyFragment_StepFour_Presence.textInputEditTextStreet.requestFocus();
                    else if (createStudyViewModel.studyCreationProcessData.get("room") == null)
                        CreateStudyFragment_StepFour_Presence.textInputEditTextRoom.requestFocus();
                }
                if (createStudyViewModel.studyCreationProcessData.get("platform") != null) {
                    return true;
                }
                if (executionType.equals(getString(R.string.remoteString))) {
                    CreateStudyFragment_StepFour_Remote.textInputEditTextPlatform
                            .setError(getString(R.string.platformError));
                    CreateStudyFragment_StepFour_Remote.textInputEditTextPlatform.requestFocus();
                }
                break;
            default:
                return false;
        }
        return false;
    }


    //Parameter:
    //Return values:
    //Sets navigation to home fragment
    private void reNavigateToHomeStudyFragment() {
        navControllerMain.navigate(R.id.action_global_homeFragment);
    }


    //Parameter:
    //Return values:
    //Sets animation properties
    public void playAnimation() {
        doneAnimation.setVisibility(LottieAnimationView.VISIBLE);
        doneAnimation.setProgress(0);
        doneAnimation.pauseAnimation();
        doneAnimation.playAnimation();
    }


}
