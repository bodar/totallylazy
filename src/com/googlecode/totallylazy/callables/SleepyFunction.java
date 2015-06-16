package com.googlecode.totallylazy.callables;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Function;

public final class SleepyFunction<T,R> extends Function<T, R> {
    private final Callable1<? super T, ? extends R> callable;
    private final int millis;

    private SleepyFunction(Callable1<? super T, ? extends R> callable, int millis) {
        this.callable = callable;
        this.millis = millis;
    }

    public final R call(T instance) throws Exception {
        R result = callable.call(instance);
        Thread.sleep(millis);
        return result;
    }

    public static <T,R> Function<T, R> sleepy(Callable1<? super T, ? extends R> callable, int millis) {
        return new SleepyFunction<T,R>(callable, millis);
    }
}
