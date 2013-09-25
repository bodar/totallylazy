package com.googlecode.totallylazy.collections;

public interface Indexed<T> {
    T get(int i) throws IndexOutOfBoundsException;

    int indexOf(Object t);
}
