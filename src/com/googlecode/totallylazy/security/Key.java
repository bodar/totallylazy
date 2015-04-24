package com.googlecode.totallylazy.security;

import com.googlecode.totallylazy.Function1;
import com.googlecode.totallylazy.Function2;
import com.googlecode.totallylazy.LazyException;
import com.googlecode.totallylazy.Value;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

import static com.googlecode.totallylazy.Strings.bytes;
import static com.googlecode.totallylazy.Strings.string;
import static com.googlecode.totallylazy.security.GZip.gzip;
import static com.googlecode.totallylazy.security.GZip.ungzip;
import static javax.crypto.Cipher.DECRYPT_MODE;
import static javax.crypto.Cipher.ENCRYPT_MODE;

public class Key implements Value<String> {
    private final String secret;

    private Key(String secret) {
        this.secret = secret;
    }

    public static Key key() {
        return key(generate());
    }

    public static Key key(String secret) {
        return new Key(secret);
    }

    public String encrypt(String value) {
        try {
            return Base64.encode(encrypt(gzip(bytes(value))));
        } catch (Exception e) {
            throw LazyException.lazyException(e);
        }
    }

    public String decrypt(String value) {
        try {
            return string(ungzip(decrypt(Base64.decode(value))));
        } catch (Exception e) {
            throw LazyException.lazyException(e);
        }
    }

    @Override
    public String value() {
        return secret;
    }

    public static final String ALGORITHM = "AES";
    private static String generate() {
        try {
            KeyGenerator generator = KeyGenerator.getInstance(ALGORITHM);
            generator.init(128);
            return Base64.encode(generator.generateKey().getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw LazyException.lazyException(e);
        }
    }

    private byte[] encrypt(byte[] content) throws GeneralSecurityException {
        return cipher(ENCRYPT_MODE).doFinal(content);
    }

    private byte[] decrypt(byte[] content) throws GeneralSecurityException {
        return cipher(DECRYPT_MODE).doFinal(content);
    }

    private Cipher cipher(int mode) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(mode, new SecretKeySpec(Base64.decode(secret), ALGORITHM));
        return cipher;
    }

    @Override
    public String toString() {
        return value();
    }

    public Function1<? super String, String> encrypt() {
        return functions.encrypt().apply(this);
    }

    public Function1<? super String, String> decrypt() {
        return functions.decrypt().apply(this);
    }

    public static class functions {
        public static Function2<Key, ? super String, String> encrypt() {
            return new Function2<Key, String, String>() {
                @Override
                public String call(Key key, String value) throws Exception {
                    return key.encrypt(value);
                }
            };
        }
        public static Function2<Key, ? super String, String> decrypt() {
            return new Function2<Key, String, String>() {
                @Override
                public String call(Key key, String value) throws Exception {
                    return key.decrypt(value);
                }
            };
        }
    }
}