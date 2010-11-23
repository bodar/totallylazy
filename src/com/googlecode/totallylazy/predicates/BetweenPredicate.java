package com.googlecode.totallylazy.predicates;

import static com.googlecode.totallylazy.Predicates.greaterThanOrEqualTo;
import static com.googlecode.totallylazy.Predicates.lessThanOrEqualTo;

public class BetweenPredicate<T extends Comparable<T>>  implements Between<T> {
    private final T lower;
    private final T upper;

    public BetweenPredicate(T lower, T upper) {
        this.lower = lower;
        this.upper = upper;
    }

    public boolean matches(T other) {
        return greaterThanOrEqualTo(lower).matches(other) && lessThanOrEqualTo(upper).matches(other);
    }

    public T lower() {
        return lower;
    }

    public T upper() {
        return upper;
    }
}
