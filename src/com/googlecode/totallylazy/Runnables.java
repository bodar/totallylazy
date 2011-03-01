package com.googlecode.totallylazy;

import java.io.PrintStream;

public class Runnables {
    public static <T> Runnable1<T> printLine(final PrintStream printStream, final String format) {
        return new Runnable1<T>() {
            public final void run(T t) {
                printStream.println(String.format(format, t));
            }
        };
    }

    public static <T> Runnable1<T> printLine(final String format) {
        return printLine(System.out, format);
    }

    public static <T> Runnable1<T> doNothing(Class<T> aClass) {
        return doNothing();
    }

    public static <T> Runnable1<T> doNothing() {
        return new Runnable1<T>() {
            public void run(T ignore) {
            }
        };
    }

    public static Runnable1<Runnable> run() {
        return new Runnable1<Runnable>() {
            public void run(Runnable runnable) {
                runnable.run();
            }
        };
    }

    public static <T> Callable1<? super T, T> run(final Runnable1<T> runnable) {
        return new Callable1<T, T>() {
            public T call(T t) throws Exception {
                runnable.run(t);
                return t;
            }
        };
    }
}
