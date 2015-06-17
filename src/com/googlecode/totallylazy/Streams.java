package com.googlecode.totallylazy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;

import static com.googlecode.totallylazy.Closeables.using;
import static com.googlecode.totallylazy.LazyException.lazyException;
import static com.googlecode.totallylazy.Predicates.notNullValue;
import static com.googlecode.totallylazy.Sequences.repeat;

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

    public static Sequence<String> lines(File file) {
        try {
            return lines(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw lazyException(e);
        }
    }

    public static Sequence<String> lines(InputStream stream) {
        return lines(inputStreamReader(stream));
    }

    public static Sequence<String> lines(Reader reader) {
        return repeat(readLine(new BufferedReader(reader))).takeWhile(notNullValue(String.class));
    }

    public static Returns<String> readLine(final BufferedReader reader) {
        return new Returns<String>() {
            public String call() throws Exception {
                String result = reader.readLine();
                if (result == null) {
                    reader.close();
                }
                return result;
            }
        };
    }
}
