package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Predicate;

public interface Container<T> {
    int size();

    boolean isEmpty();

    boolean contains(Object other);

    boolean exists(Predicate<? super T> predicate);
}
