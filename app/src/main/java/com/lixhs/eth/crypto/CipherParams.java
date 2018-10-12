package com.lixhs.eth.crypto;

public class CipherParams {
    private String iv;

    CipherParams() {
    }

    public String getIv() {
        return this.iv;
    }

    public void setIv(String str) {
        this.iv = str;
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CipherParams)) {
            return false;
        }
        CipherParams cipherParams = (CipherParams) obj;
        if (getIv() != null) {
            z = getIv().equals(cipherParams.getIv());
        } else if (cipherParams.getIv() != null) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return getIv() != null ? getIv().hashCode() : 0;
    }
}