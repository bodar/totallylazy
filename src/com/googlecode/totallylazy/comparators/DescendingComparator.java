package com.googlecode.totallylazy.comparators;

import com.googlecode.totallylazy.Callable1;

import java.util.Comparator;

import static com.googlecode.totallylazy.Callers.call;

public class DescendingComparator<T, R extends Comparable<R>> implements Comparator<T> {
    private final Callable1<T, R> callable;

    public DescendingComparator(Callable1<T, R> callable) {
        this.callable = callable;
    }

    public final int compare(final T first, final T second) {
        return call(callable, second).compareTo(call(callable, first));
    }

    public Callable1<T, R> callable() {
        return callable;
    }
}
