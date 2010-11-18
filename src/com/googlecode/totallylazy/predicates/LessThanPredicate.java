package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.predicates.LessThan;

public class LessThanPredicate<T> implements LessThan<Comparable<T>> {
    private final Comparable<T> comparable;

    public LessThanPredicate(Comparable<T> comparable) {
        this.comparable = comparable;
    }

    public boolean matches(Comparable<T> other) {
        return other.compareTo((T) comparable) < 0;
    }

    public Comparable<T> value() {
        return comparable;
    }
}
