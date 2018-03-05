package com.windwarriors.appetite.utils;


import android.app.Activity;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.windwarriors.appetite.service.SharedPreferencesService;

import java.io.InputStream;
import java.net.URL;

import static com.windwarriors.appetite.utils.Constants.SHARED_PREFERENCES_DEFAULT_USERNAME;
import static com.windwarriors.appetite.utils.Constants.SHARED_PREFERENCES_USER_KEY;
import static com.windwarriors.appetite.utils.Constants.GREETING;

public final class Helper {

    public static String setUserGreetingTextView(Activity context, int textViewId) {
        SharedPreferencesService preferences = new SharedPreferencesService(context);
        String username = preferences.getFromSharedPreferences(SHARED_PREFERENCES_USER_KEY);
        if( username.trim().length() == 0){
            username = SHARED_PREFERENCES_DEFAULT_USERNAME;
        }
        TextView loggedInUserGreeting = context.findViewById(textViewId);
        String greeting = GREETING + username;
        loggedInUserGreeting.setText(greeting);
        loggedInUserGreeting.setTypeface(Typeface.DEFAULT_BOLD);
        return username;
    }


}
