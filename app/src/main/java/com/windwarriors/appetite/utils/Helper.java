package com.windwarriors.appetite.utils;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.windwarriors.appetite.BusinessListRangeDialog;
import com.windwarriors.appetite.service.SharedPreferencesService;

import java.io.InputStream;
import java.net.URL;

import static com.windwarriors.appetite.utils.Constants.SHARED_PREFERENCES_DEFAULT_USERNAME;
import static com.windwarriors.appetite.utils.Constants.SHARED_PREFERENCES_USER_KEY;
import static com.windwarriors.appetite.utils.Constants.GREETING;

public final class Helper {

    // Set user greetings in the view indicated by textViewID. This might be used by many screens
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

    public static void OpenRangeDialog(AppCompatActivity context) {
        BusinessListRangeDialog rangeDialog = new BusinessListRangeDialog();
        rangeDialog.show(context.getSupportFragmentManager(), "Range Dialog");
    }

}
