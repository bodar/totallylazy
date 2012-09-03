package com.googlecode.totallylazy.iterators;

import java.util.ListIterator;

public abstract class ReadOnlyListIterator<T> implements ListIterator<T> {
    public final void remove() {
        throw new UnsupportedOperationException();
    }

    public void set(T t) {
        throw new UnsupportedOperationException();
    }

    public void add(T t) {
        throw new UnsupportedOperationException();
    }
}