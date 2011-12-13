package com.googlecode.totallylazy.callables;

import java.util.concurrent.Callable;

public final class LazyCallable<T> implements Callable<T> {
    private final Callable<T> callable;
    private final Object lock = new Object();
    private volatile T state;

    private LazyCallable(Callable<T> callable) {
        this.callable = callable;
    }

    public static <T> Callable<T> lazy(Callable<T> callable) {
        return new LazyCallable<T>(callable);
    }

    // Thread-safe double check idiom (Effective Java 2nd edition p.283)
    public final T call() throws Exception {
        if (state == null) {
            synchronized (lock) {
                if (state == null) {
                    state = callable.call();
                }
            }
        }
        return state;
    }
}
