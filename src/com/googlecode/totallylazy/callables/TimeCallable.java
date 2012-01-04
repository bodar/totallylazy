package com.googlecode.totallylazy.callables;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callables;
import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Runnables;
import com.googlecode.totallylazy.Sequence;

import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Callables.curry;

public final class TimeCallable<T> extends Function<T> {
    private static final String FORMAT = "Elapsed time: %s msecs";
    private static final Callable1<Double, Void> DEFAULT_REPORTER = Runnables.printLine(FORMAT);
    private final Callable<T> callable;
    private final Callable1<Double, Void> reporter;

    private TimeCallable(Callable<T> callable, Callable1<Double, Void> reporter) {
        this.callable = callable;
        this.reporter = reporter;
    }

    public final T call() throws Exception {
        long start = System.nanoTime();
        T result = callable.call();
        reporter.call(calculateMilliseconds(start, System.nanoTime()));
        return result;
    }

    public static double calculateMilliseconds(long start, long end) {
        return (end - start) / 1000000.0;
    }

    public static <T> TimeCallable<T> time(Callable<T> callable){
        return time(callable, DEFAULT_REPORTER);
    }

    public static <T> TimeCallable<T> time(Callable<T> callable, Callable1<Double, Void> reporter){
        return new TimeCallable<T>(callable, reporter);
    }

    public static <T,R> TimeCallable<R> time(Callable1<T,R> callable, T value){
        return time(callable, value, DEFAULT_REPORTER);
    }

    public static <T,R> TimeCallable<R> time(Callable1<T,R> callable, T value, Callable1<Double, Void> reporter){
        return new TimeCallable<R>(curry(callable, value), reporter);
    }

    public static <T> TimeCallable<Sequence<T>> time(Sequence<T> sequence){
        return time(Callables.<T>realise(), sequence, DEFAULT_REPORTER);
    }

    public static <T> TimeCallable<Sequence<T>> time(Sequence<T> sequence, Callable1<Double, Void> reporter){
        return time(Callables.<T>realise(), sequence, reporter);
    }
}
