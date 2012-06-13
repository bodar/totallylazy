package com.googlecode.totallylazy;

import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Function.returns;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.callables.LazyCallable.lazy;

public class Pair<F, S> implements First<F>, Second<S> {
    private final Value<? extends F> first;
    private final Value<? extends S> second;

    protected Pair(final Callable<? extends F> first, final Callable<? extends S> second) {
        this.first = lazy(first);
        this.second = lazy(second);
    }

    public static <F, S> Pair<F, S> pair(final F first, final S second) {
        return new Pair<F, S>(returns(first), returns(second));
    }

    public static <F, S> Pair<F, S> pair(final Callable<? extends F> first, final Callable<? extends S> second) {
        return new Pair<F, S>(first, second);
    }

    public final F first() {
        return first.value();
    }

    public final S second() {
        return second.value();
    }

    @Override
    public final String toString() {
        return toString("[", ",", "]");
    }

    public final String toString(String separator) {
        return toString("", separator, "");
    }

    public final String toString(String start, String separator, String end) {
        return values().toString(start, separator, end);
    }

    public Sequence<Object> values() {
        return sequence(first(), second());
    }

    @Override
    public final boolean equals(final Object o) {
        return o instanceof Pair && values().equals(((Pair) o).values());
    }

    @Override
    public final int hashCode() {
        return values().hashCode();
    }
}
