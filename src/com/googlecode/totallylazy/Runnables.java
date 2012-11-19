package com.googlecode.totallylazy;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Writer;

import static java.lang.String.format;

public class Runnables {
    public static final Void VOID = null;

    public static <T> Function1<T, Void> printLine(final PrintStream printStream, final String format) {
        return new Function1<T, Void>() {
            public final Void call(T t) {
                printStream.println(format(format, t));
                return VOID;
            }
        };
    }

    public static <T> Function1<T, Void> printLine(final String format) {
        return printLine(System.out, format);
    }

    public static <T> Function1<T, Void> doNothing(final Class<T> aClass) {
        return doNothing();
    }

    public static <T> Function1<T, Void> doNothing() {
        return new Function1<T, Void>() {
            public Void call(T ignore) {
                return VOID;
            }
        };
    }

    public static <T extends Runnable> Function1<T, Void> run() {
        return new Function1<T, Void>() {
            public Void call(T t) {
                t.run();
                return VOID;
            }
        };
    }

    public static <T> Function1<T, T> run(final Callable1<? super T, Void> callable) {
        return new Function1<T, T>() {
            public T call(T t) throws Exception {
                callable.call(t);
                return t;
            }
        };
    }

    public static Function1<OutputStream, Void> write(final byte[] bytes) {
        return new Function1<OutputStream, Void>() {
            public Void call(OutputStream outputStream) throws IOException {
                outputStream.write(bytes);
                return VOID;
            }
        };
    }

    public static Function1<Writer, Void> write(final String value) {
        return new Function1<Writer, Void>() {
            public Void call(Writer writer) throws IOException {
                writer.write(value);
                return VOID;
            }
        };
    }
}