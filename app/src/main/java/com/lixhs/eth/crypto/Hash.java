package com.lixhs.eth.crypto;

import org.bitcoinj.core.Sha256Hash;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


public class Hash {
    public static String keccak256(String str) {
        return NumericUtil.bytesToHex(keccak256(NumericUtil.hexToBytes(str)));
    }

    public static byte[] keccak256(byte[] bArr) {
        return keccak256(bArr, 0, bArr.length);
    }

    public static byte[] generateMac(byte[] bArr, byte[] bArr2) {
        byte[] bArr3 = new byte[(bArr2.length + 16)];
        System.arraycopy(bArr, 16, bArr3, 0, 16);
        System.arraycopy(bArr2, 0, bArr3, 16, bArr2.length);
        return keccak256(bArr3);
    }

    public static String sha256(String str) {
        return NumericUtil.bytesToHex(sha256(NumericUtil.hexToBytes(str)));
    }

    public static byte[] sha256(byte[] bArr) {
        return sha256(bArr, 0, bArr.length);
    }

    private static byte[] keccak256(byte[] bArr, int i, int i2) {
        Keccak keccak = new Keccak(256);
        keccak.update(bArr, i, i2);
        return keccak.digest().array();
    }

    private static byte[] sha256(byte[] bArr, int i, int i2) {
        try {
            MessageDigest instance = MessageDigest.getInstance("SHA-256");
            instance.update(bArr, i, i2);
            return instance.digest();
        } catch (Exception unused) {
            throw new RuntimeException("sha256");
        }
    }

    public static byte[] hmacSHA256(byte[] bArr, byte[] bArr2) {

        try {
            Mac instance = Mac.getInstance("HmacSHA256");
            instance.init(new SecretKeySpec(bArr, "HmacSHA256"));
            return instance.doFinal(bArr2);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] merkleHash(byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            throw new IllegalArgumentException("data should not be null");
        }
        int i;
        List arrayList = new ArrayList();
        int i2 = 0;
        while (i2 < bArr.length) {
            i = i2 + 1024;
            arrayList.add(Sha256Hash.hashTwice(Arrays.copyOfRange(bArr, i2, Math.min(i, bArr.length))));
            i2 = i;
        }
        i2 = 0;
        for (int size = arrayList.size(); size > 1; size = (size + 1) / 2) {
            for (i = 0; i < size; i += 2) {
                arrayList.add(Sha256Hash.hashTwice(ByteUtil.concat((byte[]) arrayList.get(i2 + i), (byte[]) arrayList.get(Math.min(i + 1, size - 1) + i2))));
            }
            i2 += size;
        }
        return (byte[]) arrayList.get(arrayList.size() - 1);
    }

}