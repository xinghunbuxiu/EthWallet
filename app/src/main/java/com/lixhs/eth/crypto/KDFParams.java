package com.lixhs.eth.crypto;

interface KDFParams {
    public static final int DK_LEN = 32;

    int getDklen();

    String getSalt();
}