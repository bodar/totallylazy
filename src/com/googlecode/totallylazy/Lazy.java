package com.googlecode.totallylazy;

import com.googlecode.totallylazy.callables.LazyCallable;

import java.util.concurrent.Callable;

public abstract class Lazy<T> extends Function<T> implements Memory {
    private final Object lock = new Object();
    private volatile T state;

    protected abstract T get() throws Exception;

    public static <T> Lazy<T> lazy(Callable<? extends T> callable) {
        return LazyCallable.lazy(callable);
    }

    // Thread-safe double check idiom (Effective Java 2nd edition p.283)
    public final T call() throws Exception {
        if (state == null) {
            synchronized (lock) {
                if (state == null) {
                    state = get();
                }
            }
        }
        return state;
    }

    public void forget() {
        synchronized (lock) {
            state = null;
        }
    }
}
