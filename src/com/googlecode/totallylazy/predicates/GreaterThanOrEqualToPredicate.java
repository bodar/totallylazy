package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.predicates.GreaterThanOrEqualTo;

import java.util.Date;

public class GreaterThanOrEqualToPredicate<T> implements GreaterThanOrEqualTo<Comparable<T>> {
    private final Comparable<T> comparable;

    public GreaterThanOrEqualToPredicate(Comparable<T> comparable) {
        this.comparable = comparable;
    }

    public boolean matches(Comparable<T> other) {
        return other.compareTo((T) comparable) >= 0;
    }

    public Comparable<T> value() {
        return comparable;
    }
}
