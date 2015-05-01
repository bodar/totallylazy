package com.googlecode.totallylazy.security;

import static com.googlecode.totallylazy.Strings.bytes;
import static com.googlecode.totallylazy.Strings.string;

public class Base64 {
    public static byte[] decode(String content) {
        return java.util.Base64.getDecoder().decode(bytes(content));
    }

    public static String encode(byte[] content) {
        return string(java.util.Base64.getEncoder().encode(content));
    }
}
