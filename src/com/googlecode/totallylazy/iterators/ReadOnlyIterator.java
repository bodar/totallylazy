package com.googlecode.totallylazy.iterators;

import com.googlecode.totallylazy.collections.IllegalMutationException;

import java.util.Iterator;

public interface ReadOnlyIterator<T> extends Iterator<T> {
    @Override
    default void remove() {
        throw new IllegalMutationException();
    }
}