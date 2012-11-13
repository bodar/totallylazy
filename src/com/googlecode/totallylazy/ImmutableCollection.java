package com.googlecode.totallylazy;

public interface ImmutableCollection<T> {
    boolean contains(T other);

    boolean exists(Predicate<? super T> predicate);

    int size();
}
