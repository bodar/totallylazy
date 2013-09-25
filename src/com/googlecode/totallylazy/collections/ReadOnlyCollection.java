package com.googlecode.totallylazy.collections;

import java.util.Collection;

public abstract class ReadOnlyCollection<T> implements Collection<T> {
    @Override
    public boolean add(T e) {
        throw new IllegalMutationException();
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        throw new IllegalMutationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new IllegalMutationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new IllegalMutationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new IllegalMutationException();
    }

    @Override
    public void clear() {
        throw new IllegalMutationException();
    }
}
