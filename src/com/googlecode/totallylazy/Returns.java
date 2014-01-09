package com.googlecode.totallylazy;

import com.googlecode.totallylazy.callables.LazyCallable;
import com.googlecode.totallylazy.callables.SleepyCallable;
import com.googlecode.totallylazy.callables.TimeCallable;
import com.googlecode.totallylazy.callables.TimeReport;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

public abstract class Returns<A> implements Callable<A>, Runnable, Functor<A>, Value<A>, Supplier<A> {
    public A apply() {
        return Functions.call(this);
    }

    @Override
    public void run() {
        apply();
    }

    @Override
    public A value() {
        return apply();
    }

    public Returns<A> lazy() {
        return LazyCallable.lazy(this);
    }

    public Returns<A> sleep(int millis) {
        return SleepyCallable.sleepy(this, millis);
    }

    public Sequence<A> repeat() {
        return Sequences.repeat(this);
    }

    public Returns<A> time(Callable1<? super Number, ?> report) {
        return TimeCallable.time(this, report);
    }

    public Returns<A> time() {
        return TimeCallable.time(this);
    }

    public TimeReport time(int numberOfCalls) {
        return TimeReport.time(numberOfCalls, this);
    }

    @Override
    public <B> Returns<B> map(final Callable1<? super A, ? extends B> callable) {
        return Callables.compose(this, callable);
    }

    public <B> Returns<B> then(final Callable1<? super A, ? extends B> callable) {
        return map(callable);
    }

    public Returns<A> interruptable() {
        return Functions.interruptable(this);
    }

    @Override
    public A get() {
        return apply();
    }
}