package com.googlecode.totallylazy.callables;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callers;

import java.util.Comparator;

public class AscendingComparator<T> implements Comparator<T> {
    private final Callable1<T, ? extends Comparable> callable;

    public AscendingComparator(Callable1<T, ? extends Comparable> callable) {
        this.callable = callable;
    }

    public final int compare(final T first, final T second) {
        return Callers.call(callable, first).compareTo(Callers.call(callable, second));
    }

    public Callable1<T, ? extends Comparable> callable() {
        return callable;
    }
}
