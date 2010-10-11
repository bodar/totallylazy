package com.googlecode.totallylazy.callables;

import com.googlecode.totallylazy.Runnable1;
import com.googlecode.totallylazy.Runnables;
import com.googlecode.totallylazy.Sequence;

import java.util.concurrent.Callable;

public class TimeCallable<T> implements Callable<T> {
    private final Callable<T> callable;
    private final Runnable1<Double> reporter;
    public static final String FORMAT = "Elapsed time: %s msecs";

    private TimeCallable(Callable<T> callable, Runnable1<Double> reporter) {
        this.callable = callable;
        this.reporter = reporter;
    }

    public T call() throws Exception {
        long start = System.nanoTime();
        T result = callable.call();
        reporter.run(calculateMilliseconds(start, System.nanoTime()));
        return result;
    }

    public static double calculateMilliseconds(long start, long end) {
        return (end - start) / 1000000.0;
    }

    public static <T> TimeCallable<T> time(Callable<T> callable){
        return time(callable, Runnables.<Double>printLine(System.out, FORMAT));
    }

    public static <T> Sequence<T> time(Sequence<T> sequence){
        return time(sequence, Runnables.<Double>printLine(System.out, FORMAT));
    }

    public static <T> Sequence<T> time(Sequence<T> sequence, Runnable1<Double> reporter){
        long start = System.nanoTime();
        Sequence<T> result = sequence.realise();
        reporter.run(calculateMilliseconds(start, System.nanoTime()));
        return result;
    }

    public static <T> TimeCallable<T> time(Callable<T> callable, Runnable1<Double> reporter){
        return new TimeCallable<T>(callable, reporter);
    }
}
