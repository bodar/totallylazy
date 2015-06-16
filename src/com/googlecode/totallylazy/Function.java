package com.googlecode.totallylazy;

import com.googlecode.totallylazy.callables.LazyFunction;
import com.googlecode.totallylazy.callables.SleepyFunction;
import com.googlecode.totallylazy.callables.TimeFunction;

import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Pair.pair;

public abstract class Function<A, B> extends Eq implements Callable1<A, B>, Functor<B> {
    public B apply(final A a) {
        return Functions.call(this, a);
    }

    public Returns<B> deferApply(final A a) {
        return Callables.deferApply(this, a);
    }

    public Returns<B> callConcurrently(final A a) {
        return Callers.callConcurrently(deferApply(a));
    }

    public Function<A, B> lazy() {
        return LazyFunction.lazy(this);
    }

    public Function<A, B> sleep(int millis) {
        return SleepyFunction.sleepy(this, millis);
    }

    public Function<A, Option<B>> optional() {
        return Exceptions.optional(this);
    }

    public Function<A, Either<Exception, B>> either() {
        return Exceptions.either(this);
    }

    public Function<A, Either<Exception, B>> orException() {
        return either();
    }

    public Mapper<A, B> orElse(final B result) {
        return Exceptions.orElse(this, result);
    }

    @Override
    public <C> Function<A, C> map(final Callable1<? super B, ? extends C> callable) {
        return Callables.compose(this, callable);
    }

    public <C> Function<A, C> then(final Callable1<? super B, ? extends C> callable) {
        return map(callable);
    }

    public <C> Function<A, C> then(final Callable<? extends C> callable) {
        return Callables.compose(this, callable);
    }

    public Function<A,B> interruptable() {
        return Functions.interruptable(this);
    }

    public Function<A, Returns<B>> deferExecution() {
        return Callables.deferReturn(this);
    }

    public Function<A, Pair<A, B>> capturing() {
        return new Function<A, Pair<A, B>>() {
            public Pair<A, B> call(A original) throws Exception {
                return pair(original, Function.this.apply(original));
            }
        };
    }

    public Function<A,B> time() {
        return TimeFunction.time1(this);
    }

    public Function<A,B> time(Callable1<? super Number, ?> reporter) {
        return TimeFunction.time1(this, reporter);
    }

    public Option<B> $(Option<? extends A> applicative) {
        return applicative.applicate(Option.some(this));
    }

    public <L> Either<L, B> $(Either<L, ? extends A> applicative) {
        return applicative.applicate(Either.<L, Function<A, B>>right(this));
    }

    public Sequence<B> $(Sequence<? extends A> applicative) {
        return applicative.applicate(Sequences.one(this));
    }
}
