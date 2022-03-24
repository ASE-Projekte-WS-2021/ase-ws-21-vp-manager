package com.example.vpmanager.views;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.vpmanager.AccessDatabase;
import com.example.vpmanager.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class registerFragment extends Fragment {

    private TextInputEditText emailEdittext;
    private TextInputEditText passwordEditText;
    private TextInputEditText passwordConfirmEditText;
    private TextInputEditText matnrEditText;
    private TextInputEditText vpEditText;
    private Button registerButton;
    private TextView toLoginTextView;

    private FirebaseAuth firebaseAuth;
    private NavController navController;
    private AccessDatabase accessDatabase;

    public registerFragment() {
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

        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setupView(view);
        accessDatabase = new AccessDatabase();
        ((mainActivity)getActivity()).setDrawerLocked();
        navController = Navigation.findNavController(view);
        setOnClickListeners();
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
    }

    private void registerNewUser(String email, String matNr, String neededVP) {
        accessDatabase.createNewUser(email, matNr, neededVP);
    }


    //Parameter:
    //Return values:
    //Handles account creation if the and provides feedback if the task was successful
    private void createUser() {
        String email = emailEdittext.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = passwordConfirmEditText.getText().toString();
        String matNr = matnrEditText.getText().toString();
        String neededVP = vpEditText.getText().toString();
        if(TextUtils.isEmpty(neededVP)){
            neededVP = "15";
        }
        if (TextUtils.isEmpty(email)) {
            emailEdittext.setError("Email kann nicht leer sein");
            emailEdittext.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Passwort kann nicht leer sein");
            passwordEditText.requestFocus();
        } else if (!password.equals(confirmPassword)) {
            passwordConfirmEditText.setError("Passwörter müssen übereinstimmen");
            passwordConfirmEditText.requestFocus();
        } else {
            String finalNeededVP = neededVP;
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    registerNewUser(email, matNr, finalNeededVP);
                                    Toast.makeText(getActivity(), "Registierung erfolgreich, bitte überprüfe deine Email-Postfach", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity(), "Registierung fehlgeschlagen: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getActivity(), "Registierung fehlgeschlagen: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}