package com.thz.keystorehelper;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.ProviderException;
import java.security.UnrecoverableEntryException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.security.auth.x500.X500Principal;

/**
 * Handles private, public and symmetric keys logic.
 */
class KeyPairHelper {


    private final String KEYSTORE = "AndroidKeyStore";
    private static final String PRINCIPLE_PREFIX = "CN=";

    private Context context;


    KeyPairHelper(Context context) {
        this.context = context;
    }

    RSAPublicKey getPublicKey(String alias) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, InvalidAlgorithmParameterException, IOException, UnrecoverableEntryException {
        return createPublicKey(alias);
    }

    KeyStore.PrivateKeyEntry getPrivateKey(String alias) throws CertificateException, NoSuchAlgorithmException, IOException, KeyStoreException, UnrecoverableEntryException {

        KeyStore keyStore = KeyStore.getInstance(KEYSTORE);

        keyStore.load(null);

        return (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, null);
    }


    SecretKey getSecretKey(String keystoreFileName, String keystorePassword, String alias, String aliasPassword) throws KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, CertificateException, IOException {

        File keystoreFile = new File(context.getApplicationContext().getFilesDir(), keystoreFileName);

        char[] keyStorePassword = keystorePassword.toCharArray();
        char[] pass = aliasPassword.toCharArray();


        SecretKey secretKey;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (!hasKey(keystoreFile, alias, keyStorePassword)) {
                secretKey = generateSymmetricKey(keystoreFile, alias, pass, keyStorePassword);
            } else {
                secretKey = getSymmetricKey(keystoreFile, alias, keyStorePassword);
            }
        } else {
            if (hasKey(alias)) {
                secretKey = getAndroidSymmetricKey(alias);
            } else {
                secretKey = generateAndroidSymmetricKey(alias);
            }
        }


        return secretKey;

    }

    //----------------------------- ANDROID KEYSTORE METHODS ------------------------------------//

    private SecretKey getAndroidSymmetricKey(String alias) throws KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException {

        KeyStore keyStore = getAndroidKeystore();
        return (SecretKey) keyStore.getKey(alias, null);


    }

    private boolean hasKey(String alias) throws KeyStoreException {

        return getAndroidKeystore().containsAlias(alias);

    }


    @TargetApi(Build.VERSION_CODES.M)
    private SecretKey generateAndroidSymmetricKey(String alias) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 100);
        String ALGORITHM = "AES";
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM, KEYSTORE);
        KeyGenParameterSpec keySpec = getKeyGenParameterSpec(alias, calendar);
        keyGenerator.init(keySpec);
        return keyGenerator.generateKey();

    }

    @TargetApi(Build.VERSION_CODES.M)
    private KeyGenParameterSpec getKeyGenParameterSpec(String alias, Calendar end) {
        int KEY_SIZE = 256;
        return new KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                .setKeySize(KEY_SIZE)
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .setCertificateNotAfter(end.getTime())
                .build();
    }


    private KeyStore getAndroidKeystore() throws KeyStoreException {
        return KeyStore.getInstance(KEYSTORE);
    }


    private RSAPublicKey createPublicKey(String alias) throws ProviderException, NoSuchProviderException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException, KeyStoreException, IOException, CertificateException, UnrecoverableEntryException {


        int serial = 1337;

        KeyStore keyStore = KeyStore.getInstance(KEYSTORE);

        keyStore.load(null);


        if (!keyStore.containsAlias(alias)) {

            Calendar start = new GregorianCalendar();
            Calendar end = new GregorianCalendar();
            end.add(Calendar.YEAR, 100);

            KeyPairGenerator kpGenerator = KeyPairGenerator
                    .getInstance("RSA",
                            KEYSTORE);

            AlgorithmParameterSpec spec = null;

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2 && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                // Below Android M, use the KeyPairGeneratorSpec.Builder.
                spec = getKeyGeneratorSpec(context, alias, serial, start, end);


            } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                // On Android M or above, use the KeyGenParameterSpec.Builder and specify permitted
                // properties  and restrictions of the key.
                spec = getKeyGenParameterSpec(alias, serial, start, end);


            }

            kpGenerator.initialize(spec);

            kpGenerator.generateKeyPair();

        }


        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, null);

        // return public key from Android Keystore
        return (RSAPublicKey) privateKeyEntry.getCertificate().getPublicKey();


    }

    @TargetApi(Build.VERSION_CODES.M)
    private KeyGenParameterSpec getKeyGenParameterSpec(String alias, int serial, Calendar start, Calendar end) {
        return new KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_DECRYPT)
                .setCertificateSubject(new X500Principal(PRINCIPLE_PREFIX + alias))
                .setDigests(KeyProperties.DIGEST_SHA256)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                .setCertificateSerialNumber(BigInteger.valueOf(serial))
                .setCertificateNotBefore(start.getTime())
                .setCertificateNotAfter(end.getTime())
                .build();
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private AlgorithmParameterSpec getKeyGeneratorSpec(Context context, String alias, int serial, Calendar start, Calendar end) {
        AlgorithmParameterSpec spec;
        spec = new KeyPairGeneratorSpec.Builder(context)
                // You'll use the alias later to retrieve the key.  It's a key for the key!
                .setAlias(alias)
                // The subject used for the self-signed certificate of the generated pair
                .setSubject(new X500Principal(PRINCIPLE_PREFIX + alias))
                // The serial number used for the self-signed certificate of the
                // generated pair.
                .setSerialNumber(BigInteger.valueOf(serial))
                // Date range of validity for the generated pair.
                .setStartDate(start.getTime())
                .setEndDate(end.getTime())
                .build();
        return spec;
    }


    //--------------------- DEFAULT JAVA KEYSTORE METHODS --------------------------------//

    private SecretKey generateSymmetricKey(File keystoreFile, String alias, char[] password, char[] keyStorePassword) throws NoSuchAlgorithmException, CertificateException, IOException, KeyStoreException {
        int keySize = 256;
        String algorithm = "AES";


        KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
        keyGenerator.init(keySize);
        SecretKey key = keyGenerator.generateKey();

        KeyStore.SecretKeyEntry keyEntry = new KeyStore.SecretKeyEntry(key);
        KeyStore keyStore = getDefaultKeystore(keystoreFile, keyStorePassword);

        keyStore.setEntry(alias, keyEntry, new KeyStore.PasswordProtection(password));
        keyStore.store(new FileOutputStream(keystoreFile), keyStorePassword);
        return key;


    }


    private SecretKey getSymmetricKey(File keystoreFile, String alias, char[] password) throws CertificateException, NoSuchAlgorithmException, IOException, UnrecoverableKeyException, KeyStoreException {


        KeyStore keyStore = getDefaultKeystore(keystoreFile, password);

        return (SecretKey) keyStore.getKey(alias, password);
    }

    private KeyStore getDefaultKeystore(File keystoreFile, char[] keystorePassword)
            throws CertificateException, NoSuchAlgorithmException, IOException {

        KeyStore keyStore = null;

        if (keystoreFile.exists()) {
            keyStore.load(new FileInputStream(keystoreFile), keystorePassword);
        } else {
            keyStore.load(null);
        }
        return keyStore;
    }

    private boolean hasKey(File keystoreFile, String alias, char[] password) throws CertificateException, NoSuchAlgorithmException, IOException, KeyStoreException {

        KeyStore keyStore = getDefaultKeystore(keystoreFile, password);

        return keyStore != null && keyStore.isKeyEntry(alias);

    }


}
