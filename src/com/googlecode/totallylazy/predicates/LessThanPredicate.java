package com.googlecode.totallylazy.predicates;

public class LessThanPredicate<T extends Comparable<? super T>> extends LogicalPredicate<T> implements LessThan<T> {
    private final T comparable;

    public LessThanPredicate(T comparable) {
        this.comparable = comparable;
    }

    public boolean matches(T other) {
        return other.compareTo(comparable) < 0;
    }

    public T value() {
        return comparable;
    }
}
