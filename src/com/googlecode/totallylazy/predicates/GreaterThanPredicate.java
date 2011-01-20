package com.googlecode.totallylazy.predicates;

public class GreaterThanPredicate<T extends Comparable<T>> extends LogicalPredicate<T> implements GreaterThan<T> {
    private final T comparable;

    public GreaterThanPredicate(T comparable) {
        this.comparable = comparable;
    }

    public boolean matches(T other) {
        return other.compareTo(comparable) > 0;
    }

    public T value() {
        return comparable;
    }
}
