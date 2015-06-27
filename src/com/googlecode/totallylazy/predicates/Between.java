package com.googlecode.totallylazy.predicates;

public interface Between<T> extends Predicate<T> {
    T lower();

    T upper();
}
