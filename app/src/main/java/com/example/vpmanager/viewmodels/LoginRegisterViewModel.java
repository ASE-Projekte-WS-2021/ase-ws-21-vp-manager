package com.example.vpmanager.viewmodels;

import androidx.lifecycle.ViewModel;

import com.example.vpmanager.interfaces.FirebaseAuthCreateListener;
import com.example.vpmanager.interfaces.FirebaseAuthEmailListener;
import com.example.vpmanager.interfaces.LoginListener;
import com.example.vpmanager.interfaces.RegisterListener;
import com.example.vpmanager.repositories.LoginRegisterRepository;
import com.example.vpmanager.views.LoginFragment;
import com.example.vpmanager.views.RegisterFragment;

public class LoginRegisterViewModel extends ViewModel implements LoginListener, RegisterListener,
        FirebaseAuthEmailListener, FirebaseAuthCreateListener {

    public LoginFragment loginFragment;
    public RegisterFragment registerFragment;
    private LoginRegisterRepository mRepo;

    public void prepareRepo() {
        mRepo = LoginRegisterRepository.getInstance();
        //Instance is the same but different data can be retrieved!
        mRepo.setFirestoreCallback(this, this, this, this);
    }

    public void registerNewUser(String email, String password) {
        mRepo.registerNewUser(email, password);
    }

    //can be private
    private void sendVerificationEmail(){
        mRepo.sendVerificationEmail();
    }

    public void saveUserInDb(String email, String matNr, String vph) {
        mRepo.saveUserInDb(email, matNr, vph);
    }

    @Override
    public void onLoginFinished() {

    }

    @Override
    public void onNewUserWithEmailAndPasswordFinished(String errorMessage) {
        if (errorMessage != null){
            registerFragment.showToast("Registrierung fehlgeschlagen: " + translateError(errorMessage));
        } else {
            sendVerificationEmail();
        }
    }

    @Override
    public void onSendEmailFinished(String errorMessage) {
        if (errorMessage != null){
            registerFragment.showToast("Registrierung fehlgeschlagen: " + translateError(errorMessage));
        } else {
            registerFragment.saveUserInDb();
        }
    }

    @Override
    public void onRegisterFinished() {
        registerFragment.navigateToLogin();
    }

    //helper to show an error message in german
    private String translateError(String error) {
        String translatedError = "";
        if (error.startsWith("The email address is already in use")) {
            translatedError = "Die Email-Adresse wird bereits von einem anderen Account verwendet";
        }
        if (error.startsWith("The email address is badly")) {
            translatedError = "Keine gültige Email-Adresse";
        }
        return translatedError;
    }

}
