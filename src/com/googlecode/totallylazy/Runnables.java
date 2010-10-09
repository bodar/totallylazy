package com.googlecode.totallylazy;

import java.io.PrintStream;

public class Runnables {
    public static <T> Runnable1<T> printLine(final PrintStream printStream, final String format) {
        return new Runnable1<T>() {
            public void run(T t) {
                printStream.println(String.format(format, t));
            }
        };
    }

}
