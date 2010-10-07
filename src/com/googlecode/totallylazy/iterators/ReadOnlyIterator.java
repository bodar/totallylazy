package com.googlecode.totallylazy.iterators;

import java.util.Iterator;

public abstract class ReadOnlyIterator<T> implements Iterator<T> {
    public void remove() {
        throw new UnsupportedOperationException();
    }
}