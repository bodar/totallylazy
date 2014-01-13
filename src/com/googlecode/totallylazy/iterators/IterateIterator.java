package com.googlecode.totallylazy.iterators;

import com.googlecode.totallylazy.Function;

public final class IterateIterator<T> extends ReadOnlyIterator<T> {
    private final Function<? super T, ? extends T> callable;
    private T t;

    public IterateIterator(final Function<? super T, ? extends T> callable, final T t) {
        this.callable = callable;
        this.t = t;
    }

    public final boolean hasNext() {
        return true;
    }

    public final T next() {
        T result = t;
        t = callable.apply(t);
        return result;
    }

}
