package com.googlecode.totallylazy;

import java.io.Closeable;
import java.io.IOException;

import static com.googlecode.totallylazy.Callers.call;

public class Closeables {
    public static <T extends Closeable> Callable1<? super T, Void> closeAfter(final Callable1<? super T, Void> callable) {
        return new Callable1<T, Void>() {
            public Void call(T t) throws Exception {
                return using(t, callable);
            }
        };
    }

    public static <T extends Closeable> T close(final T t) {
        try {
            t.close();
        } catch (IOException e) {
            throw new LazyException(e);
        }
        return t;
    }

    public static <T extends Closeable, R> R using(T t, Callable1<? super T,R> callable) {
        try{
            return call(callable, t);
        } finally {
            close(t);
        }
    }
}
