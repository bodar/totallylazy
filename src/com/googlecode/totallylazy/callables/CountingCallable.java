package com.googlecode.totallylazy.callables;

import com.googlecode.totallylazy.Returns;

import java.util.concurrent.Callable;

public final class CountingCallable<T> implements Returns<T> {
    private int count = 0;
    private final Callable<? extends T> callable;

    @SuppressWarnings("unchecked")
    private CountingCallable(Callable<? extends T> callable) {
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

    public static <T> CountingCallable<T> counting(Callable<? extends T> callable) {
        return new CountingCallable<T>(callable);
    }
}
