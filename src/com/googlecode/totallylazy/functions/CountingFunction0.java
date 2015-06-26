package com.googlecode.totallylazy.functions;

import java.util.concurrent.Callable;

public final class CountingFunction0<T> implements Function0<T> {
    private int count = 0;
    private final Callable<? extends T> callable;

    @SuppressWarnings("unchecked")
    private CountingFunction0(Callable<? extends T> callable) {
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

    public static CountingFunction0<Integer> counting() {
        return CountingFunction0.<Integer>counting(null);
    }

    public static <T> CountingFunction0<T> counting(Callable<? extends T> callable) {
        return new CountingFunction0<T>(callable);
    }
}
