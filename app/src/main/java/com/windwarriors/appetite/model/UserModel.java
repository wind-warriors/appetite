package com.windwarriors.appetite.model;


import com.yelp.fusion.client.models.User;

public class UserModel {
    public String Username;
    public String Email;
    public String Password;

    public UserModel() {
    }

    public UserModel(String Username, String Email, String Password) {
        this.Username = Username;
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

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }
}
