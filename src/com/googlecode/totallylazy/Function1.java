package com.googlecode.totallylazy;

import com.googlecode.totallylazy.callables.LazyCallable1;
import com.googlecode.totallylazy.callables.SleepyCallable1;

import java.util.concurrent.Callable;

public abstract class Function1<A, B> implements Callable1<A, B>, Functor<B, Function1<A, ?>> {
    public static <A, B> Function1<A, B> function(final Callable1<A, B> callable) {
        return new Function1<A, B>() {
            @Override
            public B call(A a) throws Exception {
                return callable.call(a);
            }
        };
    }

    public B apply(final A a) {
        return Callers.call(this, a);
    }

    public Function<B> curry(final A a) {
        return Callables.curry(this, a);
    }

    public Function1<A, B> lazy() {
        return LazyCallable1.lazy(this);
    }

    public Function1<A, B> sleep(int millis) {
        return SleepyCallable1.sleepy(this, millis);
    }

    public Function1<A, Option<B>> optional() {
        return Exceptions.optional(this);
    }

    public Function1<A, Either<Exception, B>> either() {
        return Exceptions.either(this);
    }

    @Override
    public <C> Function1<A, C> map(final Callable1<? super B, C> callable) {
        return Callables.compose(this, callable);
    }

    public <C> Function1<A, C> then(final Callable1<? super B, C> callable) {
        return map(callable);
    }

    public <C> Function1<A, C> then(final Callable<C> callable) {
        return Callables.compose(this, callable);
    }

    public Function1<A,B> interruptable() {
        return Callables.interruptable(this);
    }

    public Function1<A, Function<B>> deferExecution() {
        return Callables.deferExecution(this);
    }
}
