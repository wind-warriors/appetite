package com.windwarriors.appetite.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.windwarriors.appetite.utils.Constants;

import static android.content.Context.MODE_PRIVATE;
import static com.windwarriors.appetite.utils.Constants.SHARED_PREFERENCES_STORE;

public class SharedPreferencesService {

    private Context context;

    public SharedPreferencesService(Context context) {
        this.context = context;
    }

    // Public static methods to be used for all classes in the project
    public void saveToSharedPreferences(String key, String value) {
        SharedPreferences myPreference = context.getSharedPreferences(SHARED_PREFERENCES_STORE, 0);
        SharedPreferences.Editor prefEditor = myPreference.edit();
        prefEditor.putString(key, value);
        //prefEditor.commit(); // this writes to persistent storage inmediately
        prefEditor.apply(); // this will handle writing in the background
    }

    public String getFromSharedPreferences( String key) {
        SharedPreferences myPref = context.getSharedPreferences(SHARED_PREFERENCES_STORE, MODE_PRIVATE);
        return myPref.getString(key,"");
    }

}
