package com.example.vpmanager.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.vpmanager.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class loginFragment extends Fragment {


    TextInputEditText emailEdittext;
    TextInputEditText passwordEditText;
    CheckBox rememberMeCheckBox;
    Button forgotPasswordButton;
    Button loginButton;
    Button registerButton;

    FirebaseAuth firebaseAuth;


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
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setupView(view);
    }

    @Override
    public void onStart(){
        super.onStart();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user == null) {
        // von Main to Login !!
        }
    }

    private void setupView(View view){
        emailEdittext = view.findViewById(R.id.login_email_input);
        passwordEditText = view.findViewById(R.id.login_password_input);
        rememberMeCheckBox = view.findViewById(R.id.login_rememberMe_checkbox);
        forgotPasswordButton = view.findViewById(R.id.login_forgotPassword_button);
        loginButton = view.findViewById(R.id.login_button);
        registerButton = view.findViewById(R.id.register_button);
    }

    private void setOnClickListeners(){
        registerButton.setOnClickListener(view -> {
            createUser();
        });

        loginButton.setOnClickListener(view -> {
            loginUser();
        });
    }

    private void loginUser() {
        String email = emailEdittext.getText().toString();
        String password = passwordEditText.getText().toString();

        if(TextUtils.isEmpty(email)){
            emailEdittext.setError("Email kann nicht leer sein");
            emailEdittext.requestFocus();
        } else if(TextUtils.isEmpty(password)){
            passwordEditText.setError("Passwort kann nicht leer sein");
            passwordEditText.requestFocus();
        }
        else{
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        homeFragment homeFragment = new homeFragment();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.nav_host_fragment_main, homeFragment, "homeFrag")
                                .addToBackStack(null)
                                .commit();
                    } else {
                        Toast.makeText(getActivity(), "Anmeldung fehlgeschlagen: " + task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void createUser() {
        String email = emailEdittext.getText().toString();
        String password = passwordEditText.getText().toString();

        if(TextUtils.isEmpty(email)){
            emailEdittext.setError("Email kann nicht leer sein");
            emailEdittext.requestFocus();
        } else if(TextUtils.isEmpty(password)){
            passwordEditText.setError("Passwort kann nicht leer sein");
            passwordEditText.requestFocus();
        } else{
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getActivity(), "Registierung erfolgreich", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Registierung fehlgeschlagen: " + task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }




}