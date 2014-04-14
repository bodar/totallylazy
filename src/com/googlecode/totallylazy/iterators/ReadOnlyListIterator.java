package com.googlecode.totallylazy.iterators;

import com.googlecode.totallylazy.collections.IllegalMutationException;

import java.util.ListIterator;

public interface ReadOnlyListIterator<T> extends ListIterator<T>, ReadOnlyIterator<T> {
    @Override
    default void remove() { throw new IllegalMutationException(); }

    @Override
    default void set(T t) { throw new IllegalMutationException(); }

    @Override
    default void add(T t) { throw new IllegalMutationException(); }
}