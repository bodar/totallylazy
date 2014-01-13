package com.googlecode.totallylazy.predicates;

public class LessThanOrEqualToPredicate<T extends Comparable<? super T>> extends AbstractPredicate<T> implements LessThanOrEqualTo<T> {
    private final T comparable;

    private LessThanOrEqualToPredicate(T comparable) {
        this.comparable = comparable;
    }

    public static <T extends Comparable<? super T>> LessThanOrEqualToPredicate<T> lessThanOrEqualTo(T comparable) {
        return new LessThanOrEqualToPredicate<T>(comparable);
    }

    public boolean matches(T other) {
        return other.compareTo(comparable) <= 0;
    }

    public T value() {
        return comparable;
    }
}
