package com.playground.sqlcipherdemo.AndroidKeystore;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by Talha Hasan Zia on 19-Jan-18.
 * <p></p><b>Description:</b><p></p> Key manager class for encryption decryption.
 * <p></p>
 */
public class KeyManager {
    private static final KeyManager ourInstance = new KeyManager();

    public static KeyManager getInstance() {
        return ourInstance;
    }

    public String getKey(String inputEncryptedText) {

        return null;
    }

    public String encryptKey(String plainText) {
        return null;
    }


    public String decryptKey(String encryptedText) {

        return null;
    }

    private KeyManager() {
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
