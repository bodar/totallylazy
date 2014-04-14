package com.googlecode.totallylazy.iterators;

import java.util.NoSuchElementException;

public final class EmptyIterator<T> implements ReadOnlyListIterator<T> {
    @Override
    public final boolean hasNext() {
        return false;
    }

    @Override
    public final T next() {
        throw new NoSuchElementException();
    }

    @Override
    public boolean hasPrevious() {
        return false;
    }

    @Override
    public T previous() {
        throw new NoSuchElementException();
    }

    @Override
    public int nextIndex() {
        return 0;
    }

    @Override
    public int previousIndex() {
        return -1;
    }
}
