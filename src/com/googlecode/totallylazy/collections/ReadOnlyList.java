package com.googlecode.totallylazy.collections;

import java.util.Collection;
import java.util.List;

public abstract class ReadOnlyList<T> extends AbstractCollection<T> implements List<T> {
    @Override
    public void add(int index, T element) {
        throw new IllegalMutationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new IllegalMutationException();
    }

    @Override
    public T set(int index, T element) {
        throw new IllegalMutationException();
    }

    @Override
    public T remove(int index) {
        throw new IllegalMutationException();
    }
}
