package com.playground.sqlcipherdemo.AndroidKeystore;

import android.content.Context;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

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
        this.context=context;

        try {
            keystoreHelper=new KeystoreHelper(context);
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

    public PrivateKey getKey(String alias) {

        return null;
    }

    public PrivateKey getNewKey()
    {
        return null;
    }

    public String getNewRandomPhrase() {

        return getRandomText();
    }


    public String encryptKey(String plainText) {


        return null;
    }

    public String decryptKey(String encryptedText) {

        return null;
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

    void generatingNewEntries() {

    }



}
