package com.windwarriors.appetite.utils;

import com.windwarriors.appetite.database.DatabaseContract;

public final class Constants {

    // Constant for greeting the username
    static final String GREETING = "Hello, ";
    public static final String OPEN = "Open";
    public static final String CLOSED = "Closed";

    // Key names for passing data among Activities by using Bundle and Intents
    public static final String BUSINESS_ID = "BusinessId";
    public static final String BUSINESS_DISTANCE = "BusinessDistance";


    // Constants for using Shared Preferences
    public static final String SHARED_PREFERENCES_STORE = "OMGASharedPreferences";
    public static final String SHARED_PREFERENCES_USER_KEY = "UserName";
    static final String SHARED_PREFERENCES_DEFAULT_USERNAME = "Friend";
    public static final String SHARED_PREFERENCES_SEARCH_RANGE = "SP_SEARCH_RANGE";
    public static final String SHARED_PREFERENCES_FILTER_PRICE = "SP_FILTER_PRICE";
    public static final String SHARED_PREFERENCES_SORTBY= "SORT_BY";


    // Constants for SQLite creation of tables
    public static final String tables[] = {
            DatabaseContract.UserEntry.TABLE_NAME
    };

    public static final String tableCreatorString[] = {
            DatabaseContract.UserEntry.SQL_CREATE_TABLE
    };

    public static final String BROADCAST_BUSINESS_LIST_READY      = "BUSINESS_LIST_READY";
    public static final String BROADCAST_BUSINESS_READY           = "BUSINESS_READY";
    public static final String BROADCAST_BUSINESS_SERVICE         = "BUSINESS_SERVICE";
    public static final String BROADCAST_TERM                     = "BROADCAST_TERM";
    public static final String BROADCAST_LATITUDE                 = "BROADCAST_LATITUDE";
    public static final String BROADCAST_LONGITUDE                = "BROADCAST_LONGITUDE";
    public static final String BROADCAST_ID                       = "BROADCAST_ID";
    public static final String BROADCAST_RANGE_UPDATE             = "BROADCAST_RANGE_UPDATE";
    public static final String BROADCAST_FILTER_UPDATE             = "BROADCAST_FILTER_UPDATE";

    // BUSINESS SERVICE CMDS
    public static final int    BROADCAST_DESTROY_BUSINESS_SERVICE = 99;

    public static final int    BROADCAST_REFRESH_BUSINESS_LIST    = 100;
    public static final int    BROADCAST_UPDATE_LOCATION          = 102;
    public static final int    BROADCAST_UPDATE_TERM              = 103;
    public static final int    BROADCAST_LOAD_BUSINESS            = 104;
    public static final int    BROADCAST_UPDATE_RANGE             = 105;
    public static final int    BROADCAST_UPDATE_FILTER            = 106;

    // Used on BusinessListReadyBroadcaster intents
    public static final String BUSINESS_LIST = "businessList";
    public static final String BUSINESS = "business";

    // Used for paging
    public static final int PAGE_SIZE = 5;
    public static final int DEFAULT_YELP_SERVICE_LIST_SIZE = 20;

    public static final Double CENTENNIAL_LATITUDE = 43.7844571;
    public static final Double CENTENNIAL_LONGITUDE = -79.2287377;

    public static final Double MOCK_DETAIL_LATITUDE = 43.7740605;
    public static final Double MOCK_DETAIL_LONGITUDE = -79.2325777;


    // Permissions
    public static final int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 1001;
    public static final int MY_PERMISSIONS_ACCESS_COARSE_LOCATION = 1002;
}
