package com.mamba.prepladder.Helper;

import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptLib {
    public static final String TAG = GlobalHelper.TAG;
    public static final String KEY = "qiLVRZyaPR1e";
    public static final String APIKEY = "prpldr_1626957459:60f966934f259:1067895";
    private Cipher _cx;
    private byte[] _iv;
    private byte[] _key;

    public CryptLib(){
        try{
             _cx = Cipher.getInstance("AES/CBC/PKCS5Padding");
            _iv = new byte[16];
            _key = new byte[32];
        }catch (Exception ex){
            Log.e(TAG,ex.toString());
        }
    }

    /* access modifiers changed from: private */
    public enum EncryptMode {
        ENCRYPT,
        DECRYPT
    }

    private static String SHA256(String str, int i2) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest instance = MessageDigest.getInstance("SHA-256");
        instance.update(str.getBytes("UTF-8"));
        byte[] digest = instance.digest();
        StringBuilder sb = new StringBuilder();
        int length = digest.length;
        for (int i3 = 0; i3 < length; i3++) {
            sb.append(String.format("%02x", Byte.valueOf(digest[i3])));
        }
        if (i2 > sb.toString().length()) {
            return sb.toString();
        }
        return sb.toString().substring(0, i2);
    }

    private byte[] encryptDecrypt(String str, String str2, EncryptMode encryptMode, String str3) throws UnsupportedEncodingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        int length = str2.getBytes("UTF-8").length;
        int length2 = str2.getBytes("UTF-8").length;
        byte[] bArr = this._key;
        if (length2 > bArr.length) {
            length = bArr.length;
        }
        int length3 = str3.getBytes("UTF-8").length;
        int length4 = str3.getBytes("UTF-8").length;
        byte[] bArr2 = this._iv;
        if (length4 > bArr2.length) {
            length3 = bArr2.length;
        }
        System.arraycopy(str2.getBytes("UTF-8"), 0, this._key, 0, length);
        System.arraycopy(str3.getBytes("UTF-8"), 0, this._iv, 0, length3);
        SecretKeySpec secretKeySpec = new SecretKeySpec(this._key, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(this._iv);
        if (encryptMode.equals(EncryptMode.ENCRYPT)) {
            this._cx.init(1, secretKeySpec, ivParameterSpec);
            return this._cx.doFinal(str.getBytes("UTF-8"));
        }
        this._cx.init(2, secretKeySpec, ivParameterSpec);
        return this._cx.doFinal(Base64.decode(str.getBytes(), 0));
    }

    public String decryptCipherText(String str, String str2, String str3) throws Exception {
        return new String(encryptDecrypt(str, SHA256(str2, 32), EncryptMode.DECRYPT, str3));
    }

    public String decryptCipherTextWithRandomIV(String str, String str2) throws Exception {
        String str3 = new String(encryptDecrypt(str, SHA256(str2, 32), EncryptMode.DECRYPT, generateRandomIV16()));
        return str3.substring(16, str3.length());
    }

    public String encryptPlainText(String str, String str2, String str3) throws Exception {
        return Base64.encodeToString(encryptDecrypt(str, SHA256(str2, 32), EncryptMode.ENCRYPT, str3), 0);
    }

    public String encryptPlainTextWithRandomIV(String str, String str2) throws Exception {
        return Base64.encodeToString(encryptDecrypt(generateRandomIV16() + str, SHA256(str2, 32), EncryptMode.ENCRYPT, generateRandomIV16()), 0);
    }

    public String generateRandomIV16() {
        byte[] bArr = new byte[16];
        new SecureRandom().nextBytes(bArr);
        StringBuilder sb = new StringBuilder();
        for (int i2 = 0; i2 < 16; i2++) {
            sb.append(String.format("%02x", Byte.valueOf(bArr[i2])));
        }
        if (16 > sb.toString().length()) {
            return sb.toString();
        }
        return sb.toString().substring(0, 16);
    }
}
