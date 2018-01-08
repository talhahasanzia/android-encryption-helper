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
 * <b>Public Methods:</b><p></p> Only listing to public methods usage.
 */
public class DbUtil {

    private Context context;
    private SQLiteDatabase mSQLiteDatabase;
    private DbHelper mDbHelper;

    private final String old_password = "pass_old_123";

    private final String new_password = "pass_new_abc";


    public DbUtil(Context context) {
        this.context = context;
        mDbHelper = new DbHelper(context, context.getString(R.string.database_name), null, BuildConfig.VERSION_CODE);

        mSQLiteDatabase = mDbHelper.getWritableDatabase(old_password);
        pragmaRekey();
        mSQLiteDatabase = mDbHelper.getWritableDatabase(new_password);
    }


    public long insertIntoTable(String name) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.DatabaseContract.NAME_FIELD, name);


        long result = mSQLiteDatabase.insert(DbHelper.DatabaseContract.TABLE_NAME, null, contentValues);

        return result;
    }

    public void pragmaRekey() {
        String keyCommand = String.format("PRAGMA key  = \"%s\";", old_password);
        String rekeyCommand = String.format("PRAGMA rekey  = \"%s\";", new_password);

        mSQLiteDatabase.execSQL(keyCommand);
        mSQLiteDatabase.execSQL(rekeyCommand);

    }

    public String[] getDataFromTable() {
        String[] results;

        Cursor cursor = mSQLiteDatabase.rawQuery("select * from " + DbHelper.DatabaseContract.TABLE_NAME, null);

        results = new String[cursor.getCount()];

        /*cursor.moveToFirst();
        Log.d("MainActivity", "getDataFromTable: Count :: "+cursor.getCount());

        int index = 0;
        do {
        */

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            // do what you need with the cursor here
            results[cursor.getPosition()] = cursor.getString(cursor.getColumnIndex(DbHelper.DatabaseContract.NAME_FIELD));
        }

        /*} while (cursor.moveToNext());
*/
        cursor.close();

        return results;
    }


    public void resetTable() {
        mSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DbHelper.DatabaseContract.TABLE_NAME);

        mSQLiteDatabase.execSQL(DbHelper.DatabaseContract.CREATE_DUMMY_TABLE);

    }


    public void close() {
        mSQLiteDatabase.close();
        mDbHelper.close();

    }


}
