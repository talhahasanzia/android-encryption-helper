package com.thz.keystorehelper;

import android.os.AsyncTask;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

import javax.crypto.NoSuchPaddingException;

/**
 * <p></p><b>Description:</b><p></p> Task Factory responsible for providing different Tasks.
 * <p></p>
 * This class will hold objects to be used for async operations.<br>This class will instatiate and return AsyncTask types.
 * <p></p>
 * <b>Provides:</b><br><br>
 * {@link #executeDecryptionTask(String, String, EncryptionDecryptionListener)} A task running in the background for decryption, result will be delegated to its EncryptionDecryptionListener <br><br>
 * {@link #executeEncryptionTask(String, String, EncryptionDecryptionListener)} A task running in background the for encryption, result will be delegated to its EncryptionDecryptionListener
 */
class TasksRepo {

    private KeystoreHelper keystoreHelper; // KeyStore helper instance to be used by this TasksRepo for KeyStore operations.
    private EncryptionDecryptionListener encryptionDecryptionListener; // Global variable to be used by inner classes

    /**
     * @param keystoreHelper {@link KeystoreHelper } class for KeyStore operations
     * @throws CertificateException
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     * @throws IOException
     */
    public TasksRepo(KeystoreHelper keystoreHelper) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        this.keystoreHelper = keystoreHelper;
    }


    /**
     * The task's implementation is hidden from caller.<br>
     * Caller has to provide alias and data, and a callback object (listener),<br>
     * which will be used to deliver processed data once task is finished successfully,<br>
     * or failure message in other cases.
     *
     * @param data                         data to be encrypted
     * @param alias                        alias (key against which the Keystore will retrieve private keys)
     * @param encryptionDecryptionListener Callback object which will deliver results of this Task.
     */
    public void executeEncryptionTask(String data, String alias, EncryptionDecryptionListener encryptionDecryptionListener) {
        this.encryptionDecryptionListener = encryptionDecryptionListener;
        EncryptionTask encryptionTask = new EncryptionTask();
        encryptionTask.execute(data, alias);

    }


    /**
     * The task's implementation is hidden from caller.<br>
     * Caller has to provide alias and data, and a callback object (listener),<br>
     * which will be used to deliver processed data once decryption task is finished successfully,<br>
     * or failure message in other cases.
     *
     * @param data                         data to be decrypted
     * @param alias                        alias (key against which the Keystore will retrieve private keys)
     * @param encryptionDecryptionListener Callback object which will deliver results of this Task.
     */
    public void executeDecryptionTask(String data, String alias, EncryptionDecryptionListener encryptionDecryptionListener) {
        this.encryptionDecryptionListener = encryptionDecryptionListener;
        DecryptionTask decryptionTask = new DecryptionTask();
        decryptionTask.execute(data, alias);

    }


    /**
     * Class for performing encryption in background.
     * The {@link #execute(Object[])} call expects first parameter to be a Data for which the encryption will be performed,<br>
     * and second parameter to be alias i.e. key for getting keys from KeyStore.
     */
    private class EncryptionTask extends AsyncTask<String, Void, String> {

        private boolean throwError = false; // signal to call onFailure() in case of exception

        @Override
        protected String doInBackground(String... strings) { // this method runs off the Main thread
            String data = strings[0]; // first parameter must be data
            String alias = strings[1]; // Second parameter must be alias

            try {
                return keystoreHelper.encryptString(alias, data); // simple call to encrypt, but this time its in background thread
            } catch (NoSuchPaddingException | NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException
                    IOException | UnrecoverableEntryException | KeyStoreException |
                    InvalidAlgorithmParameterException e){
                throwError = true;
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) throws NullPointerException {
            if (throwError) // exception was caught
                encryptionDecryptionListener.onFailure(s); // pass exception message to caller
            else
                encryptionDecryptionListener.onSuccess(s); // deliver encrypted result to caller

        }
    }

    /**
     * Class for performing encryption in background.
     * The {@link #execute(Object[])} call expects first parameter to be a Data for which the encryption will be performed,<br>
     * and second parameter to be alias i.e. key for getting keys from KeyStore.
     */
    private class DecryptionTask extends AsyncTask<String, Void, String> {

        private boolean throwError = false; // signal to call onFailure() in case of exception is thrown during decryption

        @Override
        protected String doInBackground(String... strings) { // this method runs off the Main thread
            String data = strings[0];   // first parameter is expected to be Data
            String alias = strings[1];  // second parameter is expected to be alias

            try {
                return keystoreHelper.decryptString(alias, data);   // simple call to decrypt, but this time its in background thread
            } catch (NoSuchPaddingException | NoSuchAlgorithmException | NoSuchProviderException
                    InvalidKeyException |
            IOException | UnrecoverableEntryException | KeyStoreException e){
                throwError = true;
                return e.getMessage();
            }

        }

        @Override
        protected void onPostExecute(String s) throws NullPointerException {
            if (throwError)
                encryptionDecryptionListener.onFailure(s); // pass exception message to the caller
            else
                encryptionDecryptionListener.onSuccess(s); // deliver decrypted result to caller

        }
    }
}
