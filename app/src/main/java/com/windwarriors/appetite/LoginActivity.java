package com.windwarriors.appetite;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.windwarriors.appetite.service.SharedPreferencesService;
import com.windwarriors.appetite.service.UserService;


import static com.windwarriors.appetite.utils.Constants.SHARED_PREFERENCES_USER_KEY;

public class LoginActivity extends AppCompatActivity {

    private UserService userService;
    private SharedPreferencesService sharedPreferences;
    EditText usernameEditText;
    EditText passwordEditText;
    Button loginButton;
    TextView signupLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize User Service
        userService = new UserService(this);
        sharedPreferences = new SharedPreferencesService(this);

        loginButton = findViewById(R.id.btn_login);
        signupLink = findViewById(R.id.link_signup);
        final Context that = this;

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                usernameEditText = findViewById(R.id.input_username);
                passwordEditText = findViewById(R.id.input_password);
                String username = usernameEditText.getText().toString();
                String pasword = passwordEditText.getText().toString();

                String userId = userService.getCustomerId(username, pasword);
                if (userId != null) {
                    sharedPreferences.saveToSharedPreferences(SHARED_PREFERENCES_USER_KEY, username);
                    Intent intent = new Intent(that, BusinessListActivity.class);
                    startActivity(intent);
                    //Toast.makeText(LoginActivity.this, "Success!", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(LoginActivity.this, "Wrong username or passwordEditText", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(this, RegistrationActivity.class);
                //startActivity(intent);

            }
        });
    }


    @Override
    protected void onDestroy() {
        userService.close();
        super.onDestroy();
    }

    // PRIVATE METHODS

    // To hide keboard after user input data
    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }



}