package com.googlecode.totallylazy.iterators;

import com.googlecode.totallylazy.collections.IllegalMutationException;

import java.util.ListIterator;

public abstract class ReadOnlyListIterator<T> implements ListIterator<T> {
    public final void remove() {
        throw new IllegalMutationException();
    }

    public void set(T t) {
        throw new IllegalMutationException();
    }

    public void add(T t) {
        throw new IllegalMutationException();
    }
}