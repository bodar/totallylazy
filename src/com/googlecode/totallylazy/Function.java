package com.googlecode.totallylazy;

import com.googlecode.totallylazy.callables.LazyCallable;
import com.googlecode.totallylazy.callables.SleepyCallable;
import com.googlecode.totallylazy.callables.TimeCallable;

import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.LazyException.lazyException;

public abstract class Function<A> implements Callable<A>, Runnable, Functor<A>, Value<A> {
    public static <A> Function<A> function(final Callable<? extends A> callable) {
        return new Function<A>() {
            @Override
            public A call() throws Exception {
                return callable.call();
            }
        };
    }

    public A apply() {
        return Function.call(this);
    }

    public static <A> A call(final Callable<? extends A> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            throw lazyException(e);
        }
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

    public static <T> Function<T> returns(final T t) {
        return new Function<T>() {
            public final T call() throws Exception {
                return t;
            }
        };
    }
}