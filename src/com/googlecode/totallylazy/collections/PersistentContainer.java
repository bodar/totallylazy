package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.predicates.Predicate;

public interface PersistentContainer<T> {
    int size();

    boolean isEmpty();

    boolean contains(Object other);

    boolean exists(Predicate<? super T> predicate);
}
