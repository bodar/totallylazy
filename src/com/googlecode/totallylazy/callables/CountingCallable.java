package com.googlecode.totallylazy.callables;

import java.util.concurrent.Callable;

public final class CountingCallable<T> implements Callable<T> {
    private int count = 0;
    private final Callable<T> callable;

    private CountingCallable(Callable<T> callable) {
        this.callable = callable == null ? new Callable() {
            public Object call() throws Exception {
                return count;
            }
        } : callable;
    }

    public final T call() throws Exception {
        T result = callable.call();
        count++;
        return result;
    }

    public final int count() {
        return count;
    }

    public static CountingCallable<Integer> counting() {
        return CountingCallable.<Integer>counting(null);
    }

    public static <T> CountingCallable<T> counting(Callable<T> callable) {
        return new CountingCallable<T>(callable);
    }
}
