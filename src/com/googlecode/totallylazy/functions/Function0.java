package com.googlecode.totallylazy.functions;

import com.googlecode.totallylazy.*;

import java.util.concurrent.Callable;

public interface Function0<A> extends Callable<A>, Runnable, Functor<A>, Value<A> {
    default A apply() {
        return Functions.call(this);
    }

    @Override
    default void run() {
        apply();
    }

    @Override
    default A value() {
        return apply();
    }

    default Function0<A> lazy() {
        return Lazy.lazy(this);
    }

    default Function0<A> sleep(int millis) {
        return SleepyFunction0.sleepy(this, millis);
    }

    default Sequence<A> repeat() {
        return Sequences.repeat(this);
    }

    default Function0<A> time(Function1<? super Number, ?> report) {
        return TimeFunction0.time(this, report);
    }

    default Function0<A> time() {
        return TimeFunction0.time(this);
    }

    default TimeReport time(int numberOfCalls) {
        return TimeReport.time(numberOfCalls, this);
    }

    @Override
    default <B> Function0<B> map(final Function1<? super A, ? extends B> callable) {
        return Callables.compose(this, callable);
    }

    default <B> Function0<B> then(final Function1<? super A, ? extends B> callable) {
        return map(callable);
    }

    default Function0<A> interruptable() {
        return Functions.interruptable(this);
    }
}