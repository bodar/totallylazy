package com.googlecode.totallylazy.callables;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callables;

import java.util.Comparator;

public class DescendingComparator<T> implements Comparator<T> {
    private final Callable1<T, ? extends Comparable> callable;

    public DescendingComparator(Callable1<T, ? extends Comparable> callable) {
        this.callable = callable;
    }

    public final int compare(final T first, final T second) {
        return Callables.ascending(callable).compare(first, second) * -1;
    }

    public Callable1<T, ? extends Comparable> callable() {
        return callable;
    }
}
