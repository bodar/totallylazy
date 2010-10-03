package com.googlecode.totallylazy.iterators;

import com.googlecode.totallylazy.Callable1;

import java.util.Iterator;

import static com.googlecode.totallylazy.Callers.call;

public class MapIterator<T, S> extends ReadOnlyIterator<S> {
    private final Iterator<T> iterator;
    private final Callable1<? super T, S> callable;

    public MapIterator(Iterator<T> iterator, Callable1<? super T, S> callable) {
        this.iterator = iterator;
        this.callable = callable;
    }

    public boolean hasNext() {
        return iterator.hasNext();
    }

    public S next() {
        return call(callable, iterator.next());
    }
}
