package com.lixhs.eth.crypto;

import org.bitcoinj.core.ECKey;

import java.math.BigInteger;
import java.util.Arrays;

public class EthereumAddressCreator  {
    private static final int ADDRESS_LENGTH = 20;
    private static final int ADDRESS_LENGTH_IN_HEX = 40;
    private static final int PUBLIC_KEY_LENGTH_IN_HEX = 128;
    private static final int PUBLIC_KEY_SIZE = 64;

    public  static String fromPublicKey(BigInteger bigInteger) {
        return publicKeyToAddress(NumericUtil.bigIntegerToBytesWithZeroPadded(bigInteger, 64));
    }

    private static String publicKeyToAddress(byte[] bArr) {
        bArr = Hash.keccak256(bArr);
        return NumericUtil.bytesToHex(Arrays.copyOfRange(bArr, bArr.length - 20, bArr.length));
    }

    public static String fromPrivateKey(String str) {
        return fromECKey(ECKey.fromPrivate(NumericUtil.hexToBytes(str), false));
    }

    public static String fromPrivateKey(byte[] bArr) {
        return fromECKey(ECKey.fromPrivate(bArr, false));
    }

    private static String fromECKey(ECKey eCKey) {
        byte[] pubKey = eCKey.getPubKey();
        return publicKeyToAddress(Arrays.copyOfRange(pubKey, 1, pubKey.length));
    }
}