package com.googlecode.totallylazy.callables;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Function1;

import static com.googlecode.totallylazy.callables.TimeCallable.calculateMilliseconds;

public final class TimeCallable1<A,B> implements Function1<A, B> {
    private final Callable1<? super A, ? extends B> callable;
    private final Callable1<? super Number, ?> reporter;

    private TimeCallable1(Callable1<? super A, ? extends B> callable, Callable1<? super Number, ?> reporter) {
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

    public static <A,B> TimeCallable1<A,B> time1(Callable1<? super A, ? extends B> callable){
        return time1(callable, TimeCallable.DEFAULT_REPORTER);
    }

    public static <A,B> TimeCallable1<A,B> time1(Callable1<? super A, ? extends B> callable, Callable1<? super Number, ?> reporter){
        return new TimeCallable1<A,B>(callable, reporter);
    }
}
