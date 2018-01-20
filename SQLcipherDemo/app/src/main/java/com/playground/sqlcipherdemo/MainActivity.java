package com.playground.sqlcipherdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.playground.sqlcipherdemo.AndroidKeystore.KeyManager;

import net.sqlcipher.database.SQLiteDatabase;

public class MainActivity extends AppCompatActivity {

    private DbUtil mDbUtil;
    public KeyManager keyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize this in app object, SQLite cipher library
        SQLiteDatabase.loadLibs(this);

        // first time calls db and encrypt it with old password
        mDbUtil = new DbUtil(this);

        //mDbUtil.dropTable(); // remove all data

        keyManager=KeyManager.getInstance(this);

        String key=keyManager.getNewRandomPhrase();

        String encrypted=keyManager.encryptKey(key);

        Log.d("Key", "onCreate: encrypted "+encrypted);

        String decrypted=keyManager.decryptKey(encrypted);

        Log.d("key", "onCreate: decrypted "+decrypted);


    }

    @Override
    protected void onResume() {
        super.onResume();

        //insertDummyData(); // populate table for data

        for (String name : mDbUtil.getDataFromTable()) {

            Log.d("MainActivity", "result from db: " + name);
        }

    }

    private void insertDummyData() {
        Log.d("MainActivity", "onResume: Inserted :: "+mDbUtil.insertIntoTable("person alan"));
        Log.d("MainActivity", "onResume: Inserted :: "+mDbUtil.insertIntoTable("person bret"));
        Log.d("MainActivity", "onResume: Inserted :: "+mDbUtil.insertIntoTable("person charlie"));
        Log.d("MainActivity", "onResume: Inserted :: "+mDbUtil.insertIntoTable("person dave"));
        Log.d("MainActivity", "onResume: Inserted :: "+mDbUtil.insertIntoTable("person eggsy"));
    }

    @Override
    protected void onStop() {
        super.onStop();

        // close db for to avoid IO leaks
        mDbUtil.close();

    }



}
