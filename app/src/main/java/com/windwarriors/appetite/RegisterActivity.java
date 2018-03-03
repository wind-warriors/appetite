package com.windwarriors.appetite;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.windwarriors.appetite.model.UserModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import  com.google.gson.Gson;


public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void btnRegister_OnClick(View view) {

        final EditText etEmail = (EditText)findViewById(R.id.etEmail);
        EditText etPassword = (EditText)findViewById(R.id.etPassword);
        EditText etConfirmPassword = (EditText)findViewById(R.id.etConfirmPassword);

        String email = etEmail.getText().toString().toLowerCase().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etEmail.getText().toString().toLowerCase().trim();

        if(etEmail.getText().toString().trim().length() == 0 ||
                etPassword.getText().toString().trim().length() == 0 ||
                etConfirmPassword.getText().toString().trim().length() == 0){

            Toast.makeText(this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
            return;

        } else if (etPassword.getText().toString().trim().length() < 6) {

            Toast.makeText(this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
            return;

        } else if (!etPassword.getText().toString().trim().equals(etConfirmPassword.getText().toString().trim())) {

            Toast.makeText(this, "Password and Confirm Password must match", Toast.LENGTH_SHORT).show();
            return;

        }
        List<UserModel> spUsers = loadUsersFromSharedPreferences(this);
        
        if(spUsers == null){
            // Register User
            spUsers = new ArrayList<UserModel>();

            UserModel uModel = new UserModel(
                    etEmail.getText().toString().toLowerCase().trim(),
                    etPassword.getText().toString().trim());

            spUsers.add(uModel);

        } else {

            for (UserModel _user : spUsers) {
                if (_user.getEmail().equals(etEmail.getText().toString().trim())) {
                    Toast.makeText(this, "Email already registered", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            // Register User
            UserModel uModel = new UserModel(
                    etEmail.getText().toString().toLowerCase().trim(),
                    etPassword.getText().toString().trim());
            spUsers.add(uModel);

        }
        saveUsersToSharedPreferece(this, spUsers);
        Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show();

    }

    public static void saveUsersToSharedPreferece(Context context, List<UserModel> spUsers) {
        SharedPreferences mPrefs = context.getSharedPreferences("users", context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(spUsers);
        prefsEditor.putString("myJson", json);
        prefsEditor.commit();
    }

    public static List<UserModel> loadUsersFromSharedPreferences(Context context) {
        List<UserModel> spUsers = new ArrayList<UserModel>();
        
        SharedPreferences mPrefs = context.getSharedPreferences("users", context.MODE_PRIVATE);
        
        Gson gson = new Gson();
        
        String json = mPrefs.getString("myJson", "");
        
        if (json.isEmpty()) {
            return null;
        } else {
            Type type = new TypeToken<List<UserModel>>() {
                
            }.getType();
            spUsers = gson.fromJson(json, type);
        }
        return spUsers;
    }

}