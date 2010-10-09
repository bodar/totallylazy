package com.googlecode.totallylazy.callables;

import com.googlecode.totallylazy.Runnable1;
import com.googlecode.totallylazy.Runnables;

import java.util.concurrent.Callable;

public class TimeCallable<T> implements Callable<T> {
    private final Callable<T> callable;
    private final Runnable1<Double> reporter;

    private TimeCallable(Callable<T> callable, Runnable1<Double> reporter) {
        this.callable = callable;
        this.reporter = reporter;
    }

    public T call() throws Exception {
        long start = System.nanoTime();
        T result = callable.call();
        long end = System.nanoTime();
        reporter.run((end - start) / 1000000.0);
        return result;
    }

    public static <T> TimeCallable<T> time(Callable<T> callable){
        return time(callable, Runnables.<Double>printLine(System.out, "Elapsed time: %s msecs"));
    }

    public static <T> TimeCallable<T> time(Callable<T> callable, Runnable1<Double> reporter){
        return new TimeCallable<T>(callable, reporter);
    }
}
