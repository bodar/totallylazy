package com.googlecode.totallylazy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

public class Streams {
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

}
