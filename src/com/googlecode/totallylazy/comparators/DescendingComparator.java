package com.googlecode.totallylazy.comparators;

import com.googlecode.totallylazy.Function;

import java.util.Comparator;

import static com.googlecode.totallylazy.Unchecked.cast;

public class DescendingComparator<T, R extends Comparable<? super R>> implements Comparator<T> {
    private final Function<? super T, ? extends R> callable;

    public DescendingComparator(Function<? super T, ? extends R> callable) {
        this.callable = callable;
    }

    public final int compare(final T first, final T second) {
        return NullComparator.compare(callable.apply(second), callable.apply(first), NullComparator.Direction.Down);
    }

    public Function<T, R> callable() {
        return cast(callable);
    }
}
