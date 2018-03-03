package com.windwarriors.appetite.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.windwarriors.appetite.database.DatabaseContract;
import com.windwarriors.appetite.database.DatabaseManager;
import static com.windwarriors.appetite.utils.Constants.tableCreatorString;
import static com.windwarriors.appetite.utils.Constants.tables;


public class UserService {
    private Context context;
    private DatabaseManager dbm;

    public UserService(Context context) {
        this.dbm = new DatabaseManager(context);
        this.context = context;
        dbm.dbInitialize(tables, tableCreatorString);
    }

    public String getCustomerId(String username, String password ) {
        String customerId = null;

        SQLiteDatabase db = dbm.getReadableDatabase();
        String[] params = new String[]{ username, password };
        String[] columns = new String[]{DatabaseContract.UserEntry._ID};
        Cursor cursor = db.query(DatabaseContract.UserEntry.TABLE_NAME, columns,
                DatabaseContract.UserEntry.COLUMN_USERNAME + " = ? AND " +
                        DatabaseContract.UserEntry.COLUMN_PASSWORD + "= ?", params,
                null, null, null);

        if (cursor.moveToFirst()) {
            customerId = cursor.getString(cursor.getColumnIndex(DatabaseContract.UserEntry._ID));
        }
        cursor.close();
        return customerId;
    }

}
