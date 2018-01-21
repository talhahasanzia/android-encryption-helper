package com.thz.sqlcipherdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


import com.thz.keystorehelper.KeyStoreManager;

import net.sqlcipher.database.SQLiteDatabase;

public class MainActivity extends AppCompatActivity {

    private DbUtil mDbUtil; // sample utility for database operations
    public KeyStoreManager keyManager;  // library class

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize this in app object, SQLite cipher library
        SQLiteDatabase.loadLibs(this);

        SaveDataUtil saveDataUtil = new SaveDataUtil(this);

        String alias = "test_alias7";

        keyManager = KeyStoreManager.getInstance(this);

        init(saveDataUtil, alias);


    }

    private void init(SaveDataUtil saveDataUtil, String alias) {
        String key = null;
        if (saveDataUtil.getSecurityPhrase() == null) {
            key = keyManager.getNewRandomPhrase();


            String encrypted = keyManager.encryptData(key, alias);
            saveDataUtil.setSecurityPhrase(encrypted);
        }

        key = saveDataUtil.getSecurityPhrase();

        String decrypted = keyManager.decryptData(key, alias);

        mDbUtil = new DbUtil(this, decrypted);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //insertDummyData(); // populate table for data


        // check if key encryption operations were performed successfully
        // if its successful then it should show data
        // remember to call inserDummyData() at least once to populate some data
        for (String name : mDbUtil.getDataFromTable()) {

            Log.d("MainActivity", "result from db: " + name);
        }

    }

    private void insertDummyData() {
        Log.d("MainActivity", "onResume: Inserted :: " + mDbUtil.insertIntoTable("person alan"));
        Log.d("MainActivity", "onResume: Inserted :: " + mDbUtil.insertIntoTable("person bret"));
        Log.d("MainActivity", "onResume: Inserted :: " + mDbUtil.insertIntoTable("person charlie"));
        Log.d("MainActivity", "onResume: Inserted :: " + mDbUtil.insertIntoTable("person dave"));
        Log.d("MainActivity", "onResume: Inserted :: " + mDbUtil.insertIntoTable("person eggsy"));
    }

    @Override
    protected void onStop() {
        super.onStop();

        // close db for to avoid IO leaks
        mDbUtil.close();

    }


}
