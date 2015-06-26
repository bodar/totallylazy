package com.googlecode.totallylazy;

import com.googlecode.totallylazy.functions.Block;
import com.googlecode.totallylazy.functions.UnaryFunction;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Writer;

import static java.lang.String.format;

public class Runnables {
    public static final Void VOID = null;

    public static <T> Block<T> printLine(final PrintStream printStream, final String format) {
        return t -> printStream.println(format(format, t));
    }

    public static <T> Block<T> printLine(final String format) {
        return printLine(System.out, format);
    }

    public static <T> Block<T> doNothing(final Class<T> aClass) {
        return doNothing();
    }

    public static <T> Block<T> doNothing() {
        return t -> {};
    }

    public static <T extends Runnable> Block<T> run() {
        return T::run;
    }

    public static <T> UnaryFunction<T> run(final Block<? super T> callable) {
        return t -> {
            callable.call(t);
            return t;
        };
    }

    public static Block<OutputStream> write(final byte[] bytes) {
        return outputStream -> outputStream.write(bytes);
    }

    public static Block<Writer> write(final String value) {
        return writer -> writer.write(value);
    }
}