package com.googlecode.totallylazy.callables;

import com.googlecode.totallylazy.Lazy;

import java.util.concurrent.Callable;

public class LazyCallable<T> extends Lazy<T> {
    private final Callable<? extends T> callable;

    private LazyCallable(Callable<? extends T> callable) {
        this.callable = callable;
    }

    public static <T> LazyCallable<T> lazy(Callable<? extends T> callable) {
        return new LazyCallable<T>(callable);
    }

    public final T get() throws Exception {
        return callable.call();
    }
}
