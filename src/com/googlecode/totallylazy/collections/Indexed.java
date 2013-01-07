package com.googlecode.totallylazy.collections;

public interface Indexed<T> {
    T index(int i) throws IndexOutOfBoundsException;

    int indexOf(T t);
}
