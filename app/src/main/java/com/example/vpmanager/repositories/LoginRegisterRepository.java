package com.example.vpmanager.repositories;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.vpmanager.interfaces.FirebaseAuthCreateListener;
import com.example.vpmanager.interfaces.FirebaseAuthEmailListener;
import com.example.vpmanager.interfaces.FirebaseAuthResetListener;
import com.example.vpmanager.interfaces.LoginListener;
import com.example.vpmanager.interfaces.RegisterListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    firebaseAuthCreateListener.onNewUserWithEmailAndPasswordFinished(null);
                } else {
                    String localErrorMessage = task.getException().getMessage();
                    firebaseAuthCreateListener.onNewUserWithEmailAndPasswordFinished(localErrorMessage);
                }
            }
        });
    }

    public void sendVerificationEmail() {

        firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    firebaseAuthEmailListener.onSendEmailFinished(null);
                } else {
                    String localErrorMessage = task.getException().getMessage();
                    firebaseAuthEmailListener.onSendEmailFinished(localErrorMessage);
                }
            }
        });
    }

    public void saveUserInDb(String email, String matNr, String vph) {
        db = FirebaseFirestore.getInstance();
        //deviceId is email now!
        final DocumentReference userDocRef = db.collection("users").document(email);
        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(userDocRef);
                if (!snapshot.exists()) {
                    Map<String, Object> user = new HashMap<>();
                    user.put("deviceId", email);
                    user.put("matrikelNumber", matNr);
                    user.put("vps", vph);
                    transaction.set(userDocRef, user);

                    //maybe to early?
                    firebaseAuth.signOut();
                }
                return null;
            }
        }).addOnSuccessListener(aVoid -> registerListener.onRegisterFinished(),
                aVoid -> Log.d(TAG, "Transaction success!"
                )).addOnFailureListener(e -> Log.w(TAG, "Transaction failure.", e));
    }

    public void sendResetEmail(String email) {

        firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                firebaseAuthResetListener.onResetEmailSent();
            }
        });
    }

    public void loginUser(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user != null) {
                        if (user.isEmailVerified()) {
                            loginListener.createUserInMainActivity(email);
                        } else {
                            firebaseAuth.signOut();
                            String localErrorMessage = "Bitte verifiziere Sie zuerst Ihre Email Adresse";
                            loginListener.onLoginFailed(localErrorMessage);
                        }
                    }
                } else {
                    String localErrorMessage = task.getException().getMessage();
                    loginListener.onLoginFailed(localErrorMessage);
                }
            }
        });
    }
}
