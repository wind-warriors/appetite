package com.windwarriors.appetite.utils;

import com.windwarriors.appetite.database.DatabaseContract;

public final class Constants {

    // Constant for greeting the username
    public static final String GREETING = "Hello, ";

    // Constants for using Shared Preferences
    public static final String SHARED_PREFERENCES_STORE = "OMGASharedPreferences";
    public static final String SHARED_PREFERENCES_USER_KEY = "UserName";
    public static final String SHARED_PREFERENCES_DEFAULT_USERNAME = "Friend";


    // Constants for SQLite creation of tables
    public static final String tables[] = {
            DatabaseContract.UserEntry.TABLE_NAME
    };

    public static final String tableCreatorString[] = {
            DatabaseContract.UserEntry.SQL_CREATE_TABLE
    };


}
