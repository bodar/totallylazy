package com.googlecode.totallylazy.iterators;

import com.googlecode.totallylazy.Callable1;

import java.util.Iterator;

public class MapIterator<T,S> extends ReadOnlyIterator<S> {
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
        try {
            return callable.call(iterator.next());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
