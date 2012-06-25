package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Pair;

import java.util.NoSuchElementException;

public interface Sorted<T> {
    T first() throws NoSuchElementException;

    T last() throws NoSuchElementException;

    Pair<? extends Sorted<T>, T> removeFirst() throws NoSuchElementException;

    Pair<? extends Sorted<T>, T> removeLast() throws NoSuchElementException;

    T index(int i) throws IndexOutOfBoundsException;

    int indexOf(T t);
}
