package com.googlecode.totallylazy;

import com.googlecode.totallylazy.callables.SleepyCallable;
import com.googlecode.totallylazy.callables.TimeCallable;
import com.googlecode.totallylazy.callables.TimeReport;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

public interface Returns<A> extends Callable<A>, Runnable, Functor<A>, Value<A>, Supplier<A> {
    default A apply() {
        return Functions.call(this);
    }

    @Override
    default A get() {
        return apply();
    }

    @Override
    default void run() {
        apply();
    }

    @Override
    default A value() {
        return apply();
    }

    default Returns<A> lazy() {
        return Lazy.lazy(this);
    }

    default Returns<A> sleep(int millis) {
        return SleepyCallable.sleepy(this, millis);
    }

    default Seq<A> repeat() {
        return Sequences.repeat(this);
    }

    default Returns<A> time(Function<? super Number, ?> report) {
        return TimeCallable.time(this, report);
    }

    default Returns<A> time() {
        return TimeCallable.time(this);
    }

    default TimeReport time(int numberOfCalls) {
        return TimeReport.time(numberOfCalls, this);
    }

    @Override
    default <B> Returns<B> map(final Function<? super A, ? extends B> callable) {
        return Callables.compose(this, callable);
    }

    default <B> Returns<B> then(final Function<? super A, ? extends B> callable) {
        return map(callable);
    }

    default Returns<A> interruptable() {
        return Functions.interruptable(this);
    }
}