package com.googlecode.totallylazy.comparators;

import com.googlecode.totallylazy.Function1;

import java.util.Comparator;

import static com.googlecode.totallylazy.Callers.call;
import static com.googlecode.totallylazy.Unchecked.cast;

public class DescendingComparator<T, R extends Comparable<? super R>> implements Comparator<T> {
    private final Function1<? super T, ? extends R> callable;

    public DescendingComparator(Function1<? super T, ? extends R> callable) {
        this.callable = callable;
    }

    public final int compare(final T first, final T second) {
        return NullComparator.compare(call(callable, second), call(callable, first), NullComparator.Direction.Down);
    }

    public Function1<T, R> callable() {
        return cast(callable);
    }
}
