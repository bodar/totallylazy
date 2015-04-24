package com.googlecode.totallylazy.security;

import com.googlecode.totallylazy.Strings;

import static com.googlecode.totallylazy.Strings.bytes;

public class Base64 {
    public static byte[] decode(String content) {
        return new org.apache.commons.codec.binary.Base64().decode(bytes(content));
    }

    public static String encode(byte[] content) {
        return Strings.toString(new org.apache.commons.codec.binary.Base64().encode(content));
    }
}
