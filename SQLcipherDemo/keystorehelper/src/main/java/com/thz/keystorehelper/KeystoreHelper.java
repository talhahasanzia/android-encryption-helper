package com.thz.keystorehelper;

import android.content.Context;
import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.security.auth.x500.X500Principal;

/**
 * <p></p><b>Description:</b><p></p> Helper class for low level Keystore operations
 * <p></p>
 */
class KeystoreHelper {
    private static final String CIPHER_TYPE = "RSA/ECB/PKCS1Padding";
    private static final String CIPHER_PROVIDER = "AndroidOpenSSL";
    private static final String ALGORITHM = "RSA";
    private static final String PRINCIPLE = "CN=ABC, O=Android Authority";
    private static final String KEYSTORE = "AndroidKeyStore";
    private static final String CHARSET = "UTF-8";


    private Context context;
    private KeyStore keyStore;

    public KeystoreHelper(Context context) throws KeyStoreException, NoSuchAlgorithmException, IOException, CertificateException {
        this.context = context;
        initKeystore();

    }

    private void initKeystore() throws KeyStoreException, NoSuchAlgorithmException, IOException, CertificateException {

        keyStore = KeyStore.getInstance(KEYSTORE);

        keyStore.load(null);


    }


    public String encryptString(String alias, String plainText) throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, IOException, UnrecoverableEntryException, KeyStoreException, InvalidAlgorithmParameterException {
        String encryptedText = "";
        createKeys(context, alias);
        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, null);
        RSAPublicKey publicKey = (RSAPublicKey) privateKeyEntry.getCertificate().getPublicKey();


        Cipher inCipher = getCipher();
        inCipher.init(Cipher.ENCRYPT_MODE, publicKey);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CipherOutputStream cipherOutputStream = new CipherOutputStream(
                outputStream, inCipher);
        cipherOutputStream.write(plainText.getBytes(CHARSET));
        cipherOutputStream.close();

        byte[] vals = outputStream.toByteArray();
        encryptedText = (Base64.encodeToString(vals, Base64.DEFAULT));

        return encryptedText;
    }

    public String decryptString(String alias, String encryptedText) throws UnrecoverableEntryException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IOException {

        String decryptedText = "";
        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, null);
//        RSAPrivateKey privateKey = (RSAPrivateKey) privateKeyEntry.getPrivateKey();

        Cipher output =getCipher();
        output.init(Cipher.DECRYPT_MODE, privateKeyEntry.getPrivateKey());

        String cipherText = encryptedText;
        CipherInputStream cipherInputStream = new CipherInputStream(
                new ByteArrayInputStream(Base64.decode(cipherText, Base64.DEFAULT)), output);
        ArrayList<Byte> values = new ArrayList<>();
        int nextByte;
        while ((nextByte = cipherInputStream.read()) != -1) {
            values.add((byte) nextByte);
        }

        byte[] bytes = new byte[values.size()];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = values.get(i).byteValue();
        }

        String finalText = new String(bytes, 0, bytes.length, CHARSET);
        decryptedText = (finalText);


        return decryptedText;
    }

    /**
     * Creates RSA Public Private Key pair against given alias.<p></p>
     * Key will be accessible using this alias only.
     *
     * @param context app context
     * @param alias alias against which this RSA pair will be generated.
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     * @throws InvalidAlgorithmParameterException
     * @throws KeyStoreException
     */
    public void createKeys(Context context, String alias) throws NoSuchProviderException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException, KeyStoreException {


        if (!keyStore.containsAlias(alias)) {
            Calendar start = new GregorianCalendar();
            Calendar end = new GregorianCalendar();
            end.add(Calendar.YEAR, 100);
            KeyPairGenerator kpGenerator = KeyPairGenerator
                    .getInstance(ALGORITHM,
                            KEYSTORE);
            AlgorithmParameterSpec spec;

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                // Below Android M, use the KeyPairGeneratorSpec.Builder.

                spec = new KeyPairGeneratorSpec.Builder(context)
                        // You'll use the alias later to retrieve the key.  It's a key for the key!
                        .setAlias(alias)
                        // The subject used for the self-signed certificate of the generated pair
                        .setSubject(new X500Principal("CN=" + alias))
                        // The serial number used for the self-signed certificate of the
                        // generated pair.
                        .setSerialNumber(BigInteger.valueOf(1337))
                        // Date range of validity for the generated pair.
                        .setStartDate(start.getTime())
                        .setEndDate(end.getTime())
                        .build();


            } else {
                // On Android M or above, use the KeyGenparameterSpec.Builder and specify permitted
                // properties  and restrictions of the key.
                spec = new KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_DECRYPT)
                        .setCertificateSubject(new X500Principal("CN=" + alias))
                        .setDigests(KeyProperties.DIGEST_SHA256)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                        .setCertificateSerialNumber(BigInteger.valueOf(1337))
                        .setCertificateNotBefore(start.getTime())
                        .setCertificateNotAfter(end.getTime())
                        .build();
            }

            kpGenerator.initialize(spec);

            KeyPair kp = kpGenerator.generateKeyPair();
        }
    }

    // get Cipher object depending on API level
    private Cipher getCipher() {
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) { // below android m
                // error in android 6: InvalidKeyException: Need RSA private or public key
                return Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidOpenSSL");
            } else {
                // error in android 5: NoSuchProviderException: Provider not available: AndroidKeyStoreBCWorkaround
                return Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidKeyStoreBCWorkaround");
            }
        } catch (Exception exception) {
            throw new RuntimeException("getCipher: Failed to get an instance of Cipher", exception);
        }
    }

}
