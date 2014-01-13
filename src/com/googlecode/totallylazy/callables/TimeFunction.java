package com.googlecode.totallylazy.callables;

import com.googlecode.totallylazy.Function;

import static com.googlecode.totallylazy.callables.TimeCallable.calculateMilliseconds;

public final class TimeFunction<A,B> extends com.googlecode.totallylazy.Eq implements Function<A,B> {
    private final Function<? super A, ? extends B> callable;
    private final Function<? super Number, ?> reporter;

    private TimeFunction(Function<? super A, ? extends B> callable, Function<? super Number, ?> reporter) {
        this.callable = callable;
        this.reporter = reporter;
    }

    @Override
    public final B call(A a) throws Exception {
        long start = System.nanoTime();
        B result = callable.call(a);
        reporter.call(calculateMilliseconds(start, System.nanoTime()));
        return result;
    }

    public static <A,B> TimeFunction<A,B> time1(Function<? super A, ? extends B> callable){
        return time1(callable, TimeCallable.DEFAULT_REPORTER);
    }

    public static <A,B> TimeFunction<A,B> time1(Function<? super A, ? extends B> callable, Function<? super Number, ?> reporter){
        return new TimeFunction<A,B>(callable, reporter);
    }
}
