package com.baidu.baselibrary.util.crypto;

import android.content.Context;
import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.baidu.baselibrary.util.App;
import com.baidu.baselibrary.global.Const;
import com.base.global.PreferencesUtil;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.x500.X500Principal;

public class CipherHelper {

    private static final String TAG = "CipherHelper";
    private static final String KEYSTORE_PROVIDER = "AndroidKeyStore";
    private static final String AES_MODE = "AES/CBC/PKCS5PADDING";
    private static final String RSA_MODE = "RSA/ECB/PKCS1Padding";
    private static final String KEYSTORE_ALIAS = "xcyh";

    private static KeyStore keyStore;

    static {
        try {
            keyStore = KeyStore.getInstance(KEYSTORE_PROVIDER);
            keyStore.load(null);

            if (!keyStore.containsAlias(KEYSTORE_ALIAS)) {
                genKeyStoreKey(App.getContext());
                genAESKey();
            }
        } catch (Exception e) {
            Log.d(TAG, Log.getStackTraceString(e));
        }
    }

    private static void genKeyStoreKey(Context context) throws Exception {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            generateRSAKey_AboveApi23();
        } else {
            generateRSAKey_BelowApi23(context);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private static void generateRSAKey_AboveApi23() throws Exception {
        KeyPairGenerator keyPairGenerator =
                KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, KEYSTORE_PROVIDER);

        KeyGenParameterSpec keyGenParameterSpec = new KeyGenParameterSpec.Builder(
                KEYSTORE_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                .build();

        keyPairGenerator.initialize(keyGenParameterSpec);
        keyPairGenerator.generateKeyPair();
    }

    private static void generateRSAKey_BelowApi23(Context context)
            throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        end.add(Calendar.YEAR, 30);

        KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(context)
                .setAlias(KEYSTORE_ALIAS)
                .setSubject(new X500Principal("CN=" + KEYSTORE_ALIAS))
                .setSerialNumber(BigInteger.TEN)
                .setStartDate(start.getTime())
                .setEndDate(end.getTime())
                .build();

        KeyPairGenerator keyPairGenerator =
                KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, KEYSTORE_PROVIDER);
        keyPairGenerator.initialize(spec);
        keyPairGenerator.generateKeyPair();
    }

    public static String encrypt(String plainText) {
        try {
            return encryptAES(plainText);
        } catch (Exception e) {
            Log.d(TAG, Log.getStackTraceString(e));
            return "";
        }
    }

    public static String encryptWithIv(String plainText) {
        if (TextUtils.isEmpty(plainText)) return "";
        try {
            String result = encryptAESAppendIV(plainText);
            return result == null ? "" : result;
        } catch (Exception e) {
            Log.d(TAG, Log.getStackTraceString(e));
            return "";
        }
    }

    public static String decrypt(String encryptedText) {
        try {
            return decryptAES(encryptedText);
        } catch (Exception e) {
            Log.d(TAG, Log.getStackTraceString(e));
            return "";
        }
    }

    public static String decryptWithIv(String encryptedText) {
        try {
            return decryptAESWithAppendIv(encryptedText);
        } catch (Exception e) {
            Log.d(TAG, Log.getStackTraceString(e));
            return "";
        }
    }

    private static String encryptRSA(byte[] plainText) throws Exception {
        PublicKey publicKey = keyStore.getCertificate(KEYSTORE_ALIAS).getPublicKey();
        Cipher cipher = Cipher.getInstance(RSA_MODE);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedByte = cipher.doFinal(plainText);
        return Base64.encodeToString(encryptedByte, Base64.DEFAULT);
    }

    private static byte[] decryptRSA(String encryptedText) throws Exception {
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(KEYSTORE_ALIAS, null);
        Cipher cipher = Cipher.getInstance(RSA_MODE);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] encryptedBytes = Base64.decode(encryptedText, Base64.DEFAULT);
        return cipher.doFinal(encryptedBytes);
    }

    private static void genAESKey() throws Exception {
        byte[] aesKey = "GBVd5pVE6WT5DUuM".getBytes();
        byte[] generated = "0010100000011000".getBytes();
        String iv = Base64.encodeToString(generated, Base64.NO_WRAP);
        PreferencesUtil.put(Const.PREF_KEY_IV, iv);
        String encryptAESKey = encryptRSA(aesKey);
        PreferencesUtil.put(Const.PREF_KEY_AES, encryptAESKey);
    }

    private static String encryptAES(String plainText) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_MODE);
        cipher.init(Cipher.ENCRYPT_MODE, getAESKey(), new IvParameterSpec(getIV()));
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
        return Base64.encodeToString(encryptedBytes, Base64.NO_WRAP);
    }


    private static String encryptAESAppendIV(String plainText) {
        try {
            Cipher cipher = Cipher.getInstance(AES_MODE);
            cipher.init(Cipher.ENCRYPT_MODE, getAESKey(), new IvParameterSpec(getIV()));

            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());

            byte[] ivPlusCipher = new byte[getIV().length + encryptedBytes.length];
            System.arraycopy(getIV(), 0, ivPlusCipher, 0, getIV().length);
            System.arraycopy(encryptedBytes, 0, ivPlusCipher, getIV().length, encryptedBytes.length);

            return Base64.encodeToString(ivPlusCipher, Base64.NO_WRAP);

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String decryptAESWithAppendIv(String encryptedText) throws Exception {
        byte[] decodedBytes = Base64.decode(encryptedText.getBytes(), Base64.NO_WRAP);

        byte[] cleanDecodeBytes = new byte[decodedBytes.length - getIV().length];
        System.arraycopy(decodedBytes, getIV().length, cleanDecodeBytes, 0, cleanDecodeBytes.length);

        Cipher cipher = Cipher.getInstance(AES_MODE);
        cipher.init(Cipher.DECRYPT_MODE, getAESKey(), new IvParameterSpec(getIV()));

        byte[] decryptedBytes = cipher.doFinal(cleanDecodeBytes);

        return new String(decryptedBytes);
    }

    private static String decryptAES(String encryptedText) throws Exception {
        byte[] decodedBytes = Base64.decode(encryptedText.getBytes(), Base64.NO_WRAP);
        Cipher cipher = Cipher.getInstance(AES_MODE);
        cipher.init(Cipher.DECRYPT_MODE, getAESKey(), new IvParameterSpec(getIV()));
        return new String(cipher.doFinal(decodedBytes));
    }

    private static SecretKeySpec getAESKey() throws Exception {
        String encryptedKey = PreferencesUtil.get(Const.PREF_KEY_AES, "");
        byte[] aesKey = decryptRSA(encryptedKey);
        return new SecretKeySpec(aesKey, "AES");
    }

    private static byte[] getIV() {
        String prefIV = PreferencesUtil.get(Const.PREF_KEY_IV, "");
        return Base64.decode(prefIV, Base64.NO_WRAP);
    }
}
