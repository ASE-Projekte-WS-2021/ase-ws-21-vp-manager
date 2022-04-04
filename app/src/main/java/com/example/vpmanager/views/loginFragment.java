package com.example.vpmanager.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vpmanager.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class loginFragment extends Fragment {

    private TextInputEditText emailEdittext;
    private TextInputEditText passwordEditText;
    private Button forgotPasswordButton;
    private Button loginButton;
    private TextView registerView;

    private FirebaseAuth firebaseAuth;
    private NavController navController;


    public loginFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainActivity.currentFragment = "login";
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setupView(view);
        ((mainActivity) getActivity()).setDrawerLocked();
        navController = Navigation.findNavController(view);
        setOnClickListeners();
    }


    //Parameter:
    //Return values:
    //Connects the code with the view
    private void setupView(View view) {
        emailEdittext = view.findViewById(R.id.login_email_input);
        passwordEditText = view.findViewById(R.id.login_password_input);
        forgotPasswordButton = view.findViewById(R.id.login_forgotPassword_button);
        loginButton = view.findViewById(R.id.login_button);
        registerView = view.findViewById(R.id.already_have_an_account_clickable);
    }

    //Parameter:
    //Return values:
    //Sets clickListener on navigation items
    private void setOnClickListeners() {
        loginButton.setOnClickListener(view -> {
            loginUser();
        });

        forgotPasswordButton.setOnClickListener(view -> {
            forgotPassword();
        });

        registerView.setOnClickListener(view -> {
            navController.navigate(R.id.action_loginFragment_to_registerFragment);
        });
    }


    //Parameter:
    //Return values:
    //Handles the forgot password button press and sends a email to reset password
    private void forgotPassword() {
        String email = emailEdittext.getText().toString();

        if (TextUtils.isEmpty(email)) {
            emailEdittext.setError("Bitte Email-Adresse angeben um Passwort zurückzusetzen");
            emailEdittext.requestFocus();
        } else {
            firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(getActivity(), "Bitte überpürfen Sie ihr Email-Postfach", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    //Parameter:
    //Return values:
    //Handles login if the and provides feedback if the task was successful
    private void loginUser() {
        String email = emailEdittext.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        email.trim();
        if (TextUtils.isEmpty(email)) {
            emailEdittext.setError("Email kann nicht leer sein");
            emailEdittext.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Passwort kann nicht leer sein");
            passwordEditText.requestFocus();
        } else {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            System.out.println(user.isEmailVerified());
                            if (user.isEmailVerified()) {
                                mainActivity.uniqueID = email;
                                mainActivity.createUserId(getActivity());
                                ((mainActivity) getActivity()).setDrawerUnlocked();
                                navController.navigate(R.id.action_global_homeFragment);
                            } else {
                                firebaseAuth.signOut();
                                Toast.makeText(getActivity(), "Bitte verifiziere Sie zuerst Ihre Email Adresse", Toast.LENGTH_LONG).show();
                            }
                        }
                    } else {
                        System.out.println(task.getException().getMessage());
                        Toast.makeText(getActivity(), "Anmeldung fehlgeschlagen: " + translateError(task.getException().getMessage()), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }

    private String translateError(String error){
        String translatedError = "";
        if(error.startsWith("The password is invalid"))
        {
            translatedError = "Falsches Passwort oder es gibt keinen Account mit dieser Email-Adresse";
        }
        if(error.startsWith("The email address")){
            translatedError = "Keine gültige Email-Adresse";
        }
        if(error.startsWith("We have blocked all requests from this device due to unusual activity. Try again later")){
            translatedError = "Anmeldung wurde temporär blockiert aufgrund vieler fehlgeschlagenen Anmeldungsversuche";
        }
        return translatedError;
    }

}