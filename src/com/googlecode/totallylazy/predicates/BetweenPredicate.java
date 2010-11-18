package com.googlecode.totallylazy.predicates;

import static com.googlecode.totallylazy.Predicates.greaterThanOrEqualTo;
import static com.googlecode.totallylazy.Predicates.lessThanOrEqualTo;

public class BetweenPredicate<T> implements Between<Comparable<T>> {
    private final Comparable<T> lower;
    private final Comparable<T> upper;

    public BetweenPredicate(Comparable<T> lower, Comparable<T> upper) {
        this.lower = lower;
        this.upper = upper;
    }

    public boolean matches(Comparable<T> other) {
        return greaterThanOrEqualTo(lower).matches(other) && lessThanOrEqualTo(upper).matches(other);
    }

    public Comparable<T> lower() {
        return lower;
    }

    public Comparable<T> upper() {
        return upper;
    }
}
