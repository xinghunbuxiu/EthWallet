package com.lixhs.eth.crypto;

import java.util.Arrays;

public class ByteUtil {
    private static byte[] trimLeadingBytes(byte[] bArr, byte b) {
        int i = 0;
        while (i < bArr.length - 1 && bArr[i] == b) {
            i++;
        }
        return Arrays.copyOfRange(bArr, i, bArr.length);
    }

    public static byte[] trimLeadingZeroes(byte[] bArr) {
        return trimLeadingBytes(bArr, (byte) 0);
    }

    public static byte[] concat(byte[] bArr, byte[] bArr2) {
        byte copyOf[] = Arrays.copyOf(bArr, bArr.length + bArr2.length);
        System.arraycopy(bArr2, 0, copyOf, bArr.length, bArr2.length);
        return copyOf;
    }
}