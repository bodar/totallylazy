package com.googlecode.totallylazy.collections;

import java.util.List;
import java.util.ListIterator;

public abstract class AbstractList<T> extends ReadOnlyList<T> implements PersistentList<T> {
    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public ListIterator<T> listIterator() {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("TODO");
    }
}
