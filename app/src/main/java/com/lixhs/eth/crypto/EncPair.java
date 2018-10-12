package com.lixhs.eth.crypto;

public class EncPair {
    private String encStr;
    private String nonce;

    public String getNonce() {
        return this.nonce;
    }

    public void setNonce(String str) {
        this.nonce = str;
    }

    public String getEncStr() {
        return this.encStr;
    }

    public void setEncStr(String str) {
        this.encStr = str;
    }
}