package com.googlecode.totallylazy.iterators;

import java.util.Enumeration;

public final class EnumerationIterator<T> extends ReadOnlyIterator<T> {
    private final Enumeration<T> enumeration;

    public EnumerationIterator(final Enumeration<T> enumeration) {
        this.enumeration = enumeration;
    }

    public final boolean hasNext() {
        return enumeration.hasMoreElements();
    }

    public T next() {
        return enumeration.nextElement();
    }
}
