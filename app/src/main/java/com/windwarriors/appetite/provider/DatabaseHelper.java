package com.windwarriors.appetite.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.windwarriors.appetite.provider.UserProviderMetaData.UserProviderTableMetaData;

/**
 * Helps open, create, and upgrade the database file
 */
class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelpers";
    private Context mContext;

    DatabaseHelper(Context context) {
        super(context,
                UserProviderMetaData.DATABASE_NAME,
                null,
                UserProviderMetaData.DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "inner oncreate called");
        createTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "inner onupgrade called");
        Log.w(TAG, "Upgrading database from version "
                + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        // ChatMessage
        db.execSQL("DROP TABLE IF EXISTS " +
                UserProviderMetaData.UserProviderTableMetaData.TABLE_NAME);
        createTables(db);
    }


    private void createTables(SQLiteDatabase db) {
        // ChatMessage
        db.execSQL("CREATE TABLE IF NOT EXISTS " + UserProviderTableMetaData.TABLE_NAME + " ("
                + UserProviderTableMetaData._ID + " INTEGER PRIMARY KEY,"
                + UserProviderTableMetaData.USER_NAME + " TEXT,"
                + UserProviderTableMetaData.EMAIL + " TEXT,"
                + UserProviderTableMetaData.PASSWORD + " TEXT,"
                + UserProviderTableMetaData.FIRST_NAME + " TEXT,"
                + UserProviderTableMetaData.LAST_NAME + " TEXT"
                + ");");
    }
}
