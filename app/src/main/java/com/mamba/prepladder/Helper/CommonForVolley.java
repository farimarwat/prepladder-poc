package com.mamba.prepladder.Helper;

import android.util.Base64;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CommonForVolley {
    public static String GenerateAESKey() {
        char[] charArray = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i2 = 0; i2 < 32; i2++) {
            sb.append(charArray[random.nextInt(charArray.length)]);
        }
        return sb.toString();
    }

    public static String GenerateIVKey() {
        char[] charArray = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i2 = 0; i2 < 16; i2++) {
            sb.append(charArray[random.nextInt(charArray.length)]);
        }
        return sb.toString();
    }

    public static String GenerateRendomKey() {
        char[] charArray = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i2 = 0; i2 < 32; i2++) {
            sb.append(charArray[random.nextInt(charArray.length)]);
        }
        return sb.toString();
    }

    public static String GetData(String str, String str2) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new CredentailsApp(str.substring(0, 32), str.substring(32, 48), str.substring(48, str.length() - 10), str.substring(str.length() - 10, str.length())));
        if (encrypt(((CredentailsApp) arrayList.get(0)).getRendom(), ((CredentailsApp) arrayList.get(0)).getIv(), str2).substring(0, 10).equals(((CredentailsApp) arrayList.get(0)).getId())) {
            return decrypt(str2, ((CredentailsApp) arrayList.get(0)).getIv(), ((CredentailsApp) arrayList.get(0)).getData());
        }
        return null;
    }

    public static String decrypt(String str, String str2, String str3) {
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(str2.getBytes());
            SecretKeySpec secretKeySpec = new SecretKeySpec(str.getBytes(), "AES");
            Cipher instance = Cipher.getInstance("AES/CBC/ZeroBytePadding");
            instance.init(2, secretKeySpec, ivParameterSpec);
            return new String(instance.doFinal(Base64.decode(str3, 0)));
        } catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public static String encrypt(String str, String str2, String str3) {
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(str2.getBytes("UTF-8"));
            SecretKeySpec secretKeySpec = new SecretKeySpec(str.getBytes("UTF-8"), "AES");
            Cipher instance = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            instance.init(1, secretKeySpec, ivParameterSpec);
            return Base64.encodeToString(instance.doFinal(str3.getBytes()), 0);
        } catch (Exception unused) {
            return null;
        }
    }

    public static String encryptData(String str) {
        try {
            PublicKey generatePublic = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.decode("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC8PFTNKVlGRyeohEf+ssV6ghrPLKCf/NAGGmuWXR19q461O0eT3mpKGDbhnLNhjcN8RuTWpHgLgFFc1T2OO+F1JiMyXI9b6EEmdmsIB3njGFkmla2/Vy6iPwGWm3pesOEHDMpZoJ1VMVGmwLUps3UgrM8FcaALlRMCaUw0I0tcEQIDAQAB", 0)));
            Cipher instance = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            instance.init(1, generatePublic);
            return Base64.encodeToString(instance.doFinal(str.getBytes()), 0);
        } catch (Exception unused) {
            return "";
        }
    }
}