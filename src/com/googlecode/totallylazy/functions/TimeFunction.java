package com.googlecode.totallylazy.functions;

import com.googlecode.totallylazy.Function1;

public final class TimeFunction<A,B> implements Function1<A, B> {
    private final Function1<? super A, ? extends B> callable;
    private final Function1<? super Number, ?> reporter;

    private TimeFunction(Function1<? super A, ? extends B> callable, Function1<? super Number, ?> reporter) {
        this.callable = callable;
        this.reporter = reporter;
    }

    @Override
    public final B call(A a) throws Exception {
        long start = System.nanoTime();
        B result = callable.call(a);
        reporter.call(TimeCallable.calculateMilliseconds(start, System.nanoTime()));
        return result;
    }

    public static <A,B> TimeFunction<A,B> time1(Function1<? super A, ? extends B> callable){
        return time1(callable, TimeCallable.DEFAULT_REPORTER);
    }

    public static <A,B> TimeFunction<A,B> time1(Function1<? super A, ? extends B> callable, Function1<? super Number, ?> reporter){
        return new TimeFunction<A,B>(callable, reporter);
    }
}
