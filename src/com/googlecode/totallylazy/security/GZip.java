package com.googlecode.totallylazy.security;

import com.googlecode.totallylazy.LazyException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static com.googlecode.totallylazy.Bytes.bytes;

public class GZip {
    public static byte[] gzip(byte[] data) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        GZIPOutputStream outputStream = new GZIPOutputStream(stream);
        outputStream.write(data);
        outputStream.close();
        return stream.toByteArray();
    }

    public static byte[] ungzip(byte[] input) {
        try {
            return bytes(new GZIPInputStream(new ByteArrayInputStream(input)));
        } catch (IOException e) {
            throw LazyException.lazyException(e);
        }
    }
}
