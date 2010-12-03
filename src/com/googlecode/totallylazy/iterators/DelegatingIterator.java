package com.googlecode.totallylazy.iterators;

import java.util.Iterator;

public final class DelegatingIterator<T> extends ReadOnlyIterator<T> {
    private final Iterator<T> iterator;

    public DelegatingIterator(final Iterator<T> iterator) {
        this.iterator = iterator;
    }

    public final boolean hasNext() {
        return iterator.hasNext();
    }

    public final T next() {
        return iterator.next();
    }
}
