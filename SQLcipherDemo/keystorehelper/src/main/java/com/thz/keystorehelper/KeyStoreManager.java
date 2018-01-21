package com.thz.keystorehelper;

import android.content.Context;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.NoSuchPaddingException;

/**
 * <b>Description:</b><p></p> KeyStoreManager class for encryption decryption.
 * <p></p>
 * <b>Public Methods:</b>
 * <p></p>
 * <b>{@link #getInstance(Context)}:</b> to get an instance of this class and start using.
 * <p></p>
 * <b>{@link #decryptData(String, String)}:</b> to decrypt any encrypted data.
 * <p></p>
 *  <b>{@link #encryptData(String, String)}:</b> to encrypt any data.
 */
public class KeyStoreManager {


    private static KeyStoreManager instance; // instance for public usage
    private KeystoreHelper keystoreHelper; // keystore helper for low level operations
    private Context context; // app's context


    public static KeyStoreManager getInstance(Context context) {
        if (instance == null) // check for initialization
            instance = new KeyStoreManager(context); // initialize

        return instance;
    }

    /**
     * Private contructor.
     * @param context app's context
     */
    private KeyStoreManager(Context context) {
        this.context = context;

        try {
            keystoreHelper = new KeystoreHelper(context);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get a random phrase containing numbers and characters, of length 50-100.
     * @return A random string.
     */
    public String getNewRandomPhrase() {

        return getRandomText();
    }

    /**
     * Use this to encrypt any text or data.
     * Provide alias to fetch keys from keystore.
     * If key is not present a new one is generated.
     * To change this flow you can edit {@link KeystoreHelper} class in library source available at:
     * https://github.com/talhahasanzia/android-encryption-helper
     * @param plainText to be encrypted
     * @param alias alias against which key will be used.
     * @return an encrypted string
     */
    public String encryptData(String plainText, String alias) {


        try {
            return keystoreHelper.encryptString(alias, plainText);
        } catch (NoSuchPaddingException e) {
            return e.getMessage();
        } catch (NoSuchAlgorithmException e) {
            return e.getMessage();
        } catch (NoSuchProviderException e) {
            return e.getMessage();
        } catch (InvalidKeyException e) {
            return e.getMessage();
        } catch (IOException e) {
            return e.getMessage();
        } catch (UnrecoverableEntryException e) {
            return e.getMessage();
        } catch (KeyStoreException e) {
            return e.getMessage();
        } catch (InvalidAlgorithmParameterException e) {
            return e.getMessage();
        }
    }

    /**
     * Decrypts an encrypted key against given alias. <p></p>
     * This will get private key generated against this alias, and use it to decrypt.
     * If alias dont match or key is not persistent, this decryption will fail.
     * To change this implementation you can edit {@link KeystoreHelper} class in library source available at:
     * https://github.com/talhahasanzia/android-encryption-helper
     *
     * @param encryptedText Encrypted text that was encrypted using a private key using alias mention in next argument
     * @param alias alias to use for decryption.
     * @return plain text if successful decryption happens
     */
    public String decryptData(String encryptedText, String alias) {

        try {
            return keystoreHelper.decryptString(alias, encryptedText);
        } catch (UnrecoverableEntryException e) {
            return e.getMessage();
        } catch (NoSuchAlgorithmException e) {
            return e.getMessage();
        } catch (KeyStoreException e) {
            return e.getMessage();
        } catch (NoSuchProviderException e) {
            return e.getMessage();
        } catch (NoSuchPaddingException e) {
            return e.getMessage();
        } catch (InvalidKeyException e) {
            return e.getMessage();
        } catch (IOException e) {
            return e.getMessage();
        }
    }


    private String getRandomText() {
        Random randomizer = new Random();

        // create a random string using alphabets and characters
        char[] charactersRange = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();

        int n = randomizer.nextInt(100 - 50 + 1) + 50; //


        char[] randomData = new char[n];

        for (int i = 0; i < n - 1; i++) {

            int randomIndex = randomizer.nextInt(35 - 0 + 1) + 0;

            randomData[i] = charactersRange[randomIndex];
        }

        return Arrays.toString(randomData);
    }


}
