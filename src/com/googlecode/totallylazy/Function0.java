package com.googlecode.totallylazy;

import com.googlecode.totallylazy.functions.LazyCallable;
import com.googlecode.totallylazy.functions.SleepyCallable;
import com.googlecode.totallylazy.functions.TimeCallable;
import com.googlecode.totallylazy.functions.TimeReport;

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
        return LazyCallable.lazy(this);
    }

    default Function0<A> sleep(int millis) {
        return SleepyCallable.sleepy(this, millis);
    }

    default Sequence<A> repeat() {
        return Sequences.repeat(this);
    }

    default Function0<A> time(Function1<? super Number, ?> report) {
        return TimeCallable.time(this, report);
    }

    default Function0<A> time() {
        return TimeCallable.time(this);
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