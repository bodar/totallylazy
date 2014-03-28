package com.googlecode.totallylazy;

import java.util.concurrent.Callable;

public final class Lazy<T> implements Returns<T>, Memory {
    private final Object lock = new Object();
    private volatile T state;
    private final Callable<? extends T> callable;

    private Lazy(Callable<? extends T> callable) {this.callable = callable;}

    public static <T> Lazy<T> value(Callable<? extends T> callable) {return lazy(callable);}

    public static <T> Lazy<T> lazy(Callable<? extends T> callable) {return new Lazy<T>(callable);}

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

    public void forget() {
        synchronized (lock) {
            state = null;
        }
    }
}
