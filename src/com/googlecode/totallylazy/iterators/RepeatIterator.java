package com.googlecode.totallylazy.iterators;

import com.googlecode.totallylazy.Callers;

import java.util.concurrent.Callable;

public final class RepeatIterator<T> implements ReadOnlyIterator<T> {
    private final Callable<? extends T>  callable;

    public RepeatIterator(final Callable<? extends T> callable) {
        this.callable = callable;
    }

    public final boolean hasNext() {
        return true;
    }

    public final T next() {
        return Callers.call(callable);
    }
}
