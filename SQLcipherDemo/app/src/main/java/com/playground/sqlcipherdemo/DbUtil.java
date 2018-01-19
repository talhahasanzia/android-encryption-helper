package com.playground.sqlcipherdemo;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

/**
 * Created by Talha Hasan Zia on 08-Jan-18.
 * <p></p><b>Description:</b><p></p> Utility class for Database dummy operations.
 * <p></p>
 */
public class DbUtil {

    private Context context;
    private SQLiteDatabase mSQLiteDatabase;
    private DbHelper mDbHelper;

    private final String old_password = "pass_old_123";


    /**
     * Instantiates a new Db util.
     *
     * @param context the context
     */
    public DbUtil(Context context) {
        this.context = context;
        mDbHelper = new DbHelper(context, context.getString(R.string.database_name), null, BuildConfig.VERSION_CODE);

        mSQLiteDatabase = mDbHelper.getWritableDatabase(old_password);
        }


    /**
     * Instantiates a new Db util using new randomly generated password.
     * Has option to rekey if this call is made first.
     *
     * @param context        the context of app
     * @param randomPassword the random password generated in caller activity
     * @param rekeyNeeded    if the rekey is needed
     */
    public DbUtil(Context context, String randomPassword, boolean rekeyNeeded) {
        this.context = context;
        mDbHelper = new DbHelper(context, context.getString(R.string.database_name), null, BuildConfig.VERSION_CODE);

        mSQLiteDatabase = mDbHelper.getWritableDatabase(old_password);

        if(rekeyNeeded)
        pragmaRekey(randomPassword);

        mSQLiteDatabase = mDbHelper.getWritableDatabase(randomPassword);
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
     *
     * @param newPassword the new password to be applied.
     */
    public void pragmaRekey(String newPassword) {
        String keyCommand = String.format("PRAGMA key  = \"%s\";", old_password);
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
