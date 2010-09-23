package com.googlecode.totallylazy.iterators;

import java.util.Iterator;

public class TakeIterator<T> extends ReadOnlyIterator<T> {
    private final Iterator<T> iterator;
    private int count;

    public TakeIterator(Iterator<T> iterator, int count) {
        this.iterator = iterator;
        this.count = count;
    }

    public boolean hasNext() {
        return count > 0 && iterator.hasNext();
    }

    public T next() {
        count--;
        return iterator.next();
    }
}
