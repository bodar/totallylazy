package com.googlecode.totallylazy.callables;

import java.util.concurrent.Callable;

public class SleepyCallable<T> implements Callable<T> {
    private final Callable<T> callable;
    private final int millis;

    private SleepyCallable(Callable<T> callable, int millis) {
        this.callable = callable;
        this.millis = millis;
    }

    public T call() throws Exception {
        T t = callable.call();
        Thread.sleep(millis);
        return t;
    }

    public static <T> Callable<T> sleepy(Callable<T> callable, int millis) {
        return new SleepyCallable<T>(callable, millis);
    }
}
