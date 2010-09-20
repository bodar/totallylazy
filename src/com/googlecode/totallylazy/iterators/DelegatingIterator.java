package com.googlecode.totallylazy.iterators;

import com.googlecode.totallylazy.Iterator;

public class DelegatingIterator<T> extends Iterator<T> {
    private final java.util.Iterator<T> iterator;

    public DelegatingIterator(java.util.Iterator<T> iterator) {
        this.iterator = iterator;
    }

    public boolean hasNext() {
        return iterator.hasNext();
    }

    public T next() {
        return iterator.next();
    }
}
