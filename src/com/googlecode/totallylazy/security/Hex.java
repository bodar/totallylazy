package com.googlecode.totallylazy.security;

public interface Hex {
    char[] DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    static String encode(byte[] data) {
        int length = data.length;
        char[] result = new char[length << 1];
        int i = 0;

        for(int j = 0; i < length; ++i) {
            result[j++] = DIGITS[(240 & data[i]) >>> 4];
            result[j++] = DIGITS[15 & data[i]];
        }

        return new String(result);
    }
}