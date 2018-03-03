package com.windwarriors.appetite.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class UserProviderMetaData {

    public static final String AUTHORITY = "com.windwarriors.appetite.provider.UserProvider";//com.zv.geochat.provider.GeoChatProvider
    public static final String DATABASE_NAME = "appetite.db";
    public static final int DATABASE_VERSION = 1;

    private UserProviderMetaData() {
    }

    public static final class UserProviderTableMetaData implements BaseColumns {
        private UserProviderTableMetaData() {
        }

        public static final String TABLE_NAME = "user";
        // uri and MIME type definitions
        public static final Uri CONTENT_URI = Uri.parse("content://"
                + AUTHORITY + "/" + UserProviderTableMetaData.TABLE_NAME);
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.com.windwarriors.appetite.model.user";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.com.windwarriors.appetite.model.user";
        public static final String DEFAULT_SORT_ORDER = "_id ASC";
        // Additional Columns start here.
        // string type
        public static final String USER_NAME = "username";
        public static final String EMAIL = "email";
        public static final String PASSWORD = "password";
        public static final String FIRST_NAME = "firstname";
        public static final String LAST_NAME = "lastname";
    }
}
