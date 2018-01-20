package com.playground.sqlcipherdemo.AndroidKeystore;

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
 * Created by Talha Hasan Zia on 19-Jan-18.
 * <p></p><b>Description:</b><p></p> Key manager class for encryption decryption.
 * <p></p>
 */
public class KeyManager {


    private static KeyManager instance;
    private KeystoreHelper keystoreHelper;
    private Context context;


    public static KeyManager getInstance(Context context) {
        if (instance == null)
            instance = new KeyManager(context);

        return instance;
    }

    private KeyManager(Context context) {
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


    public String getNewRandomPhrase() {

        return getRandomText();
    }


    public String encryptKey(String plainText, String alias) {


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

    public String decryptKey(String encryptedText, String alias) {

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
