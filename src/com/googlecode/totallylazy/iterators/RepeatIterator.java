package com.googlecode.totallylazy.iterators;

import com.googlecode.totallylazy.Callers;

import java.util.concurrent.Callable;

public class RepeatIterator<T> extends ReadOnlyIterator<T> {
    private final Callable<T>  callable;

    public RepeatIterator(Callable<T> callable) {
        this.callable = callable;
    }

    public boolean hasNext() {
        return true;
    }

    public T next() {
        return Callers.call(callable);
    }
}
