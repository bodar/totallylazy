package com.googlecode.totallylazy.predicates;

public class LessThanOrEqualToPredicate<T> implements LessThanOrEqualTo<Comparable<T>> {
    private final Comparable<T> comparable;

    public LessThanOrEqualToPredicate(Comparable<T> comparable) {
        this.comparable = comparable;
    }

    public boolean matches(Comparable<T> other) {
        return other.compareTo((T) comparable) <= 0;
    }

    public Comparable<T> value() {
        return comparable;
    }
}
