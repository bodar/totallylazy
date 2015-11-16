package com.googlecode.totallylazy.iterators;

import java.util.Iterator;

public class TakeIterator<T> extends ReadOnlyIterator<T> {
    private final Iterator<? extends T> iterator;
    private int count;

    public TakeIterator(Iterator<? extends T> iterator, int count) {
        this.iterator = iterator;
        this.count = count;
    }

    @Override
    public boolean hasNext() {
        return count > 0 && iterator.hasNext();
    }

    @Override
    public T next() {
        count--;
        return iterator.next();
    }
}
