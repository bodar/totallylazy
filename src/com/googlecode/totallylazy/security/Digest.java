package com.googlecode.totallylazy.security;

import com.googlecode.totallylazy.LazyException;
import com.googlecode.totallylazy.Value;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Digest implements Value<byte[]> {
    private final byte[] bytes;

    private Digest(byte[] bytes) {
        this.bytes = bytes;
    }

    public static Digest md5(byte[] bytes) {
        return digest(bytes, "MD5");
    }

    public static Digest sha256(byte[] bytes) {
        return digest(bytes, "SHA-256");
    }

    public static Digest digest(byte[] bytes, String algorithm) {
        return new Digest(algorithm(algorithm).digest(bytes));
    }

    public String toHex() {
        return Hex.encode(bytes);
    }

    public String toBase64() { return Base64.encode(bytes); }

    @Override
    public byte[] value() {
        return bytes;
    }

    private static MessageDigest algorithm(String name) {
        try {
            return MessageDigest.getInstance(name);
        } catch (NoSuchAlgorithmException e) {
            throw LazyException.lazyException(e);
        }
    }


}

