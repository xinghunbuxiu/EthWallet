package com.lixhs.eth.crypto;

import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {

    public enum AESType {
        CTR,
        CBC
    }

    public static byte[] encryptByCTR(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        return doAES(bArr, bArr2, bArr3, 1, AESType.CTR, "PKCS5Padding");
    }

    public static byte[] decryptByCTR(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        return doAES(bArr, bArr2, bArr3, 2, AESType.CTR, "PKCS5Padding");
    }

    public static byte[] encryptByCBC(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        return doAES(bArr, bArr2, bArr3, 1, AESType.CBC, "PKCS5Padding");
    }

    public static byte[] decryptByCBC(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        return doAES(bArr, bArr2, bArr3, 2, AESType.CBC, "PKCS5Padding");
    }

    public static byte[] encryptByCTRNoPadding(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        return doAES(bArr, bArr2, bArr3, 1, AESType.CTR, "NoPadding");
    }

    public static byte[] decryptByCTRNoPadding(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        return doAES(bArr, bArr2, bArr3, 2, AESType.CTR, "NoPadding");
    }

    public static byte[] encryptByCBCNoPadding(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        return doAES(bArr, bArr2, bArr3, 1, AESType.CBC, "NoPadding");
    }

    public static byte[] decryptByCBCNoPadding(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        return doAES(bArr, bArr2, bArr3, 2, AESType.CBC, "NoPadding");
    }

    private static byte[] doAES(byte[] bArr, byte[] bArr2, byte[] bArr3, int i, AESType aESType, String str) {
        String str2 = aESType == AESType.CBC ? "CBC" : "CTR";
        try {
            AlgorithmParameterSpec ivParameterSpec = new IvParameterSpec(bArr3);
            Key secretKeySpec = new SecretKeySpec(bArr2, "AES");
            Cipher instance = Cipher.getInstance(String.format("AES/%s/%s", new Object[]{str2, str}));
            instance.init(i, secretKeySpec, ivParameterSpec);
            return instance.doFinal(bArr);
        } catch (Exception unused) {
            return new byte[0];
        }
    }
}