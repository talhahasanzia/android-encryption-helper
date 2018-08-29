package com.singledraft.encryption_samples;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.thz.keystorehelper.EncryptionDecryptionListener;
import com.thz.keystorehelper.KeyStoreManager;

import net.sqlcipher.database.SQLiteDatabase;

public class MainActivity extends AppCompatActivity implements EncryptionDecryptionListener {

    private DbUtil mDbUtil; // sample utility for database operations


    String alias = "test_alias13";

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text);

        // initialize this in app object, SQLite cipher library
        SQLiteDatabase.loadLibs(this);

        SaveDataUtil saveDataUtil = new SaveDataUtil(this);

        KeyStoreManager.init(getApplicationContext());


        testSimple(saveDataUtil); // test simple calls

        testAsync(); // test async calls


    }

    private void testSimple(SaveDataUtil saveDataUtil) {

        String key;

        if (saveDataUtil.getSecurityPhrase() == null) {
            key = KeyStoreManager.getNewRandomPhrase(10);

            addTextResult("testSimple: generated: " + key);
            String encrypted = KeyStoreManager.encryptData(key, alias);


            addTextResult("testSimple: encrypted: " + encrypted);

            saveDataUtil.setSecurityPhrase(encrypted);
        }
        key = saveDataUtil.getSecurityPhrase();


        addTextResult("testSimple: from prefs: " + key);

        String decrypted = KeyStoreManager.decryptData(key, alias);

        addTextResult("testSimple: after decrypt: " + decrypted);

        mDbUtil = new DbUtil(this, decrypted);
    }


    private void testAsync() {

        String key = KeyStoreManager.getNewRandomPhrase(15);


        addTextResult("testAsync: generated: " + key);


        // pass data (key) , alias (for keystore) and a listener reference so
        // we get results (in onSuccess) or error messages (in onFailure) after our task is completed in the background
        KeyStoreManager.encryptDataAsync(key, alias, this);


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
        addTextResult("testAsync: encrypted: " + encrypted);
        KeyStoreManager.decryptDataAsync(encrypted, alias, decryptionListener);


    }

    // called when there is a problem during encryption decryption
    @Override
    public void onFailure(String errorMessage) {
        addTextResult(errorMessage);
    }


    // METHOD 2: Example EncryptionDecryptionListener usage
    // this listener is specifically defined for decryption as separate instance

    EncryptionDecryptionListener decryptionListener = new EncryptionDecryptionListener() {
        @Override
        public void onSuccess(String data) {
            addTextResult("testAsync: after decrypt: " + data);
        }

        @Override
        public void onFailure(String errorMessage) {
            addTextResult(errorMessage);
        }
    };


    private void addTextResult(String text) {
        String resultText = textView.getText().toString() + "\n" + text + "\n";
        textView.setText(resultText);
    }
}
