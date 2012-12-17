package com.googlecode.totallylazy;

import com.googlecode.totallylazy.callables.LazyCallable1;
import com.googlecode.totallylazy.callables.SleepyCallable1;
import com.googlecode.totallylazy.callables.TimeCallable1;

import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Pair.pair;

public abstract class Function1<A, B> implements Callable1<A, B>, Functor<B> {
    public B apply(final A a) {
        return Functions.call(this, a);
    }

    public Function<B> deferApply(final A a) {
        return Callables.deferApply(this, a);
    }

    public Function<B> callConcurrently(final A a) {
        return Callers.callConcurrently(deferApply(a));
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

    public Function1<A, Either<Exception, B>> orException() {
        return either();
    }

    @Override
    public <C> Function1<A, C> map(final Callable1<? super B, ? extends C> callable) {
        return Callables.compose(this, callable);
    }

    public <C> Function1<A, C> then(final Callable1<? super B, ? extends C> callable) {
        return map(callable);
    }

    public <C> Function1<A, C> then(final Callable<? extends C> callable) {
        return Callables.compose(this, callable);
    }

    public Function1<A,B> interruptable() {
        return Callables.interruptable(this);
    }

    public Function1<A, Function<B>> deferExecution() {
        return Callables.deferReturn(this);
    }

    public Function1<A, Pair<A, B>> capturing() {
        return new Function1<A, Pair<A, B>>() {
            public Pair<A, B> call(A original) throws Exception {
                return pair(original, Function1.this.apply(original));
            }
        };
    }

    public Function1<A,B> time() {
        return TimeCallable1.time1(this);
    }

    public Function1<A,B> time(Callable1<? super Number, ?> reporter) {
        return TimeCallable1.time1(this, reporter);
    }

    public Option<B> $(Option<? extends A> applicative) {
        return applicative.applicate(Option.some(this));
    }

    public <L> Either<L, B> $(Either<L, ? extends A> applicative) {
        return applicative.applicate(Either.<L, Function1<A, B>>right(this));
    }

    public Sequence<B> $(Sequence<? extends A> applicative) {
        return applicative.applicate(Sequences.one(this));
    }
}
