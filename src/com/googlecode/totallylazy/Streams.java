package com.googlecode.totallylazy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import static com.googlecode.totallylazy.Closeables.using;
import static com.googlecode.totallylazy.Runnables.VOID;

public class Streams {
    public static Void copyAndClose(final InputStream input, final OutputStream out){
        return using(input, new Function1<InputStream, Void>() {
            @Override
            public Void call(InputStream inputStream) throws Exception {
                return using(out, new Function1<OutputStream, Void>() {
                    @Override
                    public Void call(OutputStream outputStream) throws Exception {
                        return copy(input, out);
                    }
                });
            }
        });
    }

    public static Void copy(InputStream input, OutputStream out) throws IOException {
        return copy(input, out, 4096);
    }

    public static Void copy(InputStream input, OutputStream out, int bufferSize) throws IOException {
        byte[] buffer = new byte[bufferSize];
        int read;
        while ((read = input.read(buffer)) > 0) {
            out.write(buffer, 0, read);
        }
        return VOID;
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
