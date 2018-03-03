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
        insertCustomer("irvin", "ikaur39@my.centennialcollege.ca","123", "Irving", "Kuhr");
        insertCustomer("fatih", "mfatihinan@gmail.com","123", "Fatih", "Inan");
        insertCustomer("kamal", "preetkamal110@gmail.com","123", "Kamal", "Singh");
        insertCustomer("sergio", "sergio.a.brunacci@gmail.com","123", "Sergio", "Brunacci");
        insertCustomer("rafael", "timbo.rafa@gmail.com","123", "Rafael", "Timbo");
        insertCustomer("fernando", "fito@my.centennialcollege.ca","123", "Fernando", "Ito");
        insertCustomer("rob", "robert.a.argume@gmail.com","123", "Robert", "Argume");
    }

    @SuppressWarnings("SameParameterValue")
    private void insertCustomer(String username, String email, String password, String firstName, String lastName) {
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.UserEntry.COLUMN_USERNAME, username);
        values.put(DatabaseContract.UserEntry.COLUMN_EMAIL, email);
        values.put(DatabaseContract.UserEntry.COLUMN_PASSWORD, password);
        values.put(DatabaseContract.UserEntry.COLUMN_FIRSTNAME, firstName);
        values.put(DatabaseContract.UserEntry.COLUMN_LASTNAME, lastName);

        mDb.insert(DatabaseContract.UserEntry.TABLE_NAME, null, values);
    }
}