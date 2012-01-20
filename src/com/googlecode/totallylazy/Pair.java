package com.googlecode.totallylazy;

import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Function.function;
import static com.googlecode.totallylazy.Function.returns;
import static com.googlecode.totallylazy.Sequences.sequence;

public class Pair<F, S> implements First<F>, Second<S> {
    private final F first;
    private final Function<? extends S> second;

    public Pair(final F first, final S second) {
        this(first, returns(second));
    }

    public Pair(final F first, final Callable<? extends S> second) {
        this.second = function(second).lazy();
        this.first = first;
    }

    public static <F, S> Pair<F, S> pair(final F first, final S second) {
        return new Pair<F, S>(first, second);
    }

    public static <F, S> Pair<F, S> pair(final F first, final Callable<? extends S> second) {
        return new Pair<F, S>(first, second);
    }

    public final F first() {
        return first;
    }

    public final S second() {
        return second.apply();
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
