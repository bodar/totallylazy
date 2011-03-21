package com.googlecode.totallylazy.iterators;

import com.googlecode.totallylazy.Option;

import java.util.Iterator;

public class PeekingIterator<T> extends StatefulIterator<T> {
    private final Iterator<? extends T> iterator;

    public PeekingIterator(final Iterator<? extends T> iterator) {
        this.iterator = iterator;
    }

    @Override
    protected Option<T> getNext() throws Exception {
        if (iterator.hasNext()) {
            return Option.some(iterator.next());
        }
        return Option.none();
    }
}
