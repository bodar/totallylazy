package com.googlecode.totallylazy.comparators;

import com.googlecode.totallylazy.Function;

import java.util.Comparator;

import static com.googlecode.totallylazy.Unchecked.cast;

public class AscendingComparator<T, R extends Comparable<? super R>> implements Comparator<T> {
    private final Function<? super T, ? extends R> callable;

    public AscendingComparator(Function<? super T, ? extends R> callable) {
        this.callable = callable;
    }

    public final int compare(final T first, final T second) {
        return NullComparator.compare(callable.apply(first), callable.apply(second), NullComparator.Direction.Up) ;
    }

    public Function<T, R> callable() {
        return cast(callable);
    }
}
