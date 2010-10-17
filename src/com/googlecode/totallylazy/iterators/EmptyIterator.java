package com.googlecode.totallylazy.iterators;

import java.util.NoSuchElementException;

public final class EmptyIterator<T> extends ReadOnlyIterator<T> {
    public final boolean hasNext() {
        return false;
    }

    public final T next() {
        throw new NoSuchElementException();
    }
}
