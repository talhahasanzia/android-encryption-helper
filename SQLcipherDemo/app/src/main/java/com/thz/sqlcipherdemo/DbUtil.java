package com.thz.sqlcipherdemo;

import android.content.ContentValues;
import android.content.Context;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

/**
 * <p></p><b>Description:</b><p></p> Utility class for Database dummy operations.
 * <p></p>
 */
public class DbUtil {

    private Context context; // app's context
    private SQLiteDatabase mSQLiteDatabase; // Sqlite Database object
    private DbHelper mDbHelper; // SQLiteHelper

    private final String old_password = "pass_old_123"; // for tests

    /**
     * Instantiates a new DbUtil using password.
     *
     * @param context    the context of app
     * @param passPhrase the password generated in caller activity
     */
    public DbUtil(Context context, String passPhrase) {
        this.context = context;
        mDbHelper = new DbHelper(context, context.getString(R.string.database_name), null, BuildConfig.VERSION_CODE);

        mSQLiteDatabase = mDbHelper.getWritableDatabase(passPhrase);

        mSQLiteDatabase = mDbHelper.getWritableDatabase(passPhrase);
    }


    /**
     * Instantiates a new DbUtil using password.
     * Has option to rekey if this call is to change database password.
     *
     * @param context     the context of app
     * @param oldPassword the old password
     * @param newPassword the new password
     * @param rekeyNeeded if the rekey is needed
     */
    public DbUtil(Context context, String oldPassword, String newPassword, boolean rekeyNeeded) {

        this.context = context;

        mDbHelper = new DbHelper(context, context.getString(R.string.database_name), null, BuildConfig.VERSION_CODE);

        mSQLiteDatabase = mDbHelper.getWritableDatabase(oldPassword);

        if (rekeyNeeded) // not running rekey will cause failure to retrieve db with new key
            pragmaRekey(oldPassword, newPassword);

        mSQLiteDatabase = mDbHelper.getWritableDatabase(oldPassword);
    }

    /**
     * Insert into table return long ID of row.
     *
     * @param name the name data to be inserted
     * @return the long id of row
     */
    public long insertIntoTable(String name) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.DatabaseContract.NAME_FIELD, name);


        long result = mSQLiteDatabase.insert(DbHelper.DatabaseContract.TABLE_NAME, null, contentValues);

        return result;
    }


    /**
     * Pragma rekey operations.
     * @param oldPassword old password that was used to encrypt
     * @param newPassword the new password to be applied.
     */
    public void pragmaRekey(String oldPassword,String newPassword) {
        String keyCommand = String.format("PRAGMA key  = \"%s\";", oldPassword);
        String rekeyCommand = String.format("PRAGMA rekey  = \"%s\";", newPassword);

        mSQLiteDatabase.execSQL(keyCommand);
        mSQLiteDatabase.execSQL(rekeyCommand);

    }


    /**
     * Get data from table  as string array.
     *
     * @return the string array of names from table
     */
    public String[] getDataFromTable() {
        String[] results;

        Cursor cursor = mSQLiteDatabase.rawQuery("select * from " + DbHelper.DatabaseContract.TABLE_NAME, null);

        results = new String[cursor.getCount()];


        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            // get data row by row
            results[cursor.getPosition()] = cursor.getString(cursor.getColumnIndex(DbHelper.DatabaseContract.NAME_FIELD));
        }

        cursor.close();

        return results;
    }


    /**
     * Drops table.
     */
    public void dropTable() {
        mSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DbHelper.DatabaseContract.TABLE_NAME);

        mSQLiteDatabase.execSQL(DbHelper.DatabaseContract.CREATE_DUMMY_TABLE);

    }


    /**
     * Close db object.
     */
    public void close() {
        mSQLiteDatabase.close();
        mDbHelper.close();

    }


}
