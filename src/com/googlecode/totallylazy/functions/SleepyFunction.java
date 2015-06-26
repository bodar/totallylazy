package com.googlecode.totallylazy.functions;

public final class SleepyFunction<T,R> implements Function1<T, R> {
    private final Function1<? super T, ? extends R> callable;
    private final int millis;

    private SleepyFunction(Function1<? super T, ? extends R> callable, int millis) {
        this.callable = callable;
        this.millis = millis;
    }

    public final R call(T instance) throws Exception {
        R result = callable.call(instance);
        Thread.sleep(millis);
        return result;
    }

    public static <T,R> Function1<T, R> sleepy(Function1<? super T, ? extends R> callable, int millis) {
        return new SleepyFunction<T,R>(callable, millis);
    }
}
