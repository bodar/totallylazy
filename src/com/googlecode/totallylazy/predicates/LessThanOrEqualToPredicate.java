package com.googlecode.totallylazy.predicates;

public class LessThanOrEqualToPredicate<T extends Comparable<? super T>> extends LogicalPredicate<T> implements LessThanOrEqualTo<T> {
    private final T comparable;

    public LessThanOrEqualToPredicate(T comparable) {
        this.comparable = comparable;
    }

    public boolean matches(T other) {
        return other.compareTo(comparable) <= 0;
    }

    public T value() {
        return comparable;
    }
}
