package com.googlecode.totallylazy;

import com.googlecode.totallylazy.functions.Function1;
import com.googlecode.totallylazy.http.Uri;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import static com.googlecode.totallylazy.Closeables.using;

public class Bytes {
    public static byte[] bytes(String value) {
        return Strings.bytes(value);
    }

    public static byte[] bytes(Class<?> aClass) {
        return bytes(aClass.getResourceAsStream("/" + Classes.filename(aClass)));
    }

    public static byte[] bytes(InputStream input) {
        if (input == null) {
            return new byte[0];
        }
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Streams.copyAndClose(input, output);
        return output.toByteArray();
    }

    public static byte[] bytes(Uri input) {
        return bytes(input.toURL());
    }

    public static byte[] bytes(URL input) {
        try {
            return bytes(input.openStream());
        } catch (IOException e) {
            throw LazyException.lazyException(e);
        }
    }

    public static byte[] bytes(File file) {
        try {
            return bytes(new FileInputStream(file));
        } catch (FileNotFoundException e) {
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

    public static Function1<OutputStream, OutputStream> write(final byte[] value) {
        return outputStream -> write(value, outputStream);
    }
}