package com.googlecode.totallylazy.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class IntersperseIterator<T> extends ReadOnlyIterator<T> {
    private final Iterator<? extends T> iterator;
    private final T separator;
    private boolean useSeparator = false;

    public IntersperseIterator(Iterator<? extends T> iterator, T separator) {
        this.iterator = iterator;
        this.separator = separator;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public T next() {
        if(hasNextSeparator()) {
            valueNext();
            return separator;
        }
        if(hasNext()) {
            separatorNext();
            return iterator.next();
        }
        throw new NoSuchElementException();
    }

    private void separatorNext() {
        useSeparator = true;
    }

    private void valueNext() {
        useSeparator = false;
    }

    private boolean hasNextSeparator() {
        return hasNext() && useSeparator;
    }
}
