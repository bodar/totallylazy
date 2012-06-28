package com.googlecode.totallylazy.iterators;

import java.util.Iterator;

public class PoppingIterator<T> implements Iterator<T> {
    private final Iterator<? extends T> iterator;

    public PoppingIterator(Iterator<? extends T> iterator) {
        this.iterator = iterator;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public T next() {
        try {
            return iterator.next();
        } finally {
            remove();
        }
    }

    @Override
    public void remove() {
        iterator.remove();
    }
}

