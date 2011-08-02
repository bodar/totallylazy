package com.googlecode.totallylazy;

import com.googlecode.totallylazy.numbers.Numbers;

import static com.googlecode.totallylazy.Callables.asHashCode;

public abstract class Tuple<F, S> implements First<F>, Second<S> {
    protected final F first;
    protected final S second;

    public Tuple(final F first, final S second) {
        this.second = second;
        this.first = first;
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

    protected abstract Sequence<Object> values();

    @Override
    public final boolean equals(final Object o) {
        if (o instanceof Tuple) {
            Sequence otherValues = ((Tuple) o).values();
            if (!Numbers.equalTo(values().size(), otherValues.size())) {
                return false;
            }
            return values().zip(otherValues).forAll(Predicates.equalTo());
        }
        return false;
    }

    @Override
    public final int hashCode() {
        return values().fold(31, asHashCode());
    }
}
