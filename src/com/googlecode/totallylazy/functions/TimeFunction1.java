package com.googlecode.totallylazy.functions;

public final class TimeFunction1<A,B> implements Function1<A, B> {
    private final Function1<? super A, ? extends B> callable;
    private final Function1<? super Number, ?> reporter;

    private TimeFunction1(Function1<? super A, ? extends B> callable, Function1<? super Number, ?> reporter) {
        this.callable = callable;
        this.reporter = reporter;
    }

    @Override
    public final B call(A a) throws Exception {
        long start = System.nanoTime();
        B result = callable.call(a);
        reporter.call(TimeFunction0.calculateMilliseconds(start, System.nanoTime()));
        return result;
    }

    public static <A,B> TimeFunction1<A,B> time1(Function1<? super A, ? extends B> callable){
        return time1(callable, TimeFunction0.DEFAULT_REPORTER);
    }

    public static <A,B> TimeFunction1<A,B> time1(Function1<? super A, ? extends B> callable, Function1<? super Number, ?> reporter){
        return new TimeFunction1<A,B>(callable, reporter);
    }
}
