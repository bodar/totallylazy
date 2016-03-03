package com.googlecode.totallylazy.security;

import static com.googlecode.totallylazy.Strings.bytes;
import static com.googlecode.totallylazy.Strings.string;

public class Base64 {
    public static final java.util.Base64.Encoder ENCODER = java.util.Base64.getEncoder();
    public static final java.util.Base64.Encoder URL_ENCODER = java.util.Base64.getUrlEncoder();
    public static final java.util.Base64.Decoder URL_DECODER = java.util.Base64.getUrlDecoder();
    public static final java.util.Base64.Decoder DECODER = java.util.Base64.getDecoder();

    public static byte[] decode(String content) {
        return decode(content, DECODER);
    }

    public static byte[] decodeUrlSafe(String content) {
        return decode(content, URL_DECODER);
    }

    public static String encode(byte[] content) {
        return encode(content, ENCODER);
    }

    public static String encodeUrlSafe(byte[] content) {
        return encode(content, URL_ENCODER);
    }

    private static byte[] decode(String content, java.util.Base64.Decoder decoder) {return decoder.decode(bytes(content));}

    private static String encode(byte[] content, java.util.Base64.Encoder encoder) {return string(encoder.encode(content));}
}
