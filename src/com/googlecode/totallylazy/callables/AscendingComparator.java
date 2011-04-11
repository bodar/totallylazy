package com.googlecode.totallylazy.callables;

import com.googlecode.totallylazy.Callable1;

import java.util.Comparator;

import static com.googlecode.totallylazy.Callers.call;

public class AscendingComparator<T, R extends Comparable<R>> implements Comparator<T> {
    private final Callable1<T, R> callable;

    public AscendingComparator(Callable1<T, R> callable) {
        this.callable = callable;
    }

    public final int compare(final T first, final T second) {
        return call(callable, first).compareTo(call(callable, second));
    }

    public Callable1<T, R> callable() {
        return callable;
    }
}
