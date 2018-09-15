package com.thz.keystorehelper;

import android.annotation.SuppressLint;
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
 * <b>{@link #init(Context)} :</b> initialize this manager with app context.
 * <p></p>
 * <b>{@link #decryptData(String, String)}:</b> to decrypt any encrypted data.
 * <p></p>
 * <b>{@link #encryptData(String, String)}:</b> to encrypt any data.
 */
public class KeyStoreManager {


    @SuppressLint("StaticFieldLeak")
    private static KeystoreHelper keystoreHelper; // keystore helper for low level operations

    /**
     * Initialize Keystore Manager once with app context
     *
     * @param context Context of app
     */
    public static void init(Context context) {

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
     * Get a random phrase containing numbers and characters, of length specified.
     *
     * @param length length of string to be generated
     * @return A random string.
     */
    public static String getNewRandomPhrase(int length) {
        Random randomizer = new Random();

        // create a random string using alphabets and characters
        char[] charactersRange = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();


        char[] randomData = new char[length];

        for (int i = 0; i < length - 1; i++) {

            int randomIndex = randomizer.nextInt(35 + 1);

            randomData[i] = charactersRange[randomIndex];
        }

        return Arrays.toString(randomData).replaceAll("[^a-zA-Z0-9]", "");
    }

    /**
     * Use this to encrypt any text or data.
     * Provide alias to fetch keys from keystore.
     * If key is not present a new one is generated.
     * To change this flow you can edit {@link KeystoreHelper} class in library source available at:
     * https://github.com/talhahasanzia/android-encryption-helper
     *
     * @param plainText to be encrypted
     * @param alias     alias against which key will be used.
     * @return an encrypted string
     */
    public static String encryptData(String plainText, String alias) {


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
     * Encrypts an encrypted key against given alias <b>in the background</b>. Caller must pass a reference to {@link EncryptionDecryptionListener} that will deliver the results. <p></p>
     * This will get private key generated against this alias, and use it to decrypt.
     * If alias dont match or key is not persistent, this decryption will fail.
     * Caller must implement onSuccess() and onFailure() methods of {@link EncryptionDecryptionListener} to receive result or errors after background process is completed.
     * <p>
     * To change this implementation you can edit {@link KeystoreHelper} class in library source available at:
     * https://github.com/talhahasanzia/android-encryption-helper
     *
     * @param data                         Encrypted text that was encrypted using a private key using alias mention in next argument
     * @param alias                        alias to use for decryption.
     * @param encryptionDecryptionListener a listener that Activity or Fragment must implement (or have its objects)
     *                                     <br> to receive results or failure messages after backgroung operations.
     */
    public static void encryptDataAsync(String data, String alias, EncryptionDecryptionListener encryptionDecryptionListener) {



            keystoreHelper.encryptStringAsync(alias, data, encryptionDecryptionListener);

    }


    /**
     * Decrypts an encrypted key against given alias. <p></p>
     * This will get private key generated against this alias, and use it to decrypt.
     * If alias dont match or key is not persistent, this decryption will fail.
     * To change this implementation you can edit {@link KeystoreHelper} class in library source available at:
     * https://github.com/talhahasanzia/android-encryption-helper
     *
     * @param encryptedText Encrypted text that was encrypted using a private key using alias mention in next argument
     * @param alias         alias to use for decryption.
     * @return plain text if successful decryption happens
     */
    public static String decryptData(String encryptedText, String alias) {

        try {
            return keystoreHelper.decryptString(alias, encryptedText);
        } catch (UnrecoverableEntryException | NoSuchAlgorithmException | KeyStoreException | NoSuchProviderException | NoSuchPaddingException |
                InvalidKeyException | IOException e) {
            return e.getMessage();
        }
    }


    /**
     * Decrypts an encrypted key against given alias <b>in the background</b>. Caller must pass a reference to {@link EncryptionDecryptionListener} that will deliver the results. <p></p>
     * This will get private key generated against this alias, and use it to decrypt.
     * If alias dont match or key is not persistent, this decryption will fail.
     * Caller must implement onSuccess() and onFailure() methods of {@link EncryptionDecryptionListener} to receive result or errors after background process is completed.
     * <p>
     * To change this implementation you can edit {@link KeystoreHelper} class in library source available at:
     * https://github.com/talhahasanzia/android-encryption-helper
     *
     * @param data                         Encrypted text that was encrypted using a private key using alias mention in next argument
     * @param alias                        alias to use for decryption.
     * @param encryptionDecryptionListener a listener that Activity or Fragment must implement (or have its objects)
     *                                     <br> to receive results or failure messages after backgroung operations.
     */
    public static void decryptDataAsync(String data, String alias, EncryptionDecryptionListener encryptionDecryptionListener) {

            keystoreHelper.decryptStringAsync(alias, data, encryptionDecryptionListener);

    }


}
