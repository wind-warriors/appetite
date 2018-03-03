package com.windwarriors.appetite.database;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager extends SQLiteOpenHelper {
    //
    private static final String DATABASE_NAME = "appetite.db";
    private static final int DATABASE_VERSION = 3;
    //

    private Context context;
    //
    private String tables[]; //table names
    private String tableCreatorString[]; //SQL statements to create tables

    //class constructor
    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

    }
    //initialize database table names and DDL statements
    public void dbInitialize(String[] tables, String tableCreatorString[])
    {
        this.tables = tables;
        this.tableCreatorString = tableCreatorString;
    }

    // Create tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        for (String aTable: tables) {
            db.execSQL("DROP TABLE IF EXISTS " + aTable);
        }

        for (String aCreateString: tableCreatorString)
            db.execSQL(aCreateString);

        // Load initial users data into database
        loadInitialData(db);
    }

    //create the database
    public void createDatabase(Context context)
    {
        SQLiteDatabase mDatabase;
        mDatabase = context.openOrCreateDatabase(
                DATABASE_NAME,
                //SQLiteDatabase.CREATE_IF_NECESSARY,
                SQLiteDatabase.OPEN_READWRITE,
                null);
    }

    //delete the database
    public void deleteDatabase(Context context)
    {
        context.deleteDatabase(DATABASE_NAME);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (String aTable: tables)
            db.execSQL("DROP TABLE IF EXISTS " + aTable);

        // Create tables again
        onCreate(db);
    }

    /////////////////////////
    // Database operations
    /////////////////////////
    // Add a new record
    public void addRecord(ContentValues values, String tableName, String fields[], String record[]) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (int i=1;i<record.length;i++)
            values.put(fields[i], record[i]);
        // Insert the row
        db.insert(tableName, null, values);
        db.close(); //close database connection
    }
    public void addRecordUsingContentValues(ContentValues values, String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Insert the row
        long result = db.insert(tableName, null, values);
        db.close(); //close database connection
    }
    // Read all records
    public List getTable(String tableName) {
        List<List<String>> table = new ArrayList<>(); //to store all rows
        // Select all records
        String selectQuery = "SELECT  * FROM " + tableName;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<String> row = new ArrayList<>(); //to store one row
        //scroll over rows and store each row in an array list object
        if (cursor.moveToFirst())
        {
            do
            { // for each row
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    row.add(cursor.getString(i));
                }

                table.add(row); //add row to the list

            } while (cursor.moveToNext());
        }
        cursor.close();

        // return table as a list
        return table;
    }

    // Update a record
    public int updateRecord(ContentValues values, String tableName, String fields[], String record[]) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (int i=1;i<record.length;i++)
            values.put(fields[i], record[i]);

        // updating row with given id = record[0]
        return db.update(tableName, values, fields[0] + " = ?",
                new String[] { record[0] });
    }

    // Delete a record with a given id
    public void deleteRecord(String tableName, String idName, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tableName, idName + " = ?",
                new String[] { id });
        db.close();
    }



    // PRIVATE METHODS
    private void loadInitialData(SQLiteDatabase db) {
        DatabaseDataWorker worker = new DatabaseDataWorker(db);
        worker.insertCustomers();
    }

}
