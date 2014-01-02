package com.googlecode.totallylazy.predicates;

public class GreaterThanPredicate<T extends Comparable<? super T>> extends LogicalPredicate<T> implements GreaterThan<T> {
    private final T comparable;

    private GreaterThanPredicate(T comparable) {
        this.comparable = comparable;
    }

    public static <T extends Comparable<? super T>> GreaterThanPredicate<T> greaterThan(T comparable) {
        return new GreaterThanPredicate<T>(comparable);
    }

    public boolean matches(T other) {
        return other.compareTo(comparable) > 0;
    }

    public T value() {
        return comparable;
    }
}
