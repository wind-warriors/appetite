package com.windwarriors.appetite;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameEditText = findViewById(R.id.input_username);
                passwordEditText = findViewById(R.id.input_password);
                String username = usernameEditText.getText().toString();
                String pasword = passwordEditText.getText().toString();

                String userId = userService.getCustomerId(username, pasword);
                if (userId != null) {
                    sharedPreferences.saveToSharedPreferences(SHARED_PREFERENCES_USER_KEY, username);
                    //Intent intent = new Intent(this, RestaurantsListActivity.class);
                    //startActivity(intent);

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

    public void onClicked(View view) {
        usernameEditText = findViewById(R.id.input_username);
        passwordEditText = findViewById(R.id.input_password);
        String username = usernameEditText.getText().toString();
        String pasword = passwordEditText.getText().toString();

        String userId = userService.getCustomerId(username, pasword);
        if (userId != null) {
            sharedPreferences.saveToSharedPreferences(SHARED_PREFERENCES_USER_KEY, this.usernameEditText.getText().toString());
            //Intent intent = new Intent(this, RestaurantsListActivity.class);
            //startActivity(intent);

        } else {
            Toast.makeText(LoginActivity.this, "Wrong username or passwordEditText", Toast.LENGTH_SHORT).show();
        }
    }

    public void onRegisterClicked(View view) {
        //Intent intent = new Intent(this, RegisterActivity.class);
        //startActivity(intent);
    }



}