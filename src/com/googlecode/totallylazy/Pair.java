package com.googlecode.totallylazy;

import static com.googlecode.totallylazy.Sequences.sequence;

public class Pair<F, S> implements First<F>, Second<S> {
    protected final F first;
    protected final S second;

    public Pair(final F first, final S second) {
        this.second = second;
        this.first = first;
    }

    public static <F, S> Pair<F, S> pair(final F first, final S second) {
        return new Pair<F, S>(first, second);
    }

    public final F first() {
        return first;
    }

    public final S second() {
        return second;
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
        return sequence(first, second);
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
