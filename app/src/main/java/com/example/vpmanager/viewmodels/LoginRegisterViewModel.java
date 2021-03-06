package com.example.vpmanager.viewmodels;

import androidx.lifecycle.ViewModel;

import com.example.vpmanager.interfaces.FirebaseAuthCreateListener;
import com.example.vpmanager.interfaces.FirebaseAuthEmailListener;
import com.example.vpmanager.interfaces.FirebaseAuthResetListener;
import com.example.vpmanager.interfaces.LoginListener;
import com.example.vpmanager.interfaces.RegisterListener;
import com.example.vpmanager.repositories.LoginRegisterRepository;
import com.example.vpmanager.views.LoginFragment;
import com.example.vpmanager.views.RegisterFragment;

public class LoginRegisterViewModel extends ViewModel implements LoginListener, RegisterListener,
        FirebaseAuthEmailListener, FirebaseAuthCreateListener, FirebaseAuthResetListener {

    public LoginFragment loginFragment;
    public RegisterFragment registerFragment;
    private LoginRegisterRepository mRepo;


    //Parameter:
    //Return Values:
    //Gets instance of the associated Repository and sets the Firestore Callback
    public void prepareRepo() {
        mRepo = LoginRegisterRepository.getInstance();
        //Instance is the same but different data can be retrieved!
        mRepo.setFirestoreCallback(this, this, this, this, this);
    }


    public void registerNewUser(String email, String password) {
        mRepo.registerNewUser(email, password);
    }

    private void sendVerificationEmail() {
        mRepo.sendVerificationEmail();
    }

    public void saveUserInDb(String email, String matNr, String vph) {
        mRepo.saveUserInDb(email, matNr, vph);
    }

    public void resetPassword(String email) {
        mRepo.sendResetEmail(email);
    }

    public void login(String email, String password) {
        mRepo.loginUser(email, password);
    }

    @Override
    public void onLoginFailed(String errorMessage) {
        if (errorMessage.startsWith("Bitte verifizieren Sie")) {
            loginFragment.showToast(errorMessage);
        } else {
            String errorText = "Anmeldung fehlgeschlagen: " + translateErrorLogin(errorMessage);
            loginFragment.showToast(errorText);
        }
    }

    @Override
    public void createUserInMainActivity(String email) {
        loginFragment.createUserInMainActivity(email);
    }

    @Override
    public void onNewUserWithEmailAndPasswordFinished(String errorMessage) {
        if (errorMessage != null) {
            registerFragment.showToast("Registrierung fehlgeschlagen: " + translateErrorRegister(errorMessage));
        } else {
            sendVerificationEmail();
        }
    }

    @Override
    public void onSendEmailFinished(String errorMessage) {
        if (errorMessage != null) {
            registerFragment.showToast("Registrierung fehlgeschlagen: " + translateErrorRegister(errorMessage));
        } else {
            registerFragment.saveUserInDb();
        }
    }

    @Override
    public void onRegisterFinished() {
        registerFragment.navigateToLogin();
    }

    @Override
    public void onResetEmailSent() {
        String toastText = "Bitte ??berp??rfen Sie ihr Email-Postfach";
        loginFragment.showToast(toastText);
    }


    //Parameter: error
    //Return Values: String
    //helper to show register error messages in german
    private String translateErrorRegister(String error) {
        String translatedError = "";
        if (error.startsWith("The email address is already in use")) {
            translatedError = "Die Email-Adresse wird bereits von einem anderen Account verwendet";
        }
        if (error.startsWith("The email address is badly")) {
            translatedError = "Keine g??ltige Email-Adresse";
        }
        return translatedError;
    }


    //Parameter: error
    //Return Values: String
    //helper to show login error messages in german
    private String translateErrorLogin(String error) {
        String translatedError = "";
        if (error.startsWith("The password is invalid")) {
            translatedError = "Falsches Passwort oder es gibt keinen Account mit dieser Email-Adresse";
        }
        if (error.startsWith("The email address")) {
            translatedError = "Keine g??ltige Email-Adresse";
        }
        if (error.startsWith("We have blocked all requests from this device due to unusual activity. Try again later")) {
            translatedError = "Anmeldung wurde tempor??r blockiert aufgrund vieler fehlgeschlagenen Anmeldungsversuche";
        }
        return translatedError;
    }

}
