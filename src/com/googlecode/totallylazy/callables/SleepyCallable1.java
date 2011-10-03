package com.googlecode.totallylazy.callables;

import com.googlecode.totallylazy.Callable1;

public final class SleepyCallable1<T,R> implements Callable1<T, R> {
    private final Callable1<T, R> callable;
    private final int millis;

    private SleepyCallable1(Callable1<T, R> callable, int millis) {
        this.callable = callable;
        this.millis = millis;
    }

    public final R call(T instance) throws Exception {
        R result = callable.call(instance);
        Thread.sleep(millis);
        return result;
    }

    public static <T,R> Callable1<T, R> sleepy(Callable1<T, R> callable, int millis) {
        return new SleepyCallable1<T,R>(callable, millis);
    }
}
