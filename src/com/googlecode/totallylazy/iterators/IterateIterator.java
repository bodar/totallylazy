package com.googlecode.totallylazy.iterators;

import com.googlecode.totallylazy.*;

public class IterateIterator<T> extends com.googlecode.totallylazy.Iterator<T> {
    private final Callable1<T, T> callable;
    private T t;

    public IterateIterator(Callable1<T, T> callable, T t) {
        this.callable = callable;
        this.t = t;
    }

    public boolean hasNext() {
        return true;
    }

    public T next() {
        try {
            T result = t;
            t = callable.call(t);
            return result;
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

}
