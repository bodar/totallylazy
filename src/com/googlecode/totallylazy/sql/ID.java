package com.googlecode.totallylazy.sql;

import java.security.SecureRandom;
import java.util.Arrays;

public class ID {
    private final byte[] bytes;
    private static SecureRandom secureRandom = new SecureRandom();

    public ID(byte[] bytes) {
        if(bytes == null) throw new IllegalArgumentException("bytes");
        this.bytes = bytes;
    }

    public static ID unique(){
        byte[] randomBytes = new byte[16];
        secureRandom.nextBytes(randomBytes);
        return new ID(randomBytes);
    }

    public byte[] bytes() {
        return bytes;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ID && Arrays.equals(bytes, ((ID) o).bytes());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(bytes);
    }
}
