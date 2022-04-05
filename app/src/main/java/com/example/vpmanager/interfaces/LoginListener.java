package com.example.vpmanager.interfaces;

public interface LoginListener {
    void onLoginFailed(String errorMessage);
    void createUserInMainActivity(String email);
}
