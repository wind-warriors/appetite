package com.windwarriors.appetite.model;


public class UserModel {
    public String Email;
    public String Password;

    public UserModel(String Email, String Password) {
        this.Email = Email;
        this.Password = Password;
    }

    public String getEmail() {
        return Email;
    }

    public String getPassword() {
        return Password;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public void setPassword(String password) {
        this.Password = password;
    }
}
