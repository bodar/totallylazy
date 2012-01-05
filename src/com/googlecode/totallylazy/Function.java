package com.googlecode.totallylazy;

import com.googlecode.totallylazy.callables.LazyCallable;
import com.googlecode.totallylazy.callables.SleepyCallable;
import com.googlecode.totallylazy.callables.TimeCallable;

import java.util.concurrent.Callable;

public abstract class Function<T> implements Callable<T>, Runnable {
    public T apply() {
        return Callers.call(this);
    }

    @Override
    public void run() {
        apply();
    }

    public Function<T> lazy() {
        return LazyCallable.lazy(this);
    }

    public Function<T> sleep(int millis) {
        return SleepyCallable.sleepy(this, millis);
    }

    public Sequence<T> repeat() {
        return Sequences.repeat(this);
    }

    public Function<T> time(Callable1<Double, Void> report) {
        return TimeCallable.time(this, report);
    }

    public Function<T> time() {
        return TimeCallable.time(this);
    }
}