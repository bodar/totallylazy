package com.googlecode.totallylazy;

import com.googlecode.totallylazy.callables.LazyCallable;
import com.googlecode.totallylazy.callables.SleepyCallable;
import com.googlecode.totallylazy.callables.TimeCallable;

import java.util.concurrent.Callable;

public abstract class Function<A> implements Callable<A>, Runnable, Functor<A>, Value<A> {
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

    public Function<A> lazy() {
        return LazyCallable.lazy(this);
    }

    public Function<A> sleep(int millis) {
        return SleepyCallable.sleepy(this, millis);
    }

    public Sequence<A> repeat() {
        return Sequences.repeat(this);
    }

    public Function<A> time(Callable1<? super Number, ?> report) {
        return TimeCallable.time(this, report);
    }

    public Function<A> time() {
        return TimeCallable.time(this);
    }

    @Override
    public <B> Function<B> map(final Callable1<? super A, ? extends B> callable) {
        return Callables.compose(this, callable);
    }

    public <B> Function<B> then(final Callable1<? super A, ? extends B> callable) {
        return map(callable);
    }

}