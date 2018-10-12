package com.lixhs.eth.util;

import java.util.Random;
import java.util.UUID;

public class RandUtils {
    public static String getUUID() {
        try {
            return UUID.randomUUID().toString().replace("-", "");
        } catch (Throwable th) {
            return RandUtils.getRandomStr(32);
        }
    }

    public static String getRandomStr(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789~!@#$%&*()_+=";
        Random random = new Random();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i2 = 0; i2 < length; i2++) {
            stringBuffer.append(str.charAt(random.nextInt(str.length())));
        }
        return stringBuffer.toString();
    }
}