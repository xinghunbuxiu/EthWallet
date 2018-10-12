package com.lixhs.eth.crypto;


import com.google.common.base.Strings;

import java.math.BigInteger;
import java.nio.ByteOrder;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.regex.Pattern;

public class NumericUtil {
    private static final String HEX_PREFIX = "0x";
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static byte[] generateRandomBytes(int i) {
        byte[] bArr = new byte[i];
        SECURE_RANDOM.nextBytes(bArr);
        return bArr;
    }

    public static boolean isValidHex(String str) {
        if (str == null) {
            return false;
        }
        CharSequence str2;
        if (str.startsWith(HEX_PREFIX) || str.startsWith("0X")) {
            str2 = str.substring(2, str.length());
        }
        if (str.length() == 0 || str.length() % 2 != 0) {
            return false;
        }
        return Pattern.matches("[0-9a-fA-F]+", str);
    }

    public static String cleanHexPrefix(String str) {
        return hasHexPrefix(str) ? str.substring(2) : str;
    }

    public static String prependHexPrefix(String str) {
        if (str.length() <= 1 || hasHexPrefix(str)) {
            return str;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(HEX_PREFIX);
        stringBuilder.append(str);
        return stringBuilder.toString();
    }

    private static boolean hasHexPrefix(String str) {
        return str.length() > 1 && str.charAt(0) == '0' && str.charAt(1) == 'x';
    }

    public static BigInteger bytesToBigInteger(byte[] bArr, int i, int i2) {
        return bytesToBigInteger(Arrays.copyOfRange(bArr, i, i2 + i));
    }

    public static BigInteger bytesToBigInteger(byte[] bArr) {
        return new BigInteger(1, bArr);
    }

    public static BigInteger hexToBigInteger(String str) {
        return new BigInteger(cleanHexPrefix(str), 16);
    }

    public static String bigIntegerToHex(BigInteger bigInteger) {
        return bigInteger.toString(16);
    }

    public static String bigIntegerToHexWithZeroPadded(BigInteger bigInteger, int i) {
        String bigIntegerToHex = bigIntegerToHex(bigInteger);
        int length = bigIntegerToHex.length();
        if (length > i) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Value ");
            stringBuilder.append(bigIntegerToHex);
            stringBuilder.append("is larger then length ");
            stringBuilder.append(i);
            throw new UnsupportedOperationException(stringBuilder.toString());
        } else if (bigInteger.signum() < 0) {
            throw new UnsupportedOperationException("Value cannot be negative");
        } else if (length >= i) {
            return bigIntegerToHex;
        } else {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(Strings.repeat("0", i - length));
            stringBuilder2.append(bigIntegerToHex);
            return stringBuilder2.toString();
        }
    }

    public static byte[] bigIntegerToBytesWithZeroPadded(BigInteger bigInteger, int i) {
        int length;
        byte obj[] = new byte[i];
        byte toByteArray[] = bigInteger.toByteArray();
        int i2 = 1;
        if (toByteArray[0] == 0) {
            length = toByteArray.length - 1;
        } else {
            i2 = 0;
            length = toByteArray.length;
        }
        if (length > i) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Input is too large to put in byte array of size ");
            stringBuilder.append(i);
            throw new RuntimeException(stringBuilder.toString());
        }
        System.arraycopy(toByteArray, i2, obj, i - length, length);
        return obj;
    }

    public static byte[] hexToBytes(String str) {
        str = cleanHexPrefix(str);
        int length = str.length();
        int i = 0;
        if (length == 0) {
            return new byte[0];
        }
        byte[] bArr;
        if (length % 2 != 0) {
            bArr = new byte[((length / 2) + 1)];
            bArr[0] = (byte) Character.digit(str.charAt(0), 16);
            i = 1;
        } else {
            bArr = new byte[(length / 2)];
        }
        while (i < length) {
            int i2 = i + 1;
            bArr[i2 / 2] = (byte) ((Character.digit(str.charAt(i), 16) << 4) + Character.digit(str.charAt(i2), 16));
            i += 2;
        }
        return bArr;
    }

    public static String bytesToHex(byte[] bArr) {
        StringBuilder stringBuilder = new StringBuilder();
        if (bArr.length == 0) {
            return "";
        }
        int length = bArr.length;
        for (int i = 0; i < length; i++) {
            stringBuilder.append(String.format("%02x", new Object[]{Integer.valueOf(bArr[i] & 255)}));
        }
        return stringBuilder.toString();
    }

    public static String beBigEndianHex(String str) {
        if (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN) {
            return str;
        }
        byte[] hexToBytes = hexToBytes(str);
        int length = hexToBytes.length / 2;
        for (int i = 0; i < length; i++) {
            byte b = hexToBytes[i];
            hexToBytes[i] = hexToBytes[(hexToBytes.length - 1) - i];
            hexToBytes[(hexToBytes.length - 1) - i] = b;
        }
        return bytesToHex(hexToBytes);
    }
}