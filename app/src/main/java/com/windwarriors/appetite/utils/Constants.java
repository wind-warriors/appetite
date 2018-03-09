package com.windwarriors.appetite.utils;

import com.windwarriors.appetite.database.DatabaseContract;

public final class Constants {

    // Constant for greeting the username
    public static final String GREETING = "Hello, ";

    // Constants for using Shared Preferences
    public static final String SHARED_PREFERENCES_STORE = "OMGASharedPreferences";
    public static final String SHARED_PREFERENCES_USER_KEY = "UserName";
    public static final String SHARED_PREFERENCES_DEFAULT_USERNAME = "Friend";
    public static final String SHARED_PREFERENCES_SEARCH_RANGE = "SP_SEARCH_RANGE";


    // Constants for SQLite creation of tables
    public static final String tables[] = {
            DatabaseContract.UserEntry.TABLE_NAME
    };

    public static final String tableCreatorString[] = {
            DatabaseContract.UserEntry.SQL_CREATE_TABLE
    };


    public static final String BROADCAST_BUSINESS_LIST_READY = "BUSINESS_LIST_READY";
    public static final String BROADCAST_BUSINESS_READY = "BUSINESS_READY";

}
