package com.googlecode.totallylazy;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;

import static com.googlecode.totallylazy.Closeables.using;

public class Streams {
    public static void copyAndClose(final InputStream input, final OutputStream out) {
        using(input, new Block<InputStream>() {
            @Override
            protected void execute(InputStream inputStream) throws Exception {
                using(out, new Block<OutputStream>() {
                    @Override
                    protected void execute(OutputStream outputStream) throws Exception {
                        copy(input, out);
                    }
                });
            }
        });
    }

    public static void copy(InputStream input, OutputStream out) throws IOException {
        copy(input, out, 4096);
    }

    public static void copy(InputStream input, OutputStream out, int bufferSize) throws IOException {
        byte[] buffer = new byte[bufferSize];
        int read;
        while ((read = input.read(buffer)) > 0) {
            out.write(buffer, 0, read);
        }
    }

    public static InputStream emptyInputStream() {
        return new InputStream() {
            @Override
            public int read() throws IOException {
                return -1;
            }
        };
    }

    public static OutputStream nullOutputStream() {
        return new OutputStream() {
            @Override
            public void write(int b) throws IOException {
            }
        };
    }

    public static PrintStream nullPrintStream() {
        return new PrintStream(nullOutputStream());
    }

    public static OutputStream streams(final OutputStream... streams) {
        return new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                for (OutputStream stream : streams) {
                    stream.write(b);
                }
            }
        };
    }

    public static InputStreamReader inputStreamReader(InputStream stream) {
        return new InputStreamReader(stream, Strings.UTF8);
    }
}
