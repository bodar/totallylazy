package com.googlecode.totallylazy;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Writer;

import static java.lang.String.format;

public class Runnables {
    public static final Void VOID = null;

    public static <T> Block<T> printLine(final PrintStream printStream, final String format) {
        return new Block<T>() {
            @Override
            protected void execute(T t) throws Exception {
                printStream.println(format(format, t));
            }
        };
    }

    public static <T> Block<T> printLine(final String format) {
        return printLine(System.out, format);
    }

    public static <T> Block<T> doNothing(final Class<T> aClass) {
        return doNothing();
    }

    public static <T> Block<T> doNothing() {
        return new Block<T>() {
            @Override
            protected void execute(T t) throws Exception {}
        };
    }

    public static <T extends Runnable> Block<T> run() {
        return new Block<T>() {
            @Override
            protected void execute(T t) throws Exception {
                t.run();
            }
        };
    }

    public static <T> UnaryOperator<T> run(final Block<? super T> callable) {
        return new UnaryOperator<T>() {
            public T call(T t) throws Exception {
                callable.call(t);
                return t;
            }
        };
    }

    public static Block<OutputStream> write(final byte[] bytes) {
        return new Block<OutputStream>() {
            @Override
            protected void execute(OutputStream outputStream) throws Exception {
                outputStream.write(bytes);
            }
        };
    }

    public static Block<Writer> write(final String value) {
        return new Block<Writer>() {
            @Override
            protected void execute(Writer writer) throws Exception {
                writer.write(value);
            }
        };
    }
}