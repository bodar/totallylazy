package com.googlecode.totallylazy.iterators;

import com.googlecode.totallylazy.Callable1;

import static com.googlecode.totallylazy.Callables.call;

public class IterateIterator<T> extends ReadOnlyIterator<T> {
    private final Callable1<? super T, T> callable;
    private T t;

    public IterateIterator(Callable1<? super T, T> callable, T t) {
        this.callable = callable;
        this.t = t;
    }

    public boolean hasNext() {
        return true;
    }

    public T next() {
        T result = t;
        t = call(callable, t);
        return result;
    }

}
