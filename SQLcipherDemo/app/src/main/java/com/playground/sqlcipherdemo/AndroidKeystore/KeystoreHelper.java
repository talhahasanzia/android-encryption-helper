package com.playground.sqlcipherdemo.AndroidKeystore;

import android.content.Context;
import android.security.KeyPairGeneratorSpec;
import android.util.Base64;

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
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.security.auth.x500.X500Principal;

/**
 * Created by Talha Hasan Zia on 20-Jan-18.
 * <p></p><b>Description:</b><p></p> Why class was created?
 * <p></p>
 * <b>Public Methods:</b><p></p> Only listing to public methods usage.
 */
public class KeystoreHelper {
    private static final String CIPHER_TYPE = "RSA/ECB/PKCS1Padding";
    private static final String CIPHER_PROVIDER = "AndroidOpenSSL";
    private static final String ALGORITHM = "RSA";
    private static final String PRINCIPLE = "CN=Sample Name, O=Android Authority";
    private static final String KEYSTORE = "AndroidKeystore";
    private static final String CHARSET="UTF-8";


    private List<String> keyAliases;


    private Context context;
    private KeyStore keyStore;

    public KeystoreHelper(Context context) throws KeyStoreException, NoSuchAlgorithmException, IOException, CertificateException {
        this.context = context;
        initKeystore();

    }

    private void initKeystore() throws KeyStoreException, NoSuchAlgorithmException, IOException, CertificateException {
        keyStore = KeyStore.getInstance(KEYSTORE);

        keyStore.load(null);


        refreshKeys();


    }

    public List<String> refreshKeys() throws KeyStoreException {
        keyAliases = new ArrayList<>();
        Enumeration<String> aliases = keyStore.aliases();
        while (aliases.hasMoreElements()) {
            keyAliases.add(aliases.nextElement());
        }
        return keyAliases;

    }

    public KeyPair createNewKeys(String alias) throws NullPointerException, KeyStoreException, InvalidAlgorithmParameterException, NoSuchProviderException, NoSuchAlgorithmException {
        KeyPair keyPair = null;

        // Create new key if needed
        if (!keyStore.containsAlias(alias)) {
            Calendar start = Calendar.getInstance();
            Calendar end = Calendar.getInstance();
            end.add(Calendar.YEAR, 1);
            KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(context)
                    .setAlias(alias)
                    .setSubject(new X500Principal(PRINCIPLE))
                    .setSerialNumber(BigInteger.ONE)
                    .setStartDate(start.getTime())
                    .setEndDate(end.getTime())
                    .build();
            KeyPairGenerator generator = KeyPairGenerator.getInstance(ALGORITHM, KEYSTORE);
            generator.initialize(spec);

            keyPair = generator.generateKeyPair();

        }
        refreshKeys();

        return keyPair;
    }

    public void deleteKey(final String alias) throws KeyStoreException {
        keyStore.deleteEntry(alias);
        refreshKeys();

    }

    public String encryptString(String alias, String initialText) throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, IOException, UnrecoverableEntryException, KeyStoreException {
        String encryptedText = "";

        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, null);
        RSAPublicKey publicKey = (RSAPublicKey) privateKeyEntry.getCertificate().getPublicKey();


        Cipher inCipher = Cipher.getInstance(CIPHER_TYPE, CIPHER_PROVIDER);
        inCipher.init(Cipher.ENCRYPT_MODE, publicKey);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CipherOutputStream cipherOutputStream = new CipherOutputStream(
                outputStream, inCipher);
        cipherOutputStream.write(initialText.getBytes(CHARSET));
        cipherOutputStream.close();

        byte[] vals = outputStream.toByteArray();
        encryptedText = (Base64.encodeToString(vals, Base64.DEFAULT));

        return encryptedText;
    }

    public String decryptString(String alias, String encryptedText) throws UnrecoverableEntryException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IOException {
        String decryptedText = "";
        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, null);
        RSAPrivateKey privateKey = (RSAPrivateKey) privateKeyEntry.getPrivateKey();

        Cipher output = Cipher.getInstance(CIPHER_TYPE, CIPHER_PROVIDER);
        output.init(Cipher.DECRYPT_MODE, privateKey);

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

}
