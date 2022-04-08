package com.example.vpmanager.repositories;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.example.vpmanager.interfaces.FirebaseAuthCreateListener;
import com.example.vpmanager.interfaces.FirebaseAuthEmailListener;
import com.example.vpmanager.interfaces.FirebaseAuthResetListener;
import com.example.vpmanager.interfaces.LoginListener;
import com.example.vpmanager.interfaces.RegisterListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Transaction;

import java.util.HashMap;
import java.util.Map;

public class LoginRegisterRepository {

    private static LoginRegisterRepository instance;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db;

    private LoginListener loginListener;
    private RegisterListener registerListener;
    private FirebaseAuthCreateListener firebaseAuthCreateListener;
    private FirebaseAuthEmailListener firebaseAuthEmailListener;
    private FirebaseAuthResetListener firebaseAuthResetListener;


    //Parameter:
    //Return values: LogInRegisterRepository
    //Returns instance of the LogInRegisterRepository
    public static LoginRegisterRepository getInstance() {
        if (instance == null) {
            instance = new LoginRegisterRepository();
        }
        return instance;
    }


    //Parameter: associated Listeners
    //Return values:
    //Firestore callback; sets Listeners
    public void setFirestoreCallback(LoginListener loginListener, RegisterListener registerListener,
                                     FirebaseAuthCreateListener firebaseAuthCreateListener, FirebaseAuthEmailListener
                                             firebaseAuthEmailListener, FirebaseAuthResetListener firebaseAuthResetListener) {
        this.loginListener = loginListener;
        this.registerListener = registerListener;
        this.firebaseAuthCreateListener = firebaseAuthCreateListener;
        this.firebaseAuthEmailListener = firebaseAuthEmailListener;
        this.firebaseAuthResetListener = firebaseAuthResetListener;
    }

    public void registerNewUser(String email, String password) {

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                firebaseAuthCreateListener.onNewUserWithEmailAndPasswordFinished(null);
            } else {
                String localErrorMessage = task.getException().getMessage();
                firebaseAuthCreateListener.onNewUserWithEmailAndPasswordFinished(localErrorMessage);
            }
        });
    }


    //Parameter:
    //Return values:
    //Uses firebaseAuthEmailListener to send a verification mail to user
    public void sendVerificationEmail() {

        firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                firebaseAuthEmailListener.onSendEmailFinished(null);
            } else {
                String localErrorMessage = task.getException().getMessage();
                firebaseAuthEmailListener.onSendEmailFinished(localErrorMessage);
            }
        });
    }


    //Parameter: email, individual immatrikulation number of student, number of participation hours(vps)
    //Return values:
    //Saves existing user meta data (device id, marNr, vps) in the database
    public void saveUserInDb(String email, String matNr, String vph) {
        db = FirebaseFirestore.getInstance();
        //email is used for deviceId
        final DocumentReference userDocRef = db.collection("users").document(email);
        db.runTransaction((Transaction.Function<Void>) transaction -> {
            DocumentSnapshot snapshot = transaction.get(userDocRef);
            if (!snapshot.exists()) {
                Map<String, Object> user = new HashMap<>();
                user.put("deviceId", email);
                user.put("matrikelNumber", matNr);
                user.put("vps", vph);
                transaction.set(userDocRef, user);

                firebaseAuth.signOut();
            }
            return null;
        }).addOnSuccessListener(aVoid -> registerListener.onRegisterFinished(),
                aVoid -> Log.d(TAG, "Transaction success!"
                )).addOnFailureListener(e -> Log.w(TAG, "Transaction failure.", e));
    }


    //Parameter: email
    //Return values:
    //Sends a password reset mail to the user
    public void sendResetEmail(String email) {

        firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener(unused -> firebaseAuthResetListener.onResetEmailSent());
    }


    //Parameter: email, password
    //Return values:
    //Initializes the log in process, and sets error message when needed
    public void loginUser(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    if (user.isEmailVerified()) {
                        loginListener.createUserInMainActivity(email);
                    } else {
                        firebaseAuth.signOut();
                        String localErrorMessage = "Bitte verifizieren Sie zuerst Ihre Email Adresse";
                        loginListener.onLoginFailed(localErrorMessage);
                    }
                }
            } else {
                String localErrorMessage = task.getException().getMessage();
                loginListener.onLoginFailed(localErrorMessage);
            }
        });
    }
}
