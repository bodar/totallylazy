package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.predicates.GreaterThan;

import java.util.Date;

public class GreaterThanPredicate<T> implements GreaterThan<Comparable<T>> {
    private final Comparable<T> comparable;

    public GreaterThanPredicate(Comparable<T> comparable) {
        this.comparable = comparable;
    }

    public boolean matches(Comparable<T> other) {
        return other.compareTo((T) comparable) > 0;
    }

    public Comparable<T> value() {
        return comparable;
    }
}
