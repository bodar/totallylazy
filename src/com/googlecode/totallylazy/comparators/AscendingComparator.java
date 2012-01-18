package com.googlecode.totallylazy.comparators;

import com.googlecode.totallylazy.Callable1;

import java.util.Comparator;

import static com.googlecode.totallylazy.Callers.call;

public class AscendingComparator<T, R extends Comparable<? super R>> implements Comparator<T> {
    private final Callable1<T, R> callable;

    public AscendingComparator(Callable1<T, R> callable) {
        this.callable = callable;
    }

    public final int compare(final T first, final T second) {
        return NullComparator.compare(call(callable, first), call(callable, second), NullComparator.Direction.Up) ;
    }

    public Callable1<T, R> callable() {
        return callable;
    }
}
