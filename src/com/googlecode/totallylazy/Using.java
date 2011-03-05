package com.googlecode.totallylazy;

import java.io.Closeable;
import java.io.IOException;

import static com.googlecode.totallylazy.Callers.call;

public class Using {
    public static <T extends Closeable, R> R using(T t, Callable1<? super T,R> callable) {
        try{
            return call(callable, t);
        } finally {
            try {
                t.close();
            } catch (IOException e) {
                throw new LazyException(e);
            }
        }
    }

    public static <T extends Closeable> void using(T t, Runnable1<? super T> runnable) {
        try{
            runnable.run(t);
        } finally {
            try {
                t.close();
            } catch (IOException e) {
                throw new LazyException(e);
            }
        }
    }
}
