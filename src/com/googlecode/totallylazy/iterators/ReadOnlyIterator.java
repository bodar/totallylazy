package com.googlecode.totallylazy.iterators;

import com.googlecode.totallylazy.collections.IllegalMutationException;

import java.util.Iterator;

public abstract class ReadOnlyIterator<T> implements Iterator<T> {
    public final void remove() {
        throw new IllegalMutationException();
    }
}