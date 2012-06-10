package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Pair;

public interface Sorted<T> {
    Pair<? extends Sorted<T>, T> removeFirst();

    Pair<? extends Sorted<T>, T> removeLast();

    T index(int i);
}
