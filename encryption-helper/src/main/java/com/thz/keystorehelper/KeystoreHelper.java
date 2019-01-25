package com.thz.keystorehelper;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.ProviderException;
import java.security.UnrecoverableEntryException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 * <p></p><b>Description:</b><p></p> Helper class for low level Keystore operations
 * <p></p>
 */
class KeystoreHelper {

    private final String CIPHER_TYPE_AES = "AES/CBC/PKCS7Padding";
    private final String CIPHER_TYPE_RSA = "RSA/ECB/PKCS1Padding";
    private final String CHARSET = "UTF-8";


    private KeyPairHelper keyPairHelper;

    KeystoreHelper(Context context) {
        keyPairHelper = new KeyPairHelper(context);
    }


    String encryptString(String keyStoreFile, String keyStorePassword, String alias, String aliasPassword, String plainText) throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, InvalidAlgorithmParameterException, IOException, NoSuchPaddingException, InvalidKeyException {
        String encryptedText;

        SecretKey key = keyPairHelper.getSecretKey(keyStoreFile, keyStorePassword, alias, aliasPassword);

        Cipher cipher = Cipher.getInstance(CIPHER_TYPE_AES);
        cipher.init(Cipher.ENCRYPT_MODE, key);


        byte[] encryptedBytes = decode(cipher, plainText.getBytes());

        encryptedText = (Base64.encodeToString(encryptedBytes, Base64.DEFAULT));


        return encryptedText;

    }

    void encryptStringAsync(String keyStoreFile, String keyStorePassword, String alias, String aliasPassword, String plainText, EncryptionDecryptionListener encryptionDecryptionListener) {

        CryptoTasks cryptoTasks = new CryptoTasks(this);
        cryptoTasks.executeEncryptionTask(keyStoreFile, keyStorePassword, alias, aliasPassword, plainText, encryptionDecryptionListener);

    }


    void decryptStringAsync(String keyStoreFile, String keyStorePassword, String alias, String aliasPassword, String encryptedText, EncryptionDecryptionListener encryptionDecryptionListener) {


        CryptoTasks cryptoTasks = new CryptoTasks(this);
        cryptoTasks.executeDecryptionTask(keyStoreFile, keyStorePassword, alias, aliasPassword, encryptedText, encryptionDecryptionListener);
    }


    String decryptString(String keyStoreFile, String keyStorePassword, String alias, String aliasPassword, String encryptedText) throws Exception {

        String decryptedText;

        SecretKey key = keyPairHelper.getSecretKey(keyStoreFile, keyStorePassword, alias, aliasPassword);

        Cipher cipher = Cipher.getInstance(CIPHER_TYPE_AES);
        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] decryptedBytes = decode(cipher, encryptedText.getBytes());


        decryptedText = new String(decryptedBytes, CHARSET);


        return decryptedText;
    }

    private byte[] decode(Cipher cipher, byte[] plainData)
            throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CipherOutputStream cipherOutputStream = new CipherOutputStream(baos, cipher);
        cipherOutputStream.write(plainData);
        cipherOutputStream.close();
        return baos.toByteArray();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    String asymmetricDecryptString(String alias, String encryptedText) throws ProviderException, UnrecoverableEntryException, NoSuchAlgorithmException, KeyStoreException, NoSuchPaddingException, InvalidKeyException, IOException, CertificateException {

        String decryptedText;

        KeyStore.PrivateKeyEntry key = keyPairHelper.getPrivateKey(alias);

        Cipher cipher = Cipher.getInstance(CIPHER_TYPE_RSA);
        cipher.init(Cipher.DECRYPT_MODE, key.getPrivateKey());

        byte[] decryptedBytes = decode(cipher, encryptedText.getBytes());


        decryptedText = new String(decryptedBytes, CHARSET);


        return decryptedText;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    String asymmetricEncryptString(String alias, String plainText) throws Exception {

        String decryptedText;

        RSAPublicKey key = keyPairHelper.getPublicKey(alias);

        Cipher cipher = Cipher.getInstance(CIPHER_TYPE_RSA);
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] decryptedBytes = decode(cipher, plainText.getBytes());

        decryptedText = new String(decryptedBytes, CHARSET);


        return decryptedText;
    }


}

