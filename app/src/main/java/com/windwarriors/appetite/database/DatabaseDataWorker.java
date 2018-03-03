package com.windwarriors.appetite.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

class DatabaseDataWorker {
    private SQLiteDatabase mDb;

    DatabaseDataWorker(SQLiteDatabase db) {
        mDb = db;
    }

    void insertCustomers() {
        insertCustomer("irvin", "123", "Irving", "Kuhr");
        insertCustomer("fatih", "123", "Fatih", "Inan");
        insertCustomer("kamal", "123", "Kamal", "Singh");
        insertCustomer("sergio", "123", "Sergio", "Brunacci");
        insertCustomer("rafael", "123", "Rafael", "Timbo");
        insertCustomer("fernando", "123", "Fernando", "Ito");
        insertCustomer("rob", "123", "Robert", "Argume");
    }

    @SuppressWarnings("SameParameterValue")
    private void insertCustomer(String username, String password, String firstName, String lastName) {
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.UserEntry.COLUMN_USERNAME, username);
        values.put(DatabaseContract.UserEntry.COLUMN_PASSWORD, password);
        values.put(DatabaseContract.UserEntry.COLUMN_FIRSTNAME, firstName);
        values.put(DatabaseContract.UserEntry.COLUMN_LASTNAME, lastName);

        mDb.insert(DatabaseContract.UserEntry.TABLE_NAME, null, values);
    }
}