package com.googlecode.totallylazy.functions;

import java.util.concurrent.Callable;

public final class SleepyFunction0<T> implements Function0<T> {
    private final Callable<? extends T> callable;
    private final int millis;

    private SleepyFunction0(Callable<? extends T> callable, int millis) {
        this.callable = callable;
        this.millis = millis;
    }

    public final T call() throws Exception {
        T t = callable.call();
        Thread.sleep(millis);
        return t;
    }

    public static <T> Function0<T> sleepy(Callable<? extends T> callable, int millis) {
        return new SleepyFunction0<T>(callable, millis);
    }
}
