package com.googlecode.totallylazy;

import com.googlecode.totallylazy.callables.LazyFunction;
import com.googlecode.totallylazy.callables.SleepyFunction;
import com.googlecode.totallylazy.callables.TimeFunction;

import java.util.concurrent.Callable;

@FunctionalInterface
public interface Function<A, B> extends Functor<B>, java.util.function.Function<A,B> {
    B call(A a) throws Exception;

    default B apply(final A a) {
        return Functions.call(this, a);
    }

    default Returns<B> deferApply(final A a) {
        return Callables.deferApply(this, a);
    }

    default Returns<B> callConcurrently(final A a) {
        return Callers.callConcurrently(deferApply(a));
    }

    default Function<A, B> lazy() {
        return LazyFunction.lazy(this);
    }

    default Function<A, B> sleep(int millis) {
        return SleepyFunction.sleepy(this, millis);
    }

    default Function<A, Option<B>> optional() {
        return Exceptions.optional(this);
    }

    default Function<A, Either<Exception, B>> either() {
        return Exceptions.either(this);
    }

    default Function<A, Either<Exception, B>> orException() {
        return either();
    }

    default Function<A, B> orElse(final B result) {
        return Exceptions.orElse(this, result);
    }

    @Override
    default <C> Function<A, C> map(final Function<? super B, ? extends C> callable) {
        return Callables.compose(this, callable);
    }

    default <C> Function<A, C> then(final Function<? super B, ? extends C> callable) {
        return map(callable);
    }

    default <C> Function<A, C> thenReturn(final Callable<? extends C> callable) {
        return Callables.compose(this, callable);
    }

    default Function<A,B> interruptable() {
        return Functions.interruptable(this);
    }

    default Function<A, Returns<B>> deferExecution() {
        return Callables.deferReturn(this);
    }

    default Function<A, Pair<A, B>> capturing() {
        return original -> Pair.pair(original, Function.this.apply(original));
    }

    default Function<A,B> time() {
        return TimeFunction.time1(this);
    }

    default Function<A,B> time(Function<? super Number, ?> reporter) {
        return TimeFunction.time1(this, reporter);
    }

    default Option<B> $(Option<? extends A> applicative) {
        return applicative.applicate(Option.some(this));
    }

    default <L> Either<L, B> $(Either<L, ? extends A> applicative) {
        return applicative.applicate(Either.<L, Function<A, B>>right(this));
    }

    default Seq<B> $(Seq<? extends A> applicative) {
        return applicative.applicate(Sequences.one(this));
    }
}
