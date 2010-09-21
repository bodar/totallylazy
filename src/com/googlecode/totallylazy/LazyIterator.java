package com.googlecode.totallylazy;

import java.util.Iterator;

public abstract class LazyIterator<T> implements Iterator<T> {
    public void remove() {
        throw new UnsupportedOperationException();
    }
}