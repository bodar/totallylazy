package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Predicate;

public interface PersistentCollection<T> {
    boolean contains(T other);

    boolean exists(Predicate<? super T> predicate);

    int size();
}
