package com.googlecode.totallylazy.records.sql;

import java.security.SecureRandom;
import java.util.Arrays;

public class Id {
    private final byte[] bytes;
    private static SecureRandom secureRandom = new SecureRandom();

    public Id(byte[] bytes) {
        if(bytes == null) throw new IllegalArgumentException("bytes");
        this.bytes = bytes;
    }

    public static Id unique(){
        byte[] randomBytes = new byte[16];
        secureRandom.nextBytes(randomBytes);
        return new Id(randomBytes);
    }

    public byte[] bytes() {
        return bytes;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Id && Arrays.equals(bytes, ((Id) o).bytes());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(bytes);
    }
}
