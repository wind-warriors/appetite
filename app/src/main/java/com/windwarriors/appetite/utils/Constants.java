package com.windwarriors.appetite.utils;

import com.windwarriors.appetite.database.DatabaseContract;

public final class Constants {

    // Constant for greeting the username
    public static final String GREETING = "Hello, ";
    public static final String OPEN = "Open";
    public static final String CLOSED = "Closed";


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

    // Used on BusinessListReadyBroadcaster intents
    public static final String BUSINESS_LIST = "businessList";

    public static final Double CENTENNIAL_LATITUDE = 43.7844571;
    public static final Double CENTENNIAL_LONGITUDE = -79.2287377;

    // Permissions
    public static final int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 1001;
    public static final int MY_PERMISSIONS_ACCESS_COARSE_LOCATION = 1002;

}
