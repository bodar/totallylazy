package com.googlecode.totallylazy.iterators;

import java.util.NoSuchElementException;

public class EmptyIterator<T> extends ReadOnlyIterator<T> {
    public boolean hasNext() {
        return false;
    }

    public T next() {
        throw new NoSuchElementException();
    }
}
