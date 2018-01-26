package com.thz.sqlcipherdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.thz.keystorehelper.EncryptionDecryptionListener;
import com.thz.keystorehelper.KeyStoreManager;

import net.sqlcipher.database.SQLiteDatabase;

public class MainActivity extends AppCompatActivity implements EncryptionDecryptionListener {

    private DbUtil mDbUtil; // sample utility for database operations
    public KeyStoreManager keyManager;  // library class
    String TAG = "TestInit";

    String alias = "test_alias13";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize this in app object, SQLite cipher library
        SQLiteDatabase.loadLibs(this);

        SaveDataUtil saveDataUtil = new SaveDataUtil(this);


        keyManager = KeyStoreManager.getInstance(this);

        testSimple(saveDataUtil); // test simple calls

        testAsync(); // test async calls


    }

    private void testSimple(SaveDataUtil saveDataUtil) {
        String TAG = "TestInit";
        String key = null;
        if (saveDataUtil.getSecurityPhrase() == null) {
            key = keyManager.getNewRandomPhrase();

            Log.d(TAG, "testSimple: generated: " + key);
            String encrypted = keyManager.encryptData(key, alias);
            Log.d(TAG, "testSimple: encrypted: " + encrypted);

            saveDataUtil.setSecurityPhrase(encrypted);
        }
        key = saveDataUtil.getSecurityPhrase();

        Log.d(TAG, "testSimple: from prefs: " + key);
        String decrypted = keyManager.decryptData(key, alias);
        Log.d(TAG, "testSimple: after decrypt: " + decrypted);
        mDbUtil = new DbUtil(this, decrypted);
    }


    private void testAsync() {

        String key = keyManager.getNewRandomPhrase();

        Log.d(TAG, "testAsync: generated: " + key);

        // pass data (key) , alias (for keystore) and a listener reference so
        // we get results (in onSuccess) or error messages (in onFailure) after our task is completed in the background
        keyManager.encryptDataAsync(key, alias, this);


    }

    @Override
    protected void onResume() {
        super.onResume();

        insertDummyData(); // populate table for data


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

    // called when encryption/decryption operation is successful
    @Override
    public void onSuccess(String encrypted) {
        Log.d(TAG, "testAsync: encrypted: " + encrypted);

        keyManager.decryptDataAsync(encrypted,alias,decryptionListener);

        Toast.makeText(this, "Encrypt - On Success Called", Toast.LENGTH_SHORT).show();

    }

    // called when there is a problem during encryption decryption
    @Override
    public void onFailure(String errorMessage) {
        Toast.makeText(this, "Encrypt - On Failure Called", Toast.LENGTH_SHORT).show();

    }


    // METHOD 2: Example EncryptionDecryptionListener usage
    // this listener is specifically defined for decryption as separate instance

    EncryptionDecryptionListener decryptionListener=new EncryptionDecryptionListener() {
        @Override
        public void onSuccess(String data) {
            Log.d(TAG, "testAsync: after decrypt: " + data);
            Toast.makeText(MainActivity.this, "Decrypt - onSuccess called", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onFailure(String errorMessage) {
            Toast.makeText(MainActivity.this, "Decrypt - onFailure called", Toast.LENGTH_SHORT).show();

        }
    };
}
