package com.googlecode.totallylazy;

import com.googlecode.totallylazy.callables.LazyCallable1;
import com.googlecode.totallylazy.callables.SleepyCallable1;
import com.googlecode.totallylazy.callables.TimeCallable1;

import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Pair.pair;

public interface Function1<A, B> extends Callable1<A, B>, Functor<B> {
    default B apply(final A a) {
        return Functions.call(this, a);
    }

    default Returns<B> deferApply(final A a) {
        return Callables.deferApply(this, a);
    }

    default Returns<B> callConcurrently(final A a) {
        return Callers.callConcurrently(deferApply(a));
    }

    default Function1<A, B> lazy() {
        return LazyCallable1.lazy(this);
    }

    default Function1<A, B> sleep(int millis) {
        return SleepyCallable1.sleepy(this, millis);
    }

    default Function1<A, Option<B>> optional() {
        return Exceptions.optional(this);
    }

    default Function1<A, Either<Exception, B>> either() {
        return Exceptions.either(this);
    }

    default Function1<A, Either<Exception, B>> orException() {
        return either();
    }

    default Mapper<A, B> orElse(final B result) {
        return Exceptions.orElse(this, result);
    }

    @Override
    default <C> Function1<A, C> map(final Callable1<? super B, ? extends C> callable) {
        return Callables.compose(this, callable);
    }

    default <C> Function1<A, C> then(final Callable1<? super B, ? extends C> callable) {
        return map(callable);
    }

    default <C> Function1<A, C> then(final Callable<? extends C> callable) {
        return Callables.compose(this, callable);
    }

    default Function1<A,B> interruptable() {
        return Functions.interruptable(this);
    }

    default Function1<A, Returns<B>> deferExecution() {
        return Callables.deferReturn(this);
    }

    default Function1<A, Pair<A, B>> capturing() {
        return original -> pair(original, Function1.this.apply(original));
    }

    default Function1<A,B> time() {
        return TimeCallable1.time1(this);
    }

    default Function1<A,B> time(Callable1<? super Number, ?> reporter) {
        return TimeCallable1.time1(this, reporter);
    }

    default Option<B> $(Option<? extends A> applicative) {
        return applicative.applicate(Option.some(this));
    }

    default <L> Either<L, B> $(Either<L, ? extends A> applicative) {
        return applicative.applicate(Either.<L, Function1<A, B>>right(this));
    }

    default Sequence<B> $(Sequence<? extends A> applicative) {
        return applicative.applicate(Sequences.one(this));
    }
}
