package com.googlecode.totallylazy.iterators;

import java.util.Enumeration;

public class EnumerationIterator<T> extends ReadOnlyIterator<T> {
    private final Enumeration<T> enumeration;

    public EnumerationIterator(Enumeration<T> enumeration) {
        this.enumeration = enumeration;
    }

    public boolean hasNext() {
        return enumeration.hasMoreElements();
    }

    public T next() {
        return enumeration.nextElement();
    }
}
