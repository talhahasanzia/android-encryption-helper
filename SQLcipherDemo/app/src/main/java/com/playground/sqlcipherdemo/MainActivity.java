package com.playground.sqlcipherdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteException;

public class MainActivity extends AppCompatActivity {

    private DbUtil mDbUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLiteDatabase.loadLibs(this);


        mDbUtil = new DbUtil(this);

        //mDbUtil.resetTable();


    }

    @Override
    protected void onResume() {
        super.onResume();

      /*  Log.d("MainActivity", "onResume: Insert :: "+mDbUtil.insertIntoTable("person alan"));
        Log.d("MainActivity", "onResume: Insert :: "+mDbUtil.insertIntoTable("person bret"));
        Log.d("MainActivity", "onResume: Insert :: "+mDbUtil.insertIntoTable("person charlie"));
        Log.d("MainActivity", "onResume: Insert :: "+mDbUtil.insertIntoTable("person dave"));
        Log.d("MainActivity", "onResume: Insert :: "+mDbUtil.insertIntoTable("person eggsy"));
*/

        for (String name : mDbUtil.getDataFromTable()) {

            Log.d("MainActivity", "result from db: " + name);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();

        mDbUtil.close();

    }


}
