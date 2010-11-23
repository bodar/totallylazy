package com.googlecode.totallylazy.callables;

import com.googlecode.totallylazy.Callable1;

import java.util.Comparator;

import static com.googlecode.totallylazy.Callers.call;

public class AscendingComparator<T> implements Comparator<T> {
    private final Callable1<T, ? extends Comparable> callable;

    public AscendingComparator(Callable1<T, ? extends Comparable> callable) {
        this.callable = callable;
    }

    public final int compare(final T first, final T second) {
        return call(callable, first).compareTo(call(callable, second));
    }

    public Callable1<T, ? extends Comparable> callable() {
        return callable;
    }
}
