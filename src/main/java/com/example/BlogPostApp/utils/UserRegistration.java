package com.example.BlogPostApp.utils;

import lombok.Data;

@Data
public class UserRegistration {

    private String username;
    private String password;
    private String passwordConfirmation;

    public UserRegistration() {
    }

    public UserRegistration(String username, String password, String passwordConfirmation) {
        this.username = username;
        this.password = password;
        this.passwordConfirmation = passwordConfirmation;
    }


}