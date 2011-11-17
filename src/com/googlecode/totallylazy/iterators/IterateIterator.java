package com.googlecode.totallylazy.iterators;

import com.googlecode.totallylazy.Callable1;

import static com.googlecode.totallylazy.Callers.call;

public final class IterateIterator<T> extends ReadOnlyIterator<T> {
    private final Callable1<? super T, T> callable;
    private T t;

    public IterateIterator(final Callable1<? super T, T> callable, final T t) {
        this.callable = callable;
        this.t = t;
    }

    public final boolean hasNext() {
        return true;
    }

    public final T next() {
        T result = t;
        t = call(callable, t);
        return result;
    }

}
