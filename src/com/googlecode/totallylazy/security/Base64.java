package com.googlecode.totallylazy.security;

public class Base64 {
    public static byte[] decode(String content) {
        return javax.xml.bind.DatatypeConverter.parseBase64Binary(content);
    }

    public static String encode(byte[] content) {
        return javax.xml.bind.DatatypeConverter.printBase64Binary(content);
    }
}
