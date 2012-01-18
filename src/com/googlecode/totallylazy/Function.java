package com.googlecode.totallylazy;

import com.googlecode.totallylazy.callables.LazyCallable;
import com.googlecode.totallylazy.callables.SleepyCallable;
import com.googlecode.totallylazy.callables.TimeCallable;

import java.util.concurrent.Callable;

public abstract class Function<A> implements Callable<A>, Runnable, Functor<A, Function<?>> {
    public static <A> Function<A> function(final Callable<? extends A> callable) {
        return new Function<A>() {
            @Override
            public A call() throws Exception {
                return callable.call();
            }
        };
    }

    public A apply() {
        return Callers.call(this);
    }

    @Override
    public void run() {
        apply();
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

    public Function<A> time(Callable1<Double, Void> report) {
        return TimeCallable.time(this, report);
    }

    public Function<A> time() {
        return TimeCallable.time(this);
    }

    @Override
    public <B> Function<B> map(final Callable1<? super A, B> callable) {
        return Callables.compose(this, callable);
    }

    public <B> Function<B> then(final Callable1<? super A, B> callable) {
        return map(callable);
    }
}