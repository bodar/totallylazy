package com.googlecode.totallylazy.functions;

import com.googlecode.totallylazy.Function0;

import java.util.concurrent.Callable;

public final class SleepyCallable<T> implements Function0<T> {
    private final Callable<? extends T> callable;
    private final int millis;

    private SleepyCallable(Callable<? extends T> callable, int millis) {
        this.callable = callable;
        this.millis = millis;
    }

    public final T call() throws Exception {
        T t = callable.call();
        Thread.sleep(millis);
        return t;
    }

    public static <T> Function0<T> sleepy(Callable<? extends T> callable, int millis) {
        return new SleepyCallable<T>(callable, millis);
    }
}
