package com.googlecode.totallylazy.callables;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callables;

import java.util.Comparator;

import static com.googlecode.totallylazy.Callables.ascending;
import static com.googlecode.totallylazy.Callers.call;

public class DescendingComparator<T> implements Comparator<T> {
    private final Callable1<T, ? extends Comparable> callable;

    public DescendingComparator(Callable1<T, ? extends Comparable> callable) {
        this.callable = callable;
    }

    public final int compare(final T first, final T second) {
        return call(callable, second).compareTo(call(callable, first));
    }

    public Callable1<T, ? extends Comparable> callable() {
        return callable;
    }
}
