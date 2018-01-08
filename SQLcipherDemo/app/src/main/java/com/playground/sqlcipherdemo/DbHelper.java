package com.playground.sqlcipherdemo;

import android.content.Context;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

/**
 * Created by Talha Hasan Zia on 08-Jan-18.
 * <p></p><b>Description:</b><p></p> Why class was created?
 * <p></p>
 * <b>Public Methods:</b><p></p> Only listing to public methods usage.
 */
public class DbHelper extends SQLiteOpenHelper {


    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseContract.CREATE_DUMMY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    static class DatabaseContract{

        public static final String TABLE_NAME="contacts";

        public static final String CREATE_DUMMY_TABLE="CREATE TABLE "+TABLE_NAME+" (\n" +
                " contact_id integer PRIMARY KEY AUTOINCREMENT,\n" +
                " first_name text NOT NULL\n" +
                ");";

        public static final String NAME_FIELD="first_name";

        public static final String SELECT_ALL="SELECT * FROM "+TABLE_NAME;


    }
}
