package com.googlecode.totallylazy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Bytes {
    public static byte[] bytes(InputStream stream) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[512];
            int read = stream.read(buffer);
            while (read > 0) {
                outputStream.write(buffer, 0, read);
                read = stream.read(buffer);
            }
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
