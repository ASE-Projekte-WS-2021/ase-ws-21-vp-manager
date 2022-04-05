package com.example.vpmanager.views;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.vpmanager.R;
import com.example.vpmanager.viewmodels.LoginRegisterViewModel;
import com.google.android.material.textfield.TextInputEditText;

// Custom Dialog Src: https://github.com/subhojitdp/Custom-Dialog

public class RegisterFragment extends Fragment {

    private TextInputEditText emailEdittext;
    private TextInputEditText passwordEditText;
    private TextInputEditText passwordConfirmEditText;
    private TextInputEditText matnrEditText;
    private TextInputEditText vpEditText;

    private Button registerButton;
    private TextView toLoginTextView;
    private ImageView infoButton;

    private LoginRegisterViewModel mViewModel;
    private NavController navController;

    private String eMail, matNr, finalNeededVP;

    public RegisterFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainActivity.currentFragment = "register";
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        prepareViewModel();
        setupView(view);
        //register process starts when user clicks
        setOnClickListeners();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);
        ((mainActivity) getActivity()).setDrawerLocked();
    }

    private void prepareViewModel() {
        //mainActivity should be viewModelStoreOwner
        mViewModel = new ViewModelProvider(requireActivity()).get(LoginRegisterViewModel.class);
        mViewModel.registerFragment = this;
        mViewModel.prepareRepo();
    }

    //Parameter:
    //Return values:
    //Connects the code with the view
    private void setupView(View view) {
        emailEdittext = view.findViewById(R.id.login_email_input);
        passwordEditText = view.findViewById(R.id.login_password_input);
        matnrEditText = view.findViewById(R.id.register_matnr);
        vpEditText = view.findViewById(R.id.register_vp);
        registerButton = view.findViewById(R.id.register_button);
        toLoginTextView = view.findViewById(R.id.already_have_an_account_clickable);
        passwordConfirmEditText = view.findViewById(R.id.confirm_login_password_input);
        infoButton = view.findViewById(R.id.register_informationButton);
    }

    //Parameter:
    //Return values:
    //Sets clickListener on navigation items
    private void setOnClickListeners() {
        registerButton.setOnClickListener(view -> {
            createUser();
        });
        toLoginTextView.setOnClickListener(view -> {
            navController.navigate(R.id.action_registerFragment_to_loginFragment);
        });
        infoButton.setOnClickListener(view -> {
            showToolTip();
        });
    }

    private void showToolTip() {
        Dialog dialog = new Dialog(getActivity(), R.style.DialogStyle);
        dialog.setContentView(R.layout.info_dialog);

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_window);

        ImageView btnClose = dialog.findViewById(R.id.btn_close);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        TextView title = dialog.findViewById(R.id.info_title);
        TextView desc = dialog.findViewById(R.id.info_desc);
        title.setText(getString(R.string.register_info_title));
        desc.setText(getString(R.string.register_info_desc));

        dialog.show();
    }

    //Parameter:
    //Return values:
    //Handles account creation if the and provides feedback if the task was successful
    private void createUser() {
        eMail = emailEdittext.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = passwordConfirmEditText.getText().toString().trim();
        matNr = matnrEditText.getText().toString().trim();
        String neededVP = vpEditText.getText().toString().trim();
        if (TextUtils.isEmpty(neededVP)) {
            neededVP = "15";
        }
        if (TextUtils.isEmpty(eMail)) {
            emailEdittext.setError("Email kann nicht leer sein");
            emailEdittext.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Passwort kann nicht leer sein");
            passwordEditText.requestFocus();
        } else if (!password.equals(confirmPassword)) {
            passwordConfirmEditText.setError("Passwörter müssen übereinstimmen");
            passwordConfirmEditText.requestFocus();
        } else {
            finalNeededVP = neededVP;

            //start of register chain
            mViewModel.registerNewUser(eMail, password);

            /*
            firebaseAuth.createUserWithEmailAndPassword(eMail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    //registerNewUser(eMail, matNr, finalNeededVP);
                                    //firebaseAuth.signOut();
                                    Toast.makeText(getActivity(), "Registierung erfolgreich, bitte überprüfe deine Email-Postfach", Toast.LENGTH_LONG).show();
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            navController.navigate(R.id.action_registerFragment_to_loginFragment);
                                        }
                                    },1000);
                                } else {
                                    Toast.makeText(getActivity(), "Registierung fehlgeschlagen: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getActivity(), "Registierung fehlgeschlagen: " + translateError(task.getException().getMessage()), Toast.LENGTH_LONG).show();
                    }
                }
            });

             */
        }
    }

    public void saveUserInDb() {
        mViewModel.saveUserInDb(eMail, matNr, finalNeededVP);
    }

    public void showToast(String errorText) {
        Toast.makeText(requireActivity(), errorText, Toast.LENGTH_LONG).show();
    }

    public void navigateToLogin() {
        String toastString = "Registierung erfolgreich, bitte überprüfe deine Email-Postfach";
        showToast(toastString);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                navController.navigate(R.id.action_registerFragment_to_loginFragment);
            }
        }, 1000);
    }

    /*
    private void registerNewUser(String email, String matNr, String neededVP) {
        accessDatabase.createNewUser(email, matNr, neededVP);
    }
     */
}