package com.googlecode.totallylazy;

public interface Container<T> {
    boolean contains(T other);

    boolean exists(Predicate<? super T> predicate);

    int size();
}
