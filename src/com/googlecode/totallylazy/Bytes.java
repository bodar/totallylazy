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
            Streams.copy(stream, outputStream);
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

    public static Function<OutputStream, OutputStream> write(final byte[] value) {
        return new Function<OutputStream,OutputStream>() {
            @Override
            public OutputStream call(OutputStream outputStream) throws Exception {
                return write(value, outputStream);
            }
        };
    }
}
