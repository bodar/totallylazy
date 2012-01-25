package com.googlecode.totallylazy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Bytes {
    public static byte[] bytes(InputStream stream) {
        if(stream == null) {
            return new byte[0];
        }
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
            throw LazyException.lazyException(e);
        }
    }


    public static <T extends OutputStream> T write(final byte[] value, final T outputStream) {
        try {
            outputStream.write(value);
            return outputStream;
        } catch (IOException e) {
            throw LazyException.lazyException(e);
        }
    }
    
}
