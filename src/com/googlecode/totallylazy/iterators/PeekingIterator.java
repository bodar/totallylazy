package com.googlecode.totallylazy.iterators;

import java.util.Iterator;

public class PeekingIterator<T> extends StatefulIterator<T> {
    private final Iterator<? extends T> iterator;

    public PeekingIterator(final Iterator<? extends T> iterator) {
        this.iterator = iterator;
    }

    @Override
    protected T getNext() throws Exception {
        if (iterator.hasNext()) {
            return iterator.next();
        }
        return finished();
    }
}
