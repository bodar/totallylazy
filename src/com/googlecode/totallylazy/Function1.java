package com.googlecode.totallylazy;

import com.googlecode.totallylazy.callables.LazyCallable1;
import com.googlecode.totallylazy.callables.SleepyCallable1;

public abstract class Function1<A, B> implements Callable1<A, B> {
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
}
